<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Model;

class TrackingEstado extends Model
{
    protected $table = 'ecommerce_tracking_estados';

    protected $fillable = [
        'codigo',
        'nombre',
        'descripcion',
        'icono',
        'color',
        'orden',
        'es_final',
        'activo',
    ];

    protected $casts = [
        'es_final' => 'boolean',
        'activo' => 'boolean',
    ];

    public function scopeActivos($query)
    {
        return $query->where('activo', true);
    }

    public function scopeOrdenados($query)
    {
        return $query->orderBy('orden');
    }

    public static function obtenerPorCodigo(string $codigo): ?self
    {
        return self::where('codigo', $codigo)->first();
    }
}
