<?php

namespace App\Models\Ecommerce;

use App\Models\User;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Support\Str;
use Carbon\Carbon;

class RefreshToken extends Model
{
    use HasFactory;

    protected $table = 'ecommerce_refresh_tokens';

    protected $fillable = [
        'user_id',
        'token',
        'expires_at',
        'revoked',
        'ip_address',
        'user_agent',
    ];

    protected $casts = [
        'expires_at' => 'datetime',
        'revoked' => 'boolean',
    ];

    protected $hidden = [
        'token',
    ];

    // Relaciones
    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    // Scopes
    public function scopeActivos($query)
    {
        return $query->where('revoked', false)
                     ->where('expires_at', '>', now());
    }

    public function scopeExpirados($query)
    {
        return $query->where('expires_at', '<=', now());
    }

    public function scopeRevocados($query)
    {
        return $query->where('revoked', true);
    }

    // Methods
    public static function generar(int $userId, ?string $ipAddress = null, ?string $userAgent = null): self
    {
        return self::create([
            'user_id' => $userId,
            'token' => Str::random(64),
            'expires_at' => Carbon::now()->addDays(30),
            'ip_address' => $ipAddress,
            'user_agent' => $userAgent,
        ]);
    }

    public function revocar(): void
    {
        $this->revoked = true;
        $this->save();
    }

    public function esValido(): bool
    {
        return !$this->revoked && $this->expires_at > now();
    }

    public static function revocarTodos(int $userId): int
    {
        return self::where('user_id', $userId)
                   ->where('revoked', false)
                   ->update(['revoked' => true]);
    }

    public static function limpiarExpirados(): int
    {
        return self::where('expires_at', '<=', now()->subDays(7))->delete();
    }

    // Accessors
    public function getEsValidoAttribute(): bool
    {
        return $this->esValido();
    }
}
