<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class RolesMenu extends Model
{
    use HasFactory;

    protected $table = 'seguridad_roles_menu';
    protected $primaryKey = 'id_roles_modulo';
    protected $guarded = [];
    
}