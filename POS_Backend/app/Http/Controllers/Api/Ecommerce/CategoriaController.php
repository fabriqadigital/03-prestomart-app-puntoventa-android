<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Categoria;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class CategoriaController extends Controller
{
    // ────────────────────────────────────────────────
    //           MÉTODOS PÚBLICOS (frontend / ecommerce)
    // ────────────────────────────────────────────────

    public function index(Request $request): JsonResponse
    {
        $query = Categoria::activas()->ordenadas();

        if ($request->boolean('principales', false)) {
            $query->principales();
        }

        if ($request->boolean('con_hijos', false)) {
            $query->with(['hijos' => fn($q) => $q->activas()->ordenadas()]);
        }

        if ($request->boolean('con_productos', false)) {
            $query->withCount(['productos' => fn($q) => $q->where('activo', true)]);
        }

        $categorias = $query->get();

        return response()->json([
            'success' => true,
            'data'    => $categorias->map(fn($cat) => $this->formatCategoria($cat))
        ]);
    }

    public function show($id): JsonResponse
    {
        $categoria = Categoria::with([
            'hijos' => fn($q) => $q->activas()->ordenadas(),
            'padre'
        ])
            ->withCount(['productos' => fn($q) => $q->where('activo', true)])
            ->find($id);

        if (!$categoria) {
            return response()->json([
                'success' => false,
                'message' => 'Categoría no encontrada'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data'    => $this->formatCategoria($categoria, true)
        ]);
    }

    public function productos($id, Request $request): JsonResponse
    {
        $categoria = Categoria::find($id);

        if (!$categoria) {
            return response()->json([
                'success' => false,
                'message' => 'Categoría no encontrada'
            ], 404);
        }

        $query = $categoria->productos()
            ->activos()
            ->with(['marca', 'categoria']);

        if ($request->has('marca_id')) {
            $query->where('marca_id', $request->marca_id);
        }

        if ($request->has('precio_min')) {
            $query->where('precio', '>=', $request->precio_min);
        }

        if ($request->has('precio_max')) {
            $query->where('precio', '<=', $request->precio_max);
        }

        if ($request->boolean('en_oferta', false)) {
            $query->enOferta();
        }

        if ($request->boolean('con_stock', true)) {
            $query->conStock();
        }

        $ordenar   = $request->input('ordenar', 'created_at');
        $direccion = $request->input('direccion', 'desc');

        match ($ordenar) {
            'precio'      => $query->orderBy('precio', $direccion),
            'nombre'      => $query->orderBy('nombre', $direccion),
            'popularidad' => $query->withCount('reviews')->orderBy('reviews_count', $direccion),
            default       => $query->orderBy('created_at', $direccion),
        };

        $perPage = min($request->input('per_page', 20), 50);
        $productos = $query->paginate($perPage);

        return response()->json([
            'success' => true,
            'data'    => $productos->items(),
            'meta'    => [
                'current_page' => $productos->currentPage(),
                'last_page'    => $productos->lastPage(),
                'per_page'     => $productos->perPage(),
                'total'        => $productos->total(),
            ]
        ]);
    }

    // ────────────────────────────────────────────────
    //           MÉTODOS EXCLUSIVOS ADMIN
    // ────────────────────────────────────────────────

    public function indexAdmin(Request $request): JsonResponse
    {
        $query = Categoria::query()
            // ->withTrashed()   // descomentar SOLO si usas SoftDeletes
            ->ordenadas();

        if ($request->boolean('con_hijos', true)) {
            $query->with(['hijos' => fn($q) => $q->ordenadas()]);
        }

        // Opcional: permitir ver inactivas
        // if ($request->boolean('incluir_inactivas', false)) {
        //     $query->orWhere('activo', false);
        // }

        $categorias = $query->get();

        return response()->json([
            'success' => true,
            'data'    => $categorias->map(fn($cat) => $this->formatCategoria($cat, true))
        ]);
    }

    public function showAdmin($id): JsonResponse
    {
        $categoria = Categoria::query()
            // ->withTrashed()   // descomentar SOLO si usas SoftDeletes
            ->with([
                'hijos' => fn($q) => $q->ordenadas(),
                'padre',
            ])
            ->withCount('productos')
            ->findOrFail($id);

        return response()->json([
            'success' => true,
            'data'    => $this->formatCategoria($categoria, true)
        ]);
    }

    public function store(Request $request): JsonResponse
    {
        
        $validated = $request->validate([
            'nombre'        => 'required|string|max:120|unique:ecommerce_categorias,nombre',
            'slug'          => 'nullable|string|max:150|unique:ecommerce_categorias,slug',
            'padre_id'      => 'nullable|exists:ecommerce_categorias,id_categoria',
            'descripcion'   => 'nullable|string|max:1500',
            'activo'        => 'boolean',
            'orden'         => 'nullable|integer|min:0',
            // 'imagen'        => 'nullable|image|mimes:jpg,jpeg,png,webp|max:2048',
            ]);
            
            Log::channel('stderr')->info("test" . json_encode($request->all()));

        if (empty($validated['slug'])) {
            $baseSlug = Str::slug($validated['nombre']);
            $validated['slug'] = $baseSlug;
            $count = 1;
            while (Categoria::where('slug', $validated['slug'])->exists()) {
                $validated['slug'] = $baseSlug . '-' . $count++;
            }
        }

        if ($request->hasFile('imagen') && $request->file('imagen')->isValid()) {
            $path = $request->file('imagen')->store('categorias', 'public');
            $validated['imagen_url'] = Storage::url($path);
        }

        $categoria = Categoria::create($validated);

        return response()->json([
            'success' => true,
            'message' => 'Categoría creada correctamente',
            'data'    => $this->formatCategoria($categoria, true)
        ], 201);
    }

    public function update(Request $request, $id): JsonResponse
    {
        $categoria = Categoria::findOrFail($id);

        $validated = $request->validate([
            'nombre'        => 'sometimes|required|string|max:120',
            'slug'          => [
                'nullable',
                'string',
                'max:150',
                Rule::unique('categorias', 'slug')->ignore($categoria->id_categoria),
            ],
            'padre_id'      => 'nullable|exists:categorias,id_categoria',
            'descripcion'   => 'nullable|string|max:1500',
            'activo'        => 'sometimes|boolean',
            'orden'         => 'nullable|integer|min:0',
            'imagen'        => 'nullable|image|mimes:jpg,jpeg,png,webp|max:2048',
        ]);

        if (isset($validated['padre_id']) && $validated['padre_id'] == $categoria->id_categoria) {
            return response()->json([
                'success' => false,
                'message' => 'Una categoría no puede ser su propio padre'
            ], 422);
        }

        if ($request->hasFile('imagen') && $request->file('imagen')->isValid()) {
            if ($categoria->imagen_url) {
                $oldPath = str_replace('/storage/', '', $categoria->imagen_url);
                Storage::disk('public')->delete($oldPath);
            }
            $path = $request->file('imagen')->store('categorias', 'public');
            $validated['imagen_url'] = Storage::url($path);
        }

        $categoria->update($validated);

        return response()->json([
            'success' => true,
            'message' => 'Categoría actualizada correctamente',
            'data'    => $this->formatCategoria($categoria->fresh(), true)
        ]);
    }

    public function destroy($id): JsonResponse
    {
        $categoria = Categoria::findOrFail($id);

        if ($categoria->productos()->exists() || $categoria->hijos()->exists()) {
            return response()->json([
                'success' => false,
                'message' => 'No se puede eliminar: tiene productos o subcategorías asociadas.'
            ], 422);
        }

        $categoria->delete();   // soft delete si está activado en el modelo

        return response()->json([
            'success' => true,
            'message' => 'Categoría eliminada correctamente'
        ]);
    }

    // Opcional: solo si usas SoftDeletes en el modelo Categoria
    public function restore($id): JsonResponse
    {
        $categoria = Categoria::withTrashed()->findOrFail($id);

        if (!$categoria->trashed()) {
            return response()->json([
                'success' => false,
                'message' => 'La categoría no está eliminada'
            ], 400);
        }

        $categoria->restore();

        return response()->json([
            'success' => true,
            'message' => 'Categoría restaurada correctamente',
            'data'    => $this->formatCategoria($categoria, true)
        ]);
    }

    // ────────────────────────────────────────────────
    //                   FORMATEADOR
    // ────────────────────────────────────────────────

    private function formatCategoria($categoria, bool $detalle = false): array
    {
        $data = [
            'id_categoria'              => $categoria->id_categoria,
            'nombre'          => $categoria->nombre,
            'slug'            => $categoria->slug,
            'imagen_url'      => $categoria->imagen_url,
            'activo'          => $categoria->activo ?? true,
            'orden'           => $categoria->orden ?? 0,
            'productos_count' => $categoria->productos_count ?? $categoria->productos_count ?? 0,
        ];

        if ($detalle) {
            $data['descripcion'] = $categoria->descripcion;
            $data['padre'] = $categoria->padre ? [
                'id_categoria'     => $categoria->padre->id_categoria,
                'nombre' => $categoria->padre->nombre,
                'slug'   => $categoria->padre->slug,
            ] : null;
        }

        if ($categoria->relationLoaded('hijos') && $categoria->hijos->isNotEmpty()) {
            $data['subcategorias'] = $categoria->hijos->map(
                fn($hijo) => $this->formatCategoria($hijo)
            );
        }

        return $data;
    }
}