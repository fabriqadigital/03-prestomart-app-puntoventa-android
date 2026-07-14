<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Model;

class TrackingNotificacion extends Model
{
    protected $table = 'ecommerce_tracking_notificaciones';

    public $timestamps = false;

    protected $fillable = [
        'id_pedido',
        'id_cliente',
        'tipo',
        'id_estado',
        'titulo',
        'mensaje',
        'enviado',
        'fecha_envio',
        'error_mensaje',
        'created_at',
    ];

    protected $casts = [
        'enviado' => 'boolean',
        'fecha_envio' => 'datetime',
    ];

    const TIPO_EMAIL = 'email';
    const TIPO_PUSH = 'push';
    const TIPO_SMS = 'sms';
    const TIPO_WHATSAPP = 'whatsapp';

    public function pedido()
    {
        return $this->belongsTo(Pedido::class, 'id_pedido', 'id_pedido');
    }

    public function cliente()
    {
        return $this->belongsTo(Cliente::class, 'id_cliente', 'id_cliente');
    }

    public function estado()
    {
        return $this->belongsTo(TrackingEstado::class, 'id_estado');
    }

    public function scopeEnviadas($query)
    {
        return $query->where('enviado', true);
    }

    public function scopePendientes($query)
    {
        return $query->where('enviado', false);
    }

    public function scopePorTipo($query, string $tipo)
    {
        return $query->where('tipo', $tipo);
    }

    public function marcarEnviada()
    {
        $this->update([
            'enviado' => true,
            'fecha_envio' => now(),
        ]);
    }

    public function marcarError(string $error)
    {
        $this->update([
            'error_mensaje' => $error,
        ]);
    }
}
