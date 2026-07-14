<?php

namespace App\Traits;

use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\Eloquent\ModelNotFoundException;

trait CrudOperationsTrait
{
    /**
     * Crea un nuevo registro
     *
     * @param Request $request
     * @param array $validationRules Reglas de validación
     * @param array $validationMessages Mensajes de validación personalizados
     * @param string $modelClass Clase del modelo Eloquent
     * @param array $defaultValues Valores por defecto para la creación
     * @param string $idFieldName Nombre del campo ID (opcional)
     * @return JsonResponse
     */
    public function crear(
        Request $request,
        array $validationRules,
        array $validationMessages,
        string $modelClass,
        array $defaultValues = [],
        string $idFieldName = 'id'
    ): JsonResponse {
        try {
            // Validar campos requeridos
            $validatedData = $request->validate($validationRules, $validationMessages);

            // Preparar datos para creación
            $datosCreacion = array_merge($validatedData, $defaultValues, [
                'created_at' => now(),
                'updated_at' => now(),
            ]);

            /** @var Model $model */
            $model = new $modelClass;
            $result = $model->create($datosCreacion);

            return response()->json([
                'success' => true,
                'message' => 'Registro creado exitosamente',
                'result' => $result,
                $idFieldName => $result->{$idFieldName}
            ], 201);

        } catch (ValidationException $e) {
            return $this->validationErrorResponse($e);
        } catch (\Illuminate\Database\QueryException $e) {
            return $this->databaseErrorResponse($e);
        } catch (\Exception $e) {
            return $this->generalErrorResponse($e, 'Error al crear el registro');
        }
    }

    /**
     * Actualiza un registro existente
     *
     * @param Request $request
     * @param string $modelClass Clase del modelo Eloquent
     * @param array $allowedFields Campos permitidos para actualización
     * @param string $idField Nombre del campo ID en la base de datos
     * @param string $idRequestField Nombre del campo ID en el request (opcional)
     * @return JsonResponse
     */
    public function actualizar(
        Request $request,
        string $modelClass,
        array $allowedFields,
        string $idField = 'id',
        string $idRequestField = 'id'
    ): JsonResponse {
        try {
            $id = $request->input($idRequestField);

            if (empty($id)) {
                throw new \Exception('ID del registro no proporcionado');
            }

            // Preparar los datos a actualizar
            $datosActualizar = ['updated_at' => now()];

            // Verificar y agregar cada campo permitido si existe en el request
            foreach ($allowedFields as $field) {
                if ($request->has($field)) {
                    $datosActualizar[$field] = $request->{$field};
                }
            }

            // Verificar que al menos hay un campo para actualizar (además de updated_at)
            if (count($datosActualizar) <= 1) {
                return response()->json([
                    'success' => false,
                    'message' => 'No se proporcionaron datos para actualizar',
                    'result' => null
                ], 400);
            }

            /** @var Model $model */
            $model = new $modelClass;
            $result = $model->where($idField, $id)->update($datosActualizar);

            if ($result === 0) {
                throw new ModelNotFoundException('No se encontró el registro o no hubo cambios');
            }

            return response()->json([
                'success' => true,
                'message' => 'Registro actualizado correctamente',
                'result' => $result
            ]);

        } catch (ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => $e->getMessage(),
                'result' => null
            ], 404);
        } catch (\Exception $e) {
            return $this->generalErrorResponse($e, 'Error al actualizar el registro');
        }
    }

    /**
     * Elimina un registro
     *
     * @param Request $request
     * @param string $modelClass Clase del modelo Eloquent
     * @param string $idField Nombre del campo ID en la base de datos
     * @param string $idRequestField Nombre del campo ID en el request (opcional)
     * @return JsonResponse
     */
    public function eliminar(
        Request $request,
        string $modelClass,
        string $idField = 'id',
        string $idRequestField = 'id'
    ): JsonResponse {
        try {
            $id = $request->input($idRequestField);

            if (empty($id)) {
                throw new \Exception('ID del registro no proporcionado');
            }

            /** @var Model $model */
            $model = new $modelClass;
            $registro = $model->where($idField, $id)->first();

            if (!$registro) {
                throw new ModelNotFoundException('El registro no existe o ya fue eliminado');
            }

            $result = $registro->delete();

            if (!$result) {
                throw new \Exception('No se pudo eliminar el registro');
            }

            return response()->json([
                'success' => true,
                'message' => 'Registro eliminado correctamente',
                'result' => $result
            ]);

        } catch (ModelNotFoundException $e) {
            return response()->json([
                'success' => false,
                'message' => $e->getMessage(),
                'result' => null
            ], 404);
        } catch (\Illuminate\Database\QueryException $e) {
            return $this->databaseErrorResponse($e);
        } catch (\Exception $e) {
            return $this->generalErrorResponse($e, 'Error al eliminar el registro');
        }
    }

    /**
     * Respuesta para errores de validación
     *
     * @param ValidationException $e
     * @return JsonResponse
     */
    protected function validationErrorResponse(ValidationException $e): JsonResponse
    {
        return response()->json([
            'success' => false,
            'message' => 'Error de validación',
            'errors' => $e->validator->errors(),
            'result' => null
        ], 422);
    }

    /**
     * Respuesta para errores de base de datos
     *
     * @param \Exception $e
     * @return JsonResponse
     */
    protected function databaseErrorResponse(\Exception $e): JsonResponse
    {
        return response()->json([
            'success' => false,
            'message' => 'Error en la base de datos: ' . (config('app.debug') ? $e->getMessage() : 'Contacte al administrador'),
            'result' => null
        ], 500);
    }

    /**
     * Respuesta para errores generales
     *
     * @param \Exception $e
     * @param string $defaultMessage
     * @return JsonResponse
     */
    protected function generalErrorResponse(\Exception $e, string $defaultMessage): JsonResponse
    {
        return response()->json([
            'success' => false,
            'message' => $defaultMessage . (config('app.debug') ? ': ' . $e->getMessage() : ''),
            'result' => null
        ], 400);
    }
}