<?php

namespace App\Models\Ecommerce;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Carrito extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_carritos';

    protected $fillable = [
        'id_cliente',
        'session_id',
        'codigo_cupon',
    ];

    // Relaciones
    public function cliente(): BelongsTo
    {
        return $this->belongsTo(Cliente::class, 'id_cliente');
    }

    public function cupon(): BelongsTo
    {
        return $this->belongsTo(Cupon::class, 'codigo_cupon');
    }

    public function items(): HasMany
    {
        return $this->hasMany(CarritoItem::class, 'id_carrito');
    }

    // Methods
    public function agregarItem(int $productoId, int $cantidad = 1, ?int $varianteId = null): CarritoItem
    {
        $producto = Producto::findOrFail($productoId);
        $precio = $producto->precio_final;

        if ($varianteId) {
            $variante = ProductoVariante::findOrFail($varianteId);
            $precio += $variante->precio_adicional;
        }

        $item = $this->items()->where('id_producto', $productoId)
                              ->where('id_variante', $varianteId)
                              ->first();

        if ($item) {
            $item->cantidad += $cantidad;
            $item->save();
        } else {
            $item = $this->items()->create([
                'id_producto' => $productoId,
                'id_variante' => $varianteId,
                'cantidad' => $cantidad,
                'precio_unitario' => $precio,
            ]);
        }

        return $item;
    }

    public function actualizarCantidad(int $itemId, int $cantidad): ?CarritoItem
    {
        $item = $this->items()->find($itemId);

        if (!$item) {
            return null;
        }

        if ($cantidad <= 0) {
            $item->delete();
            return null;
        }

        $item->cantidad = $cantidad;
        $item->save();

        return $item;
    }

    public function eliminarItem(int $itemId): bool
    {
        return $this->items()->where('id', $itemId)->delete() > 0;
    }

    public function vaciar(): void
    {
        $this->items()->delete();
        $this->codigo_cupon = null;
        $this->save();
    }

    public function aplicarCupon(string $codigo): array
    {
        $cupon = Cupon::where('codigo', $codigo)->first();

        if (!$cupon) {
            return ['success' => false, 'message' => 'Cupón no encontrado'];
        }

        if (!$cupon->esValido($this->subtotal)) {
            return ['success' => false, 'message' => 'Cupón no válido o expirado'];
        }

        $this->codigo_cupon = $cupon->id;
        $this->save();

        return [
            'success' => true,
            'message' => 'Cupón aplicado correctamente',
            'descuento' => $cupon->calcularDescuento($this->subtotal)
        ];
    }

    public function removerCupon(): void
    {
        $this->codigo_cupon = null;
        $this->save();
    }

    // Accessors
    public function getSubtotalAttribute(): float
    {
        return $this->items->sum(function ($item) {
            return $item->precio_unitario * $item->cantidad;
        });
    }

    public function getDescuentoAttribute(): float
    {
        if (!$this->cupon) {
            return 0;
        }
        return $this->cupon->calcularDescuento($this->subtotal);
    }

    public function getTotalItemsAttribute(): int
    {
        return $this->items->sum('cantidad');
    }

    public function getTotalAttribute(): float
    {
        return $this->subtotal - $this->descuento;
    }
}
