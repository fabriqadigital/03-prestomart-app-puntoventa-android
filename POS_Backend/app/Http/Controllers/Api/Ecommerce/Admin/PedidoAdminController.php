<?php

namespace App\Http\Controllers\Api\Ecommerce\Admin;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Pedido;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

class PedidoAdminController extends Controller
{
    public function cambiarEstado(Request $request, $id): JsonResponse
    {
        $pedido = Pedido::find($id);

        if (!$pedido) {
            return response()->json([
                'success' => false,
                'message' => 'Pedido no encontrado'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'estado' => 'required|in:pendiente,procesando,enviado,entregado,cancelado,reembolsado',
            'comentario' => 'nullable|string|max:500',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors' => $validator->errors()
            ], 422);
        }

        $pedido->cambiarEstado(
            $request->estado,
            $request->comentario,
            auth()->user()->name ?? 'admin'
        );

        return response()->json([
            'success' => true,
            'message' => 'Estado actualizado',
            'data' => [
                'estado' => $pedido->estado,
                'estado_label' => $pedido->estado_label,
            ]
        ]);
    }

    public function marcarPagado(Request $request, $id): JsonResponse
    {
        Log::channel('stderr')->info("marcarPagado called for id: {$id}");
        $pedido = Pedido::find($id);

        if (!$pedido) {
            return response()->json([
                'success' => false,
                'message' => 'Pedido no encontrado'
            ], 404);
        }

        // Si el request trae explícitamente pagado = false, entonces desmarcamos
        if ($request->has('pagado') && $request->input('pagado') === false) {
            // Si ya está sin pagar, informar
            if (!$pedido->pagado) {
                return response()->json([
                    'success' => false,
                    'message' => 'El pedido ya está sin marcar como pagado'
                ], 400);
            }

            // Revertir pago
            $pedido->pagado = false;
            $pedido->fecha_pago = null;
            $pedido->save();

            // Registrar historial de cambio (manteniendo el mismo estado pero con comentario)
            try {
                $pedido->historial()->create([
                    'estado_anterior' => $pedido->estado,
                    'estado_nuevo' => $pedido->estado,
                    'comentario' => 'Pago revertido por administrador',
                    'creado_por' => auth()->user()->name ?? 'admin',
                ]);
            } catch (\Exception $e) {
                Log::channel('stderr')->warning('No se pudo crear historial al desmarcar pago: ' . $e->getMessage());
            }

            return response()->json([
                'success' => true,
                'message' => 'Pago removido del pedido',
                'data' => [
                    'pagado' => false,
                    'fecha_pago' => null,
                ]
            ]);
        }

        // Caso normal: marcar como pagado
        if ($pedido->pagado) {
            return response()->json([
                'success' => false,
                'message' => 'El pedido ya está marcado como pagado'
            ], 400);
        }

        $pedido->marcarComoPagado();

        return response()->json([
            'success' => true,
            'message' => 'Pedido marcado como pagado',
            'data' => [
                'pagado' => true,
                'fecha_pago' => $pedido->fecha_pago?->format('Y-m-d H:i:s'),
            ]
        ]);
    }
    
    /**
     * Actualizar (editar) pedido por administrador.
     * Campos permitidos: envio_direccion (array), id_metodo_envio, id_metodo_pago, notas
     */
    public function actualizar(Request $request, $id): JsonResponse
    {
        $pedido = Pedido::find($id);

        if (!$pedido) {
            return response()->json([
                'success' => false,
                'message' => 'Pedido no encontrado'
            ], 404);
        }

        // Sólo administradores (refuerzo extra)
        $user = auth()->user();
        if (!$user || !$user->isAdmin()) {
            return response()->json([
                'success' => false,
                'message' => 'No autorizado'
            ], 403);
        }

        $validator = Validator::make($request->all(), [
            'envio_direccion' => 'nullable|array',
            'id_metodo_envio' => 'nullable|integer',
            'id_metodo_pago' => 'nullable|integer',
            'notas' => 'nullable|string|max:1000',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors' => $validator->errors()
            ], 422);
        }

        // Actualizar campos permitidos
        $changed = false;
        if ($request->has('envio_direccion')) {
            $pedido->envio_direccion = $request->input('envio_direccion');
            $changed = true;
        }
        if ($request->has('id_metodo_envio')) {
            $pedido->id_metodo_envio = $request->input('id_metodo_envio');
            $changed = true;
        }
        if ($request->has('id_metodo_pago')) {
            $pedido->id_metodo_pago = $request->input('id_metodo_pago');
            $changed = true;
        }
        if ($request->has('notas')) {
            $pedido->notas = $request->input('notas');
            $changed = true;
        }

        if ($changed) {
            $pedido->save();
            // Registrar en historial de admin que se editó el pedido
            try {
                $pedido->historial()->create([
                    'estado_anterior' => $pedido->estado,
                    'estado_nuevo' => $pedido->estado,
                    'comentario' => 'Pedido editado por administrador',
                    'creado_por' => $user->name ?? 'admin',
                ]);
            } catch (\Exception $e) {
                Log::channel('stderr')->warning('No se pudo crear historial al actualizar pedido: ' . $e->getMessage());
            }
        }

        // Recargar relaciones importantes para devolver al frontend
        $pedido->load(['items.producto', 'metodoEnvio', 'metodoPago', 'historial', 'cliente.usuario']);

        return response()->json([
            'success' => true,
            'message' => 'Pedido actualizado',
            'data' => $pedido
        ]);
    }
}
