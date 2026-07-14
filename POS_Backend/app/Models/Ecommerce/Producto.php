<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Producto extends Model
{
    use HasFactory, SoftDeletes;

    protected $table = 'ecommerce_productos';
    protected $primaryKey = 'id_producto'; // ← AÑADE ESTO

    protected $fillable = [
        'id_categoria',
        'id_marca',
        'nombre',
        'slug',
        'descripcion',
        'descripcion_corta',
        'precio',
        'precio_oferta',
        'costo',
        'sku',
        'stock',
        'stock_minimo',
        'peso',
        'dimensiones',
        'imagen_principal',
        'imagenes',
        'especificaciones',
        'destacado',
        'nuevo',
        'activo',
    ];

    protected $casts = [
        'precio' => 'decimal:2',
        'precio_oferta' => 'decimal:2',
        'costo' => 'decimal:2',
        'peso' => 'decimal:3',
        'imagenes' => 'array',
        'especificaciones' => 'array',
        'destacado' => 'boolean',
        'nuevo' => 'boolean',
        'activo' => 'boolean',
        'stock' => 'integer',
        'stock_minimo' => 'integer',
    ];

    // Relaciones
    public function categoria(): BelongsTo
    {
        return $this->belongsTo(Categoria::class, 'id_categoria');
    }

    public function marca(): BelongsTo
    {
        return $this->belongsTo(Marca::class, 'id_marca');
    }

    public function variantes(): HasMany
    {
        return $this->hasMany(ProductoVariante::class, 'id_producto');
    }

    public function reviews(): HasMany
    {
        return $this->hasMany(Review::class, 'id_producto');
    }

    public function wishlistItems(): HasMany
    {
        return $this->hasMany(Wishlist::class, 'id_producto');
    }

    // Scopes
    public function scopeActivos($query)
    {
        return $query->where('activo', true);
    }

    public function scopeDestacados($query)
    {
        return $query->where('destacado', true);
    }

    public function scopeNuevos($query)
    {
        return $query->where('nuevo', true);
    }

    public function scopeEnOferta($query)
    {
        return $query->whereNotNull('precio_oferta');
    }

    public function scopeConStock($query)
    {
        return $query->where('stock', '>', 0);
    }

    public function scopeBuscar($query, $termino)
    {
        return $query->where(function ($q) use ($termino) {
            $q->where('nombre', 'LIKE', "%{$termino}%")
              ->orWhere('descripcion', 'LIKE', "%{$termino}%")
              ->orWhere('descripcion_corta', 'LIKE', "%{$termino}%")
              ->orWhere('sku', 'LIKE', "%{$termino}%");
        });
    }

    // Accessors
    public function getPrecioFinalAttribute(): float
    {
        return $this->precio_oferta ?? $this->precio;
    }

    public function getDescuentoPorcentajeAttribute(): ?float
    {
        if (!$this->precio_oferta || $this->precio <= 0) {
            return null;
        }
        return round((($this->precio - $this->precio_oferta) / $this->precio) * 100, 2);
    }

    public function getRatingPromedioAttribute(): float
    {
        return $this->reviews()->where('aprobado', true)->avg('rating') ?? 0;
    }

    public function getReviewsCountAttribute(): int
    {
        return $this->reviews()->where('aprobado', true)->count();
    }

    public function getDisponibleAttribute(): bool
    {
        return $this->stock > 0 && $this->activo;
    }
}
