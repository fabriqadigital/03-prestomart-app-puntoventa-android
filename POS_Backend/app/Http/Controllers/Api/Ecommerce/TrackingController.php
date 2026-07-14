<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\PedidoTracking;
use App\Models\Ecommerce\TrackingEstado;
use App\Models\Ecommerce\TrackingEtapa;
use App\Models\Ecommerce\TrackingHistorial;
use App\Models\Ecommerce\TrackingUbicacion;
use App\Models\Ecommerce\Pedido;
use App\Models\Ecommerce\Cliente;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Validator;

class TrackingController extends Controller
{
    /**
     * Buscar tracking por código de rastreo (Público)
     */
    public function buscar(Request $request): JsonResponse
    {
        $codigo = $request->input('codigo');

        if (!$codigo || strlen($codigo) < 5) {
            return response()->json([
                'success' => false,
                'message' => 'Código de rastreo inválido'
            ], 400);
        }

        $tracking = PedidoTracking::with([
            'estado',
            'etapa',
            'pedido:id,numero_pedido,estado,total,created_at,envio_direccion',
            'historial.estado',
            'historial.etapa',
            'ubicaciones.etapa'
        ])->where('codigo_rastreo', $codigo)->first();

        if (!$tracking) {
            // Intentar buscar por número de pedido
            $pedido = Pedido::where('numero_pedido', $codigo)->first();
            if ($pedido) {
                $tracking = PedidoTracking::with([
                    'estado',
                    'etapa',
                    'pedido:id,numero_pedido,estado,total,created_at,envio_direccion',
                    'historial.estado',
                    'historial.etapa',
                    'ubicaciones.etapa'
                ])->where('id_pedido', $pedido->id)->first();
            }
        }

        if (!$tracking) {
            return response()->json([
                'success' => false,
                'message' => 'No se encontró el pedido con ese código'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $this->formatTracking($tracking)
        ]);
    }

    /**
     * Obtener tracking de pedido (Usuario autenticado)
     */
    public function obtenerPorPedido($pedidoId): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        $pedido = Pedido::where('id', $pedidoId)
                        ->where('id_cliente', $cliente->id)
                        ->first();

        if (!$pedido) {
            return response()->json([
                'success' => false,
                'message' => 'Pedido no encontrado'
            ], 404);
        }

        $tracking = PedidoTracking::with([
            'estado',
            'etapa',
            'pedido',
            'historial.estado',
            'historial.etapa',
            'ubicaciones.etapa'
        ])->where('id_pedido', $pedidoId)->first();

        if (!$tracking) {
            return response()->json([
                'success' => false,
                'message' => 'Tracking no disponible para este pedido'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $this->formatTracking($tracking)
        ]);
    }

    /**
     * Obtener todos los estados de tracking
     */
    public function estados(): JsonResponse
    {
        $estados = TrackingEstado::activos()
            ->ordenados()
            ->get(['id', 'codigo', 'nombre', 'descripcion', 'icono', 'color', 'orden', 'es_final']);

        return response()->json([
            'success' => true,
            'data' => $estados
        ]);
    }

    /**
     * Obtener todas las etapas de ruta
     */
    public function etapas(): JsonResponse
    {
        $etapas = TrackingEtapa::where('activo', true)
            ->orderBy('orden')
            ->get(['id', 'codigo', 'nombre', 'descripcion', 'tipo', 'orden', 'icono']);

        return response()->json([
            'success' => true,
            'data' => $etapas
        ]);
    }

    /**
     * Obtener mis pedidos con tracking
     */
    public function misPedidos(Request $request): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        if (!$cliente) {
            return response()->json([
                'success' => true,
                'data' => []
            ]);
        }

        $pedidos = Pedido::where('id_cliente', $cliente->id)
            ->with(['tracking.estado', 'tracking.etapa'])
            ->whereHas('tracking')
            ->latest()
            ->get()
            ->map(function ($pedido) {
                return [
                    'id' => $pedido->id,
                    'numero_pedido' => $pedido->numero_pedido,
                    'total' => (float)$pedido->total,
                    'fecha' => $pedido->created_at->format('Y-m-d H:i'),
                    'tracking' => $pedido->tracking ? [
                        'codigo_rastreo' => $pedido->tracking->codigo_rastreo,
                        'estado' => [
                            'codigo' => $pedido->tracking->estado->codigo,
                            'nombre' => $pedido->tracking->estado->nombre,
                            'icono' => $pedido->tracking->estado->icono,
                            'color' => $pedido->tracking->estado->color,
                        ],
                        'fecha_estimada' => $pedido->tracking->fecha_estimada_entrega?->format('Y-m-d'),
                        'progreso' => $pedido->tracking->progreso,
                    ] : null,
                ];
            });

        return response()->json([
            'success' => true,
            'data' => $pedidos
        ]);
    }

