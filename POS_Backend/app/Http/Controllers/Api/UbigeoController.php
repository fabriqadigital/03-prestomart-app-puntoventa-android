<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;

class UbigeoController extends Controller
{
    public function listar_paises()
    {
        try {
            $result = DB::select('SELECT id, nombre FROM ubigeo_paises ORDER BY nombre');

            return response()->json([
                'success' => true,
                'message' => 'Países listados correctamente',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error al listar países: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al listar países',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function obtener_departamentos(Request $request): JsonResponse
    {
        try {
            $pais = $request->query('pais');

            $result = DB::select(
                'SELECT id, nombre FROM ubigeo_peru_departments WHERE id_pais = ? ORDER BY nombre',
                [$pais]
            );

            return response()->json([
                'success' => true,
                'message' => 'Departamentos listados correctamente',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error al listar departamentos: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al listar departamentos',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function obtener_provicias(Request $request): JsonResponse
    {
        try {
            $pais = $request->query('pais');
            $departamento = $request->query('departamento');

            $result = DB::select(
                'SELECT id, nombre FROM ubigeo_peru_provinces WHERE id_pais = ? AND id_department = ? ORDER BY nombre',
                [$pais, $departamento]
            );

            return response()->json([
                'success' => true,
                'message' => 'Provincias listadas correctamente',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error al listar provincias: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al listar provincias',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function obtener_distritos(Request $request): JsonResponse
    {
        try {
            $pais = $request->query('pais');
            $departamento = $request->query('departamento');
            $provincia = $request->query('provincia');

            $result = DB::select(
                'SELECT id, nombre FROM ubigeo_peru_districts WHERE id_pais = ? AND id_department = ? AND id_province = ? ORDER BY nombre',
                [$pais, $departamento, $provincia]
            );

            return response()->json([
                'success' => true,
                'message' => 'Distritos listados correctamente',
                'result' => $result
            ]);
        } catch (\Exception $e) {
            Log::error('Error al listar distritos: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Error al listar distritos',
                'error' => $e->getMessage()
            ], 500);
        }
    }
}
