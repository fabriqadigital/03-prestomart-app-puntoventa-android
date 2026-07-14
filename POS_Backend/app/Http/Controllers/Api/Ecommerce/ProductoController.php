<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Producto;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class ProductoController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $query = Producto::activos()->with(['categoria', 'marca']);

        // Filtros
        if ($request->has('categoria_id')) {
            $query->where('categoria_id', $request->categoria_id);
        }

        if ($request->has('marca_id')) {
            $query->where('marca_id', $request->marca_id);
        }

        if ($request->has('precio_min')) {
            $query->where('precio', '>=', $request->precio_min);
        }

        if ($request->has('precio_max')) {
            $query->where('precio', '<=', $request->precio_max);
        }

        if ($request->boolean('destacados', false)) {
            $query->destacados();
        }

        if ($request->boolean('nuevos', false)) {
            $query->nuevos();
        }

        if ($request->boolean('en_oferta', false)) {
            $query->enOferta();
        }

        if ($request->boolean('con_stock', true)) {
            $query->conStock();
        }

        // Búsqueda
        if ($request->has('buscar')) {
            $query->buscar($request->buscar);
        }

        // Ordenamiento
        $ordenar = $request->input('ordenar', 'created_at');
        $direccion = $request->input('direccion', 'desc');

        switch ($ordenar) {
            case 'precio':
                $query->orderByRaw('COALESCE(precio_oferta, precio) ' . $direccion);
                break;
            case 'nombre':
                $query->orderBy('nombre', $direccion);
                break;
            case 'popularidad':
                $query->withCount(['reviews' => function ($q) {
                    $q->where('aprobado', true);
                }])->orderBy('reviews_count', $direccion);
                break;
            case 'rating':
                $query->withAvg(['reviews' => function ($q) {
                    $q->where('aprobado', true);
                }], 'rating')->orderBy('reviews_avg_rating', $direccion);
                break;
            default:
                $query->orderBy('created_at', $direccion);
        }

        $perPage = min($request->input('per_page', 20), 50);
        $productos = $query->paginate($perPage);

        return response()->json([
            'success' => true,
            'data' => collect($productos->items())->map(function ($producto) {
                return $this->formatProducto($producto);
            }),
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
        $producto = Producto::with([
            'categoria',
            'marca',
            'variantes' => function ($q) {
                $q->activas();
            },
            'reviews' => function ($q) {
                $q->aprobadas()->latest()->limit(5);
            },
            'reviews.cliente'
        ])
        ->withCount(['reviews' => function ($q) {
            $q->where('aprobado', true);
        }])
        ->withAvg(['reviews' => function ($q) {
            $q->where('aprobado', true);
        }], 'rating')
        ->find($id);

        if (!$producto || !$producto->activo) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $this->formatProducto($producto, true)
        ]);
    }

    public function destacados(Request $request): JsonResponse
    {
        $limite = min($request->input('limite', 10), 20);

        $productos = Producto::activos()
            ->destacados()
            ->conStock()
            ->with(['categoria', 'marca'])
            ->limit($limite)
            ->get();

        return response()->json([
            'success' => true,
            'data' => $productos->map(function ($producto) {
                return $this->formatProducto($producto);
            })
        ]);
    }

    public function nuevos(Request $request): JsonResponse
    {
        $limite = min($request->input('limite', 10), 20);

        $productos = Producto::activos()
            ->nuevos()
            ->conStock()
            ->with(['categoria', 'marca'])
            ->latest()
            ->limit($limite)
            ->get();

        return response()->json([
            'success' => true,
            'data' => $productos->map(function ($producto) {
                return $this->formatProducto($producto);
            })
        ]);
    }

    public function ofertas(Request $request): JsonResponse
    {
        $perPage = min($request->input('per_page', 20), 50);

        $productos = Producto::activos()
            ->enOferta()
            ->conStock()
            ->with(['categoria', 'marca'])
            ->paginate($perPage);

        return response()->json([
            'success' => true,
            'data' => collect($productos->items())->map(function ($producto) {
                return $this->formatProducto($producto);
            }),
            'meta' => [
                'current_page' => $productos->currentPage(),
                'last_page' => $productos->lastPage(),
                'per_page' => $productos->perPage(),
                'total' => $productos->total(),
            ]
        ]);
    }

    public function buscar(Request $request): JsonResponse
    {
        $termino = $request->input('q', '');

        if (strlen($termino) < 2) {
            return response()->json([
                'success' => false,
                'message' => 'El término de búsqueda debe tener al menos 2 caracteres'
            ], 400);
        }

        $perPage = min($request->input('per_page', 20), 50);

        $productos = Producto::activos()
            ->buscar($termino)
            ->with(['categoria', 'marca'])
            ->paginate($perPage);

        return response()->json([
            'success' => true,
            'data' => collect($productos->items())->map(function ($producto) {
                return $this->formatProducto($producto);
            }),
            'meta' => [
                'current_page' => $productos->currentPage(),
                'last_page' => $productos->lastPage(),
                'per_page' => $productos->perPage(),
                'total' => $productos->total(),
                'termino' => $termino,
            ]
        ]);
    }

    public function relacionados($id, Request $request): JsonResponse
    {
        $producto = Producto::find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        $limite = min($request->input('limite', 8), 20);

        $relacionados = Producto::activos()
            ->conStock()
            ->where('id_producto', '!=', $id)
            ->where(function ($q) use ($producto) {
                $q->where('categoria_id', $producto->categoria_id)
                  ->orWhere('marca_id', $producto->marca_id);
            })
            ->with(['categoria', 'marca'])
            ->inRandomOrder()
            ->limit($limite)
            ->get();

        return response()->json([
            'success' => true,
            'data' => $relacionados->map(function ($producto) {
                return $this->formatProducto($producto);
            })
        ]);
    }

    public function reviews($id, Request $request): JsonResponse
    {
        $producto = Producto::find($id);

        if (!$producto) {
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrado'
            ], 404);
        }

        $perPage = min($request->input('per_page', 10), 50);

        $reviews = $producto->reviews()
            ->aprobadas()
            ->with('cliente:id,nombre,apellido,avatar')
            ->latest()
            ->paginate($perPage);

        $stats = [
            'promedio' => round($producto->reviews()->aprobadas()->avg('rating') ?? 0, 1),
            'total' => $producto->reviews()->aprobadas()->count(),
            'distribucion' => [
                5 => $producto->reviews()->aprobadas()->where('rating', 5)->count(),
                4 => $producto->reviews()->aprobadas()->where('rating', 4)->count(),
                3 => $producto->reviews()->aprobadas()->where('rating', 3)->count(),
                2 => $producto->reviews()->aprobadas()->where('rating', 2)->count(),
                1 => $producto->reviews()->aprobadas()->where('rating', 1)->count(),
            ]
        ];

        return response()->json([
            'success' => true,
            'data' => [
                'reviews' => $reviews->items(),
                'stats' => $stats,
            ],
            'meta' => [
                'current_page' => $reviews->currentPage(),
                'last_page' => $reviews->lastPage(),
                'per_page' => $reviews->perPage(),
                'total' => $reviews->total(),
            ]
        ]);
    }

    private function formatProducto($producto, $detalle = false): array
    {
        $data = [
            'id_producto' => $producto->id_producto,
            'nombre' => $producto->nombre,
            'slug' => $producto->slug,
            'descripcion_corta' => $producto->descripcion_corta,
            'precio' => (float)$producto->precio,
            'precio_oferta' => $producto->precio_oferta ? (float)$producto->precio_oferta : null,
            'precio_final' => (float)$producto->precio_final,
            'descuento_porcentaje' => $producto->descuento_porcentaje,
            'imagen_principal' => $producto->imagen_principal,
            'stock' => $producto->stock,
            'disponible' => $producto->disponible,
            'destacado' => $producto->destacado,
            'nuevo' => $producto->nuevo,
            'rating' => round($producto->reviews_avg_rating ?? $producto->rating_promedio, 1),
            'reviews_count' => $producto->reviews_count ?? $producto->reviewsCount,
            'categoria' => $producto->categoria ? [
                'id_categoria' => $producto->categoria->id_categoria,
                'nombre' => $producto->categoria->nombre,
                'slug' => $producto->categoria->slug,
            ] : null,
            'marca' => $producto->marca ? [
                'id_marca' => $producto->marca->id_marca,
                'nombre' => $producto->marca->nombre,
                'logo_url' => $producto->marca->logo_url,
            ] : null,
        ];

        if ($detalle) {
            $data['descripcion'] = $producto->descripcion;
            $data['sku'] = $producto->sku;
            $data['peso'] = $producto->peso;
            $data['dimensiones'] = $producto->dimensiones;
            $data['imagenes'] = $producto->imagenes ?? [];
            $data['especificaciones'] = $producto->especificaciones ?? [];
            $data['variantes'] = $producto->variantes->map(function ($v) {
                return [
                    'id_producto' => $v->id_producto,
                    'nombre' => $v->nombre,
                    'sku' => $v->sku,
                    'precio_adicional' => (float)$v->precio_adicional,
                    'stock' => $v->stock,
                    'atributos' => $v->atributos,
                ];
            });
            $data['reviews_recientes'] = $producto->reviews->map(function ($r) {
                return [
                    'id_producto' => $r->id_producto,
                    'rating' => $r->rating,
                    'titulo' => $r->titulo,
                    'comentario' => $r->comentario,
                    'verificado' => $r->verificado,
                    'cliente' => [
                        'nombre' => $r->cliente->nombre,
                        'avatar' => $r->cliente->avatar,
                    ],
                    'fecha' => $r->created_at->format('Y-m-d'),
                ];
            });
        }

        return $data;
    }
}
