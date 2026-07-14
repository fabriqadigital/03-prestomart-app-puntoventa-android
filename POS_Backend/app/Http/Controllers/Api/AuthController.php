<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;
class AuthController extends Controller
{
    /**    
     * Login de usuario
     * Retorna token, usuario y perfil con menu_objetos
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|string',
            'password' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'status' => 422,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        try {
            // Buscar usuario usando el modelo User (necesario para JWT)
            $user = User::where('email', $request->email)
                ->orWhere('name', $request->email)
                ->first();

            if (!$user) {
                return response()->json([
                    'status' => 422,
                    'message' => 'Credenciales incorrectas'
                ], 422);
            }

            // Verificar contraseña
            if (!Hash::check($request->password, $user->password)) {
                return response()->json([
                    'status' => 422,
                    'message' => 'Credenciales incorrectas'
                ], 422);
            }

            // Verificar que el usuario esté activo
            if (isset($user->Activo) && $user->Activo !== 'S') {
                return response()->json([
                    'status' => 422,
                    'message' => 'Usuario inactivo'
                ], 422);
            }

            // Llamar al procedimiento almacenado para obtener perfiles y permisos
            $perfilData = DB::select('CALL USP_ADMINISTRACION_PERFIL_LISTAR_LOGIN(?, ?, ?)', [
                $user->id,
                null, // p_id_perfil (null para obtener todos)
                null  // p_id_roles (null para obtener todos)
            ]);

            if (empty($perfilData)) {
                return response()->json([
                    'status' => 422,
                    'message' => 'El usuario no tiene un perfil o rol asignado'
                ], 422);
            }

            // Generar token JWT usando tymon/jwt-auth
            // El token JWT se genera con información mínima del usuario (solo el ID)
            // Esto evita tokens demasiado largos
            try {
                $token = JWTAuth::fromUser($user);
                
                // Verificar que el token se generó correctamente
                if (!$token) {
                    throw new \Exception('No se pudo generar el token JWT');
                }
                
                Log::info('Token JWT generado exitosamente para usuario: ' . $user->id);
            } catch (JWTException $e) {
                Log::error('Error generando token JWT: ' . $e->getMessage());
                return response()->json([
                    'status' => 500,
                    'message' => 'Error al generar el token de autenticación: ' . $e->getMessage()
                ], 500);
            } catch (\Exception $e) {
                Log::error('Error generando token: ' . $e->getMessage());
                return response()->json([
                    'status' => 500,
                    'message' => 'Error al generar el token de autenticación'
                ], 500);
            }

            // Formatear datos del usuario
            $userFormatted = $this->formatUser($user, $perfilData);

            // Formatear perfiles para el frontend
            $perfilesFormatted = $this->formatPerfiles($perfilData);

            return response()->json([
                'status' => 200,
                'token' => $token,
                'user' => $userFormatted,
                'perfil' => $perfilesFormatted,
            ], 200);

        } catch (\Exception $e) {
            Log::error('Error en login: ' . $e->getMessage());
            return response()->json([
                'status' => 500,
                'message' => 'Error en el servidor: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Validar perfil y rol seleccionado
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function validarPerfil(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'id_perfil' => 'required|integer',
            'id_roles' => 'required|integer',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'status' => 422,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        try {
            // Obtener usuario autenticado desde JWT
            try {
                $user = JWTAuth::parseToken()->authenticate();
                $userId = $user ? $user->id : null;
            } catch (JWTException $e) {
                // Si falla el parse del token, intentar obtener desde request
                $userId = $request->user_id;
            }

            if (!$userId) {
                return response()->json([
                    'status' => 401,
                    'message' => 'Usuario no autenticado'
                ], 401);
            }

            // Llamar al SP con perfil y rol específicos
            $perfilData = DB::select('CALL USP_ADMINISTRACION_PERFIL_LISTAR_LOGIN(?, ?, ?)', [
                $userId,
                $request->id_perfil,
                $request->id_roles
            ]);

            if (empty($perfilData)) {
                return response()->json([
                    'status' => 422,
                    'message' => 'Perfil o rol no válido para este usuario'
                ], 422);
            }

            // Formatear perfil
            $perfilFormatted = $this->formatPerfiles($perfilData)[0] ?? null;

            if (!$perfilFormatted) {
                return response()->json([
                    'status' => 422,
                    'message' => 'No se pudo formatear el perfil'
                ], 422);
            }

            return response()->json([
                'status' => 200,
                'perfil' => $perfilFormatted,
            ], 200);

        } catch (\Exception $e) {
            Log::error('Error en validarPerfil: ' . $e->getMessage());
            return response()->json([
                'status' => 500,
                'message' => 'Error en el servidor: ' . $e->getMessage()
            ], 500);
        }
    }


    /**
     * Formatear usuario con cantidad de perfiles
     * 
     * @param object $user
     * @param array $perfilData
     * @return array
     */
    private function formatUser($user, $perfilData)
    {
        $cantidadPerfiles = count(array_unique(array_column($perfilData, 'id_perfil')));

        return [
            'id' => $user->id,
            'name' => $user->name,
            'email' => $user->email,
            'avatar' => $user->avatar ?? null,
            'count' => $cantidadPerfiles,
            'Activo' => $user->Activo ?? 'S',
            'created_at' => $user->created_at ?? null,
            'updated_at' => $user->updated_at ?? null,
        ];
    }

    /**
     * Formatear perfiles para el frontend
     * El SP retorna datos con menu_objetos que necesitamos preservar
     * 
     * @param array $perfilData
     * @return array
     */
    private function formatPerfiles($perfilData)
    {
        $perfiles = [];

        foreach ($perfilData as $row) {
            // Agrupar por id_perfil e id_roles
            $key = $row->id_perfil . '_' . $row->id_roles;
            
            if (!isset($perfiles[$key])) {
                $perfiles[$key] = [
                    'id_perfil' => (int)$row->id_perfil,
                    'nombre' => $row->nombre_perfil,
                    'descripcion' => $row->nombre_perfil, // Ajustar si tienes descripción
                    'id_usuario' => (int)$row->id_usuario,
                    'nombre_usuario' => $row->nombre_usuario,
                    'email' => $row->email,
                    'cantidad_perfiles' => (int)$row->cantidad_perfiles,
                    'menu_objetos' => $row->menu_objetos ?? '', // CRÍTICO: Preservar menu_objetos
                    'rol' => [
                        'id_roles' => (int)$row->id_roles,
                        'nombre' => $this->getNombreRol($row->id_roles), // Obtener nombre del rol
                        'id_perfil' => (int)$row->id_perfil,
                    ]
                ];
            }
        }

        return array_values($perfiles);
    }

    /**
     * Obtener nombre del rol desde la BD
     * 
     * @param int $idRoles
     * @return string
     */
    private function getNombreRol($idRoles)
    {
        $rol = DB::table('seguridad_roles_perfil')
            ->where('id_roles', $idRoles)
            ->first();

        // Si no existe en seguridad_roles_perfil, buscar en otra tabla
        if (!$rol) {
            $rol = DB::table('seguridad_roles')
                ->where('id_roles', $idRoles)
                ->first();
        }

        return $rol->nombre ?? 'ROL_' . $idRoles;
    }

    /**
     * Merge user with profile count (método auxiliar)
     * 
     * @param object $user
     * @param array $perfilData
     * @return array
     */
    private function mergeUserWithProfileCount($user, $perfilData)
    {
        return $this->formatUser($user, $perfilData);
    }
}
