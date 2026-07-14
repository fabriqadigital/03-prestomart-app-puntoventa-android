<?php

namespace App\Http\Controllers\Api\Ecommerce\Admin;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\PedidoTracking;
use App\Models\Ecommerce\TrackingEstado;
use App\Models\Ecommerce\TrackingEtapa;
use App\Models\Ecommerce\TrackingHistorial;
use App\Models\Ecommerce\TrackingUbicacion;
use App\Models\Ecommerce\Pedido;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;
use App\Models\Ecommerce\TrackingNotificacion;

class TrackingAdminController extends Controller
{
    /**
     * Listar todos los trackings
     */
    public function index(Request $request): JsonResponse
    {
        $query = PedidoTracking::with(['estado', 'etapa', 'pedido.cliente']);

        // Filtro por estado
        if ($request->has('estado')) {
            $query->whereHas('estado', function ($q) use ($request) {
                $q->where('codigo', $request->estado);
            });
        }

        // Búsqueda
        if ($request->has('buscar')) {
            $buscar = $request->buscar;
            $query->where(function ($q) use ($buscar) {
                $q->where('codigo_rastreo', 'like', "%{$buscar}%")
                  ->orWhere('numero_guia', 'like', "%{$buscar}%")
                  ->orWhereHas('pedido', function ($q2) use ($buscar) {
                      $q2->where('numero_pedido', 'like', "%{$buscar}%");
                  });
            });
        }

        $perPage = min($request->input('per_page', 20), 50);
        $trackings = $query->latest('updated_at')->paginate($perPage);

        return response()->json([
            'success' => true,
            'data' => collect($trackings->items())->map(function ($t) {
                return [
                    'id' => $t->id,
                    'id_pedido' => $t->id_pedido,
                    'codigo_rastreo' => $t->codigo_rastreo,
                    'numero_pedido' => $t->pedido->numero_pedido,
                    'cliente' => $t->pedido->cliente ?
                        $t->pedido->cliente->nombre . ' ' . $t->pedido->cliente->apellido : null,
                    'estado' => [
                        'codigo' => $t->estado->codigo,
                        'nombre' => $t->estado->nombre,
                        'color' => $t->estado->color,
                    ],
                    'etapa' => $t->etapa ? $t->etapa->nombre : null,
                    'transportista' => $t->transportista,
                    'numero_guia' => $t->numero_guia,
                    'fecha_estimada' => $t->fecha_estimada_entrega?->format('Y-m-d'),
                    'progreso' => $t->progreso,
                    'updated_at' => $t->updated_at->format('Y-m-d H:i'),
                ];
            }),
            'meta' => [
                'current_page' => $trackings->currentPage(),
                'last_page' => $trackings->lastPage(),
                'per_page' => $trackings->perPage(),
                'total' => $trackings->total(),
            ]
        ]);
    }

    /**
     * Obtener tracking específico
     */
    public function show($id): JsonResponse
    {
        $tracking = PedidoTracking::with([
            'estado',
            'etapa',
            'pedido.cliente',
            'pedido.items.producto',
            'historial.estado',
            'historial.etapa',
            'ubicaciones.etapa'
        ])->find($id);

        if (!$tracking) {
            return response()->json([
                'success' => false,
                'message' => 'Tracking no encontrado'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $this->formatTrackingCompleto($tracking)
        ]);
    }

    /**
     * Inicializar tracking para un pedido
     */
    public function inicializar(Request $request): JsonResponse
    {

        Log::channel('stderr')->info("test" . json_encode($request->all()));
        $validator = Validator::make($request->all(), [
            // Accepts 'id_pedido' from frontend; ensure it exists in table (primary key is id_pedido)
            'id_pedido' => 'required|exists:ecommerce_pedidos,id_pedido',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors' => $validator->errors()
            ], 422);
        }

        $pedidoId = $request->input('id_pedido');

        // Verificar si ya tiene tracking
        if (PedidoTracking::where('id_pedido', $pedidoId)->exists()) {
            return response()->json([
                'success' => false,
                'message' => 'Este pedido ya tiene tracking inicializado'
            ], 400);
        }

        DB::beginTransaction();
        try {
            $pedido = Pedido::where('id_pedido', $pedidoId)->first();
            $estadoInicial = TrackingEstado::obtenerPorCodigo('creado');
            $etapaInicial = TrackingEtapa::obtenerPorCodigo('almacen_origen');

            // Crear tracking
            $tracking = PedidoTracking::create([
                'id_pedido' => $pedido->id_pedido,
                'id_estado' => $estadoInicial->id,
                'id_etapa' => $etapaInicial->id,
                'codigo_rastreo' => PedidoTracking::generarCodigoRastreo($pedido->id_pedido),
            ]);

            // Registrar en historial
            TrackingHistorial::create([
                'id_pedido' => $pedido->id_pedido,
                'id_estado' => $estadoInicial->id,
                'id_etapa' => $etapaInicial->id,
                'titulo' => 'Tracking Iniciado',
                'descripcion' => 'Se ha iniciado el seguimiento del pedido',
                'operador' => auth()->user()->name ?? 'admin',
                'fecha_evento' => now(),
            ]);

            // Crear ubicaciones del recorrido
            $etapas = TrackingEtapa::where('activo', true)->orderBy('orden')->get();
            foreach ($etapas as $index => $etapa) {
                TrackingUbicacion::create([
                    'id_pedido' => $pedido->id_pedido,
                    'id_etapa' => $etapa->id,
                    'nombre' => $etapa->nombre,
                    'orden' => $index + 1,
                ]);
            }

            DB::commit();

            // Crear notificación para el cliente (pendiente)
            try {
                TrackingNotificacion::create([
                    'id_pedido' => $pedido->id_pedido,
                    'id_cliente' => $pedido->id_cliente,
                    'tipo' => TrackingNotificacion::TIPO_PUSH,
                    'id_estado' => $estadoInicial->id,
                    'titulo' => 'Tracking Iniciado',
                    'mensaje' => "Se ha iniciado el seguimiento del pedido. Código: {$tracking->codigo_rastreo}",
                    'enviado' => false,
                    'created_at' => now(),
                ]);
            } catch (\Exception $e) {
                Log::channel('stderr')->error('Error creando notificación: ' . $e->getMessage());
            }

            return response()->json([
                'success' => true,
                'message' => 'Tracking inicializado correctamente',
                'data' => [
                    'id' => $tracking->id,
                    'codigo_rastreo' => $tracking->codigo_rastreo,
                ]
            ], 201);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'message' => 'Error al inicializar tracking',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Actualizar estado del tracking
     */
    public function actualizarEstado(Request $request, $id): JsonResponse
    {

        Log::channel('stderr')->info("test" . json_encode($request->all()));
        $validator = Validator::make($request->all(), [
            'estado' => 'required|exists:ecommerce_tracking_estados,codigo',
            'etapa' => 'nullable|exists:ecommerce_tracking_etapas,codigo',
            'titulo' => 'nullable|string|max:150',
            'descripcion' => 'nullable|string',
            'ubicacion' => 'nullable|string|max:255',
            'latitud' => 'nullable|numeric',
            'longitud' => 'nullable|numeric',
        ]);


        
        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors' => $validator->errors()
                ], 422);
                }
                
