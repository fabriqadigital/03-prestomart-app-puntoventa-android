<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class PaginaAcceso extends Model
{
    protected $table = 'administracion_paginas_acceso';
    protected $fillable = ['id_menu', 'nombre', 'ruta', 'icono', 'tipo', 'id_padre', 'orden'];
    public $timestamps = true;

    public function children()
    {
        return $this->hasMany(PaginaAcceso::class, 'id_padre', 'id_menu')
                   ->orderBy('orden');
    }

    public static function getNavigationStructure()
    {
        // Obtener elementos raíz (sin padre)
        $rootItems = self::whereNull('id_padre')
                        ->orderBy('orden')
                        ->get();

        return $rootItems->map(function ($item) {
            return self::formatNavigationItem($item);
        })->toArray();
    }

    protected static function formatNavigationItem($item)
    {
        $formatted = [
            'name' => $item->nombre,
            'id_menu' => $item->id_menu,
        ];

        // Agregar icono o iconText según corresponda
        if ($item->icono === 'SI') {
            $formatted['iconText'] = $item->icono;
        } elseif (!empty($item->icono)) {
            $formatted['icon'] = $item->icono;
        }

        // Agregar ruta si existe
        if (!empty($item->ruta)) {
            $formatted['path'] = $item->ruta;
        }

        // Manejar tipos especiales
        if ($item->tipo === 'label') {
            $formatted['label'] = $item->nombre;
            $formatted['type'] = 'label';
            unset($formatted['name']);
        } else {
            $formatted['type'] = $item->children->isEmpty() ? 'link' : 'extLink';
        }

        // Agregar hijos si existen
        if ($item->children->isNotEmpty()) {
            $formatted['children'] = $item->children->map(function ($child) {
                return self::formatNavigationItem($child);
            })->toArray();
        }

        return $formatted;
    }
}