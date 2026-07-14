<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;


class PaginaAcceso extends Model
{
    protected $table = 'sistema_menu';
    public $timestamps = false;

    public static function getHierarchy()
    {
        // Ejecutar el procedimiento almacenado
        $results = DB::select('CALL USP_ADMINISTRACION_PAGINA_ACCESO_LISTAR()');
        
        // Convertir a colección para mejor manipulación
        $menuItems = collect($results);
        
        // Construir la jerarquía
        return self::buildTree($menuItems);
    }

    protected static function buildTree($items, $parentId = null)
    {
        $tree = collect();
        
        $filtered = $items->where('id_padre', $parentId)
                         ->sortBy('orden');
        
        foreach ($filtered as $item) {
            $node = [
                'name' => $item->nombre,
                'icon' => $item->icono ?: null,
                'path' => $item->ruta ?: null,
                'id_menu' => $item->id_menu,
            ];
            
            // Si no es hoja, buscar hijos
            if (!$item->es_hoja) {
                $children = self::buildTree($items, $item->id_menu);
                if ($children->isNotEmpty()) {
                    $node['children'] = $children->toArray();
                }
            }
            
            // Manejar iconText para items sin icono
            if(empty($node['icon'])) {
                $node['iconText'] = strtoupper(substr($item->nombre, 0, 2));
            }
            
            $tree->push($node);
        }
        
        return $tree;
    }
}