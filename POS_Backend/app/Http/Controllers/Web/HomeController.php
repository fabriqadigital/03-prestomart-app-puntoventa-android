<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Api\BannerPopularController;
use App\Http\Controllers\Api\CategoriaPopularController;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\OfertaDelDia;
use Illuminate\Support\Facades\View;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Log;
use Session;
use App\Models\Producto;

class HomeController  extends Controller
{
    public function Home()
    {
        return view('web.pages.index');
    }
}
