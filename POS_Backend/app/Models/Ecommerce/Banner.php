<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;

class Banner extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_banners';

    protected $fillable = [
        'titulo',
        'subtitulo',
        'imagen_url',
        'enlace',
        'posicion',
        'orden',
        'fecha_inicio',
        'fecha_fin',
        'activo',
    ];

    protected $casts = [
        'orden' => 'integer',
        'fecha_inicio' => 'datetime',
        'fecha_fin' => 'datetime',
        'activo' => 'boolean',
    ];

    // Scopes
    public function scopeActivos($query)
    {
        return $query->where('activo', true);
    }

    public function scopeVigentes($query)
    {
        $now = Carbon::now();
        return $query->where(function ($q) use ($now) {
            $q->whereNull('fecha_inicio')
              ->orWhere('fecha_inicio', '<=', $now);
        })->where(function ($q) use ($now) {
            $q->whereNull('fecha_fin')
              ->orWhere('fecha_fin', '>=', $now);
        });
    }

    public function scopePorPosicion($query, string $posicion)
    {
        return $query->where('posicion', $posicion);
    }

    public function scopeOrdenados($query)
    {
        return $query->orderBy('orden');
    }

    // Accessors
    public function getEsVigenteAttribute(): bool
    {
        $now = Carbon::now();
        $inicioValido = !$this->fecha_inicio || $now >= $this->fecha_inicio;
        $finValido = !$this->fecha_fin || $now <= $this->fecha_fin;
        return $inicioValido && $finValido;
    }
}
