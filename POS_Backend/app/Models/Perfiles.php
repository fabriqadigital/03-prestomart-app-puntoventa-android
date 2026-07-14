<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Perfiles extends Model
{
    use HasFactory;

    protected $table = 'seguridad_perfil';
    protected $primaryKey = 'id_perfil';
    protected $guarded = [];
    
}