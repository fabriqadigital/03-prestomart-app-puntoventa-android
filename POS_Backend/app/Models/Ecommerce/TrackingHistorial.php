<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class TrackingHistorial extends Model
{
    protected $table = 'ecommerce_tracking_historial';

    public $timestamps = false;

    protected $fillable = [
        'id_pedido',
        'id_estado',
        'id_etapa',
        'titulo',
        'descripcion',
        'ubicacion',
        'latitud',
        'longitud',
        'operador',
        'ip_address',
        'metadata',
        'fecha_evento',
    ];

    protected $casts = [
        'metadata' => 'array',
        'fecha_evento' => 'datetime',
        'latitud' => 'decimal:8',
        'longitud' => 'decimal:8',
    ];

    // Relaciones
    public function pedido(): BelongsTo
    {
        return $this->belongsTo(Pedido::class, 'id_pedido', 'id_pedido');
    }

    public function estado(): BelongsTo
    {
        return $this->belongsTo(TrackingEstado::class, 'id_estado');
    }

    public function etapa(): BelongsTo
    {
        return $this->belongsTo(TrackingEtapa::class, 'id_etapa');
    }
}
