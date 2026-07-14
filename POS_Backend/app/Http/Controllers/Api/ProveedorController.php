<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\ClienteResource;
use App\Models\Proveedor;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;

class ProveedorController extends Controller
{

    public function obtener(Request $request) : JsonResponse
    {
        $id = $request->id;
        $result = DB::select('select * from administracion_proveedor where id_proveedor = ?', [$id]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }
    
    public function listar()
    {
        $result = DB::select('CALL USP_ADMIMNISTRACION_PROVEEDOR_LISTAR()');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' => $result
        ]);
    }

    public function crear(Request $request) : JsonResponse
    {
        $result = Proveedor::create([
            // Campos principales
            'nombre_empresa'      => $request->nombre_empresa,
            'contacto_principal'  => $request->contacto_principal,
            'ruc'                 => $request->ruc,
            'tipo_proveedor'      => $request->tipo_proveedor,
            
            // Información de contacto
            'direccion'           => $request->direccion,
            'telefono'            => $request->telefono,
            'email'               => $request->email,
            
            // Información financiera
            'terminos_pago'       => $request->terminos_pago,
            'cuenta_bancaria'     => $request->cuenta_bancaria,
            
            // Información adicional
            'sitio_web'           => $request->sitio_web,
            'notas'              => $request->notas,
            'fecha_inicio'       => Carbon::parse($request->fecha_inicio)->format('Y-m-d H:i:s'),
            'calificacion'       => $request->calificacion,
            
            // Metadatos
            'Activo'             => $request->Activo ?? 'S', // Valor por defecto 'S'
            'updated_at'         => now(),
            'created_at'         => now(),
        ]);

            return response()->json([
                'success' => true,
                'message' => 'Proveedor registrado correctamente',
                'result'  => $result // Asegúrate de usar ProveedorResource en lugar de ClienteResource
            ]);
        }

        
        public function actualizar(Request $request) : JsonResponse
        {
            try {
                $id = $request->input('id_proveedor');
                Log::channel('stderr')->info("Update attempt for proveedor ID: $id with data: ".json_encode($request->all()));

                $updateData = [
                    // Campos principales
                    'nombre_empresa'      => $request->nombre_empresa,
                    'contacto_principal'  => $request->contacto_principal,
                    'ruc'                 => $request->ruc,
                    'tipo_proveedor'      => $request->tipo_proveedor,
                    
                    // Información de contacto
                    'direccion'           => $request->direccion,
                    'telefono'            => $request->telefono,
                    'email'               => $request->email,
                    
                    // Información financiera
                    'terminos_pago'       => $request->terminos_pago,
                    'cuenta_bancaria'     => $request->cuenta_bancaria,
                    
                    // Información adicional
                    'sitio_web'          => $request->sitio_web,
                    'notas'              => $request->notas,
                    'Activo'             => $request->Activo ?? 'S',
                    'updated_at'         => now(),
                ];

                // Solo procesar fecha_inicio si viene en el request
                if ($request->has('fecha_inicio') && !empty($request->fecha_inicio)) {
                    $updateData['fecha_inicio'] = Carbon::parse($request->fecha_inicio)->format('Y-m-d H:i:s');
                }

                // Solo procesar calificacion si viene en el request
                if ($request->has('calificacion')) {
                    $updateData['calificacion'] = $request->calificacion;
                }

                $affectedRows = Proveedor::where('id_proveedor', $id)->update($updateData);

                if ($affectedRows === 0) {
                    Log::channel('stderr')->error("No records updated. Possible causes: ID $id not found or data identical to current values");
                    return response()->json([
                        'success' => false,
                        'message' => 'No se encontró el proveedor o los datos son idénticos',
                        'affected_rows' => $affectedRows
                    ], 404);
                }

                Log::channel('stderr')->info("Successfully updated $affectedRows record(s)");
                return response()->json([
                    'success' => true,
                    'message' => 'Registro actualizado correctamente',
                    'affected_rows' => $affectedRows,
                    'updated_data' => $updateData // Opcional: devolver los datos actualizados
                ]);

            } catch (\Exception $e) {
                Log::channel('stderr')->error("Update error: ".$e->getMessage());
                Log::channel('single')->error("Full error: ".$e->getTraceAsString());
                
                return response()->json([
                    'success' => false,
                    'message' => 'Error al actualizar el proveedor',
                    'error' => [
                        'message' => $e->getMessage(),
                        'file' => $e->getFile(),
                        'line' => $e->getLine()
                        // No exponer trace completo en producción
                    ]
                ], 500);
            }
        }
    public function eliminar(Request $request) : JsonResponse
    {
        $id = $request->input('id');
        $result = Proveedor::where('id_proveedor',$id)->delete();
 
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }
}