                $tracking = PedidoTracking::find($id);
                
                if (!$tracking) {
                    return response()->json([
                        'success' => false,
                        'message' => 'Tracking no encontrado'
                        ], 404);
                        }
                        Log::channel('stderr')->info("test 2222" . json_encode($request->all()));

        $tracking->actualizarEstado(
            $request->estado,
            $request->etapa,
            [
                'titulo' => $request->titulo,
                'descripcion' => $request->descripcion,
                'ubicacion' => $request->ubicacion,
                'latitud' => $request->latitud,
                'longitud' => $request->longitud,
                'operador' => auth()->user()->name ?? 'admin',
            ]
        );

        // Crear notificación para el cliente sobre cambio de estado
        try {
            $estadoModel = TrackingEstado::obtenerPorCodigo($request->estado);
            TrackingNotificacion::create([
                'pedido_id' => $tracking->id_pedido,
                'cliente_id' => $tracking->pedido?->id_cliente,
                'tipo' => TrackingNotificacion::TIPO_PUSH,
                'estado_id' => $estadoModel?->id,
                'titulo' => "Estado actualizado: " . ($estadoModel?->nombre ?? $request->estado),
                'mensaje' => $request->descripcion ?? ($estadoModel?->descripcion ?? ''),
                'enviado' => false,
                'created_at' => now(),
            ]);
        } catch (\Exception $e) {
            Log::channel('stderr')->error('Error creando notificación de estado: ' . $e->getMessage());
        }

