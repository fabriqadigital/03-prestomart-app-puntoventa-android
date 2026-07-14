<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class CarritoItem extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_carrito_items';

    protected $fillable = [
        'id_carrito',
        'id_producto',
        'id_variante',
        'cantidad',
        'precio_unitario',
    ];

    protected $casts = [
        'cantidad' => 'integer',
        'precio_unitario' => 'decimal:2',
    ];

    // Relaciones
    public function carrito(): BelongsTo
    {
        return $this->belongsTo(Carrito::class, 'id_carrito');
    }

    public function producto(): BelongsTo
    {
        return $this->belongsTo(Producto::class, 'id_producto');
    }

    public function variante(): BelongsTo
    {
        return $this->belongsTo(ProductoVariante::class, 'id_variante');
    }

    // Accessors
    public function getSubtotalAttribute(): float
    {
        return $this->precio_unitario * $this->cantidad;
    }

    public function getNombreCompletoAttribute(): string
    {
        $nombre = $this->producto->nombre;
        if ($this->variante) {
            $nombre .= ' - ' . $this->variante->nombre;
        }
        return $nombre;
    }
}
