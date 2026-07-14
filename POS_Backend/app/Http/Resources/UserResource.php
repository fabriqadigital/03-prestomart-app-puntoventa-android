<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class UserResource extends JsonResource
{
    public function toArray(Request $request): array
    { 
        return [
            'id' => $this->id,
            'name' => $this->name,
            'email' => $this->email,
            'id_perfil' => $this->id_perfil,
            // 'token' => $this->createToken('API TOKEN')->plainTextToken
        ];
    }
}
