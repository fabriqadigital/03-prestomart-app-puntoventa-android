<?php

namespace App\Services;

use Illuminate\Support\Facades\DB;

class CorrelativoService
{
    public function generarNuevoCorrelativo(): string
    {
        // Obtener el último número de correlativo de la tabla
        $ultimoCorrelativo = DB::table('finanzas_guia_remision')
                            ->orderBy('id_guia_remision', 'desc')
                            ->value('numero_correlativo');

        // Si no hay registros, empezar con 1
        if (!$ultimoCorrelativo) {
            $nuevoNumero = 1;
        } else {
            // Extraer solo los dígitos numéricos del último correlativo
            $ultimoNumero = (int)preg_replace('/[^0-9]/', '', $ultimoCorrelativo);
            $nuevoNumero = $ultimoNumero + 1;
        }

        // Formatear a 8 dígitos con ceros a la izquierda
        return str_pad($nuevoNumero, 8, '0', STR_PAD_LEFT);
    }
}