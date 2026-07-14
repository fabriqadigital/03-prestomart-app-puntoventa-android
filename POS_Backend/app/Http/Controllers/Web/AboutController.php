<?php

namespace App\Http\Controllers\Web;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\View;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;
use Session;

class AboutController  extends Controller {

    public function web_about() {
        $AboutData = DB::table('web_about')->get();
        return view('web.pages.web_about')
        ->with(compact('AboutData'));
    }

}