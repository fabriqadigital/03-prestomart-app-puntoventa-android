<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class PedidoHistorial extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_pedido_historial';

    protected $fillable = [
        'id_pedido',
        'estado_anterior',
        'estado_nuevo',
        'comentario',
        'creado_por',
    ];

    // Relaciones
    public function pedido(): BelongsTo
    {
        return $this->belongsTo(Pedido::class, 'id_pedido');
    }
}
