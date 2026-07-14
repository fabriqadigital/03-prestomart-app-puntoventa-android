<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Direccion extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_direcciones';
        protected $primaryKey = 'id_direccion'; // ← AÑADE ESTO
        protected $keyType = 'int'; // si es entero

    protected $fillable = [
        'id_cliente',
        'tipo',
        'nombre_completo',
        'telefono',
        'direccion',
        'direccion_adicional',
        'ciudad',
        'estado',
        'codigo_postal',
        'pais',
        'latitud',
        'longitud',
        'es_principal',
        'activo',
    ];

    protected $casts = [
        'latitud' => 'decimal:8',
        'longitud' => 'decimal:8',
        'es_principal' => 'boolean',
        'activo' => 'boolean',
    ];

    // Relaciones
    public function cliente(): BelongsTo
    {
        return $this->belongsTo(Cliente::class, 'id_cliente');
    }

    // Scopes
    public function scopeActivas($query)
    {
        return $query->where('activo', true);
    }

    public function scopePrincipales($query)
    {
        return $query->where('es_principal', true);
    }

    // Accessors
    public function getDireccionCompletaAttribute(): string
    {
        $parts = [$this->direccion];

        if ($this->direccion_adicional) {
            $parts[] = $this->direccion_adicional;
        }

        $parts[] = $this->ciudad;

        if ($this->estado) {
            $parts[] = $this->estado;
        }

        if ($this->codigo_postal) {
            $parts[] = $this->codigo_postal;
        }

        $parts[] = $this->pais;

        return implode(', ', $parts);
    }
}
