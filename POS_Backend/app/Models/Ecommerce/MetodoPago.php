<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class MetodoPago extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_metodos_pago';
      protected $primaryKey = 'id_metodo_pago'; // ← AÑADE ESTO

    protected $fillable = [
        'nombre',
        'descripcion',
        'icono',
        'configuracion',
        'activo',
    ];

    protected $casts = [
        'configuracion' => 'array',
        'activo' => 'boolean',
    ];

    protected $hidden = [
        'configuracion',
    ];

    // Scopes
    public function scopeActivos($query)
    {
        return $query->where('activo', true);
    }
}
