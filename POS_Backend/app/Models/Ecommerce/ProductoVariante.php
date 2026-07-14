<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class ProductoVariante extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_producto_variantes';

    protected $fillable = [
        'id_producto',
        'nombre',
        'sku',
        'precio_adicional',
        'stock',
        'atributos',
        'activo',
    ];

    protected $casts = [
        'precio_adicional' => 'decimal:2',
        'stock' => 'integer',
        'atributos' => 'array',
        'activo' => 'boolean',
    ];

    // Relaciones
    public function producto(): BelongsTo
    {
        return $this->belongsTo(Producto::class, 'id_producto');
    }

    // Scopes
    public function scopeActivas($query)
    {
        return $query->where('activo', true);
    }

    public function scopeConStock($query)
    {
        return $query->where('stock', '>', 0);
    }

    // Accessors
    public function getPrecioTotalAttribute(): float
    {
        return $this->producto->precio_final + $this->precio_adicional;
    }
}
