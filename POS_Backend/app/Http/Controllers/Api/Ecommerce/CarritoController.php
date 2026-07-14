<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Carrito;
use App\Models\Ecommerce\CarritoItem;
use App\Models\Ecommerce\Producto;
use App\Models\Ecommerce\ProductoVariante;
use App\Models\Ecommerce\Cupon;
use App\Models\Ecommerce\Cliente;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Validator;

class CarritoController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $carrito = $this->obtenerCarrito($request);

        return response()->json([
            'success' => true,
            'data' => $this->formatCarrito($carrito)
        ]);
    }

    public function agregar(Request $request): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'id_producto' => 'required|exists:ecommerce_productos,id_producto',
            'id_variante' => 'nullable|exists:ecommerce_producto_variantes,id_variante',
            'cantidad' => 'required|integer|min:1|max:10',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $producto = Producto::find($request->id_producto);

        if (!$producto || !$producto->activo) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no disponible'
            ], 404);
        }

        // Verificar stock
        $stockDisponible = $producto->stock;
        if ($request->id_variante) {
            $variante = ProductoVariante::find($request->id_variante);
            if (!$variante || !$variante->activo) {
                return response()->json([
                    'success' => false,
                    'message' => 'Variante no disponible'
                ], 404);
            }
            $stockDisponible = $variante->stock;
        }

        if ($stockDisponible < $request->cantidad) {
            return response()->json([
                'success' => false,
                'message' => 'Stock insuficiente',
                'data' => ['stock_disponible' => $stockDisponible]
            ], 400);
        }

        $carrito = $this->obtenerCarrito($request);

        // Buscar si ya existe el item
        $item = $carrito->items()
            ->where('id_producto', $request->id_producto)
            ->where('id_variante', $request->id_variante)
            ->first();

        $precioUnitario = $producto->precio_final;
        if ($request->id_variante && isset($variante)) {
            $precioUnitario += $variante->precio_adicional;
        }

        if ($item) {
            $nuevaCantidad = $item->cantidad + $request->cantidad;
            if ($nuevaCantidad > $stockDisponible) {
                return response()->json([
                    'success' => false,
                    'message' => 'Stock insuficiente para la cantidad solicitada',
                    'data' => ['stock_disponible' => $stockDisponible]
                ], 400);
            }
            $item->cantidad = $nuevaCantidad;
            $item->precio_unitario = $precioUnitario;
            $item->save();
        } else {
            $item = $carrito->items()->create([
                'id_producto' => $request->id_producto,
                'id_variante' => $request->id_variante,
                'cantidad' => $request->cantidad,
                'precio_unitario' => $precioUnitario,
            ]);
        }

        $carrito->load('items.producto', 'items.variante', 'cupon');

        return response()->json([
            'success' => true,
            'message' => 'Producto agregado al carrito',
            'data' => $this->formatCarrito($carrito)
        ]);
    }

    public function actualizar(Request $request, $itemId): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'cantidad' => 'required|integer|min:1|max:10',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $carrito = $this->obtenerCarrito($request);
        $item = $carrito->items()->find($itemId);

        if (!$item) {
            return response()->json([
                'success' => false,
                'message' => 'Item no encontrado en el carrito'
            ], 404);
        }

        // Verificar stock
        $stockDisponible = $item->variante ? $item->variante->stock : $item->producto->stock;

        if ($stockDisponible < $request->cantidad) {
            return response()->json([
                'success' => false,
                'message' => 'Stock insuficiente',
                'data' => ['stock_disponible' => $stockDisponible]
            ], 400);
        }

        $item->cantidad = $request->cantidad;
        $item->save();

        $carrito->load('items.producto', 'items.variante', 'cupon');

        return response()->json([
            'success' => true,
            'message' => 'Cantidad actualizada',
            'data' => $this->formatCarrito($carrito)
        ]);
    }

    public function eliminar(Request $request, $itemId): JsonResponse
    {
        $carrito = $this->obtenerCarrito($request);
        $item = $carrito->items()->find($itemId);

        if (!$item) {
            return response()->json([
                'success' => false,
                'message' => 'Item no encontrado en el carrito'
            ], 404);
        }

        $item->delete();

        $carrito->load('items.producto', 'items.variante', 'cupon');

        return response()->json([
            'success' => true,
            'message' => 'Producto eliminado del carrito',
            'data' => $this->formatCarrito($carrito)
        ]);
    }

    public function vaciar(Request $request): JsonResponse
    {
        $carrito = $this->obtenerCarrito($request);
        $carrito->items()->delete();
        $carrito->codigo_cupon = null;
        $carrito->save();

        return response()->json([
            'success' => true,
            'message' => 'Carrito vaciado',
            'data' => $this->formatCarrito($carrito)
        ]);
    }

    public function aplicarCupon(Request $request): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'codigo' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Código de cupón requerido'
            ], 422);
        }

        $cupon = Cupon::where('codigo', strtoupper($request->codigo))
                      ->activos()
                      ->first();

        if (!$cupon) {
            return response()->json([
                'success' => false,
                'message' => 'Cupón inválido o expirado'
            ], 400);
        }

        $carrito = $this->obtenerCarrito($request);
        $subtotal = $carrito->subtotal;

        // Validar mínimo de compra
        if ($cupon->minimo_compra && $subtotal < $cupon->minimo_compra) {
            return response()->json([
                'success' => false,
                'message' => "El monto mínimo para este cupón es \${$cupon->minimo_compra}"
            ], 400);
        }

        // Validar usos
        if ($cupon->usos_totales && $cupon->usos_actuales >= $cupon->usos_totales) {
            return response()->json([
                'success' => false,
                'message' => 'Este cupón ha alcanzado su límite de usos'
            ], 400);
        }

        $carrito->codigo_cupon = $cupon->id;
        $carrito->save();

        $carrito->load('items.producto', 'items.variante', 'cupon');

        return response()->json([
            'success' => true,
            'message' => 'Cupón aplicado correctamente',
            'data' => $this->formatCarrito($carrito)
        ]);
    }

    public function removerCupon(Request $request): JsonResponse
    {
        $carrito = $this->obtenerCarrito($request);
        $carrito->codigo_cupon = null;
        $carrito->save();

        $carrito->load('items.producto', 'items.variante', 'cupon');

        return response()->json([
            'success' => true,
            'message' => 'Cupón removido',
            'data' => $this->formatCarrito($carrito)
        ]);
    }

    private function obtenerCarrito(Request $request): Carrito
    {
        $user = auth()->user();

        if ($user) {
            $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente);

            if ($idClienteConsulta) {
                $carrito = Carrito::firstOrCreate(
                    ['id_cliente' => $idClienteConsulta],
                    ['session_id' => null]
                );
            }
        }

        if (!isset($carrito)) {
            $sessionId = $request->header('X-Session-ID') ?? $request->session_id ?? session()->getId();
            $carrito = Carrito::firstOrCreate(
                ['session_id' => $sessionId],
                ['id_cliente' => null]
            );
        }

        $carrito->load('items.producto', 'items.variante', 'cupon');

        return $carrito;
    }

    /**
     * Resuelve el id_cliente a consultar según el rol del usuario
     */
    private function resolverIdCliente($user, ?string $idClienteSolicitado): ?int
    {
        // Si es admin y solicita un cliente específico, usar ese
        if ($user->isAdmin() && $idClienteSolicitado) {
            return (int) $idClienteSolicitado;
        }

        // Para usuarios normales o admin sin id_cliente, buscar su propio cliente
        $cliente = Cliente::where('id_usuario', $user->id)->first();

        return $cliente?->id;
    }

    private function formatCarrito(Carrito $carrito): array
    {
        $items = $carrito->items->map(function ($item) {
            return [
                'id' => $item->id,
                'id_producto' => $item->id_producto,
                'id_variante' => $item->id_variante,
                'cantidad' => $item->cantidad,
                'precio_unitario' => (float)$item->precio_unitario,
                'subtotal' => (float)$item->subtotal,
                'producto' => [
                    'id' => $item->producto->id,
                    'nombre' => $item->producto->nombre,
                    'slug' => $item->producto->slug,
                    'imagen_principal' => $item->producto->imagen_principal,
                    'stock' => $item->producto->stock,
                ],
                'variante' => $item->variante ? [
                    'id' => $item->variante->id,
                    'nombre' => $item->variante->nombre,
                    'atributos' => $item->variante->atributos,
                    'stock' => $item->variante->stock,
                ] : null,
            ];
        });

        $subtotal = $carrito->subtotal;
        $descuento = 0;

        if ($carrito->cupon) {
            $descuento = $carrito->cupon->calcularDescuento($subtotal);
        }

        return [
            'id' => $carrito->id,
            'items' => $items,
            'items_count' => $carrito->total_items,
            'subtotal' => (float)$subtotal,
            'descuento' => (float)$descuento,
            'total' => (float)($subtotal - $descuento),
            'cupon' => $carrito->cupon ? [
                'id' => $carrito->cupon->id,
                'codigo' => $carrito->cupon->codigo,
                'tipo' => $carrito->cupon->tipo,
                'valor' => (float)$carrito->cupon->valor,
                'descuento_aplicado' => (float)$descuento,
            ] : null,
        ];
    }
}
