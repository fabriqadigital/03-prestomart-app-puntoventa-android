<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PaginaMenuObjetos extends Model
{
    use HasFactory;

    protected $table = 'sistema_objetos';
    protected $primaryKey = 'id_objetos';
    protected $guarded = [];
    
}