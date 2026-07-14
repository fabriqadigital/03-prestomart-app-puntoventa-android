<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Compania extends Model
{
    use HasFactory;

    protected $table = 'administracion_compania';
    protected $primaryKey = 'id_compania';
    protected $guarded = [];
    
}