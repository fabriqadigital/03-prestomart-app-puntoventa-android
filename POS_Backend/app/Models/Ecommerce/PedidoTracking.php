<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Support\Facades\Log;

class PedidoTracking extends Model
{
    protected $table = 'ecommerce_pedido_tracking';

    protected $fillable = [
        'id_pedido',
        'id_estado',
        'id_etapa',
        'codigo_rastreo',
        'transportista',
        'numero_guia',
        'url_transportista',
        'fecha_estimada_entrega',
        'fecha_entrega_real',
        'direccion_actual',
        'latitud',
        'longitud',
        'notas',
    ];

    protected $casts = [
        'fecha_estimada_entrega' => 'date',
        'fecha_entrega_real' => 'datetime',
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

    public function historial(): HasMany
    {
        return $this->hasMany(TrackingHistorial::class, 'id_pedido', 'id_pedido')
                    ->orderBy('fecha_evento', 'desc');
    }

    public function ubicaciones(): HasMany
    {
        return $this->hasMany(TrackingUbicacion::class, 'id_pedido', 'id_pedido')
                    ->orderBy('orden');
    }

    // Methods
    public static function generarCodigoRastreo(int $pedidoId): string
    {
        return 'TRK' . date('Y') . str_pad($pedidoId, 8, '0', STR_PAD_LEFT);
    }

    public function actualizarEstado(string $codigoEstado, ?string $codigoEtapa = null, array $datos = []): void
    {
        $estado = TrackingEstado::obtenerPorCodigo($codigoEstado);
        $etapa = $codigoEtapa ? TrackingEtapa::obtenerPorCodigo($codigoEtapa) : null;



        
        $this->update([
            'id_estado' => $estado->id,
            'id_etapa' => $etapa?->id ?? $this->id_etapa,
            'direccion_actual' => $datos['ubicacion'] ?? $this->direccion_actual,
            'latitud' => $datos['latitud'] ?? $this->latitud,
            'longitud' => $datos['longitud'] ?? $this->longitud,
        ]);


                Log::channel('stderr')->info("test11" . json_encode($estado));
        Log::channel('stderr')->info("test22" . json_encode($etapa));

        // Registrar en historial
        TrackingHistorial::create([
            'id_pedido' => $this->id_pedido,
            'id_estado' => $estado->id,
            'id_etapa' => $etapa?->id,
            'titulo' => $datos['titulo'] ?? $estado->nombre,
            'descripcion' => $datos['descripcion'] ?? $estado->descripcion,
            'ubicacion' => $datos['ubicacion'] ?? null,
            'latitud' => $datos['latitud'] ?? null,
            'longitud' => $datos['longitud'] ?? null,
            'operador' => $datos['operador'] ?? 'sistema',
            'metadata' => $datos['metadata'] ?? null,
            'fecha_evento' => now(),
        ]);

        // Si está entregado
        if ($codigoEstado === 'entregado') {
            $this->update(['fecha_entrega_real' => now()]);
            $this->pedido->update(['estado' => 'entregado']);
        }

        // Marcar ubicación como completada
        if ($etapa) {
            TrackingUbicacion::where('id_pedido', $this->id_pedido)
                ->where('id_etapa', $etapa->id)
                ->update([
                    'completado' => true,
                    'fecha_llegada' => now(),
                ]);
        }
    }

    public static function buscarPorCodigo(string $codigoRastreo): ?self
    {
        return self::with(['estado', 'etapa', 'pedido', 'historial', 'ubicaciones'])
                   ->where('codigo_rastreo', $codigoRastreo)
                   ->first();
    }

    // Accessors
    public function getProgresoAttribute(): int
    {
        if (!$this->estado) return 0;

        $totalEstados = TrackingEstado::where('es_final', false)->count();
        $ordenActual = $this->estado->orden;

        return min(100, round(($ordenActual / $totalEstados) * 100));
    }
}
