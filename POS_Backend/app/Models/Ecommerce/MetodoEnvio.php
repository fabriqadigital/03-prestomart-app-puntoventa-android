<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class MetodoEnvio extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_metodos_envio';

      protected $primaryKey = 'id_metodo_envio'; // ← AÑADE ESTO

    protected $fillable = [
        'nombre',
        'descripcion',
        'precio',
        'tiempo_entrega_min',
        'tiempo_entrega_max',
        'activo',
    ];

    protected $casts = [
        'precio' => 'decimal:2',
        'tiempo_entrega_min' => 'integer',
        'tiempo_entrega_max' => 'integer',
        'activo' => 'boolean',
    ];

    // Scopes
    public function scopeActivos($query)
    {
        return $query->where('activo', true);
    }

    // Accessors
    public function getTiempoEntregaAttribute(): string
    {
        if ($this->tiempo_entrega_min === $this->tiempo_entrega_max) {
            return "{$this->tiempo_entrega_min} días";
        }
        return "{$this->tiempo_entrega_min}-{$this->tiempo_entrega_max} días";
    }
}
