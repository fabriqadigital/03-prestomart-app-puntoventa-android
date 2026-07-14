<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\ClienteResource;
use App\Models\OpePedidos;
use App\Models\ProductoImagen;
use App\Models\ProductoFichaTecnica;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use PDO;

class FileController extends Controller
{ 
    public function show($filename)
    {//jorge
        if (!Storage::exists("public/comprobantes/{$filename}")) {
            abort(404);
        }
        return response()->file(storage_path("app/public/comprobantes/{$filename}"));
        //  return response()->file(storage_path("app/public/comprobantes/payment__1747957021.pdf"));
    }

}
