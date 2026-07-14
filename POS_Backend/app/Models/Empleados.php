<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Empleados extends Model
{
    use HasFactory;

    protected $table = 'administracion_empleado';
    protected $primaryKey = 'id_empleado';
    protected $guarded = [];
    
}