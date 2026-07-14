<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Clientes extends Model
{
    use HasFactory;

    protected $table = 'administracion_cliente';
    protected $primaryKey = 'id_cliente';
    protected $guarded = [];
    
}