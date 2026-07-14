<?php

namespace App\Models\Ecommerce;

use App\Models\User;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\Relations\HasOne;

class Cliente extends Model
{
    use HasFactory, SoftDeletes;

    protected $table = 'administracion_cliente';
        protected $primaryKey = 'id_cliente'; // ← AÑADE ESTO
        protected $keyType = 'int'; // si es entero

    protected $fillable = [
        'id_usuario',
        'nombre',
        'apellido',
        'email',
        'telefono',
        'avatar',
        'fecha_nacimiento',
        'genero',
        'activo',
    ];

    protected $casts = [
        'fecha_nacimiento' => 'date',
        'activo' => 'boolean',
    ];

    // Relaciones
    public function usuario(): BelongsTo
    {
        return $this->belongsTo(User::class, 'id_usuario');
    }

    public function direcciones(): HasMany
    {
        return $this->hasMany(Direccion::class, 'id_cliente');
    }

    public function direccionPrincipal(): HasOne
    {
        return $this->hasOne(Direccion::class, 'id_cliente')->where('es_principal', true);
    }

    public function carrito(): HasOne
    {
        return $this->hasOne(Carrito::class, 'id_cliente');
    }

    public function pedidos(): HasMany
    {
        return $this->hasMany(Pedido::class, 'id_cliente');
    }

    public function wishlist(): HasMany
    {
        return $this->hasMany(Wishlist::class, 'id_cliente');
    }

    public function reviews(): HasMany
    {
        return $this->hasMany(Review::class, 'id_cliente');
    }

    public function notificaciones(): HasMany
    {
        return $this->hasMany(Notificacion::class, 'id_cliente');
    }

    // Scopes
    public function scopeActivos($query)
    {
        return $query->where('activo', true);
    }

    // Accessors
    public function getNombreCompletoAttribute(): string
    {
        return "{$this->nombre} {$this->apellido}";
    }

    public function getTotalPedidosAttribute(): int
    {
        return $this->pedidos()->count();
    }

    public function getTotalGastadoAttribute(): float
    {
        return $this->pedidos()->where('pagado', true)->sum('total');
    }
}
