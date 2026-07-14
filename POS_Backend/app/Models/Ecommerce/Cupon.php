<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;

class Cupon extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_cupones';

    protected $fillable = [
        'codigo',
        'tipo',
        'valor',
        'minimo_compra',
        'maximo_descuento',
        'usos_totales',
        'usos_por_usuario',
        'usos_actuales',
        'fecha_inicio',
        'fecha_fin',
        'activo',
    ];

    protected $casts = [
        'valor' => 'decimal:2',
        'minimo_compra' => 'decimal:2',
        'maximo_descuento' => 'decimal:2',
        'usos_totales' => 'integer',
        'usos_por_usuario' => 'integer',
        'usos_actuales' => 'integer',
        'fecha_inicio' => 'datetime',
        'fecha_fin' => 'datetime',
        'activo' => 'boolean',
    ];

    // Scopes
    public function scopeActivos($query)
    {
        return $query->where('activo', true);
    }

    public function scopeVigentes($query)
    {
        $now = Carbon::now();
        return $query->where('fecha_inicio', '<=', $now)
                     ->where('fecha_fin', '>=', $now);
    }

    public function scopeDisponibles($query)
    {
        return $query->activos()
                     ->vigentes()
                     ->whereRaw('usos_totales IS NULL OR usos_actuales < usos_totales');
    }

    // Methods
    public function esValido(float $subtotal = 0): bool
    {
        if (!$this->activo) {
            return false;
        }

        $now = Carbon::now();
        if ($now < $this->fecha_inicio || $now > $this->fecha_fin) {
            return false;
        }

        if ($this->usos_totales && $this->usos_actuales >= $this->usos_totales) {
            return false;
        }

        if ($this->minimo_compra && $subtotal < $this->minimo_compra) {
            return false;
        }

        return true;
    }

    public function calcularDescuento(float $subtotal): float
    {
        if (!$this->esValido($subtotal)) {
            return 0;
        }

        $descuento = 0;

        switch ($this->tipo) {
            case 'porcentaje':
                $descuento = $subtotal * ($this->valor / 100);
                if ($this->maximo_descuento) {
                    $descuento = min($descuento, $this->maximo_descuento);
                }
                break;
            case 'fijo':
                $descuento = min($this->valor, $subtotal);
                break;
            case 'envio_gratis':
                $descuento = 0;
                break;
        }

        return round($descuento, 2);
    }

    public function incrementarUso(): void
    {
        $this->increment('usos_actuales');
    }

    // Accessors
    public function getEsVigenteAttribute(): bool
    {
        $now = Carbon::now();
        return $now >= $this->fecha_inicio && $now <= $this->fecha_fin;
    }

    public function getUsosDisponiblesAttribute(): ?int
    {
        if (!$this->usos_totales) {
            return null;
        }
        return max(0, $this->usos_totales - $this->usos_actuales);
    }
}
