<?php

namespace App\Http\Controllers\Api\Ecommerce\Admin;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Marca;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\Storage;
use Illuminate\Validation\Rule;

class MarcaAdminController extends Controller
{
    /**
     * Listar todas las marcas
     */
    public function index(Request $request): JsonResponse
    {
        $query = Marca::query()->withCount('productos');

        if ($request->has('activo')) {
            $query->where('activo', $request->boolean('activo'));
        }

        if ($request->has('buscar')) {
            $query->where('nombre', 'like', "%{$request->buscar}%");
        }

        $marcas = $query->orderBy('nombre')->get();

        return response()->json([
            'success' => true,
            'data' => $marcas->map(fn($marca) => $this->formatMarca($marca))
        ]);
    }

    /**
     * Obtener una marca por ID
     */
    public function show($id): JsonResponse
    {
        $marca = Marca::withCount('productos')->find($id);

        if (!$marca) {
            return response()->json([
                'success' => false,
                'message' => 'Marca no encontrada'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $this->formatMarca($marca)
        ]);
    }

    /**
     * Crear una nueva marca
     */
    public function store(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'nombre'      => 'required|string|max:100|unique:ecommerce_marcas,nombre',
            'slug'        => 'nullable|string|max:120|unique:ecommerce_marcas,slug',
            'descripcion' => 'nullable|string|max:1000',
            'logo'        => 'nullable|string|max:255',
            'activo'      => 'boolean',
        ]);

        // Generar slug automáticamente si no se proporciona
        if (empty($validated['slug'])) {
            $baseSlug = Str::slug($validated['nombre']);
            $validated['slug'] = $baseSlug;
            $count = 1;
            while (Marca::where('slug', $validated['slug'])->exists()) {
                $validated['slug'] = $baseSlug . '-' . $count++;
            }
        }

        // Valor por defecto para activo
        if (!isset($validated['activo'])) {
            $validated['activo'] = true;
        }

        $marca = Marca::create($validated);

        return response()->json([
            'success' => true,
            'message' => 'Marca creada correctamente',
            'data' => $this->formatMarca($marca)
        ], 201);
    }

    /**
     * Actualizar una marca existente
     */
    public function update(Request $request, $id): JsonResponse
    {
        $marca = Marca::find($id);

        if (!$marca) {
            return response()->json([
                'success' => false,
                'message' => 'Marca no encontrada'
            ], 404);
        }

        $validated = $request->validate([
            'nombre'      => [
                'sometimes',
                'required',
                'string',
                'max:100',
                Rule::unique('ecommerce_marcas', 'nombre')->ignore($marca->id_marca, 'id_marca'),
            ],
            'slug'        => [
                'nullable',
                'string',
                'max:120',
                Rule::unique('ecommerce_marcas', 'slug')->ignore($marca->id_marca, 'id_marca'),
            ],
            'descripcion' => 'nullable|string|max:1000',
            'logo'        => 'nullable|string|max:255',
            'activo'      => 'sometimes|boolean',
        ]);

        $marca->update($validated);

        return response()->json([
            'success' => true,
            'message' => 'Marca actualizada correctamente',
            'data' => $this->formatMarca($marca->fresh())
        ]);
    }

    /**
     * Eliminar una marca
     */
    public function destroy($id): JsonResponse
    {
        $marca = Marca::find($id);

        if (!$marca) {
            return response()->json([
                'success' => false,
                'message' => 'Marca no encontrada'
            ], 404);
        }

        // Verificar si tiene productos asociados
        if ($marca->productos()->exists()) {
            return response()->json([
                'success' => false,
                'message' => 'No se puede eliminar: tiene productos asociados'
            ], 422);
        }

        $marca->delete();

        return response()->json([
            'success' => true,
            'message' => 'Marca eliminada correctamente'
        ]);
    }

    /**
     * Toggle estado activo de una marca
     */
    public function toggleActivo($id): JsonResponse
    {
        $marca = Marca::find($id);

        if (!$marca) {
            return response()->json([
                'success' => false,
                'message' => 'Marca no encontrada'
            ], 404);
        }

        $marca->activo = !$marca->activo;
        $marca->save();

        return response()->json([
            'success' => true,
            'message' => $marca->activo ? 'Marca activada' : 'Marca desactivada',
            'data' => $this->formatMarca($marca)
        ]);
    }

    /**
     * Formatear marca para respuesta
     */
    private function formatMarca(Marca $marca): array
    {
        return [
            'id_marca'        => $marca->id_marca,
            'nombre'          => $marca->nombre,
            'slug'            => $marca->slug,
            'descripcion'     => $marca->descripcion,
            'logo'            => $marca->logo,
            'activo'          => $marca->activo ?? true,
            'productos_count' => $marca->productos_count ?? 0,
            'created_at'      => $marca->created_at?->format('Y-m-d H:i:s'),
            'updated_at'      => $marca->updated_at?->format('Y-m-d H:i:s'),
        ];
    }
}
