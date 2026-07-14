<?php

namespace App\Services;

use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class SunatConsultarRucService
{

    protected $token;

    public function __construct()
    {
        $this->token = config('services.sunat.token');
    }

    public function consultarRuc($ruc)
    {
        $response = Http::withToken($this->token)
            ->get("https://api.apis.net.pe/v1/ruc", [
                'numero' => $ruc
            ]);

        if ($response->successful()) {
            return $response->json();
        }

        throw new \Exception("Error consultando RUC: " . $response->body());
    }

   public function consultarDNI($dni)
{
    // Obtener la configuración de la base de datos
    $configuracion = DB::table('finanzas_configuracion')
        ->where('id_configuracion', 1)
        ->first();

    if (!$configuracion) {
        throw new \Exception("Configuración no encontrada en la base de datos");
    }

    if (empty($configuracion->token)) {
        throw new \Exception("Token no configurado en la base de datos");
    }

    // Construir URL
    $url = rtrim($configuracion->url_api, '/') . '/dni/info/' . $dni;

    try {
        $response = Http::withHeaders([
            'Authorization' => 'Bearer ' . $configuracion->token,
            'Accept' => 'application/json'
        ])->get($url);

        // Registrar la respuesta completa para depuración
        //Log::channel('stderr')->info("Respuesta de la API: " . $response->body());

        if ($response->successful()) {
            return $response->json();
        }

        // Intentar obtener mensaje de error si la respuesta es JSON
        $errorData = $response->json();
        $errorMessage = $errorData['message'] ?? $response->body();

        throw new \Exception("Error consultando DNI: " . $errorMessage);

    } catch (\Exception $e) {
        // Capturar errores de conexión u otros
        throw new \Exception("Error al conectar con el servicio de DNI: " . $e->getMessage());
    }
}
}
