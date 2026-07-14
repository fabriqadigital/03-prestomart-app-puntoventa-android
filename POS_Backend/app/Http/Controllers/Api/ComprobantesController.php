<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\MaterialCategoria;
use Illuminate\Support\Facades\DB;

class ComprobantesController extends Controller
{
    public function listar()
    {
        $result = DB::select('select * from administracion_comprobante ORDER BY id_comprobante ASC');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros!',
            'result' => $result
        ]);
    }
}