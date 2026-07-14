<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class ClienteResource extends JsonResource
{
    public function toArray(Request $request): array
    { 
        return [
            'id_empleado'       => $this->id_empleado,
            'nombre'            => $this->nombre,
            'apellido'          => $this->apellido,
            'telefono'          => $this->telefono,
            'email'             => $this->email,
            'direccion'         => $this->direccion,
            'ruc'               => $this->ruc,
            'razon_social'      => $this->razon_social,
            'created_at'        => $this->created_at,
            'updated_at'        => $this->updated_at,
            'Activo'            => $this->Activo,
        ];
    }
}
