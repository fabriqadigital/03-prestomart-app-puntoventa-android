<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\UnidadMedidaResource;
use App\Models\Compania;
use Illuminate\Support\Facades\DB;

class CompaniaController extends Controller
{

    public function obtener(Request $request): JsonResponse
    {
        $id = $request->id;
        $result = DB::select('select * from administracion_compania where id_compania = ?', [$id]);
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
            'result' => Compania::orderBy('id_compania', 'DESC')->get()
        ]);
    }

    public function crear(Request $request): JsonResponse
    {
        // Log::channel('stderr')->info($request->Activo);
        $result = Compania::create([
            'razon_social' => $request->razon_social,
            'ruc' => $request->ruc,
            'created_at'      => date("Y-m-d H:i:s"),
            'updated_at'      => date("Y-m-d H:i:s"),

        ]);
        return response()->json([
            'success' => true,
            'message' => 'Registro insertado',
            'result' =>  new UnidadMedidaResource($result)
        ]);
    }

    public function actualizar(Request $request): JsonResponse
    {
        $id = $request->input('id');

        $result = Compania::where('id_compania', $id)
            ->update([
                'razon_social' => $request->razon_social,
                'ruc' => $request->ruc,
                'updated_at'      => date("Y-m-d H:i:s"),
            ]);

        return response()->json([
            'success' => true,
            'message' => 'Registro actualizado',
            'result'  => $result
        ]);
    }

    public function eliminar(Request $request): JsonResponse
    {
        $id = $request->input('id');
        $result = Compania::where('id_compania', $id)->delete();

        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }
}
