<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\DB;
use App\Models\PaginaMenuObjetos;
use Illuminate\Support\Facades\Log;

class AdminPaginaObjetosController extends Controller
{
    public function obtener(Request $request): JsonResponse
    {
        $id = $request->id;
        $result = DB::select('SELECT * FROM sistema_objetos WHERE id_objetos = ?', [$id]);
        
        return response()->json([
            'success' => true,
            'message' => 'Objeto obtenido con éxito',
            'result' => $result
        ]);
    }

    public function listar(): JsonResponse
    {
        $result = DB::select('SELECT * FROM sistema_objetos ORDER BY id_objetos ASC');
        
        return response()->json([
            'success' => true,
            'message' => 'Listado de objetos obtenido',
            'result' => $result
        ]);
    }

    public function crear(Request $request): JsonResponse
    {
        try {
            $result = PaginaMenuObjetos::create([
                'nombre'    => $request->nombre,
                'Activo'   => $request->Activo ?? 'S',
                'created_at' => date("Y-m-d H:i:s"),
                'updated_at' => date("Y-m-d H:i:s"),
            ]);

            return response()->json([
                'success' => true,
                'message' => 'Objeto creado con éxito',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error al crear Objeto: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al crear Objeto',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function actualizar(Request $request): JsonResponse
    {
        try {
            $id = $request->input('id');
            $result = PaginaMenuObjetos::where('id_objetos', $id)
                ->update([
                    'nombre'    => $request->nombre,
                    'Activo'    => $request->Activo,
                    'updated_at' => date("Y-m-d H:i:s"),
                ]);

            return response()->json([
                'success' => true,
                'message' => 'Objeto actualizado con éxito',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error al actualizar Objeto: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al actualizar Objeto',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function eliminar(Request $request): JsonResponse
    {
        try {
            $id = $request->input('id');
            $result = PaginaMenuObjetos::where('id_objetos', $id)->delete();

            return response()->json([
                'success' => true,
                'message' => 'Objeto eliminado con éxito',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error al eliminar Objeto: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al eliminar Objeto',
                'error' => $e->getMessage()
            ], 500);
        }
    }
}