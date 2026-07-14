<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Review extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_reviews';

    protected $fillable = [
        'id_producto',
        'id_cliente',
        'id_pedido',
        'rating',
        'titulo',
        'comentario',
        'imagenes',
        'verificado',
        'aprobado',
    ];

    protected $casts = [
        'rating' => 'integer',
        'imagenes' => 'array',
        'verificado' => 'boolean',
        'aprobado' => 'boolean',
    ];

    // Relaciones
    public function producto(): BelongsTo
    {
        return $this->belongsTo(Producto::class, 'id_producto');
    }

    public function cliente(): BelongsTo
    {
        return $this->belongsTo(Cliente::class, 'id_cliente');
    }

    public function pedido(): BelongsTo
    {
        return $this->belongsTo(Pedido::class, 'id_pedido');
    }

    // Scopes
    public function scopeAprobadas($query)
    {
        return $query->where('aprobado', true);
    }

    public function scopeVerificadas($query)
    {
        return $query->where('verificado', true);
    }

    public function scopePendientes($query)
    {
        return $query->where('aprobado', false);
    }
}
