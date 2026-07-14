<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Cache;

class Configuracion extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_configuracion';

    protected $fillable = [
        'clave',
        'valor',
        'tipo',
        'descripcion',
    ];

    // Cache key
    const CACHE_KEY = 'ecommerce_config';
    const CACHE_TTL = 3600; // 1 hora

    // Methods
    public static function obtener(string $clave, $default = null)
    {
        $config = self::where('clave', $clave)->first();

        if (!$config) {
            return $default;
        }

        return self::castearValor($config->valor, $config->tipo);
    }

    public static function establecer(string $clave, $valor, string $tipo = 'string', ?string $descripcion = null): self
    {
        $config = self::updateOrCreate(
            ['clave' => $clave],
            [
                'valor' => is_array($valor) || is_object($valor) ? json_encode($valor) : (string)$valor,
                'tipo' => $tipo,
                'descripcion' => $descripcion,
            ]
        );

        self::limpiarCache();

        return $config;
    }

    public static function todas(): array
    {
        return Cache::remember(self::CACHE_KEY, self::CACHE_TTL, function () {
            $configs = [];
            foreach (self::all() as $config) {
                $configs[$config->clave] = self::castearValor($config->valor, $config->tipo);
            }
            return $configs;
        });
    }

    public static function limpiarCache(): void
    {
        Cache::forget(self::CACHE_KEY);
    }

    protected static function castearValor($valor, string $tipo)
    {
        return match($tipo) {
            'boolean' => filter_var($valor, FILTER_VALIDATE_BOOLEAN),
            'integer', 'number' => (int)$valor,
            'float', 'decimal' => (float)$valor,
            'array', 'json' => json_decode($valor, true),
            default => $valor,
        };
    }

    // Accessors
    public function getValorCasteadoAttribute()
    {
        return self::castearValor($this->valor, $this->tipo);
    }
}
