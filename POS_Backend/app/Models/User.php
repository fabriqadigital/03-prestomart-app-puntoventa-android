<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Support\Facades\DB;
use Laravel\Sanctum\HasApiTokens;
use Tymon\JWTAuth\Contracts\JWTSubject;

class User extends Authenticatable implements JWTSubject
{
    use HasApiTokens, HasFactory, Notifiable;

    private const PERFIL_ADMINISTRADOR = 1;

    //======= jorge
    public function getJWTIdentifier()
    {
        return $this->getKey();
    }

    public function getJWTCustomClaims()
    {
        return [];
    }
    //=====================

    protected $fillable = [
        'name',
        'email',
        'password',
        'id_usuario',
        'avatar',
        'Activo',
    ];
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * Verifica si el usuario es administrador
     */
    public function isAdmin(): bool
    {
        return $this->tienePerfilId(self::PERFIL_ADMINISTRADOR);
    }

    /**
     * Verifica si el usuario tiene un perfil específico
     */
    public function tienePerfilId(int $idPerfil): bool
    {
        return DB::table('seguridad_perfil_users')
            ->where('id_usuario', $this->id)
            ->where('id_perfil', $idPerfil)
            ->exists();
    }

    /**
     * Obtiene los IDs de perfiles del usuario
     */
    public function getPerfilesIds(): array
    {
        return DB::table('seguridad_perfil_users')
            ->where('id_usuario', $this->id)
            ->pluck('id_perfil')
            ->toArray();
    }
}
