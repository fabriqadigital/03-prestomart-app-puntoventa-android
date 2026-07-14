<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class TipoBombaResource extends JsonResource
{
    public function toArray(Request $request): array
    { 
        return [
            'id_tipo_bomba' => $this->id_tipo_bomba,
            'nombre' => $this->nombre,
        ];
    }
}
