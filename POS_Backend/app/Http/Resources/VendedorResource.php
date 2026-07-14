<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class VendedorResource extends JsonResource
{
    public function toArray(Request $request): array
    { 
        return [
            'id_vendedor' => $this->id_vendedor,
            'nombre' => $this->nombre,
        ];
    }
}
