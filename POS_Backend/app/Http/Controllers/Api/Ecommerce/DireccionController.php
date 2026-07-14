<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Direccion;
use App\Models\Ecommerce\Cliente;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

class DireccionController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $user = auth()->user();
        $idClienteSolicitado = $request->id_cliente;

        // Determinar el id_cliente a consultar según el rol
        $idClienteConsulta = $this->resolverIdCliente($user, $idClienteSolicitado);

        if ($idClienteConsulta === null) {
            return response()->json([
                'success' => true,
                'data' => []
            ]);
        }

        $direcciones = Direccion::where('id_cliente', $idClienteConsulta)
            ->activas()
            ->orderByDesc('es_principal')
            ->orderByDesc('created_at')
            ->get();

        return response()->json([
            'success' => true,
            'data' => $direcciones->map(fn($direccion) => $this->formatDireccion($direccion))
        ]);
    }

    /**
     * Resuelve el id_cliente a consultar según el rol del usuario
     */
    private function resolverIdCliente($user, ?string $idClienteSolicitado): ?int
    {
        // Si es admin y solicita un cliente específico, usar ese
        if ($user->isAdmin() && $idClienteSolicitado) {
            return (int) $idClienteSolicitado;
        }

        // Para usuarios normales o admin sin id_cliente, buscar su propio cliente
        $cliente = Cliente::where('id_usuario', $user->id)->first();

        return $cliente?->id;
    }

    public function show(Request $request, $id): JsonResponse
    {
        $user = auth()->user();
        $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente);

        if ($idClienteConsulta === null) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $direccion = Direccion::where('id_cliente', $idClienteConsulta)->find($id);

        if (!$direccion) {
            return response()->json([
                'success' => false,
                'message' => 'Dirección no encontrada'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'data' => $this->formatDireccion($direccion)
        ]);
    }

    public function crear(Request $request): JsonResponse
    {

        $validator = Validator::make($request->all(), [
             'tipo' => 'sometimes|in:envio,facturacion',
            'nombre_completo' => 'required|string|max:150',
            'telefono' => 'nullable|string|max:20',
            'direccion' => 'required|string|max:255',
            'referencia' => 'nullable|string|max:255',
            
            'departamento' => 'required|string|max:100',
            'provincia' => 'nullable|string|max:100',
            'distrito' => 'nullable|string|max:100',
            
            'codigo_postal' => 'nullable|string|max:20',
            'latitud' => 'nullable|numeric',
            'longitud' => 'nullable|numeric',
            'es_principal' => 'sometimes|boolean',
            ]);
            
            Log::channel('stderr')->info("test" . json_encode($request->all()));
        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $user = auth()->user();
        $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente);

        if ($idClienteConsulta === null) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        // Si es principal, quitar principal de otras
        if ($request->boolean('es_principal', false)) {
            Direccion::where('id_cliente', $idClienteConsulta)
                ->update(['es_principal' => false]);
        }

        // Si es la primera dirección, hacerla principal
        $esPrimera = !Direccion::where('id_cliente', $idClienteConsulta)->exists();

        $direccion = Direccion::create([
            'id_cliente' => $idClienteConsulta,
            'tipo' => $request->input('tipo'),
            'nombre_completo' => $request->nombre_completo,
            'telefono' => $request->telefono,
            'direccion' => $request->direccion,
            'referencia' => $request->referencia,

            'departamento' => $request->departamento,
            'provincia' => $request->provincia,
            'distrito' => $request->distrito,

            'codigo_postal' => $request->codigo_postal,
            'latitud' => $request->latitud,
            'longitud' => $request->longitud,
            'es_principal' => $esPrimera || $request->boolean('es_principal', false),
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Dirección creada exitosamente',
            'data' => $this->formatDireccion($direccion)
        ], 201);
    }

    public function actualizar(Request $request, $id): JsonResponse
    {
        $validator = Validator::make($request->all(), [
            'tipo' => 'sometimes|in:envio,facturacion',
            'nombre_completo' => 'sometimes|string|max:150',
            'telefono' => 'nullable|string|max:20',
            'direccion' => 'sometimes|string|max:255',
            'referencia' => 'nullable|string|max:255',

            'departamento' => 'sometimes|string|max:100',
            'provincia' => 'nullable|string|max:100',
            'distrito' => 'nullable|string|max:100',

            'codigo_postal' => 'nullable|string|max:20',
            'latitud' => 'nullable|numeric',
            'longitud' => 'nullable|numeric',
            'es_principal' => 'sometimes|boolean',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Error de validación',
                'errors' => $validator->errors()
            ], 422);
        }

        $user = auth()->user();
        $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente);

        if ($idClienteConsulta === null) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $direccion = Direccion::where('id_cliente', $idClienteConsulta)->find($id);

        if (!$direccion) {
            return response()->json([
                'success' => false,
                'message' => 'Dirección no encontrada'
            ], 404);
        }

        // Si se establece como principal
        if ($request->boolean('es_principal', false)) {
            Direccion::where('id_cliente', $idClienteConsulta)
                ->where('id', '!=', $id)
                ->update(['es_principal' => false]);
        }

        $direccion->update($request->only([
            'tipo',
            'nombre_completo',
            'telefono',
            'direccion',
            'referencia',
            'ciudad',
            'estado',
            'codigo_postal',
            'latitud',
            'longitud',
            'es_principal'
        ]));

        return response()->json([
            'success' => true,
            'message' => 'Dirección actualizada',
            'data' => $this->formatDireccion($direccion->fresh())
        ]);
    }

    public function eliminar(Request $request, $id): JsonResponse
    {
        $user = auth()->user();
        $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente);

        if ($idClienteConsulta === null) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $direccion = Direccion::where('id_cliente', $idClienteConsulta)->find($id);

        if (!$direccion) {
            return response()->json([
                'success' => false,
                'message' => 'Dirección no encontrada'
            ], 404);
        }

        $eraPrincipal = $direccion->es_principal;
        $direccion->update(['activo' => false]);

        // Si era principal, asignar otra como principal
        if ($eraPrincipal) {
            $otraDireccion = Direccion::where('id_cliente', $idClienteConsulta)
                ->activas()
                ->first();
            if ($otraDireccion) {
                $otraDireccion->update(['es_principal' => true]);
            }
        }

        return response()->json([
            'success' => true,
            'message' => 'Dirección eliminada'
        ]);
    }

    public function establecerPrincipal(Request $request, $id): JsonResponse
    {
        $user = auth()->user();
        $idClienteConsulta = $this->resolverIdCliente($user, $request->id_cliente);

        if ($idClienteConsulta === null) {
            return response()->json([
                'success' => false,
                'message' => 'Cliente no encontrado'
            ], 404);
        }

        $direccion = Direccion::where('id_cliente', $idClienteConsulta)->find($id);

        if (!$direccion) {
            return response()->json([
                'success' => false,
                'message' => 'Dirección no encontrada'
            ], 404);
        }

        Direccion::where('id_cliente', $idClienteConsulta)
            ->update(['es_principal' => false]);

        $direccion->update(['es_principal' => true]);

        return response()->json([
            'success' => true,
            'message' => 'Dirección principal actualizada',
            'data' => $this->formatDireccion($direccion->fresh())
        ]);
    }

    private function formatDireccion($direccion): array
    {
        return [
            'id_direccion' => $direccion->id_direccion,
            'tipo' => $direccion->tipo,
            'nombre_completo' => $direccion->nombre_completo,
            'telefono' => $direccion->telefono,
            'direccion' => $direccion->direccion,
            'referencia' => $direccion->referencia,
            'ciudad' => $direccion->ciudad,
            'estado' => $direccion->estado,
            'codigo_postal' => $direccion->codigo_postal,
            'latitud' => $direccion->latitud,
            'longitud' => $direccion->longitud,
            'es_principal' => $direccion->es_principal,
            'direccion_completa' => $direccion->direccion_completa,
        ];
    }
}
