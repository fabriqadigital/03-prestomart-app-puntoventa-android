<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Vendedor extends Model
{
    use HasFactory;

    protected $table = 'administracion_vendedor';
    protected $primaryKey = 'id_vendedor';
    protected $guarded = [];
    
}