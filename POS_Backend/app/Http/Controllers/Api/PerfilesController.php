<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\PerfilResource;
use App\Models\Perfiles;
use Illuminate\Support\Facades\DB;

class PerfilesController extends Controller
{

    public function obtener(Request $request) : JsonResponse
    {
        $id = $request->id;
        $result = DB::select('select * from seguridad_perfil where id_perfil = ?', [$id]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }
    
    public function listar()
    {
        $result = DB::select('CALL USP_SEGURIDAD_PERFIL_LISTAR()');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' =>  $result
        ]);
    }

    public function crear(Request $request) : JsonResponse
    {
        // Log::channel('stderr')->info($request->Activo);
		$result = Perfiles::create([
                    'nombre' => $request->nombre,

                    'created_at'      => date("Y-m-d H:i:s"),
                    'updated_at'      => date("Y-m-d H:i:s"),
                    'Activo'          => $request->Activo,
            
                    ]);
        return response()->json([
            'success' => true,
            'message' => 'Registro insertado',
            'result' =>  new PerfilResource($result)
        ]);
    }

    public function actualizar(Request $request) : JsonResponse
    {
        $id = $request->input('id');
        $result = Perfiles::where('id_perfil',$id)
                ->update([
                        'nombre' => $request->nombre,

                        'updated_at'      => date("Y-m-d H:i:s"),
                        'Activo'          => $request->Activo,
                    ]);
 
        return response()->json([
            'success' => true,
            'message' => 'Registro actualizado',
            'result'  => $result
        ]);
    }

    public function eliminar(Request $request) : JsonResponse
    {
        $id = $request->input('id');
        $result = Perfiles::where('id_perfil',$id)->delete();
 
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }

    public function obtener_asignar(Request $request) : JsonResponse
    {
        $id_usuario = $request->id_usuario;
        $cadena_id_perfil = $request->id_perfil;
        $result = DB::select('CALL USP_SEGURIDAD_PERFIL_ASIGNAR(?,?)', [$id_usuario, $cadena_id_perfil]);
        return response()->json([
            'success' => true,
            'message' => 'Registros asignados!',
            'result' => $result
        ]);
    }

    public function obtener_lista(Request $request) : JsonResponse
    {
        $id_usuario = $request->id_usuario;
        $result = DB::select('CALL USP_SEGURIDAD_PERFIL_OBTENER_LISTA(?)', [$id_usuario]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }

    public function obtener_check(Request $request) : JsonResponse
    {
        $id_usuario = $request->id_usuario;
        $result = DB::select('CALL USP_SEGURIDAD_PERFIL_OBTENER_CHECK(?)', [$id_usuario]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }

    public function obtener_eliminar(Request $request) : JsonResponse
    {
        $id_perfil_users = $request->input('id');
        $result = DB::select('CALL USP_SEGURIDAD_PERFIL_OBTENER_ELIMINAR(?)', [$id_perfil_users]);
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }

    public function eliminar_multiple(Request $request) : JsonResponse
    {
        $id_perfil = $request->input('id_perfil');
        $result = DB::select('CALL USP_SEGURIDAD_PERFIL_ELIMINAR_MULTIPLE(?)', [$id_perfil]);
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }
}