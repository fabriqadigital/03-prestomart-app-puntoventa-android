<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Categoria extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_categorias';

    protected $primaryKey = 'id_categoria'; // ← AÑADE ESTO
    public $incrementing = true; // si es autoincremental
    protected $keyType = 'int'; // si es entero


    protected $fillable = [
        'nombre',
        'slug',
        'descripcion',
        'imagen_url',
        'padre_id',
        'orden',
        'activo',
    ];

    protected $casts = [
        'activo' => 'boolean',
        'orden' => 'integer',
    ];

    // Relaciones
    public function padre(): BelongsTo
    {
        return $this->belongsTo(Categoria::class, 'padre_id');
    }

    public function hijos(): HasMany
    {
        return $this->hasMany(Categoria::class, 'padre_id')->orderBy('orden');
    }

    public function productos(): HasMany
    {
        return $this->hasMany(Producto::class, 'id_categoria');
    }

    // Scopes
    public function scopeActivas($query)
    {
        return $query->where('activo', true);
    }

    public function scopePrincipales($query)
    {
        return $query->whereNull('padre_id');
    }

    public function scopeOrdenadas($query)
    {
        return $query->orderBy('orden');
    }

    // Accessors
    public function getProductosCountAttribute(): int
    {
        return $this->productos()->where('activo', true)->count();
    }
}
