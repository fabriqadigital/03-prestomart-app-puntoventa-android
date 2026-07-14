<?php

namespace App\Http\Controllers\Api\Ecommerce\Admin;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Producto;
use App\Models\Ecommerce\Cliente;
use App\Models\Ecommerce\Pedido;
use App\Models\Ecommerce\PedidoItem;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;

class DashboardAdminController extends Controller
{
    public function estadisticas(): JsonResponse
    {
        $mesActual = Carbon::now()->startOfMonth();
        $mesAnterior = Carbon::now()->subMonth()->startOfMonth();

        // Ventas del mes
        $ventasMes = Pedido::where('pagado', true)
            ->whereMonth('created_at', now()->month)
            ->whereYear('created_at', now()->year)
            ->sum('total');

        // Pedidos del mes
        $pedidosMes = Pedido::whereMonth('created_at', now()->month)
            ->whereYear('created_at', now()->year)
            ->count();

        // Total clientes
        $totalClientes = Cliente::where('activo', true)->count();

        // Total productos
        $totalProductos = Producto::where('activo', true)->count();

        // Pedidos por estado
        $pedidosPorEstado = Pedido::select('estado', DB::raw('count(*) as cantidad'))
            ->whereMonth('created_at', now()->month)
            ->whereYear('created_at', now()->year)
            ->groupBy('estado')
            ->get();

        return response()->json([
            'success' => true,
            'data' => [
                'ventas_mes' => (float)$ventasMes,
                'pedidos_mes' => $pedidosMes,
                'total_clientes' => $totalClientes,
                'total_productos' => $totalProductos,
                'pedidos_por_estado' => $pedidosPorEstado,
            ]
        ]);
    }

    public function ventas(Request $request): JsonResponse
    {
        $periodo = $request->input('periodo', 'mes');

        $query = Pedido::where('pagado', true);

        switch ($periodo) {
            case 'semana':
                $query->where('created_at', '>=', now()->subWeek());
                break;
            case 'mes':
            default:
                $query->whereMonth('created_at', now()->month)
                      ->whereYear('created_at', now()->year);
                break;
            case 'anio':
                $query->whereYear('created_at', now()->year);
                break;
        }

        $ventas = $query->select(
            DB::raw('DATE(created_at) as fecha'),
            DB::raw('SUM(total) as total'),
            DB::raw('COUNT(*) as pedidos')
        )
        ->groupBy('fecha')
        ->orderBy('fecha')
        ->get();

        return response()->json([
            'success' => true,
            'data' => $ventas
        ]);
    }

    public function productosMasVendidos(Request $request): JsonResponse
    {
        $limite = min($request->input('limite', 10), 50);

        $productos = PedidoItem::select(
            'id_producto',
            DB::raw('SUM(cantidad) as total_vendido')
        )
        ->join('ecommerce_pedidos', 'ecommerce_pedido_items.id_pedido', '=', 'ecommerce_pedidos.id')
        ->where('ecommerce_pedidos.pagado', true)
        ->whereNull('ecommerce_pedidos.deleted_at')
        ->groupBy('id_producto')
        ->orderByDesc('total_vendido')
        ->limit($limite)
        ->with('producto:id,nombre,imagen_principal,sku')
        ->get()
        ->map(function ($item) {
            return [
                'id' => $item->id_producto,
                'nombre' => $item->producto?->nombre,
                'imagen_principal' => $item->producto?->imagen_principal,
                'sku' => $item->producto?->sku,
                'total_vendido' => $item->total_vendido,
            ];
        });

        return response()->json([
            'success' => true,
            'data' => $productos
        ]);
    }

    public function pedidosRecientes(Request $request): JsonResponse
    {
        $limite = min($request->input('limite', 10), 50);

        $pedidos = Pedido::with(['cliente:id,nombre,apellido'])
            ->latest()
            ->limit($limite)
            ->get()
            ->map(function ($pedido) {
                return [
                    'id' => $pedido->id,
                    'numero_pedido' => $pedido->numero_pedido,
                    'estado' => $pedido->estado,
                    'total' => (float)$pedido->total,
                    'pagado' => $pedido->pagado,
                    'cliente' => [
                        'nombre' => $pedido->cliente?->nombre . ' ' . $pedido->cliente?->apellido,
                    ],
                    'fecha' => $pedido->created_at->format('Y-m-d H:i'),
                ];
            });

        return response()->json([
            'success' => true,
            'data' => $pedidos
        ]);
    }

    public function clientesNuevos(Request $request): JsonResponse
    {
        $limite = min($request->input('limite', 10), 50);

        $clientes = Cliente::where('activo', true)
            ->latest()
            ->limit($limite)
            ->get();

        return response()->json([
            'success' => true,
            'data' => $clientes
        ]);
    }

    public function productosBajoStock(Request $request): JsonResponse
    {
        $limite = min($request->input('limite', 10), 50);

        $productos = Producto::where('activo', true)
            ->whereColumn('stock', '<=', 'stock_minimo')
            ->orderBy('stock')
            ->limit($limite)
            ->get(['id', 'nombre', 'sku', 'imagen_principal', 'stock', 'stock_minimo']);

        return response()->json([
            'success' => true,
            'data' => $productos
        ]);
    }

    public function resumenPedidos(): JsonResponse
    {
        $resumen = [
            'pendientes' => Pedido::where('estado', 'pendiente')->count(),
            'procesando' => Pedido::where('estado', 'procesando')->count(),
            'enviados' => Pedido::where('estado', 'enviado')->count(),
            'entregados' => Pedido::where('estado', 'entregado')->count(),
            'cancelados' => Pedido::where('estado', 'cancelado')->count(),
        ];

        return response()->json([
            'success' => true,
            'data' => $resumen
        ]);
    }
}
