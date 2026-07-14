<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;
use App\Http\Resources\UserResource;
use App\Models\User;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;

class UserController extends Controller
{

    public function obtener(Request $request) : JsonResponse
    {
        $id = $request->id;
        // $result = User::where('id',$id)->get();
        $result = DB::select('select id,name as username,email,created_at,Activo,avatar from users where id = ?', [$id]);
       if($result[0]->avatar){
           $file = Storage::get($result[0]->avatar);
           $fileBase64 = base64_encode($file);
       }else{
            $fileBase64=null;
       }
      
       // Log::channel('stderr')->info($contents);

        return response()->json([
            'success' => true,
            'message' => 'Obtener registros!',
            'result' => array_merge($result,['fileBase64' => $fileBase64]) 
        ]);
    }
    
    public function listar()
    {
        return response()->json([
            'success' => true,
            'message' => 'Listar registros',
            'result' => User::orderBy('id', 'DESC')->get()
        ]);
    }

    public function crear(Request $request) : JsonResponse
    {
        // Log::channel('stderr')->info($request->Activo);
		$result = User::create([
            'name' => $request->name,
            'email' => $request->email,

            'password' => Hash::make($request->password),
            'id_owner' => auth()->user()->id,
            'Activo' => $request->Activo,
            
        ]);
        return response()->json([
            'success' => true,
            'message' => 'Registro insertado',
            'result' =>  new UserResource($result)
        ]);
    }

    public function actualizar(Request $request) : JsonResponse
    {

        $filePath = "";
        //Validamos si existe
        if($request->hasFile('image')){
             // 1. possibility
            Storage::delete($request->avatar);

            $file = $request->file('image');
            //Para darle un nombre con su extension
            $ext = $file->getClientOriginalExtension();
            $filename = time().'.'.$ext; 
           // $file->move('avatar/', $filename);// Para almacenar en el public - accedercualquier
           // $filePath = $file->store('avatar');// Para almacenarlo en el storage - Con un numero random
            $filePath = $file->storeAs('avatar',$filename);// Para almacenarlo en el storage con nombre especifico
      
            // $file = Storage::get($filePath);
            // $fileBase64 = base64_encode($file);
        }

        $id = $request->input('id');

        if($request->password!="" && $request->password !="undefined"){
            if($filePath!=""){
                $result = User::where('id',$id)
                        ->update([
                                'name' => $request->name,
                                'email' => $request->email,
                                'password' => Hash::make($request->password),
                                'avatar' => $filePath,
                                'Activo' => $request->Activo,
                            ]);
            }else{
                $result = User::where('id',$id)
                ->update([
                        'name' => $request->name,
                        'email' => $request->email,
                        'password' => Hash::make($request->password),
                        'Activo' => $request->Activo,
                    ]);
            }
        }else{

            if($filePath!=""){
                $result = User::where('id',$id)
                ->update([
                        'name' => $request->name,
                        'email' => $request->email,
                        'avatar' => $filePath,
                        'Activo' => $request->Activo,
                    ]);
            }else{
                $result = User::where('id',$id)
                ->update([
                        'name' => $request->name,
                        'email' => $request->email,
                        'Activo' => $request->Activo,
                    ]);
            }
        }

        return response()->json([
            'success' => true,
            'message' => 'Registro actualizado',
            'result' => $result
        ]);
    }

    public function eliminar(Request $request) : JsonResponse
    {
        $id = $request->input('id');
        $result = User::where('id',$id)->delete();
 
        return response()->json([
            'success' => true,
            'message' => 'Registro eliminado!',
            'result'  => $result
        ]);
    }
}