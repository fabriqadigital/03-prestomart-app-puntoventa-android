<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class EmpleadoResource extends JsonResource
{
    public function toArray(Request $request): array
    { 
        return [
            'id_empleado'       => $this->id_empleado,
            'id_perfil'         => $this->id_perfil,
            'nombre'            => $this->nombre,
            'apellido'          => $this->apellido,
            'telefono'          => $this->telefono,
            'email'             => $this->email,
            'direccion'         => $this->direccion,
            'dni'               => $this->dni,
            'created_at'        => $this->created_at,
            'updated_at'        => $this->updated_at,
            'Activo'            => $this->Activo,
        ];
    }
}
