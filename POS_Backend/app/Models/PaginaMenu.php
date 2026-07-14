<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\Relations\BelongsTo;


class PaginaMenu extends Model
{
protected $table = 'sistema_menu';
    protected $primaryKey = 'id'; // Usar el id auto-incremental como clave primaria
    
    // Mantener id_menu como identificador único pero no como PK
    protected $fillable = [
        'id_menu', // Añadir esto al fillable
        'nombre',
        'icono',
        'icon_text',
        'ruta',
        'tipo',
        'id_padre',
        'orden',
        'id',
    ];

        public static function getNextMenuId()
    {
        $last = self::orderBy('id_menu', 'desc')->first();
        return $last ? $last->id_menu + 1 : 1;
    }
    
    public function children(): HasMany
    {
        return $this->hasMany(PaginaMenu::class, 'id_padre')->orderBy('orden');
    }

    public function parent(): BelongsTo
    {
        return $this->belongsTo(PaginaMenu::class, 'id_padre');
    }


}