    /**
     * Formatear tracking para respuesta
     */
    private function formatTracking(PedidoTracking $tracking): array
    {
        $estados = TrackingEstado::activos()->ordenados()->get();
        $estadoActualOrden = $tracking->estado->orden;

        return [
            'id' => $tracking->id,
            'codigo_rastreo' => $tracking->codigo_rastreo,
            'progreso' => $tracking->progreso,
            'estado_actual' => [
                'codigo' => $tracking->estado->codigo,
                'nombre' => $tracking->estado->nombre,
                'descripcion' => $tracking->estado->descripcion,
                'icono' => $tracking->estado->icono,
                'color' => $tracking->estado->color,
                'es_final' => $tracking->estado->es_final,
            ],
            'etapa_actual' => $tracking->etapa ? [
                'codigo' => $tracking->etapa->codigo,
                'nombre' => $tracking->etapa->nombre,
                'tipo' => $tracking->etapa->tipo,
                'icono' => $tracking->etapa->icono,
            ] : null,
            'transportista' => [
                'nombre' => $tracking->transportista,
                'numero_guia' => $tracking->numero_guia,
                'url' => $tracking->url_transportista,
            ],
            'fechas' => [
                'pedido' => $tracking->pedido->created_at->format('Y-m-d H:i'),
                'estimada_entrega' => $tracking->fecha_estimada_entrega?->format('Y-m-d'),
                'entrega_real' => $tracking->fecha_entrega_real?->format('Y-m-d H:i'),
            ],
            'ubicacion_actual' => [
                'direccion' => $tracking->direccion_actual,
                'latitud' => $tracking->latitud,
                'longitud' => $tracking->longitud,
            ],
            'pedido' => [
                'id' => $tracking->pedido->id,
                'numero' => $tracking->pedido->numero_pedido,
                'total' => (float)$tracking->pedido->total,
                'envio_direccion' => $tracking->pedido->envio_direccion,
            ],
            'timeline' => $tracking->historial->map(function ($h) {
                return [
                    'id' => $h->id,
                    'titulo' => $h->titulo,
                    'descripcion' => $h->descripcion,
                    'ubicacion' => $h->ubicacion,
                    'fecha' => $h->fecha_evento->format('Y-m-d H:i'),
                    'estado' => [
                        'codigo' => $h->estado->codigo,
                        'nombre' => $h->estado->nombre,
                        'icono' => $h->estado->icono,
                        'color' => $h->estado->color,
                    ],
                    'etapa' => $h->etapa ? [
                        'nombre' => $h->etapa->nombre,
                        'icono' => $h->etapa->icono,
                    ] : null,
                ];
            }),
            'recorrido' => $tracking->ubicaciones->map(function ($u) {
                return [
                    'id' => $u->id,
                    'nombre' => $u->nombre,
                    'direccion' => $u->direccion,
                    'ciudad' => $u->ciudad,
                    'orden' => $u->orden,
                    'completado' => $u->completado,
                    'fecha_llegada' => $u->fecha_llegada?->format('Y-m-d H:i'),
                    'etapa' => $u->etapa ? [
                        'codigo' => $u->etapa->codigo,
                        'nombre' => $u->etapa->nombre,
                        'tipo' => $u->etapa->tipo,
                        'icono' => $u->etapa->icono,
                    ] : null,
                ];
            }),
            'estados_disponibles' => $estados->map(function ($e) use ($estadoActualOrden) {
                return [
                    'codigo' => $e->codigo,
                    'nombre' => $e->nombre,
                    'icono' => $e->icono,
                    'color' => $e->color,
                    'orden' => $e->orden,
                    'completado' => $e->orden < $estadoActualOrden,
                    'actual' => $e->orden == $estadoActualOrden,
                    'pendiente' => $e->orden > $estadoActualOrden,
                ];
            }),
        ];
    }
}
