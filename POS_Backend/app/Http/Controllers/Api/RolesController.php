<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\RolesResource;
use App\Models\Roles;
use App\Models\RolesMenu;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class RolesController extends Controller
{

    public function obtener(Request $request): JsonResponse
    {
        $id = $request->id;
        $result = DB::select('select * from seguridad_roles where id_roles = ?', [$id]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }

    public function listar()
    {
        $result = DB::select('CALL USP_SEGURIDAD_ROLES_LISTAR()');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' =>  $result
        ]);
    }



    public function crear(Request $request): JsonResponse
    {
        // Log::channel('stderr')->info($request->Activo);
        $result = Roles::create([
            'nombre'          => $request->nombre,
            'created_at'      => date("Y-m-d H:i:s"),
            'updated_at'      => date("Y-m-d H:i:s"),
            'Activo'          => $request->Activo,
        ]);
        return response()->json([
            'success' => true,
            'message' => 'Registro insertado',
            'result' =>  new RolesResource($result)
        ]);
    }

    public function actualizar(Request $request): JsonResponse
    {
        $id = $request->input('id');

        $result = Roles::where('id_roles', $id)
            ->update([
                'nombre'          => $request->nombre,

                'updated_at'      => date("Y-m-d H:i:s"),
                'Activo'          => $request->Activo,
            ]);

        return response()->json([
            'success' => true,
            'message' => 'Registro actualizado',
            'result'  => $result
        ]);
    }

    public function eliminar(Request $request): JsonResponse
    {
        $id = $request->input('id');
        $result = Roles::where('id_roles', $id)->delete();

        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }

    //======= Seleccion multiple =========

    public function obtener_asignar(Request $request): JsonResponse
    {
        $id_perfil = $request->id_perfil;
        $cadena_id_roles = $request->id_roles;
        $result = DB::select('CALL USP_SEGURIDAD_ROLES_ASIGNAR(?,?)', [$id_perfil, $cadena_id_roles]);
        return response()->json([
            'success' => true,
            'message' => 'Registros asignados!',
            'result' => $result
        ]);
    }

    public function obtener_lista(Request $request): JsonResponse
    {
        $id_perfil = $request->id_perfil;
        $result = DB::select('CALL USP_SEGURIDAD_ROLES_OBTENER_LISTA(?)', [$id_perfil]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }

    public function obtener_check(Request $request): JsonResponse
    {
        $id_perfil = $request->id_perfil;
        $result = DB::select('CALL USP_SEGURIDAD_ROLES_OBTENER_CHECK(?)', [$id_perfil]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }

    public function obtener_eliminar(Request $request): JsonResponse
    {
        $id_roles_perfil = $request->input('id');
        $result = DB::select('CALL USP_SEGURIDAD_ROLES_OBTENER_ELIMINAR(?)', [$id_roles_perfil]);
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }

    public function eliminar_multiple(Request $request): JsonResponse
    {
        $id_roles_perfil = $request->input('id_roles_perfil');
        $result = DB::select('CALL USP_SEGURIDAD_ROLES_ELIMINAR_MULTIPLE(?)', [$id_roles_perfil]);
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }

    public function agregar_permisos(Request $request): JsonResponse
    {

        /*
            function special_chunk($array, $size) {
                $chunks = array_chunk($array, $size);
                $count = count($chunks);
                
                if( count($chunks[$count-1]) < $size ) {
                    $chunks[$count-2] = array_merge($chunks[$count-2], $chunks[$count-1]);
                    unset($chunks[$count-1]);
                }
                return $chunks;
            }

            $languages = ['php', 'mysql', 'java', 'nodejs', 'ruby', 'go', 'c#'];
            $languages2 ="1|2|3|4|5|44|45|46|47|4478";
            $pieces = explode("|", $languages2);
            $arrasy= special_chunk($pieces,20) 	;
        */

        $id_roles = $request->id_roles;
        $id_menu = $request->id_menu;
        $id_menu_Arrays = explode("|", $id_menu);

        DB::beginTransaction();

        try {
            // $isValue = RolesMenu::where('id_roles', '=', $id_roles)
            // ->where('id_roles', '=',$id_menu)
            // ->get(); 
            RolesMenu::where('id_roles', $id_roles)->delete();

            foreach ($id_menu_Arrays as $row) {
                RolesMenu::insert(array(
                    'id_roles' => $id_roles,
                    'id_menu' => $row,
                ));
            }
            // all good

            DB::commit();
        } catch (\Exception $e) {
            Log::channel('stderr')->info($e);
            DB::rollback();
            // something went wrong
        }

        /*
        $result = DB::select('CALL USP_SEGURIDAD_ROLES_ASIGNAR_PERMISOS(?,?)', [$id_roles,$id_menu]);
        */
        return response()->json([
            'success' => true,
            'message' => 'Registros asignados!',
            'result' => 1
        ]);
    }


    //=========== treeview ===============
    public function listar_treeviewv2(Request $request)
    {
        $id_rol = $request->input('id_roles');

        $results = DB::select(
            'CALL USP_SEGURIDAD_PERFIL_MENU_LISTAR_TREEVIEW(?)',
            [$id_rol]
        );

        // Log::channel('stderr')->info("results: ".json_encode($results));

        return response()->json([
            'success' => true,
            'data' => $results
        ]);
    }



    public function listar_treeviewv2_guardar(Request $request)
    {
      

        /*
    $request->validate([
        'id_rol' => 'required|integer|exists:sistema_menu_objetos,id_roles',
        'permisos' => 'required|string'
    ]);
    */
        $request->validate([
            'permisos' => 'required|array' // Ahora esperamos un array directamente
        ]);
        Log::channel('stderr')->info("Aqui" . json_encode( $request->input('id_rol')));

        $idRol = $request->input('id_rol');
        $permisos = $request->input('permisos');
        // Procesar la cadena de permisos
        // $permisosProcesados = $this->procesarPermisos($permisosString);

        DB::beginTransaction();
        try {
            // 1. Primero eliminamos todos los permisos anteriores del rol
            // DB::table('sistema_menu_objetos')->where('id_roles', $idRol)->delete();

            // 2. Opcional: Desactivar permisos no incluidos
            // DB::table('sistema_menu_objetos_roles')
            //     ->where('id_roles', $idRol)
            //     ->whereNotIn('id_menu_objetos', $permisos)
            //     ->update(['id_roles' => null]); // o puedes eliminarlos

            // 1. Actualizar los registros existentes
            // foreach ($permisos as $idMenuObjeto) {
            //     DB::table('sistema_menu_objetos_roles')
            //         ->where('id_menu_objetos', $idMenuObjeto)
            //         ->update([
            //             'id_roles' => $idRol,
            //             'updated_at' => now()
            //         ]);
            // }

            // 1. Eliminar permisos anteriores del rol
            DB::table('sistema_menu_objetos_roles')
                ->where('id_roles', $idRol)
                ->delete();

            // 2. Preparar datos para inserción masiva
            $insertData = array_map(function ($idMenuObjeto) use ($idRol) {
                return [
                    'id_menu_objetos' => $idMenuObjeto,
                    'id_roles' => $idRol,
                ];
            }, $permisos);

            // 3. Insertar nuevos permisos (en lotes de 100 para mejor performance)
            $chunks = array_chunk($insertData, 100);
            foreach ($chunks as $chunk) {
                DB::table('sistema_menu_objetos_roles')->insert($chunk);
            }


            /*
 // 1. Eliminar permisos anteriores del rol
        DB::table('sistema_menu_objetos_roles')
            ->where('id_roles', $idRol)
            ->delete();

        // 2. Preparar datos para inserción masiva
        $now = now();
        $insertData = array_map(function($idMenuObjeto) use ($idRol, $now) {
            return [
                'id_menu_objetos' => $idMenuObjeto,
                'id_roles' => $idRol,
                'created_at' => $now,
                'updated_at' => $now
            ];
        }, $permisos);

        // 3. Insertar nuevos permisos (en lotes de 100 para mejor performance)
        $chunks = array_chunk($insertData, 100);
        foreach ($chunks as $chunk) {
            DB::table('sistema_menu_objetos_roles')->insert($chunk);
        }
*/

            /*
        // 2. Guardar nuevos permisos
        foreach ($permisosProcesados as $menuId => $objetos) {
            foreach ($objetos as $objetoId) {
                DB::table('sistema_menu_objetos')->insert([
                    'id_menu' => $menuId,
                    'id_objetos' => $objetoId,
                    'id_roles' => $idRol,
                    'created_at' => now(),
                    'updated_at' => now()
                ]);
            }
        }
*/
            DB::commit();

            return response()->json([
                'success' => true,
                'message' => 'Permisos guardados correctamente'
            ]);
        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'message' => 'Error al guardar permisos: ' . $e->getMessage()
            ], 500);
        }
    }

    protected function procesarPermisos($permisosString)
    {
        // Eliminar los pipes iniciales y finales
        $permisosString = trim($permisosString, '|');

        // Separar los diferentes grupos de permisos
        $grupos = explode('|-|', $permisosString);

        $permisos = [];

        foreach ($grupos as $grupo) {
            // Separar el ID del menú de los objetos
            $partes = explode('_', $grupo);

            if (count($partes) === 2) {
                $menuId = $partes[0];
                $objetos = explode(',', $partes[1]);

                // Filtrar valores vacíos y asegurar que sean numéricos
                $objetos = array_filter($objetos, function ($objeto) {
                    return !empty($objeto) && is_numeric($objeto);
                });

                if (!empty($objetos)) {
                    $permisos[$menuId] = $objetos;
                }
            }
        }

        return $permisos;
    }
}
