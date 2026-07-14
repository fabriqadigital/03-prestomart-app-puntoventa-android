<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Storage;

class FileController extends Controller
{
    public function show($filename)
    {
        if (!Storage::exists("avatar/{$filename}")) {
            abort(404);
        }
        return response()->file(storage_path("app/avatar/{$filename}"));
    }

    public function show_caja_chica($filename)
    {
        if (!Storage::exists("caja_chica_egresos/{$filename}")) {
            abort(404);
        }
        return response()->file(storage_path("app/caja_chica_egresos/{$filename}"));
    }
}
