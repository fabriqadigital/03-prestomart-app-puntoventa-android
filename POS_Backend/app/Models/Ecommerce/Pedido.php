<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Pedido extends Model
{
    use HasFactory, SoftDeletes;

    protected $table = 'ecommerce_pedidos';

    protected $primaryKey = 'id_pedido';

    protected $fillable = [
        'id_cliente',
        'numero_pedido',
        'estado',
        'envio_direccion',
        'id_metodo_envio',
        'id_metodo_pago',
        'subtotal',
        'descuento',
        'codigo_cupon',
        // 'envio',
        // 'impuestos',
        'total',
        'notas',
        'pagado',
        'fecha_pago',
        // 'ip_address',
    ];

    protected $casts = [
        'envio_direccion' => 'array',
        'subtotal' => 'decimal:2',
        'descuento' => 'decimal:2',
        // 'envio' => 'decimal:2',
        // 'impuestos' => 'decimal:2',
        'total' => 'decimal:2',
        'pagado' => 'boolean',
        'fecha_pago' => 'datetime',
    ];

    const ESTADO_PENDIENTE = 'pendiente';
    const ESTADO_PROCESANDO = 'procesando';
    const ESTADO_ENVIADO = 'enviado';
    const ESTADO_ENTREGADO = 'entregado';
    const ESTADO_CANCELADO = 'cancelado';
    const ESTADO_REEMBOLSADO = 'reembolsado';

    // Relaciones
    public function cliente(): BelongsTo
    {
        return $this->belongsTo(Cliente::class, 'id_cliente');
    }

    public function metodoEnvio(): BelongsTo
    {
        return $this->belongsTo(MetodoEnvio::class, 'id_metodo_envio');
    }

    public function metodoPago(): BelongsTo
    {
        return $this->belongsTo(MetodoPago::class, 'id_metodo_pago');
    }

    public function cupon(): BelongsTo
    {
        return $this->belongsTo(Cupon::class, 'codigo_cupon');
    }

    public function items(): HasMany
    {
        return $this->hasMany(PedidoItem::class, 'id_pedido');
    }

    public function historial(): HasMany
    {
        return $this->hasMany(PedidoHistorial::class, 'id_pedido')->orderBy('created_at', 'desc');
    }

    // Scopes
    public function scopePendientes($query)
    {
        return $query->where('estado', self::ESTADO_PENDIENTE);
    }

    public function scopeProcesando($query)
    {
        return $query->where('estado', self::ESTADO_PROCESANDO);
    }

    public function scopeEnviados($query)
    {
        return $query->where('estado', self::ESTADO_ENVIADO);
    }

    public function scopeEntregados($query)
    {
        return $query->where('estado', self::ESTADO_ENTREGADO);
    }

    public function scopeCancelados($query)
    {
        return $query->where('estado', self::ESTADO_CANCELADO);
    }

    public function scopePagados($query)
    {
        return $query->where('pagado', true);
    }

    // Methods
    public function cambiarEstado(string $nuevoEstado, ?string $comentario = null, ?string $creadoPor = null): void
    {
        $estadoAnterior = $this->estado;
        $this->estado = $nuevoEstado;
        $this->save();

        $this->historial()->create([
            'estado_anterior' => $estadoAnterior,
            'estado_nuevo' => $nuevoEstado,
            'comentario' => $comentario,
            'creado_por' => $creadoPor ?? 'sistema',
        ]);
    }

    public function marcarComoPagado(): void
    {
        $this->pagado = true;
        $this->fecha_pago = now();
        $this->save();

        if ($this->estado === self::ESTADO_PENDIENTE) {
            $this->cambiarEstado(self::ESTADO_PROCESANDO, 'Pago confirmado');
        }
    }

    public function cancelar(?string $motivo = null): void
    {
        $this->cambiarEstado(self::ESTADO_CANCELADO, $motivo);
    }

    public static function generarNumeroPedido(): string
    {
        $year = date('Y');
        $ultimoPedido = self::whereYear('created_at', $year)
                            ->orderBy('id_pedido', 'desc')
                            ->first();

        $numero = $ultimoPedido ? intval(substr($ultimoPedido->numero_pedido, -5)) + 1 : 1;

        return sprintf('ORD-%s-%05d', $year, $numero);
    }

    // Accessors
    public function getEstadoLabelAttribute(): string
    {
        return match($this->estado) {
            self::ESTADO_PENDIENTE => 'Pendiente',
            self::ESTADO_PROCESANDO => 'Procesando',
            self::ESTADO_ENVIADO => 'Enviado',
            self::ESTADO_ENTREGADO => 'Entregado',
            self::ESTADO_CANCELADO => 'Cancelado',
            self::ESTADO_REEMBOLSADO => 'Reembolsado',
            default => $this->estado,
        };
    }

    public function getTotalItemsAttribute(): int
    {
        return $this->items->sum('cantidad');
    }
}
