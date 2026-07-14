<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\ClienteResource;
use App\Models\Clientes;
use Illuminate\Support\Facades\DB;

class ClientesController extends Controller
{

    public function obtener(Request $request) : JsonResponse
    {
        $id = $request->id;
        $result = DB::select('select * from administracion_cliente where id_cliente = ?', [$id]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }
    
    public function listar()
    {
        $result = DB::select('CALL USP_ADMINISTRACION_CLIENTE_LISTAR()');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' => $result
        ]);
    }

    public function crear(Request $request) : JsonResponse
    {
        // Log::channel('stderr')->info($request->Activo);
		$result = Clientes::create([
            'nombre'          => $request->nombre,
            'apellido'        => $request->apellido,
            'telefono'        => $request->telefono,
            'email'           => $request->email,
            'direccion'       => $request->direccion,
            'ruc'             => $request->ruc,
            'razon_social'    => $request->razon_social,

            'created_at'      => date("Y-m-d H:i:s"),
            'updated_at'      => date("Y-m-d H:i:s"),
            'Activo'          => $request->Activo,
            
        ]);
        return response()->json([
            'success' => true,
            'message' => 'Registro insertado',
            'result' =>  new ClienteResource($result)
        ]);
    }

    public function actualizar(Request $request) : JsonResponse
    {
        $id = $request->input('id');
        
        $result = Clientes::where('id_cliente',$id)
                ->update([
                        'nombre'          => $request->nombre,
                        'apellido'        => $request->apellido,
                        'telefono'        => $request->telefono,
                        'email'           => $request->email,
                        'direccion'       => $request->direccion,
                        'ruc'             => $request->ruc,
                        'razon_social'    => $request->razon_social,
            
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
        $result = Clientes::where('id_cliente',$id)->delete();
 
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }
}