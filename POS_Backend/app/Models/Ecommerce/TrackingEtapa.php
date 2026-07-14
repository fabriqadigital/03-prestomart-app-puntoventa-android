<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Model;

class TrackingEtapa extends Model
{
    protected $table = 'ecommerce_tracking_etapas';

    protected $fillable = [
        'codigo',
        'nombre',
        'descripcion',
        'tipo',
        'orden',
        'icono',
        'activo',
    ];

    protected $casts = [
        'activo' => 'boolean',
    ];

    public function scopeActivas($query)
    {
        return $query->where('activo', true);
    }

    public function scopeOrdenadas($query)
    {
        return $query->orderBy('orden');
    }

    public static function obtenerPorCodigo(string $codigo): ?self
    {
        return self::where('codigo', $codigo)->first();
    }
}
