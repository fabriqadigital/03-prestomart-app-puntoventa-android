<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class UnidadMedidaResource extends JsonResource
{
    public function toArray(Request $request): array
    { 
        return [
            'id_unidad_medida' => $this->id_unidad_medida,
            'nombre' => $this->nombre,
        ];
    }
}
