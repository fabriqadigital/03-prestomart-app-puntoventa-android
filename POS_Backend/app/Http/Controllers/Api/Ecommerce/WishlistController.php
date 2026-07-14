<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Wishlist;
use App\Models\Ecommerce\Producto;
use App\Models\Ecommerce\Cliente;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class WishlistController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        if (!$cliente) {
            return response()->json([
                'success' => true,
                'data' => []
            ]);
        }

        $wishlist = Wishlist::where('id_cliente', $cliente->id)
                            ->with(['producto.categoria', 'producto.marca'])
                            ->latest()
                            ->get();

        return response()->json([
            'success' => true,
            'data' => $wishlist->map(function ($item) {
                return $this->formatWishlistItem($item);
            })
        ]);
    }

    public function agregar(Request $request): JsonResponse
    {
        $request->validate([
            'id_producto' => 'required|exists:ecommerce_productos,id',
        ]);

        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        if (!$cliente) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $producto = Producto::activos()->find($request->id_producto);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no disponible'
            ], 404);
        }

        // Verificar si ya está en wishlist
        $existe = Wishlist::where('id_cliente', $cliente->id)
                          ->where('id_producto', $request->id_producto)
                          ->exists();

        if ($existe) {
            return response()->json([
                'success' => false,
                'message' => 'El producto ya está en tu lista de deseos'
            ], 400);
        }

        $wishlistItem = Wishlist::create([
            'id_cliente' => $cliente->id,
            'id_producto' => $request->id_producto,
        ]);

        $wishlistItem->load(['producto.categoria', 'producto.marca']);

        return response()->json([
            'success' => true,
            'message' => 'Producto agregado a la lista de deseos',
            'data' => $this->formatWishlistItem($wishlistItem)
        ], 201);
    }

    public function eliminar($productoId): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        if (!$cliente) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $item = Wishlist::where('id_cliente', $cliente->id)
                        ->where('id_producto', $productoId)
                        ->first();

        if (!$item) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado en la lista de deseos'
            ], 404);
        }

        $item->delete();

        return response()->json([
            'success' => true,
            'message' => 'Producto eliminado de la lista de deseos'
        ]);
    }

    public function toggle(Request $request): JsonResponse
    {
        $request->validate([
            'id_producto' => 'required|exists:ecommerce_productos,id',
        ]);

        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        if (!$cliente) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $item = Wishlist::where('id_cliente', $cliente->id)
                        ->where('id_producto', $request->id_producto)
                        ->first();

        if ($item) {
            $item->delete();
            return response()->json([
                'success' => true,
                'message' => 'Producto eliminado de la lista de deseos',
                'data' => ['in_wishlist' => false]
            ]);
        }

        $producto = Producto::activos()->find($request->id_producto);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no disponible'
            ], 404);
        }

        Wishlist::create([
            'id_cliente' => $cliente->id,
            'id_producto' => $request->id_producto,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Producto agregado a la lista de deseos',
            'data' => ['in_wishlist' => true]
        ]);
    }

    public function verificar($productoId): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        if (!$cliente) {
            return response()->json([
                'success' => true,
                'data' => ['in_wishlist' => false]
            ]);
        }

        $existe = Wishlist::where('id_cliente', $cliente->id)
                          ->where('id_producto', $productoId)
                          ->exists();

        return response()->json([
            'success' => true,
            'data' => ['in_wishlist' => $existe]
        ]);
    }

    public function vaciar(): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        if (!$cliente) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        Wishlist::where('id_cliente', $cliente->id)->delete();

        return response()->json([
            'success' => true,
            'message' => 'Lista de deseos vaciada'
        ]);
    }

    private function formatWishlistItem($item): array
    {
        $producto = $item->producto;

        return [
            'id' => $item->id,
            'id_producto' => $producto->id,
            'fecha_agregado' => $item->created_at->format('Y-m-d H:i:s'),
            'producto' => [
                'id' => $producto->id,
                'nombre' => $producto->nombre,
                'slug' => $producto->slug,
                'descripcion_corta' => $producto->descripcion_corta,
                'precio' => (float)$producto->precio,
                'precio_oferta' => $producto->precio_oferta ? (float)$producto->precio_oferta : null,
                'precio_final' => (float)$producto->precio_final,
                'imagen_principal' => $producto->imagen_principal,
                'stock' => $producto->stock,
                'disponible' => $producto->disponible,
                'categoria' => $producto->categoria ? [
                    'id' => $producto->categoria->id,
                    'nombre' => $producto->categoria->nombre,
                ] : null,
                'marca' => $producto->marca ? [
                    'id' => $producto->marca->id,
                    'nombre' => $producto->marca->nombre,
                ] : null,
            ],
        ];
    }
}
