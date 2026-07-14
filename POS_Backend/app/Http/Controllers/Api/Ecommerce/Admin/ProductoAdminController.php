<?php

namespace App\Http\Controllers\Api\Ecommerce\Admin;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Producto;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class ProductoAdminController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $query = Producto::with(['categoria', 'marca']);
        // Filtros
        if ($request->has('categoria_id')) {
            $query->where('id_categoria', $request->categoria_id);
        }

        if ($request->has('id_marca')) {
            $query->where('id_marca', $request->id_marca);
        }

        if ($request->has('activo')) {
            $query->where('activo', $request->boolean('activo'));
        }

        if ($request->has('destacado')) {
            $query->where('destacado', $request->boolean('destacado'));
        }

        if ($request->has('buscar')) {
            $buscar = $request->buscar;
            $query->where(function ($q) use ($buscar) {
                $q->where('nombre', 'like', "%{$buscar}%")
                  ->orWhere('sku', 'like', "%{$buscar}%")
                  ->orWhere('descripcion', 'like', "%{$buscar}%");
            });
        }

        $perPage = min($request->input('per_page', 20), 100);
        $productos = $query->latest()->paginate($perPage);


            
        return response()->json([
            'success' => true,
            'data' => $productos->items(),
            'meta' => [
                'current_page' => $productos->currentPage(),
                'last_page' => $productos->lastPage(),
                'per_page' => $productos->perPage(),
                'total' => $productos->total(),
            ]
        ]);
    }

    public function show($id): JsonResponse
    {
        $producto = Producto::with(['categoria', 'marca', 'variantes'])->find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $producto
        ]);
    }

    public function store(Request $request): JsonResponse
    {

        $validator = Validator::make($request->all(), [
            'nombre' => 'required|string|max:255',
            'sku' => 'required|string|max:50|unique:ecommerce_productos,sku',
            'id_categoria' => 'nullable|exists:ecommerce_categorias,id_categoria',
            'id_marca' => 'nullable|exists:ecommerce_marcas,id_marca',
            'precio' => 'required|numeric|min:0',
            'precio_oferta' => 'nullable|numeric|min:0',
            'costo' => 'nullable|numeric|min:0',
            'stock' => 'nullable|integer|min:0',
            'stock_minimo' => 'nullable|integer|min:0',
            ]);
            
            Log::channel('stderr')->info("tes33333t" . json_encode($request->all()));
            
            if ($validator->fails()) {
                return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
                ], 422);
                }
                
                $data = $request->all();
                $data['slug'] = $data['slug'] ?? Str::slug($data['nombre']);
             
        $producto = Producto::create($data);

        return response()->json([
            'success' => true,
            'message' => 'Producto creado exitosamente',
            'data' => $producto->load(['categoria', 'marca'])
        ], 201);
    }

    public function update(Request $request, $id): JsonResponse
    {
        $producto = Producto::find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'nombre' => 'sometimes|string|max:255',
            'sku' => 'sometimes|string|max:50|unique:ecommerce_productos,sku,' . $id,
            'id_categoria' => 'nullable|exists:ecommerce_categorias,id_categoria',
            'id_marca' => 'nullable|exists:ecommerce_marcas,id_marca',
            'precio' => 'sometimes|numeric|min:0',
            'precio_oferta' => 'nullable|numeric|min:0',
            'costo' => 'nullable|numeric|min:0',
            'stock' => 'nullable|integer|min:0',
            'stock_minimo' => 'nullable|integer|min:0',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $data = $request->all();
        if (isset($data['nombre']) && !isset($data['slug'])) {
            $data['slug'] = Str::slug($data['nombre']);
        }

        $producto->update($data);

        return response()->json([
            'success' => true,
            'message' => 'Producto actualizado',
            'data' => $producto->fresh()->load(['categoria', 'marca'])
        ]);
    }

    public function destroy($id): JsonResponse
    {
        $producto = Producto::find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        $producto->delete();

        return response()->json([
            'success' => true,
            'message' => 'Producto eliminado'
        ]);
    }

    public function toggleActivo($id): JsonResponse
    {
        $producto = Producto::find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        $producto->activo = !$producto->activo;
        $producto->save();

        return response()->json([
            'success' => true,
            'message' => $producto->activo ? 'Producto activado' : 'Producto desactivado',
            'data' => ['activo' => $producto->activo]
        ]);
    }

    public function toggleDestacado($id): JsonResponse
    {
        $producto = Producto::find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        $producto->destacado = !$producto->destacado;
        $producto->save();

        return response()->json([
            'success' => true,
            'message' => $producto->destacado ? 'Producto destacado' : 'Producto quitado de destacados',
            'data' => ['destacado' => $producto->destacado]
        ]);
    }

    public function updateStock(Request $request, $id): JsonResponse
    {
        $producto = Producto::find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'stock' => 'required|integer|min:0',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors' => $validator->errors()
            ], 422);
        }

        $producto->stock = $request->stock;
        $producto->save();

        return response()->json([
            'success' => true,
            'message' => 'Stock actualizado',
            'data' => ['stock' => $producto->stock]
        ]);
    }
}
