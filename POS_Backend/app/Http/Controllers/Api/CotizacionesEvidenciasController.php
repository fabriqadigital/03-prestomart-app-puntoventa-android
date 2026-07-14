<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;

class CotizacionesEvidenciasController extends Controller
{
    /**
     * Cambiar estado con evidencia
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function cambiarEstadoConEvidencia(Request $request): JsonResponse
    {
        DB::beginTransaction();
        
        try {
            $id_cotizacion = $request->input('id_cotizacion');
            $estado_nuevo = $request->input('estado_nuevo');
            $comentario = $request->input('comentario', '');
            $id_usuario = auth()->id() ?? 1;
            
            // Verificar si tiene archivo
            $tiene_evidencia = $request->hasFile('evidencia');
            
            // Estados que requieren evidencia
            $estados_con_evidencia = ['APROBADO', 'RECHAZADO', 'FACTURADO'];
            
            if (in_array($estado_nuevo, $estados_con_evidencia) && !$tiene_evidencia) {
                return response()->json([
                    'success' => false,
                    'message' => 'Este cambio de estado requiere adjuntar una evidencia (PDF o imagen)'
                ], 400);
            }
            
            // Cambiar estado
            $result = DB::select('CALL USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO_CON_EVIDENCIA(?, ?, ?, ?, ?)', [
                $id_cotizacion,
                $estado_nuevo,
                $id_usuario,
                $comentario,
                $tiene_evidencia
            ]);
            
            $id_historial = $result[0]->id_historial ?? null;
            
            // Si hay evidencia, guardarla
            if ($tiene_evidencia && $id_historial) {
                $file = $request->file('evidencia');
                
                // Validar tipo de archivo
                $allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'application/pdf'];
                if (!in_array($file->getMimeType(), $allowedTypes)) {
                    DB::rollBack();
                    return response()->json([
                        'success' => false,
                        'message' => 'Tipo de archivo no permitido. Solo se aceptan: JPG, PNG, PDF'
                    ], 400);
                }
                
                // Validar tamaño (máximo 5MB)
                if ($file->getSize() > 5242880) {
                    DB::rollBack();
                    return response()->json([
                        'success' => false,
                        'message' => 'El archivo es muy grande. Máximo 5MB'
                    ], 400);
                }
                
                // Generar nombre único
                $extension = $file->getClientOriginalExtension();
                $nombre_original = $file->getClientOriginalName();
                $nombre_archivo = 'evidencia_' . $id_cotizacion . '_' . time() . '.' . $extension;
                
                // Guardar archivo
                $ruta = $file->storeAs('cotizaciones/evidencias', $nombre_archivo, 'public');
                
                // Registrar en base de datos
                DB::select('CALL USP_FINANZAS_COTIZACIONES_EVIDENCIAS_CREAR(?, ?, ?, ?, ?, ?, ?, ?, ?)', [
                    $id_cotizacion,
                    $id_historial,
                    $estado_nuevo,
                    $nombre_original,
                    $ruta,
                    $file->getMimeType(),
                    $file->getSize(),
                    $id_usuario,
                    $comentario
                ]);
                
                // Construir objeto respuesta de la evidencia (para devolver al frontend aunque el SP falle en insertar)
                $evidenciaObj = (object)[
                    'id_evidencia' => null,
                    'nombre_archivo' => $nombre_original,
                    'ruta_archivo' => $ruta,
                    'tipo_archivo' => $file->getMimeType(),
                    'tamano_archivo' => $file->getSize(),
                    'created_at' => now()->format('Y-m-d H:i:s'),
                    'usuario_nombre' => auth()->user()->name ?? 'Usuario',
                    'estado_aplicado' => $estado_nuevo ?? null,
                    'url' => Storage::url($ruta),
                    'url_completa' => url(Storage::url($ruta)),
                ];
            }
            
            DB::commit();
            
            return response()->json([
                'success' => true,
                'message' => 'Estado actualizado correctamente',
                'result' => [
                    'id_historial' => $id_historial,
                    'estado_nuevo' => $estado_nuevo
                ]
            ]);
            
        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error en cambiar estado con evidencia: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al cambiar estado: ' . $e->getMessage(),
                'error' => $e->getMessage()
            ], 500);
        }
    }
    
    /**
     * Listar evidencias de una cotización
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function listarEvidencias(Request $request): JsonResponse
    {
        try {
            $id_cotizacion = $request->input('id_cotizacion');
            
            $evidencias = DB::select('CALL USP_FINANZAS_COTIZACIONES_EVIDENCIAS_LISTAR(?)', [
                $id_cotizacion
            ]);
            
            // Agregar URL completa para cada evidencia
            foreach ($evidencias as $evidencia) {
                $evidencia->url = Storage::url($evidencia->ruta_archivo);
                $evidencia->url_completa = url(Storage::url($evidencia->ruta_archivo));
            }
            
            return response()->json([
                'success' => true,
                'message' => 'Evidencias listadas',
                'result' => $evidencias
            ]);
            
        } catch (\Exception $e) {
            Log::error('Error en listar evidencias: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al listar evidencias',
                'error' => $e->getMessage()
            ], 500);
        }
    }
    
    /**
     * Obtener una evidencia específica
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function obtenerEvidencia(Request $request): JsonResponse
    {
        try {
            $id_evidencia = $request->input('id_evidencia');
            
            $result = DB::select('CALL USP_FINANZAS_COTIZACIONES_EVIDENCIAS_OBTENER(?)', [
                $id_evidencia
            ]);
            
            if (empty($result)) {
                return response()->json([
                    'success' => false,
                    'message' => 'Evidencia no encontrada'
                ], 404);
            }
            
            $evidencia = $result[0];
            $evidencia->url = Storage::url($evidencia->ruta_archivo);
            $evidencia->url_completa = url(Storage::url($evidencia->ruta_archivo));
            
            return response()->json([
                'success' => true,
                'message' => 'Evidencia obtenida',
                'result' => $evidencia
            ]);
            
        } catch (\Exception $e) {
            Log::error('Error en obtener evidencia: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al obtener evidencia',
                'error' => $e->getMessage()
            ], 500);
        }
    }
    
    /**
     * Descargar/visualizar evidencia
     * 
     * @param Request $request
     * @return BinaryFileResponse
     */
    public function descargarEvidencia(Request $request)
    {
        try {
            $id_evidencia = $request->input('id_evidencia');
            
            $result = DB::select('CALL USP_FINANZAS_COTIZACIONES_EVIDENCIAS_OBTENER(?)', [
                $id_evidencia
            ]);
            
            if (empty($result)) {
                return response()->json([
                    'success' => false,
                    'message' => 'Evidencia no encontrada'
                ], 404);
            }
            
            $evidencia = $result[0];
            $ruta_completa = storage_path('app/public/' . $evidencia->ruta_archivo);
            
            if (!file_exists($ruta_completa)) {
                return response()->json([
                    'success' => false,
                    'message' => 'Archivo no encontrado en el servidor'
                ], 404);
            }
            
            return response()->file($ruta_completa, [
                'Content-Type' => $evidencia->tipo_archivo,
                'Content-Disposition' => 'inline; filename="' . $evidencia->nombre_archivo . '"'
            ]);
            
        } catch (\Exception $e) {
            Log::error('Error en descargar evidencia: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al descargar evidencia',
                'error' => $e->getMessage()
            ], 500);
        }
    }
    
    /**
     * Obtener evidencias por estado
     * 
     * @param Request $request
     * @return JsonResponse
     */
    public function evidenciasPorEstado(Request $request): JsonResponse
    {
        try {
            $id_cotizacion = $request->input('id_cotizacion');
            $estado = $request->input('estado');
            
            $evidencias = DB::select('CALL USP_FINANZAS_COTIZACIONES_EVIDENCIAS_POR_ESTADO(?, ?)', [
                $id_cotizacion,
                $estado
            ]);
            
            // Agregar URL completa
            foreach ($evidencias as $evidencia) {
                $evidencia->url = Storage::url($evidencia->ruta_archivo);
                $evidencia->url_completa = url(Storage::url($evidencia->ruta_archivo));
            }
            
            return response()->json([
                'success' => true,
                'message' => 'Evidencias obtenidas',
                'result' => $evidencias
            ]);
            
        } catch (\Exception $e) {
            Log::error('Error en evidencias por estado: ' . $e->getMessage());
            
            return response()->json([
                'success' => false,
                'message' => 'Error al obtener evidencias',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Subir evidencia sin cambiar estado
     */
    public function subirEvidencia(Request $request): JsonResponse
    {
        DB::beginTransaction();
        try {
            $id_cotizacion = $request->input('id_cotizacion');
            $comentario = $request->input('comentario', '');
            $id_usuario = auth()->id() ?? 1;

            if (!$request->hasFile('evidencia')) {
                return response()->json([
                    'success' => false,
                    'message' => 'No se envió ningún archivo'
                ], 400);
            }

            $file = $request->file('evidencia');
            $allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'application/pdf'];
            if (!in_array($file->getMimeType(), $allowedTypes)) {
                return response()->json([
                    'success' => false,
                    'message' => 'Tipo de archivo no permitido. Solo JPG, PNG o PDF'
                ], 400);
            }
            if ($file->getSize() > 5242880) {
                return response()->json([
                    'success' => false,
                    'message' => 'El archivo es muy grande. Máximo 5MB'
                ], 400);
            }

            $extension = $file->getClientOriginalExtension();
            $nombre_original = $file->getClientOriginalName();
            $nombre_archivo = 'evidencia_' . $id_cotizacion . '_' . time() . '.' . $extension;
            $ruta = $file->storeAs('cotizaciones/evidencias', $nombre_archivo, 'public');

            // Registrar en base de datos (id_historial y estado_nuevo nulos)
            DB::select('CALL USP_FINANZAS_COTIZACIONES_EVIDENCIAS_CREAR(?, ?, ?, ?, ?, ?, ?, ?, ?)', [
                $id_cotizacion,
                null,
                null,
                $nombre_original,
                $ruta,
                $file->getMimeType(),
                $file->getSize(),
                $id_usuario,
                $comentario
            ]);
            
            // Construir objeto de evidencia para retornar al frontend
            $evidenciaObj = (object)[
                'id_evidencia' => null,
                'nombre_archivo' => $nombre_original,
                'ruta_archivo' => $ruta,
                'tipo_archivo' => $file->getMimeType(),
                'tamano_archivo' => $file->getSize(),
                'created_at' => now()->format('Y-m-d H:i:s'),
                'usuario_nombre' => auth()->user()->name ?? 'Usuario',
                'estado_aplicado' => null,
                'url' => Storage::url($ruta),
                'url_completa' => url(Storage::url($ruta)),
            ];

            DB::commit();

            return response()->json([
                'success' => true,
                'message' => 'Evidencia subida correctamente',
                'result' => $evidenciaObj
            ]);
        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error en subir evidencia: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al subir evidencia',
                'error' => $e->getMessage()
            ], 500);
        }
    }
}