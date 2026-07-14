<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;

class CotizacionesController extends Controller
{
    /**
     * Obtener una cotización específica con sus items
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function obtener(Request $request): JsonResponse
    {
        try {
            $id = $request->id;
            
            // Obtener datos de la cotización
            $cotizacion = DB::selectOne(
                'SELECT * FROM finanzas_cotizacion 
                 WHERE id_cotizacion = ? AND Activo = "S"',
                [$id]
            );
            
            if (!$cotizacion) {
                return response()->json([
                    'success' => false,
                    'message' => 'Cotización no encontrada',
                    'result' => null
                ], 404);
            }
            
            // Obtener items de la cotización
            $items = DB::select(
                'SELECT * FROM finanzas_cotizacion_items 
                 WHERE id_cotizacion = ? AND Activo = "S" 
                 ORDER BY orden ASC',
                [$id]
            );
            
            // Combinar cotización con items
            $result = (array) $cotizacion;
            $result['items'] = $items;
            
            return response()->json([
                'success' => true,
                'message' => 'Cotización obtenida',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error en obtener cotización: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al obtener cotización',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Listar todas las cotizaciones
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function listar(Request $request): JsonResponse
    {
        try {
            $result = DB::select('CALL USP_FINANZAS_COTIZACIONES_LISTAR()');
            
            return response()->json([
                'success' => true,
                'message' => 'Listar cotizaciones',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error en listar cotizaciones: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al listar cotizaciones',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Crear una nueva cotización con sus items
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function crear(Request $request): JsonResponse
    {
        try {
            // Obtener id de usuario de la sesión/token
            $id_usuario = auth()->id() ?? 1;
            
            // Crear la cotización usando el SP mejorado
            $result = DB::select('CALL USP_FINANZAS_COTIZACIONES_CREAR_COMPLETO(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)', [
                $request->codigo ?? '',
                $request->version ?? '2.0',
                $request->proyecto,
                $request->etapa ?? '',
                $request->cliente,
                $request->ruc ?? '',
                $request->fecha,
                $request->direccion ?? '',
                $request->ciudad ?? 'LIMA',
                $request->provincia ?? 'LIMA',
                $request->validez_oferta ?? '3 DÍAS CALENDARIO',
                $request->tiempo_ejecucion ?? '10 DÍAS CALENDARIO',
                $request->forma_pago ?? 'PRESENTADA LA FACTURA PAGO A 15 DÍAS',
                $request->adelanto ?? '50% DE ADELANTO DEL MONTO TOTAL',
                $request->garantia ?? '',
                $request->observaciones ?? '',
                $request->costo_directo ?? 0,
                $request->gastos_generales ?? 11,
                $request->utilidad ?? 8,
                $request->subtotal ?? 0,
                $request->igv ?? 18,
                $request->total ?? 0,
                $id_usuario
            ]);
            
            // Verificar si hubo error
            if ($result[0]->id_cotizacion == -1) {
                return response()->json([
                    'success' => false,
                    'message' => $result[0]->mensaje ?? 'Error al crear cotización',
                ], 500);
            }
            
            $id_cotizacion = $result[0]->id_cotizacion;
            
            // Crear los items si existen
            if ($request->has('items') && is_array($request->items)) {
                foreach ($request->items as $index => $item) {
                    try {
                        DB::select('CALL USP_FINANZAS_COTIZACIONES_ITEMS_CREAR(?, ?, ?, ?, ?, ?, ?, ?, ?)', [
                            $id_cotizacion,
                            $item['item'] ?? '',
                            $item['descripcion'],
                            $item['unidad'] ?? 'glb',
                            $item['cantidad'] ?? 1,
                            $item['precio_unitario'] ?? 0,
                            $item['subtotal'] ?? 0,
                            $index + 1, // orden
                            $item['seccion'] ?? null
                        ]);
                    } catch (\Exception $itemError) {
                        Log::error("Error al crear item: " . $itemError->getMessage());
                        // Continuar con los demás items
                    }
                }
            }
            
            return response()->json([
                'success' => true,
                'message' => 'Cotización creada exitosamente',
                'result' => [
                    'id_cotizacion' => $id_cotizacion
                ]
            ]);
        } catch (\Exception $e) {
            Log::error('Error en crear cotización: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al crear cotización: ' . $e->getMessage(),
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Actualizar una cotización existente
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function actualizar(Request $request): JsonResponse
    {
        try {
            $id = $request->input('id');
            
            // Actualizar la cotización principal
            DB::select('CALL USP_FINANZAS_COTIZACIONES_ACTUALIZAR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)', [
                $id,
                $request->codigo,
                $request->version ?? '2.0',
                $request->proyecto,
                $request->etapa,
                $request->cliente,
                $request->ruc,
                $request->fecha,
                $request->direccion,
                $request->ciudad ?? 'LIMA',
                $request->provincia ?? 'LIMA',
                $request->validez_oferta ?? '3 DÍAS CALENDARIO',
                $request->tiempo_ejecucion ?? '10 DÍAS CALENDARIO',
                $request->forma_pago ?? 'PRESENTADA LA FACTURA PAGO A 15 DÍAS',
                $request->adelanto ?? '50% DE ADELANTO DEL MONTO TOTAL',
                $request->garantia,
                $request->observaciones,
                $request->costo_directo ?? 0,
                $request->gastos_generales ?? 11,
                $request->utilidad ?? 8,
                $request->subtotal ?? 0,
                $request->igv ?? 18,
                $request->total ?? 0
            ]);
            
            // Eliminar items anteriores (lógicamente)
            DB::select('CALL USP_FINANZAS_COTIZACIONES_ITEMS_ELIMINAR_POR_COTIZACION(?)', [$id]);
            
            // Crear los nuevos items
            if ($request->has('items') && is_array($request->items)) {
                foreach ($request->items as $index => $item) {
                    DB::select('CALL USP_FINANZAS_COTIZACIONES_ITEMS_CREAR(?, ?, ?, ?, ?, ?, ?, ?, ?)', [
                        $id,
                        $item['item'] ?? '',
                        $item['descripcion'],
                        $item['unidad'] ?? 'glb',
                        $item['cantidad'] ?? 1,
                        $item['precio_unitario'] ?? 0,
                        $item['subtotal'] ?? 0,
                        $index + 1, // orden
                        $item['seccion'] ?? null
                    ]);
                }
            }
            
            return response()->json([
                'success' => true,
                'message' => 'Cotización actualizada exitosamente',
                'result' => true
            ]);
        } catch (\Exception $e) {
            Log::error('Error en actualizar cotización: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al actualizar cotización',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Eliminar una cotización (lógico)
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function eliminar(Request $request): JsonResponse
    {
        try {
            $id = $request->input('id');
            
            $result = DB::select('CALL USP_FINANZAS_COTIZACIONES_ELIMINAR(?)', [$id]);
            
            return response()->json([
                'success' => true,
                'message' => 'Cotización eliminada',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error en eliminar cotización: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al eliminar cotización',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Cambiar el estado de una cotización
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function cambiarEstado(Request $request): JsonResponse
    {
        try {
            $id = $request->input('id');
            $estado = $request->input('estado');
            $comentario = $request->input('comentario', '');
            $id_usuario = auth()->id() ?? 1;
            
            $result = DB::select('CALL USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO(?, ?, ?, ?)', [
                $id,
                $estado,
                $id_usuario,
                $comentario
            ]);
            
            return response()->json([
                'success' => true,
                'message' => 'Estado actualizado',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error en cambiar estado: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al cambiar estado',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Generar PDF de cotización
     * 
     * @param Request $request
     * @return BinaryFileResponse
     */
    public function generarPdf(Request $request)
    {
        try {
            $id = $request->input('id');
            
            // Obtener datos de la cotización
            $cotizacion = DB::selectOne(
                'SELECT * FROM finanzas_cotizacion WHERE id_cotizacion = ? AND Activo = "S"',
                [$id]
            );
            
            if (!$cotizacion) {
                return response()->json([
                    'success' => false,
                    'message' => 'Cotización no encontrada'
                ], 404);
            }
            
            // Obtener items
            $items = DB::select(
                'SELECT * FROM finanzas_cotizacion_items 
                 WHERE id_cotizacion = ? AND Activo = "S" 
                 ORDER BY orden ASC',
                [$id]
            );
            
            // Preparar datos para el PDF
            $data = [
                'cotizacion' => $cotizacion,
                'items' => $items
            ];
            
            // Generar PDF usando DomPDF
            // Nota: Necesitas instalar barryvdh/laravel-dompdf
            // composer require barryvdh/laravel-dompdf
            $pdf = \Barryvdh\DomPDF\Facade\Pdf::loadView('pdf.cotizacion', $data);
            $pdf->setPaper('a4', 'portrait');
            
            // Nombre del archivo
            $filename = 'Cotizacion_' . ($cotizacion->codigo ?? $id) . '_' . date('YmdHis') . '.pdf';
            
            // Retornar PDF para descarga
            return $pdf->download($filename);
            
        } catch (\Exception $e) {
            Log::error('Error en generar PDF: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al generar PDF: ' . $e->getMessage(),
                'error' => $e->getMessage()
            ], 500);
        }
    }
}