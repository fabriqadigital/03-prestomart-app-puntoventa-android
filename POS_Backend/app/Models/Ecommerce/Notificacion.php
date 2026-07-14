<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Notificacion extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_notificaciones';

    protected $fillable = [
        'id_cliente',
        'tipo',
        'titulo',
        'mensaje',
        'datos',
        'leido',
    ];

    protected $casts = [
        'datos' => 'array',
        'leido' => 'boolean',
    ];

    // Relaciones
    public function cliente(): BelongsTo
    {
        return $this->belongsTo(Cliente::class, 'id_cliente');
    }

    // Scopes
    public function scopeNoLeidas($query)
    {
        return $query->where('leido', false);
    }

    public function scopeLeidas($query)
    {
        return $query->where('leido', true);
    }

    public function scopePorTipo($query, string $tipo)
    {
        return $query->where('tipo', $tipo);
    }

    public function scopeRecientes($query, int $dias = 30)
    {
        return $query->where('created_at', '>=', now()->subDays($dias));
    }

    // Methods
    public function marcarComoLeida(): void
    {
        $this->leido = true;
        $this->save();
    }

    public static function enviar(int $clienteId, string $tipo, string $titulo, string $mensaje, array $datos = []): self
    {
        return self::create([
            'id_cliente' => $clienteId,
            'tipo' => $tipo,
            'titulo' => $titulo,
            'mensaje' => $mensaje,
            'datos' => $datos,
        ]);
    }
}
