<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Company;
use App\Rules\UniqueCompanyRule;
use Illuminate\Http\Request;

class CompanyController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function listar()
    {
        $companies = Company::where('user_id', 1)
            ->get();

        return response()->json($companies, 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function crear(Request $request)
    {
        $data = $request->validate([
            'razon_social' => 'required|string|max:255',
            'ruc' => [
                'required',
                'string',
                'regex:/^(10|20)\d{9}$/',
                new UniqueCompanyRule(),
            ],
            'direccion' => 'required|string|max:255',
            'logo' => 'nullable|file|image',
            'sol_user' => 'required|string|max:255',
            'sol_pass' => 'required|string|max:255',
            'cert' => 'required|file|mimes:pem,txt',
            'client_id' => 'nullable|string|max:255',
            'client_secret' => 'nullable|string|max:255',
            'production' => 'nullable|boolean',
        ]);

        if ($request->hasFile('logo')) {
            $data['logo_path'] = $request->file('logo')->store('logos');
        }

        $data['cert_path'] = $request->file('cert')->store('certs');
        $data['user_id'] = 1;

        $company = Company::create($data);

        return response()->json([
            'message' => 'Empresa creada correctamente',
            'company' => $company,
        ], 201);
    }

    /**
     * Display the specified resource.
     */
    public function obtener_ruc(Request $request)
    {
        $ruc_=$request->ruc;
        $company = Company::where('ruc', $ruc_)
            ->where('user_id', 1)
            ->firstOrFail();

        return response()->json($company, 200);
    }

    /**
     * Update the specified resource in storage.
     */
    public function actualizar(Request $request)
    {
        $ruc_ = $request->ruc;
        $company = Company::where('ruc', $ruc_)
            ->where('user_id', 1)
            ->firstOrFail();

        $data = $request->validate([
            'razon_social' => 'nullable|string|max:255',
            'ruc' => [
                'nullable',
                'string',
                'regex:/^(10|20)\d{9}$/',
                new UniqueCompanyRule($company->id_compania),
            ],
            'direccion' => 'nullable|string|max:255',
            'logo' => 'nullable|file|image',
            'sol_user' => 'nullable|string|max:255',
            'sol_pass' => 'nullable|string|max:255',
            'cert' => 'nullable|file|mimes:pem,txt',
            'client_id' => 'nullable|string|max:255',
            'client_secret' => 'nullable|string|max:255',
            'production' => 'nullable|boolean',
        ]);

        if ($request->hasFile('logo')) {
            $data['logo_path'] = $request->file('logo')->store('logos');
        }

        if ($request->hasFile('cert')) {
            $data['cert_path'] = $request->file('cert')->store('certs');
        }

        $company->update($data);

        return response()->json([
            'message' => 'Empresa actualizada correctamente',
            'company' => $company,
        ], 200);

    }

    /**
     * Remove the specified resource from storage.
     */
    public function eliminar(Request $request)
    {
        $ruc_=$request->ruc;
        $company = Company::where('ruc', $ruc_)
            ->where('user_id', 1)
            ->firstOrFail();

        $company->delete();

        return response()->json([
            'message' => 'Empresa eliminada correctamente',
        ], 200);
    }
}