        return response()->json([
            'success' => true,
            'message' => 'Estado actualizado correctamente',
            'data' => [
                'estado' => $tracking->fresh()->estado->nombre,
                'progreso' => $tracking->fresh()->progreso,
            ]
        ]);
    }

    /**
     * Actualizar transportista
     */
    public function actualizarTransportista(Request $request, $id): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'transportista' => 'required|string|max:100',
            'numero_guia' => 'required|string|max:100',
            'url_transportista' => 'nullable|url|max:500',
            'fecha_estimada' => 'nullable|date',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors' => $validator->errors()
            ], 422);
        }

        $tracking = PedidoTracking::find($id);

        if (!$tracking) {
            return response()->json([
                'success' => false,
                'message' => 'Tracking no encontrado'
            ], 404);
        }

        $tracking->update([
            'transportista' => $request->transportista,
            'numero_guia' => $request->numero_guia,
            'url_transportista' => $request->url_transportista,
            'fecha_estimada_entrega' => $request->fecha_estimada,
        ]);

        // Registrar en historial
        TrackingHistorial::create([
            'id_pedido' => $tracking->id_pedido,
            'id_estado' => $tracking->id_estado,
            'titulo' => 'Transportista Asignado',
            'descripcion' => "Transportista: {$request->transportista} - Guía: {$request->numero_guia}",
            'operador' => auth()->user()->name ?? 'admin',
            'metadata' => [
                'transportista' => $request->transportista,
                'numero_guia' => $request->numero_guia,
            ],
            'fecha_evento' => now(),
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Transportista actualizado'
        ]);
    }

    /**
     * Agregar evento al historial
     */
    public function agregarEvento(Request $request, $id): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'titulo' => 'required|string|max:150',
            'descripcion' => 'nullable|string',
            'ubicacion' => 'nullable|string|max:255',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors' => $validator->errors()
            ], 422);
        }

        $tracking = PedidoTracking::find($id);

        if (!$tracking) {
            return response()->json([
                'success' => false,
                'message' => 'Tracking no encontrado'
            ], 404);
        }

        TrackingHistorial::create([
            'id_pedido' => $tracking->id_pedido,
            'id_estado' => $tracking->id_estado,
            'id_etapa' => $tracking->id_etapa,
            'titulo' => $request->titulo,
            'descripcion' => $request->descripcion,
            'ubicacion' => $request->ubicacion,
            'operador' => auth()->user()->name ?? 'admin',
            'fecha_evento' => now(),
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Evento agregado al historial'
        ]);
    }

    /**
     * Resumen de tracking por estados
     */
    public function resumen(): JsonResponse
    {
        $resumen = PedidoTracking::select('id_estado', DB::raw('count(*) as cantidad'))
            ->groupBy('id_estado')
            ->with('estado:id,codigo,nombre,color')
            ->get()
            ->map(function ($item) {
                return [
                    'estado' => $item->estado->nombre,
                    'codigo' => $item->estado->codigo,
                    'color' => $item->estado->color,
                    'cantidad' => $item->cantidad,
                ];
            });

        $total = PedidoTracking::count();
        $entregados = PedidoTracking::whereHas('estado', function ($q) {
            $q->where('codigo', 'entregado');
        })->count();

        $enTransito = PedidoTracking::whereHas('estado', function ($q) {
            $q->whereIn('codigo', ['despachado', 'en_transito', 'centro_distribucion', 'en_ruta']);
        })->count();

        return response()->json([
            'success' => true,
            'data' => [
                'total' => $total,
                'entregados' => $entregados,
                'en_transito' => $enTransito,
                'por_estado' => $resumen,
            ]
        ]);
    }

    private function formatTrackingCompleto($tracking): array
    {
        return [
            'id' => $tracking->id,
            'codigo_rastreo' => $tracking->codigo_rastreo,
            'progreso' => $tracking->progreso,
            'estado' => [
                'codigo' => $tracking->estado->codigo,
                'nombre' => $tracking->estado->nombre,
                'descripcion' => $tracking->estado->descripcion,
                'icono' => $tracking->estado->icono,
                'color' => $tracking->estado->color,
            ],
            'etapa' => $tracking->etapa ? [
                'codigo' => $tracking->etapa->codigo,
                'nombre' => $tracking->etapa->nombre,
                'tipo' => $tracking->etapa->tipo,
            ] : null,
            'transportista' => $tracking->transportista,
            'numero_guia' => $tracking->numero_guia,
            'url_transportista' => $tracking->url_transportista,
            'fecha_estimada' => $tracking->fecha_estimada_entrega?->format('Y-m-d'),
            'fecha_entrega' => $tracking->fecha_entrega_real?->format('Y-m-d H:i'),
            'ubicacion_actual' => $tracking->direccion_actual,
            'pedido' => [
                'id' => $tracking->pedido->id,
                'numero' => $tracking->pedido->numero_pedido,
                'estado' => $tracking->pedido->estado,
                'total' => (float)$tracking->pedido->total,
                'fecha' => $tracking->pedido->created_at->format('Y-m-d H:i'),
                'cliente' => $tracking->pedido->cliente ? [
                    'nombre' => $tracking->pedido->cliente->nombre . ' ' . $tracking->pedido->cliente->apellido,
                    'email' => $tracking->pedido->cliente->email,
                    'telefono' => $tracking->pedido->cliente->telefono,
                ] : null,
                'envio_direccion' => $tracking->pedido->envio_direccion,
                'items' => $tracking->pedido->items->map(function ($i) {
                    return [
                        'nombre' => $i->nombre_producto,
                        'cantidad' => $i->cantidad,
                        'precio' => (float)$i->precio_unitario,
                    ];
                }),
            ],
            'historial' => $tracking->historial->map(function ($h) {
                return [
                    'id' => $h->id,
                    'titulo' => $h->titulo,
                    'descripcion' => $h->descripcion,
                    'ubicacion' => $h->ubicacion,
                    'operador' => $h->operador,
                    'fecha' => $h->fecha_evento->format('Y-m-d H:i'),
                    'estado' => $h->estado->nombre,
                    'color' => $h->estado->color,
                ];
            }),
            'ubicaciones' => $tracking->ubicaciones->map(function ($u) {
                return [
                    'id' => $u->id,
                    'nombre' => $u->nombre,
                    'orden' => $u->orden,
                    'completado' => $u->completado,
                    'fecha_llegada' => $u->fecha_llegada?->format('Y-m-d H:i'),
                    'etapa' => $u->etapa?->nombre,
                ];
            }),
        ];
    }
}
