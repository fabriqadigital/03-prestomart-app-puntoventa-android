<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\Auth;
use App\Http\Controllers\Web\HomeController;
/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/
/*
//::::::::::::::::::::::: WEB : HOME ::::::::::::::::::::::::::
Route::get('/',                                     [HomeController::class, 'Home'])->name('home');
//::::::::::::::::::::::: WEB: LOGEO ::::::::::::::::::::::::::
Auth::routes();
Route::get('/web_session_login',                    [HomeController::class, 'web_session_login']);

// ========= Es una ruta para el compilador de admin ==========================
Route::get('/admin/{any?}', function () {
  return file_get_contents(public_path('admin/index.html'));
})->where('any', '.*');

Route::get('/admin', function () {
  return file_get_contents(public_path('admin/index.html'));
});
*/

// ========= Es una ruta para el compilador de admin ==========================
// Exclude asset and API paths (like storage, api) so static files and API routes are served normally.
Route::get('/{any?}', function ($any = null) {
  // If the requested path corresponds to a real file under public, serve it directly.
  $requested = $any ? ltrim($any, '/') : '';
  $publicPath = public_path($requested);
  if ($requested && file_exists($publicPath) && is_file($publicPath)) {
    return response()->file($publicPath);
  }

  // If index.html exists (built admin), return it. Otherwise return 404.
  $indexFile = public_path('index.html');
  if (file_exists($indexFile)) {
    return file_get_contents($indexFile);
  }

  abort(404, 'Not Found');
})->where('any', '^(?!api|_debugbar|vendor).*$');

Route::get('/', function () {
  $indexFile = public_path('index.html');
  if (file_exists($indexFile)) {
    return file_get_contents($indexFile);
  }
  abort(404, 'Not Found');
});
