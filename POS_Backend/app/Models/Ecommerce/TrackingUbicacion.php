<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Model;

class TrackingUbicacion extends Model
{
    protected $table = 'ecommerce_tracking_ubicaciones';

    protected $fillable = [
        'id_pedido',
        'id_etapa',
        'nombre',
        'direccion',
        'ciudad',
        'latitud',
        'longitud',
        'fecha_llegada',
        'fecha_salida',
        'completado',
        'orden',
        'notas',
    ];

    protected $casts = [
        'latitud' => 'decimal:8',
        'longitud' => 'decimal:8',
        'fecha_llegada' => 'datetime',
        'fecha_salida' => 'datetime',
        'completado' => 'boolean',
    ];

    public function pedido()
    {
        return $this->belongsTo(Pedido::class, 'id_pedido', 'id_pedido');
    }

    public function etapa()
    {
        return $this->belongsTo(TrackingEtapa::class, 'id_etapa');
    }

    public function scopeCompletadas($query)
    {
        return $query->where('completado', true);
    }

    public function scopePendientes($query)
    {
        return $query->where('completado', false);
    }

    public function scopeOrdenadas($query)
    {
        return $query->orderBy('orden');
    }

    public function marcarCompletada()
    {
        $this->update([
            'completado' => true,
            'fecha_llegada' => $this->fecha_llegada ?? now(),
        ]);
    }

    public function marcarSalida()
    {
        $this->update([
            'fecha_salida' => now(),
        ]);
    }
}
