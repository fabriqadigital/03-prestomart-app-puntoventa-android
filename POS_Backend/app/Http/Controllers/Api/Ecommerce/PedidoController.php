<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Pedido;
use App\Models\Ecommerce\PedidoItem;
use App\Models\Ecommerce\Cliente;
use App\Models\Ecommerce\Direccion;
use App\Models\Ecommerce\MetodoEnvio;
use App\Models\Ecommerce\MetodoPago;
use App\Models\Ecommerce\Configuracion;
use App\Models\Ecommerce\Producto;
use App\Models\Ecommerce\PedidoTracking;
use App\Models\Ecommerce\TrackingEstado;
use App\Models\Ecommerce\TrackingEtapa;
use App\Models\Ecommerce\TrackingHistorial;
use App\Models\Ecommerce\TrackingUbicacion;
use App\Models\Ecommerce\TrackingNotificacion;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

class PedidoController extends Controller
{
    /**
     * Listar pedidos (sirve tanto para cliente como para admin)
     */
    public function index(Request $request): JsonResponse
    {
        /** @var \App\Models\User $user */
        $user = auth()->user();
        $isAdmin = $user->isAdmin();
        $idClienteSolicitado = $request->id_cliente;

        if ($isAdmin && $idClienteSolicitado) {
            // Modo ADMIN con cliente específico
            $query = Pedido::where('id_cliente', $idClienteSolicitado);
        } elseif ($isAdmin && !$idClienteSolicitado) {
            // Modo ADMIN: todos los pedidos
            $query = Pedido::query();
        } else {
            // Modo CLIENTE: solo pedidos del usuario autenticado
            $idClienteConsulta = $this->resolverIdCliente($user, null);

            if (!$idClienteConsulta) {
                return response()->json([
                    'success' => false,
                    'message' => 'Cliente no encontrado'
                ], 404);
            }

            $query = Pedido::where('id_cliente', $idClienteConsulta);
        }

        // Cargar relaciones comunes
        $query = $query->with(['metodoEnvio', 'metodoPago'])
            ->withCount('items');

        // Si es admin, cargar relación con cliente
        if ($isAdmin) {
            $query->with(['cliente.usuario']);
        }

        // Aplicar filtros comunes
        $query = $this->applyFilters($query, $request, $isAdmin);

        // Ordenar
        $sortField = $request->input('sort_field', 'created_at');
        $sortDirection = $request->input('sort_direction', 'desc');
        $query->orderBy($sortField, $sortDirection);

        // Paginación
        $perPage = $isAdmin
            ? min($request->input('per_page', 15), 100)
            : min($request->input('per_page', 10), 50);

        $pedidos = $query->paginate($perPage);

        // Formatear respuesta según el modo
        $formattedData = $pedidos->map(function ($pedido) use ($isAdmin) {
            return $isAdmin
                ? $this->formatPedido($pedido, false, true) // Modo admin
                : $this->formatPedido($pedido); // Modo cliente
        });


        return response()->json([
            'success' => true,
            'data' => $formattedData,
            'meta' => [
                'current_page' => $pedidos->currentPage(),
                'last_page' => $pedidos->lastPage(),
                'per_page' => $pedidos->perPage(),
                'total' => $pedidos->total(),
            ]
        ]);
    }

