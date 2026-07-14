<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\VendedorResource;
use App\Models\Vendedor;
use Illuminate\Support\Facades\DB;

class VendedorController extends Controller
{

    public function obtener(Request $request) : JsonResponse
    {
        $id = $request->id;
        $result = DB::select('select * from administracion_vendedor where id_vendedor = ?', [$id]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }
    public function listar()
    {
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' => Vendedor::orderBy('id_vendedor', 'DESC')->get()
        ]);
    }

    public function crear(Request $request) : JsonResponse
    {
        // Log::channel('stderr')->info($request->Activo);
		$result = Vendedor::create([
                    'nombre'          => $request->nombre,
                    'created_at'      => date("Y-m-d H:i:s"),
                    'updated_at'      => date("Y-m-d H:i:s"),
                    'Activo'          => $request->Activo,
            
        ]);
        return response()->json([
            'success' => true,
            'message' => 'Registro insertado',
            'result' =>  new VendedorResource($result)
        ]);
    }

    public function actualizar(Request $request) : JsonResponse
    {
        $id = $request->input('id');
        $result = Vendedor::where('id_vendedor',$id)
                ->update([
                    'nombre'          => $request->nombre,
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
        $result = Vendedor::where('id_vendedor',$id)->delete();
 
        return response()->json([
            'success' => true,
           'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }
}