<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\VendedorResource;
use App\Models\MaterialCategoria;
use Illuminate\Support\Facades\DB;
use App\Models\PaginaAcceso;
use Illuminate\Support\Collection;
use Illuminate\Database\Eloquent\Collection as EloquentCollection;


class AdminPaginaAccesoController extends Controller
{
    public function listar_menu_accesos(Request $request)
    {
        try {
            // El middleware auth:api ya valida el token JWT
            // Obtenemos el usuario autenticado (opcional, solo para logs)
            $user = auth()->user();
            if ($user) {
                Log::info('Usuario autenticado solicitando menús: ' . $user->id);
            }

            // Consulta SQL para obtener todos los menús con su estructura
            // Ajusta según tus nombres de tablas y columnas
            $menus = DB::table('sistema_menu as SM')
                ->leftJoin('sistema_menu_objetos as SMO', 'SM.id_menu', '=', 'SMO.id_menu')
                ->select(
                    'SM.id_menu',
                    'SM.id_padre',
                    'SM.nombre',
                    'SM.ruta',
                    'SM.icono',
                    'SM.icon_text',
                    'SM.orden',
                    'SM.Activo',
                    DB::raw('GROUP_CONCAT(DISTINCT CONCAT(SMO.id_menu, "_", SMO.id_objetos) SEPARATOR ",") as objetos_asignados')
                )
                ->where('SM.Activo', 'S')
                ->groupBy(
                    'SM.id_menu',
                    'SM.id_padre',
                    'SM.nombre',
                    'SM.ruta',
                    'SM.icono',
                    'SM.icon_text',
                    'SM.orden',
                    'SM.Activo'
                )
                ->orderBy('SM.orden', 'ASC')
                ->get();

            // Formatear datos según el formato esperado por el frontend
            $menusFormatted = $menus->map(function ($menu) {
                // Formatear objetos_asignados si existe
                $objetosAsignados = '';
                if ($menu->objetos_asignados) {
                    // Agrupar por id_menu y crear string en formato |menuId_obj1,obj2|
                    $objetosArray = explode(',', $menu->objetos_asignados);
                    $objetosPorMenu = [];

                    foreach ($objetosArray as $obj) {
                        if (strpos($obj, '_') !== false) {
                            list($menuId, $objId) = explode('_', $obj);
                            if (!isset($objetosPorMenu[$menuId])) {
                                $objetosPorMenu[$menuId] = [];
                            }
                            $objetosPorMenu[$menuId][] = $objId;
                        }
                    }

                    // Construir string en formato |menuId_obj1,obj2|
                    foreach ($objetosPorMenu as $menuId => $objetos) {
                        if ($menuId == $menu->id_menu) {
                            $objetosAsignados = '|' . $menuId . '_' . implode(',', $objetos) . '|';
                            break;
                        }
                    }
                }

                return [
                    'id_menu' => (int)$menu->id_menu,
                    'id_padre' => $menu->id_padre ? (int)$menu->id_padre : null,
                    'nombre' => $menu->nombre,
                    'ruta' => $menu->ruta ?? null,
                    'icono' => $menu->icono ?? $menu->icon_text ?? null,
                    'icon_text' => $menu->icon_text ?? null,
                    'orden' => (int)$menu->orden,
                    'activo' => $menu->Activo ?? 'S',
                    'objetos_asignados' => $objetosAsignados,
                ];
            });

            // Retornar en formato esperado por el frontend
            return response()->json([
                'success' => true,
                'message' => 'Menús obtenidos correctamente',
                'result' => $menusFormatted->values()->all()
            ], 200);
        } catch (\Exception $e) {
            Log::error('Error en listar_menu_accesos: ' . $e->getMessage());
            Log::error('Stack trace: ' . $e->getTraceAsString());

            return response()->json([
                'success' => false,
                'message' => 'Error al obtener los menús: ' . $e->getMessage(),
                'result' => []
            ], 500);
        }
    }

    //============= OPERACIONES DE CRUD (El resto aun del crud de arriba no estan funcional) =================================
    //================= ESTA FUNCION ME TRAE TODA LA RAMIFICACION DEL MENU O LISTADO =========
    public function listar_paginas_accesos(): JsonResponse
    {
        try {
            $menuItems = DB::select('CALL USP_ADMINISTRACION_PAGINA_ACCESO_LISTAR()');

            $menuArray = array_map(function ($item) {
                $itemArray = (array)$item;

                // Convertir el string JSON a array PHP
                $itemArray['objetos_asignados'] = json_decode($itemArray['objetos_asignados'], true) ?? [];

                return $itemArray;
            }, $menuItems);

            $hierarchicalMenu = $this->buildMenuHierarchy($menuArray);

            return response()->json([
                'success' => true,
                'message' => 'Listar registros',
                'result' => $hierarchicalMenu
            ]);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Error al cargar el menú: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Construir la estructura jerárquica del menú
     */
    private function buildMenuHierarchy(array $items, ?int $parentId = null): array
    {
        $result = [];

        $filteredItems = array_filter($items, function ($item) use ($parentId) {
            return $item['id_padre'] == $parentId;
        });

        usort($filteredItems, function ($a, $b) {
            return $a['orden'] <=> $b['orden'];
        });

        foreach ($filteredItems as $item) {
            $node = [
                'id_menu' => $item['id_menu'],
                'name' => $item['nombre'],
                'id_padre' => $item['id_padre'],
                'path' => $item['ruta'],
                'icon' => $item['icono'],
                'tipo' => $item['tipo'],
                'iconText' => $item['iconText'] ?? null,
                'orden' => $item['orden'],
                'id' => $item['id'],
                'objetos_asignados' => $item['objetos_asignados'] // Asegúrate de incluir esto
            ];

            $children = $this->buildMenuHierarchy($items, $item['id_menu']);
            if (!empty($children)) {
                $node['children'] = $children;
            }

            $result[] = $node;
        }

        return $result;
    }
}
