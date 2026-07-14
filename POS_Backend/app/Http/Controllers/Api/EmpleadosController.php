<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\EmpleadoResource;
use App\Models\Empleados;
use Illuminate\Support\Facades\DB;

class EmpleadosController extends Controller
{

    public function obtener(Request $request) : JsonResponse
    {
        $id = $request->id;
        $result = DB::select('select * from administracion_empleado where id_empleado = ?', [$id]);
        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => $result
        ]);
    }
    
    public function listar()
    {
        $result = DB::select('CALL USP_ADMINISTRACION_EMPLEADO_LISTAR()');
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' => $result
        ]);
    }


    public function crearValidar($dni,$email_acceso,$email)
    {
        $result = DB::select('CALL USP_ADMINISTRACION_EMPLEADO_CREAR_VALIDAR(?,?,?)',[
            $dni,
            $email_acceso,
            $email,
        ]);
  
        return $result[0]->MensajeValidacion != "" ? $result[0]->MensajeValidacion : "";
    }

    public function actualizarValidar($dni,$email,$id_empleado)
    {
        $result = DB::select('CALL USP_ADMINISTRACION_EMPLEADO_ACTUALIZAR_VALIDAR(?,?,?)',[
            $dni,
            $email,
            $id_empleado
        ]);
  
        return $result[0]->MensajeValidacion != "" ? $result[0]->MensajeValidacion : "";
    }

    public function crear(Request $request) : JsonResponse
    {
        if($request->email_acceso =="EXAMPLE@GMAL.COM"){
            $email_acceso = "";
        }else{
            $email_acceso = $request->email_acceso;
        }

        $constValidarMensaje = $this->crearValidar($request->dni,$email_acceso,$request->email);

        if($constValidarMensaje == "")
        {

            DB::beginTransaction();
            try {
                // Log::channel('stderr')->info($request->Activo);
                $result = DB::select('CALL USP_ADMINISTRACION_EMPLEADO_CREAR(?,?,?,?,?,?,?,?,?,?,?)',[
                    $request->nombre,
                    $request->apellido,
                    $request->telefono,
                    $request->email,
                    $request->direccion,
        
                    $request->dni,
                    $request->licencia,
                    $request->Activo,

                    $email_acceso,
                    Hash::make($request->password),
                    auth()->user()->id,
                ]);

                DB::commit();
            } catch (\Exception $e) {
                Log::channel('stderr')->info($e);
                DB::rollback();
                // something went wrong
            }

            $constArrays = [0 => true, 1 => $constValidarMensaje];
         }else{
            $constArrays = [0 => false, 1 => $constValidarMensaje];
         }
        
         return response()->json([
            'result' => [
                'success' => $constArrays[0],
                'message' => $constArrays[1],
            ]
        ]);

    }

    public function actualizar(Request $request) : JsonResponse
    {
        $id = $request->input('id');//id_empleado
        $constValidarMensaje = $this->actualizarValidar($request->dni,$request->email,$id);

        if($constValidarMensaje == "")
        {

            DB::beginTransaction();
            try {
                
                Empleados::where('id_empleado',$id)
                ->update([
                        'nombre'          => $request->nombre,
                        'apellido'        => $request->apellido,
                        'telefono'        => $request->telefono,
                        'email'           => $request->email,
                        'direccion'       => $request->direccion,
                        'dni'             => $request->dni,
                        'licencia'        => $request->licencia,

                        'updated_at'      => date("Y-m-d H:i:s"),
                        'Activo'          => $request->Activo,
                    ]);

                DB::commit();
            } catch (\Exception $e) {
                Log::channel('stderr')->info($e);
                DB::rollback();
                // something went wrong
            }

            $constArrays = [0 => true, 1 => $constValidarMensaje];
         }else{
            $constArrays = [0 => false, 1 => $constValidarMensaje];
         }
        
         return response()->json([
            'result' => [
                'success' => $constArrays[0],
                'message' => $constArrays[1],
            ]
        ]);
    }

    public function eliminar(Request $request) : JsonResponse
    {
        $id = $request->input('id');
        $result = Empleados::where('id_empleado',$id)->delete();
 
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }

    public function asignar_usuario(Request $request) : JsonResponse
    {
        $id_usuario = $request->input('id_usuario');
        $id_empleado = $request->input('id_empleado');

        DB::beginTransaction();
        try {
            
            $result = Empleados::where('id_empleado',$id_empleado)
            ->update([
                    'id_usuario'      => $request->id_usuario,
                    'updated_at'      => date("Y-m-d H:i:s"),
                ]);

            DB::commit();
        } catch (\Exception $e) {
            Log::channel('stderr')->info($e);
            DB::rollback();
            // something went wrong
        }
 
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }
}