<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\User;
use App\Models\Ecommerce\Cliente;
use App\Models\Ecommerce\RefreshToken;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;

class AuthController extends Controller
{
    public function register(Request $request): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'nombre' => 'required|string|max:100',
            'apellido' => 'required|string|max:100',
            'email' => 'required|email|unique:users,email',
            'password' => 'required|string|min:6|confirmed',
            'telefono' => 'nullable|string|max:20',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $user = User::create([
            'name' => $request->nombre . ' ' . $request->apellido,
            'email' => $request->email,
            'password' => Hash::make($request->password),
        ]);

        $cliente = Cliente::create([
            'usuario_id' => $user->id,
            'nombre' => $request->nombre,
            'apellido' => $request->apellido,
            'email' => $request->email,
            'telefono' => $request->telefono,
        ]);

        $token = JWTAuth::fromUser($user);
        $refreshToken = RefreshToken::generar(
            $user->id,
            $request->ip(),
            $request->userAgent()
        );

        return response()->json([
            'success' => true,
            'message' => 'Registro exitoso',
            'data' => [
                'user' => $this->formatUser($user, $cliente),
                'token' => $token,
                'refresh_token' => $refreshToken->token,
                'expires_in' => config('jwt.ttl') * 60,
            ]
        ], 201);
    }

    public function login(Request $request): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
            'password' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $credentials = $request->only('email', 'password');

        try {
            if (!$token = JWTAuth::attempt($credentials)) {
                return response()->json([
                    'success' => false,
                    'message' => 'Credenciales inválidas'
                ], 401);
            }
        } catch (JWTException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Error al crear el token'
            ], 500);
        }

        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        $refreshToken = RefreshToken::generar(
            $user->id,
            $request->ip(),
            $request->userAgent()
        );

        return response()->json([
            'success' => true,
            'message' => 'Login exitoso',
            'data' => [
                'user' => $this->formatUser($user, $cliente),
                'token' => $token,
                'refresh_token' => $refreshToken->token,
                'expires_in' => config('jwt.ttl') * 60,
            ]
        ]);
    }

    public function refreshToken(Request $request): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'refresh_token' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Refresh token requerido'
            ], 422);
        }

        $refreshToken = RefreshToken::where('token', $request->refresh_token)
                                    ->activos()
                                    ->first();

        if (!$refreshToken) {
            return response()->json([
                'success' => false,
                'message' => 'Refresh token inválido o expirado'
            ], 401);
        }

        $user = $refreshToken->user;

        // Revocar el token anterior
        $refreshToken->revocar();

        // Generar nuevos tokens
        $newToken = JWTAuth::fromUser($user);
        $newRefreshToken = RefreshToken::generar(
            $user->id,
            $request->ip(),
            $request->userAgent()
        );

        $cliente = Cliente::where('usuario_id', $user->id)->first();

        return response()->json([
            'success' => true,
            'message' => 'Token renovado',
            'data' => [
                'user' => $this->formatUser($user, $cliente),
                'token' => $newToken,
                'refresh_token' => $newRefreshToken->token,
                'expires_in' => config('jwt.ttl') * 60,
            ]
        ]);
    }

    public function logout(Request $request): JsonResponse
    {
        try {
            $user = auth()->user();

            // Revocar todos los refresh tokens del usuario
            if ($user) {
                RefreshToken::revocarTodos($user->id);
            }

            JWTAuth::invalidate(JWTAuth::getToken());

            return response()->json([
                'success' => true,
                'message' => 'Sesión cerrada correctamente'
            ]);
        } catch (JWTException $e) {
            return response()->json([
                'success' => false,
                'message' => 'Error al cerrar sesión'
            ], 500);
        }
    }

    public function me(): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        return response()->json([
            'success' => true,
            'data' => $this->formatUser($user, $cliente)
        ]);
    }

    public function updateProfile(Request $request): JsonResponse
    {
        $user = auth()->user();
        $cliente = Cliente::where('usuario_id', $user->id)->first();

        $validator = Validator::make($request->all(), [
            'nombre' => 'sometimes|string|max:100',
            'apellido' => 'sometimes|string|max:100',
            'telefono' => 'nullable|string|max:20',
            'avatar' => 'nullable|string|max:500',
            'fecha_nacimiento' => 'nullable|date',
            'genero' => 'nullable|in:M,F,O',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        if ($cliente) {
            $cliente->update($request->only([
                'nombre', 'apellido', 'telefono', 'avatar', 'fecha_nacimiento', 'genero'
            ]));

            if ($request->has('nombre') || $request->has('apellido')) {
                $user->name = ($request->nombre ?? $cliente->nombre) . ' ' . ($request->apellido ?? $cliente->apellido);
                $user->save();
            }
        }

        return response()->json([
            'success' => true,
            'message' => 'Perfil actualizado',
            'data' => $this->formatUser($user->fresh(), $cliente->fresh())
        ]);
    }

    public function changePassword(Request $request): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'current_password' => 'required|string',
            'password' => 'required|string|min:6|confirmed',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $user = auth()->user();

        if (!Hash::check($request->current_password, $user->password)) {
            return response()->json([
                'success' => false,
                'message' => 'La contraseña actual es incorrecta'
            ], 400);
        }

        $user->password = Hash::make($request->password);
        $user->save();

        // Revocar todos los refresh tokens
        RefreshToken::revocarTodos($user->id);

        return response()->json([
            'success' => true,
            'message' => 'Contraseña actualizada correctamente'
        ]);
    }

    private function formatUser($user, $cliente): array
    {
        return [
            'id' => $user->id,
            'id_cliente' => $cliente?->id,
            'nombre' => $cliente?->nombre ?? $user->name,
            'apellido' => $cliente?->apellido ?? '',
            'nombre_completo' => $cliente?->nombre_completo ?? $user->name,
            'email' => $user->email,
            'telefono' => $cliente?->telefono,
            'avatar' => $cliente?->avatar,
            'fecha_nacimiento' => $cliente?->fecha_nacimiento?->format('Y-m-d'),
            'genero' => $cliente?->genero,
        ];
    }
}
