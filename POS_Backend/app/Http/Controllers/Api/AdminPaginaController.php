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
use App\Models\PaginaMenu;
use Illuminate\Support\Collection;
use Illuminate\Database\Eloquent\Collection as EloquentCollection;

class AdminPaginaController extends Controller
{
    public function listar()
    {
        $result = DB::select('CALL USP_ADMINISTRACION_PAGINA_ACCESO_LISTAR()');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' =>  $result
        ]);
    }

    //============= OPERACIONES DE CRUD (El resto aun del crud de arriba no estan funcional) =================================


    public function admin_pagina_crear(Request $request)
    {
        DB::beginTransaction();
        try {
            $validated = $request->validate([
                'nombre' => 'required|string|max:255',
                'icono' => 'nullable|string|max:50',
                'icon_text' => 'nullable|string|max:10',
                'ruta' => 'nullable|string|max:255',
                'tipo' => 'required|in:parent,label,submenu',
                'id_padre' => 'nullable|exists:sistema_menu,id_menu',
                'orden' => 'nullable|integer',
                'objetos_asignados' => 'nullable|array',
                'objetos_asignados.*' => 'integer|exists:sistema_objetos,id_objetos'
            ]);

            // Generar id_menu secuencial
            $validated['id_menu'] = PaginaMenu::getNextMenuId();

            // Calcular orden si no viene
            if (!isset($validated['orden'])) {
                $lastOrder = PaginaMenu::where('id_padre', $validated['id_padre'] ?? null)
                    ->max('orden');
                $validated['orden'] = $lastOrder ? $lastOrder + 1 : 0;
            }

            // Validar icono por defecto si no se proporciona
            if (!isset($validated['icono'])) {
                $validated['icono'] = 'dashboard';
            } else {
                $validated['icono'] = $request->icono;
            }

            $menu = PaginaMenu::create($validated);

            // Guardar relaciones con objetos
            if (!empty($request->objetos_asignados)) {
                $objetosData = [];
                foreach ($request->objetos_asignados as $id_objeto) {
                    $objetosData[] = [
                        'id_menu' => $menu->id_menu,
                        'id_objetos' => $id_objeto,
                        'created_at' => now(),
                        'updated_at' => now()
                    ];
                }

                DB::table('sistema_menu_objetos')->insert($objetosData);
            }

            DB::commit();
            return response()->json([
                'success' => true,
                'menu' => $menu,
                'objetos_asignados' => $request->objetos_asignados ?? []
            ], 201);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function admin_pagina_actualizar(Request $request)
    {
        $id = $request->input('id');
        DB::beginTransaction();
        try {
            $validated = $request->validate([
                'nombre' => 'required|string|max:255',
                'icono' => 'nullable|string|max:50',
                'icon_text' => 'nullable|string|max:10',
                'ruta' => 'nullable|string|max:255',
                'tipo' => 'required|in:parent,label,submenu',
                'id_padre' => 'nullable|exists:sistema_menu,id_menu',
                'orden' => 'nullable|integer',
                'objetos_asignados' => 'nullable|array',
                'objetos_asignados.*' => 'integer|exists:sistema_objetos,id_objetos'
            ]);

            // Actualización del menú principal
            $updated = PaginaMenu::where('id_menu', $id)->update($validated);

            if (!$updated) {
                throw new \Exception("No se encontró el menú con ID: {$id}");
            }

            // Sincronizar relaciones con objetos (sin eliminación)
            if (array_key_exists('objetos_asignados', $validated)) {
                $objetosActuales = $validated['objetos_asignados'] ?? [];
                $objetosExistentes = DB::table('sistema_menu_objetos')
                    ->where('id_menu', $id)
                    ->pluck('id_objetos')
                    ->toArray();

                // Identificar objetos nuevos a agregar (los que no existen actualmente)
                $objetosNuevos = array_diff($objetosActuales, $objetosExistentes);

                if (!empty($objetosNuevos)) {
                    $objetosData = array_map(function ($id_objeto) use ($id) {
                        return [
                            'id_menu' => $id,
                            'id_objetos' => $id_objeto,
                            'created_at' => now(),
                            'updated_at' => now(),
                            'Activo' => 'S' // Asumiendo que este campo es necesario
                        ];
                    }, $objetosNuevos);

                    DB::table('sistema_menu_objetos')->insert($objetosData);
                }

                // Los objetos que ya existían permanecen sin cambios
                // (No se realiza ninguna operación de eliminación)
            }

            DB::commit();
            return response()->json([
                'success' => true,
                'message' => 'Menú actualizado correctamente'
            ]);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function admin_pagina_eliminar(Request $request)
    {
        // Log::channel('stderr')->info("Aqui".json_encode($request->all()));  
        $id = $request->input('id_principal');
        DB::beginTransaction();
        try {
            PaginaMenu::where('id_menu', $id)->delete();
            // $menu = PaginaMenu::findOrFail($id);
            // $menu->delete();

            DB::commit();
            return response()->json(null, 204);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json(['error' => $e->getMessage()], 500);
        }
    }

    public function show($id)
    {
        try {
            $menu = PaginaMenu::with('children')->findOrFail($id);
            return response()->json($menu);
        } catch (\Exception $e) {
            return response()->json(['error' => $e->getMessage()], 404);
        }
    }

    // En tu controlador Laravel
    public function admin_pagina_actualizar_reordenar(Request $request)
    {
        $request->validate([
            '*.id_menu' => 'required|integer|exists:sistema_menu,id_menu',
            '*.orden' => 'required|integer'
        ]);

        DB::beginTransaction();
        try {
            foreach ($request->all() as $item) {
                Log::channel('stderr')->info("Aqui" . json_encode($request->all()));
                PaginaMenu::where('id_menu', $item['id_menu'])
                    ->update(['orden' => $item['orden']]);
            }

            DB::commit();
            return response()->json(['success' => true]);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json(['error' => $e->getMessage()], 500);
        }
    }
    //mE TRAE TODOS LOS MENUS RAMIFICADOS Y RECURSIVOS
    //================ ESTA FUNCION ES ESCLISIVA PARA EL MENU O SIDERvAN LEFT ========
    public function admin_pagina_listar_path()
    {
        $result = DB::select('CALL USP_ADMINISTRACION_PAGINA_LISTAR_PATH()');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' =>  $result
        ]);
    }
}