    /**
     * Mostrar pedido específico
     */
    public function show(Request $request, $id): JsonResponse
    {
        /** @var \App\Models\User $user */
        $user = auth()->user();
        $isAdmin = $user->isAdmin();

        if ($isAdmin) {
            // Modo ADMIN: ver cualquier pedido (o filtrar por id_cliente si se envía)
            $query = Pedido::with(['items.producto', 'metodoEnvio', 'metodoPago', 'cupon', 'historial', 'cliente.usuario']);

            if ($request->id_cliente) {
                $query->where('id_cliente', $request->id_cliente);
            }

            $pedido = $query->find($id);
        } else {
            // Modo CLIENTE: verificar que el pedido pertenezca al cliente
            $idClienteConsulta = $this->resolverIdCliente($user, null);

            $pedido = Pedido::where('id_cliente', $idClienteConsulta)
                ->with(['items.producto', 'metodoEnvio', 'metodoPago', 'cupon', 'historial'])
                ->find($id);
        }

        if (!$pedido) {
            return response()->json([
                'success' => false,
                'message' => 'Pedido no encontrado'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $this->formatPedido($pedido, true, $isAdmin)
        ]);
    }

    /**
     * Actualizar estado del pedido (solo admin)
     */
    public function updateStatus(Request $request, $id): JsonResponse
    {
        
        /** @var \App\Models\User $user */
        $user = auth()->user();

        // Verificar que sea administrador
        if (!$user->isAdmin()) {
            return response()->json([
                'success' => false,
                'message' => 'No autorizado'
            ], 403);
        }

        $validator = Validator::make($request->all(), [
            'estado' => 'required|in:' . implode(',', Pedido::getEstados()),
            'comentario' => 'nullable|string|max:500',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $pedido = Pedido::find($id);

        if (!$pedido) {
            return response()->json([
                'success' => false,
                'message' => 'Pedido no encontrado'
            ], 404);
        }

        DB::beginTransaction();

        try {
            $estadoAnterior = $pedido->estado;
            $pedido->cambiarEstado(
                $request->estado,
                $request->comentario ?? 'Actualizado por administrador',
                'admin'
            );

            DB::commit();

            return response()->json([
                'success' => true,
                'message' => 'Estado del pedido actualizado',
                'data' => $this->formatPedido($pedido->fresh(), false, true)
            ]);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'message' => 'Error al actualizar el estado',
                'error' => $e->getMessage()
            ], 500);
        }
    }


    /**
     * Aplicar filtros a la consulta
     */
    private function applyFilters($query, Request $request, bool $isAdmin = false)
    {
        // Filtro por estado (común para ambos)
        if ($request->has('estado') && $request->estado !== '') {
            $query->where('estado', $request->estado);
        }

        // Filtros específicos para admin
        if ($isAdmin) {
            // Filtro por cliente
            if ($request->has('id_cliente') && $request->id_cliente) {
                $query->where('id_cliente', $request->id_cliente);
            }

            // Filtro por número de pedido
            if ($request->has('numero_pedido') && $request->numero_pedido) {
                $query->where('numero_pedido', 'like', '%' . $request->numero_pedido . '%');
            }

            // Filtro por fecha
            if ($request->has('fecha_inicio') && $request->fecha_inicio) {
                $query->whereDate('created_at', '>=', $request->fecha_inicio);
            }

            if ($request->has('fecha_fin') && $request->fecha_fin) {
                $query->whereDate('created_at', '<=', $request->fecha_fin);
            }
        }

        return $query;
    }


    /**
     * Crear pedido
     *
     * Payload:
     * {
     *   "id_cliente": int,
     *   "id_direccion": int,
     *   "id_metodo_envio": int,
     *   "id_metodo_pago": int,
     *   "notas": string (opcional),
     *   "items": [
     *     { "id_producto": int, "cantidad": int, "precio_unitario": float },
     *     ...
     *   ]
     * }
     */
    public function crear(Request $request)
    {
        $validator = Validator::make($request->all(), [
            // Si se envía id_cliente debe existir; para invitados puede ser null
            'id_cliente' => 'nullable|integer|exists:administracion_cliente,id_cliente',
            'id_direccion' => 'required|exists:ecommerce_direcciones,id_direccion',
            'id_metodo_envio' => 'required|integer',
            'id_metodo_pago' => 'required|integer',
            'notas' => 'nullable|string|max:500',
            'items' => 'required|array|min:1',
            'items.*.id_producto' => 'required|exists:ecommerce_productos,id_producto',
            'items.*.cantidad' => 'required|integer|min:1',
            'items.*.precio_unitario' => 'required|numeric|min:0',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        // Obtener datos validados
        // $user puede ser null (invitado). resolverIdCliente manejará ese caso.
        $user = auth()->user();
        $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente ?? null);


        $direccion = Direccion::find($request->id_direccion);
        $metodoEnvio = MetodoEnvio::find($request->id_metodo_envio);
        $metodoPago = MetodoPago::find($request->id_metodo_pago);

        // Preparar items
        $itemsParaPedido = [];
        foreach ($request->items as $itemData) {
            $producto = Producto::find($itemData['id_producto']);

            $itemsParaPedido[] = [
                'producto' => $producto,
                'id_producto' => $producto->id_producto,
                'id_variante' => null,
                'cantidad' => $itemData['cantidad'],
                'precio_unitario' => $itemData['precio_unitario'],
                'subtotal' => $itemData['cantidad'] * $itemData['precio_unitario'],
            ];
        }

        DB::beginTransaction();

        try {
            // Calcular totales
            $subtotal = array_sum(array_column($itemsParaPedido, 'subtotal'));
            $descuento = 0;
            $envio = $metodoEnvio->precio;

            // Verificar envío gratis
            $minimoEnvioGratis = Configuracion::obtener('free_shipping_min', 0);
            if ($minimoEnvioGratis > 0 && $subtotal >= $minimoEnvioGratis) {
                $envio = 0;
            }

            $tasaImpuestos = Configuracion::obtener('tax_rate', 18) / 100;
            $impuestos = $subtotal * $tasaImpuestos;
            $total = $subtotal - $descuento + $envio + $impuestos;

            // Crear pedido (id_cliente puede ser null para pedidos de invitados)
            $pedido = Pedido::create([
                'id_cliente' => $idClienteConsulta,
                'numero_pedido' => Pedido::generarNumeroPedido(),
                'estado' => Pedido::ESTADO_PENDIENTE,
                'envio_direccion' => [
                    'nombre' => $direccion->nombre_completo ?? $direccion->destinatario,
                    'telefono' => $direccion->telefono,
                    'direccion' => $direccion->direccion,
                    'direccion_adicional' => $direccion->direccion_adicional ?? $direccion->referencia,
                    'ciudad' => $direccion->ciudad ?? $direccion->distrito,
                    'estado' => $direccion->estado ?? $direccion->provincia,
                    'codigo_postal' => $direccion->codigo_postal,
                    'pais' => $direccion->pais ?? 'Perú',
                ],
                'id_metodo_envio' => $metodoEnvio->id_metodo_envio,
                'id_metodo_pago' => $metodoPago->id_metodo_pago,
                'subtotal' => $subtotal,
                'descuento' => $descuento,
                'codigo_cupon' => null,
                'envio' => $envio,
                'impuestos' => $impuestos,
                'total' => $total,
                'notas' => $request->notas,
                'ip_address' => $request->ip(),
            ]);

            // Crear items del pedido y reducir stock
            foreach ($itemsParaPedido as $item) {
                $producto = $item['producto'];

                PedidoItem::create([
                    'id_pedido' => $pedido->id_pedido,
                    'id_producto' => $item['id_producto'],
                    'id_variante' => $item['id_variante'],
                    'nombre_producto' => $producto->nombre,
                    'sku' => $producto->sku,
                    'cantidad' => $item['cantidad'],
                    'precio_unitario' => $item['precio_unitario'],
                    'subtotal' => $item['subtotal'],
                ]);

                // Reducir stock
                $producto->decrement('stock', $item['cantidad']);
            }

            // Registrar historial
            $pedido->historial()->create([
                'estado_anterior' => null,
                'estado_nuevo' => Pedido::ESTADO_PENDIENTE,
                'comentario' => 'Pedido creado',
                'creado_por' => 'sistema',
            ]);

            // Inicializar tracking automáticamente al crear pedido
            $estadoInicial = TrackingEstado::obtenerPorCodigo('creado');
            $etapaInicial = TrackingEtapa::obtenerPorCodigo('almacen_origen');

            $tracking = PedidoTracking::create([
                'id_pedido' => $pedido->id_pedido,
                'id_estado' => $estadoInicial->id,
                'id_etapa' => $etapaInicial->id,
                'codigo_rastreo' => PedidoTracking::generarCodigoRastreo($pedido->id_pedido),
            ]);

            // Registrar primer evento en historial de tracking
            TrackingHistorial::create([
                'id_pedido' => $pedido->id_pedido,
                'id_estado' => $estadoInicial->id,
                'id_etapa' => $etapaInicial->id,
                'titulo' => 'Tracking Iniciado',
                'descripcion' => 'Se ha iniciado el seguimiento del pedido',
                'operador' => 'sistema',
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

            // Crear notificación pendiente para el cliente (tracking iniciado)
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
                Log::channel('stderr')->error('Error creando notificacion al crear pedido: '.$e->getMessage());
            }

            $pedido->load(['items.producto', 'metodoEnvio', 'metodoPago', 'historial']);

            return response()->json([
                'success' => true,
                'message' => 'Pedido creado exitosamente',
                // 'data' => $this->formatPedido($pedido, true)
            ], 201);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'message' => 'Error al crear el pedido',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function cancelar(Request $request, $id): JsonResponse
    {
        /** @var \App\Models\User $user */
        $user = auth()->user();
        $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente);

        if (!$idClienteConsulta) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $pedido = Pedido::where('id_cliente', $idClienteConsulta)->find($id);

        if (!$pedido) {
            return response()->json([
                'success' => false,
                'message' => 'Pedido no encontrado'
            ], 404);
        }

        if (!in_array($pedido->estado, [Pedido::ESTADO_PENDIENTE, Pedido::ESTADO_PROCESANDO])) {
            return response()->json([
                'success' => false,
                'message' => 'Este pedido no puede ser cancelado'
            ], 400);
        }

        DB::beginTransaction();

        try {
            // Restaurar stock
            foreach ($pedido->items as $item) {
                if ($item->id_variante) {
                    \App\Models\Ecommerce\ProductoVariante::where('id', $item->id_variante)
                        ->increment('stock', $item->cantidad);
                } else {
                    \App\Models\Ecommerce\Producto::where('id', $item->id_producto)
                        ->increment('stock', $item->cantidad);
                }
            }

            $pedido->cambiarEstado(Pedido::ESTADO_CANCELADO, 'Cancelado por el cliente', 'cliente');

            DB::commit();

            return response()->json([
                'success' => true,
                'message' => 'Pedido cancelado exitosamente',
                'data' => $this->formatPedido($pedido->fresh())
            ]);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'message' => 'Error al cancelar el pedido'
            ], 500);
        }
    }

    public function metodosEnvio(): JsonResponse
    {
        $metodos = MetodoEnvio::activos()->orderBy('precio')->get();

        return response()->json([
            'success' => true,
            'data' => $metodos->map(function ($metodo) {
                return [
                    'id_metodo_envio' => $metodo->id_metodo_envio,
                    'nombre' => $metodo->nombre,
                    'descripcion' => $metodo->descripcion,
                    'precio' => (float)$metodo->precio,
                    'activo' => $metodo->activo,

                    'tiempo_entrega' => $metodo->tiempo_entrega_min . '-' . $metodo->tiempo_entrega_max . ' días',
                ];
            })
        ]);
    }

    public function metodosPago(): JsonResponse
    {
        $metodos = MetodoPago::activos()->get();

        return response()->json([
            'success' => true,
            'data' => $metodos->map(function ($metodo) {
                return [
                    'id_metodo_pago' => $metodo->id_metodo_pago,
                    'nombre' => $metodo->nombre,
                    'descripcion' => $metodo->descripcion,
                    'icono' => $metodo->icono,
                    'activo' => $metodo->activo,
                ];
            })
        ]);
    }

    /**
     * Resuelve el id_cliente a consultar según el rol del usuario
     */
    private function resolverIdCliente($user, ?string $idClienteSolicitado): ?int
    {
        // Manejar usuario null (invitado)
        if ($user && $user->isAdmin() && $idClienteSolicitado) {
            // Admin puede especificar id_cliente existente
            return (int) $idClienteSolicitado;
        }

        if ($user) {
            // Para usuarios normales, buscar su propio cliente
            $cliente = Cliente::where('id_usuario', $user->id)->first();
            return $cliente?->id_cliente;
        }

        // Invitado -> no hay id_cliente asociado
        return null;
    }

    /**
     * Formatear pedido (única función para ambos modos)
     */
    private function formatPedido($pedido, bool $detalle = false, bool $isAdmin = false): array
    {
        $data = [
            'id_pedido' => $pedido->id_pedido,
            'numero_pedido' => $pedido->numero_pedido,
            'estado' => $pedido->estado,
            'estado_label' => $pedido->estado_label,
            'subtotal' => (float)$pedido->subtotal,
            'descuento' => (float)$pedido->descuento,
            'envio' => (float)$pedido->envio,
            'impuestos' => (float)$pedido->impuestos,
            'total' => (float)$pedido->total,
            'pagado' => $pedido->pagado,
            'items_count' => $pedido->items_count ?? $pedido->total_items,
            'metodo_envio' => $pedido->metodoEnvio ? $pedido->metodoEnvio->nombre : null,
            'metodo_pago' => $pedido->metodoPago ? $pedido->metodoPago->nombre : null,
            'fecha' => $pedido->created_at->format('Y-m-d H:i:s'),
            'fecha_formato' => $pedido->created_at->format('d/m/Y'),
        ];

        // Información adicional para admin
        if ($isAdmin) {
            $data['cliente'] = $pedido->cliente ? [
                'id' => $pedido->cliente->id,
                'nombre' => $pedido->cliente->nombre_completo ?? 'N/A',
                'email' => $pedido->cliente->usuario->email ?? 'N/A',
                'telefono' => $pedido->cliente->telefono ?? 'N/A',
            ] : null;
        }

        // Detalles completos
        if ($detalle) {
            $data['envio_direccion'] = $pedido->envio_direccion;
            $data['notas'] = $pedido->notas;
            $data['fecha_pago'] = $pedido->fecha_pago?->format('Y-m-d H:i:s');
            $data['items'] = $pedido->items->map(function ($item) {
                return [
                    'id' => $item->id,
                    'id_producto' => $item->id_producto,
                    'nombre_producto' => $item->nombre_producto,
                    'sku' => $item->sku,
                    'cantidad' => $item->cantidad,
                    'precio_unitario' => (float)$item->precio_unitario,
                    'subtotal' => (float)$item->subtotal,
                    'imagen' => $item->producto?->imagen_principal,
                ];
            });
            $data['historial'] = $pedido->historial->map(function ($h) {
                return [
                    'estado' => $h->estado_nuevo,
                    'comentario' => $h->comentario,
                    'fecha' => $h->created_at->format('Y-m-d H:i:s'),
                    'creado_por' => $h->creado_por, // Solo visible en admin
                ];
            });
            $data['cupon'] = $pedido->cupon ? [
                'codigo' => $pedido->cupon->codigo,
                'descuento' => (float)$pedido->descuento,
            ] : null;

            // Información extra para admin en detalle
            if ($isAdmin) {
                $data['ip_address'] = $pedido->ip_address;
                if ($pedido->cupon) {
                    $data['cupon']['id'] = $pedido->cupon->id;
                }
            }
        }

        return $data;
    }
}
