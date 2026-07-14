<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

use App\Http\Controllers\Api\AuthController;

use App\Http\Controllers\Api\ClientesController;
use App\Http\Controllers\Api\EmpleadosController;
use App\Http\Controllers\Api\PerfilesController;
use App\Http\Controllers\Api\RolesController;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Api\VendedorController;
use App\Http\Controllers\Api\CompanyController;
use App\Http\Controllers\Api\UbigeoController;
use App\Http\Controllers\Api\CompaniaController;
use App\Http\Controllers\Api\ProveedorController;
use App\Http\Controllers\Api\AdminPaginaAccesoController;
use App\Http\Controllers\Api\AdminPaginaController;
use App\Http\Controllers\Api\AdminPaginaObjetosController;
use App\Http\Controllers\Api\CotizacionesController;
use App\Http\Controllers\Api\CotizacionesEvidenciasController;
use App\Http\Controllers\Api\FileController;

use App\Http\Controllers\Api\Ecommerce\Admin\DashboardAdminController;
use App\Http\Controllers\Api\Ecommerce\Admin\MarcaAdminController;
use App\Http\Controllers\Api\Ecommerce\Admin\PedidoAdminController;
use App\Http\Controllers\Api\Ecommerce\Admin\ProductoAdminController;
use App\Http\Controllers\Api\Ecommerce\Admin\TrackingAdminController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

        Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
            return $request->user();
        });

        Route::post('/register',                    [AuthController::class, 'register']);
        Route::post('/login',                       [AuthController::class, 'login']);

Route::group(['middleware' => ['auth:api']], function () {

    Route::post('/perfil/validar_conexion',       [AuthController::class, 'validar_conexion']);
    Route::post('/perfil/validar_perfil',         [AuthController::class, 'validar_perfil']);

    //Clientes
    Route::get('/clientes/obtener',               [ClientesController::class, 'obtener']);
    Route::post('/clientes/listar',                [ClientesController::class, 'listar']);
    Route::post('/clientes/crear',                [ClientesController::class, 'crear']);
    Route::put('/clientes/actualizar',            [ClientesController::class, 'actualizar']);
    Route::delete('/clientes/eliminar',           [ClientesController::class, 'eliminar']);

    //Empleados
    Route::get('/empleados/obtener',              [EmpleadosController::class, 'obtener']);
    Route::get('/empleados/listar',               [EmpleadosController::class, 'listar']);
    Route::post('/empleados/crear',               [EmpleadosController::class, 'crear']);
    Route::put('/empleados/actualizar',           [EmpleadosController::class, 'actualizar']);
    Route::delete('/empleados/eliminar',          [EmpleadosController::class, 'eliminar']);
    Route::put('/empleados/asignar_usuario',     [EmpleadosController::class, 'asignar_usuario']);

    //Perfiles
    Route::get('/perfiles/obtener',               [PerfilesController::class, 'obtener']);
    Route::get('/perfiles/listar',                [PerfilesController::class, 'listar']);
    Route::post('/perfiles/crear',                [PerfilesController::class, 'crear']);
    Route::put('/perfiles/actualizar',            [PerfilesController::class, 'actualizar']);
    Route::delete('/perfiles/eliminar',           [PerfilesController::class, 'eliminar']);

    Route::post('/perfiles/obtener_asignar',      [PerfilesController::class, 'obtener_asignar']);
    Route::get('/perfiles/obtener_lista',         [PerfilesController::class, 'obtener_lista']);
    Route::get('/perfiles/obtener_check',         [PerfilesController::class, 'obtener_check']);
    Route::delete('/perfiles/obtener_eliminar',   [PerfilesController::class, 'obtener_eliminar']);
    Route::delete('/perfiles/eliminar_multiple',  [PerfilesController::class, 'eliminar_multiple']);

    //Roles-1
    Route::get('/roles/obtener',                            [RolesController::class, 'obtener']);
    Route::get('/roles/listar',                             [RolesController::class, 'listar']);
    Route::post('/roles/crear',                             [RolesController::class, 'crear']);
    Route::put('/roles/actualizar',                         [RolesController::class, 'actualizar']);
    Route::delete('/roles/eliminar',                        [RolesController::class, 'eliminar']);
    Route::get('/roles/listar_treeview',                    [RolesController::class, 'listar_treeview']); //treview

    Route::get('/roles/listar_treeviewv2',                  [RolesController::class, 'listar_treeviewv2']); //treview
    Route::post('/roles/listar_treeviewv2_guardar',         [RolesController::class, 'listar_treeviewv2_guardar']);


    Route::post('/roles/obtener_asignar',         [RolesController::class, 'obtener_asignar']);
    Route::get('/roles/obtener_lista',            [RolesController::class, 'obtener_lista']);
    Route::get('/roles/obtener_check',            [RolesController::class, 'obtener_check']);
    Route::delete('/roles/obtener_eliminar',      [RolesController::class, 'obtener_eliminar']);
    Route::delete('/roles/eliminar_multiple',     [RolesController::class, 'eliminar_multiple']);
    Route::post('/roles/agregar_permisos',        [RolesController::class, 'agregar_permisos']);


    //Usuarios
    Route::post('/usuario/obtener',               [UserController::class, 'obtener']);
    Route::get('/usuario/listar',                 [UserController::class, 'listar']);
    Route::post('/usuario/crear',                 [UserController::class, 'crear']);
    Route::post('/usuario/actualizar',            [UserController::class, 'actualizar']);
    Route::delete('/usuario/eliminar',            [UserController::class, 'eliminar']);

    //Vendedor
    Route::get('/vendedor/obtener',               [VendedorController::class, 'obtener']);
    Route::get('/vendedor/listar',                [VendedorController::class, 'listar']);
    Route::post('/vendedor/crear',                [VendedorController::class, 'crear']);
    Route::put('/vendedor/actualizar',            [VendedorController::class, 'actualizar']);
    Route::delete('/vendedor/eliminar',           [VendedorController::class, 'eliminar']);

    //Compania
    Route::get('/compania/obtener',                                     [CompaniaController::class, 'obtener']);
    Route::get('/compania/listar',                                      [CompaniaController::class, 'listar']);
    Route::post('/compania/crear',                                      [CompaniaController::class, 'crear']);
    Route::put('/compania/actualizar',                                  [CompaniaController::class, 'actualizar']);
    Route::delete('/compania/eliminar',                                 [CompaniaController::class, 'eliminar']);


    //Proveedores
    Route::get('/proveedor/obtener',                                        [ProveedorController::class, 'obtener']);
    Route::get('/proveedor/listar',                                         [ProveedorController::class, 'listar']);
    Route::post('/proveedor/crear',                                         [ProveedorController::class, 'crear']);
    Route::put('/proveedor/actualizar',                                     [ProveedorController::class, 'actualizar']);
    Route::delete('/proveedor/eliminar',                                    [ProveedorController::class, 'eliminar']);

    //Administracion de Paginas de Acceso
    Route::get('/admin_pagina_acceso/listar_paginas_accesos',            [AdminPaginaAccesoController::class, 'listar_paginas_accesos']);
    Route::get('/admin_pagina_acceso/listar_menu_accesos',               [AdminPaginaAccesoController::class, 'listar_menu_accesos']);

    //Administracion de Paginas :AdminPaginaController
    Route::post('/admin_pagina/admin_pagina_crear',                       [AdminPaginaController::class, 'admin_pagina_crear']);
    Route::delete('/admin_pagina/admin_pagina_eliminar',                  [AdminPaginaController::class, 'admin_pagina_eliminar']);
    Route::put('/admin_pagina/admin_pagina_actualizar',                   [AdminPaginaController::class, 'admin_pagina_actualizar']);
    Route::post('admin_pagina/admin_pagina_actualizar_reordenar',         [AdminPaginaController::class, 'admin_pagina_actualizar_reordenar']);
    Route::get('/admin_pagina/admin_pagina_listar_path',                  [AdminPaginaController::class, 'admin_pagina_listar_path']);

    //Administracion de Objetos
    Route::get('/admin_pagina_objetos/obtener',                           [AdminPaginaObjetosController::class, 'obtener']);
    Route::get('/admin_pagina_objetos/listar',                            [AdminPaginaObjetosController::class, 'listar']);
    Route::post('/admin_pagina_objetos/crear',                            [AdminPaginaObjetosController::class, 'crear']);
    Route::put('/admin_pagina_objetos/actualizar',                        [AdminPaginaObjetosController::class, 'actualizar']);
    Route::delete('/admin_pagina_objetos/eliminar',                       [AdminPaginaObjetosController::class, 'eliminar']);

    //Administracion de archivos
    Route::get('/storage/{filename}',                                     [FileController::class, 'show']);
    Route::get('/storage_caja_chica/{filename}',                          [FileController::class, 'show_caja_chica']);



    // Route::get('/invoce',    [InvoiceController::class, 'send']);
    Route::post('companies/listar',                     [CompanyController::class, 'index']);
    Route::post('companies/crear',                      [CompanyController::class, 'crear']);
    Route::post('companies/actualizar',                 [CompanyController::class, 'actualizar']);
    Route::post('companies/obtener_ruc',                [CompanyController::class, 'obtener_ruc']);
    Route::post('companies/eliminar',                   [CompanyController::class, 'eliminar']);

    //Ubigue
    Route::get('/ubigeo/listar_paises',                    [UbigeoController::class, 'listar_paises']);
    Route::get('/ubigeo/obtener_departamentos',            [UbigeoController::class, 'obtener_departamentos']);
    Route::get('/ubigeo/obtener_provicias',                [UbigeoController::class, 'obtener_provicias']);
    Route::get('/ubigeo/obtener_distritos',                [UbigeoController::class, 'obtener_distritos']);

    // Cotizaciones
    Route::get('/cotizaciones/obtener',                              [CotizacionesController::class, 'obtener']);
    Route::post('/cotizaciones/listar',                              [CotizacionesController::class, 'listar']);
    Route::post('/cotizaciones/crear',                               [CotizacionesController::class, 'crear']);
    Route::put('/cotizaciones/actualizar',                           [CotizacionesController::class, 'actualizar']);
    Route::delete('/cotizaciones/eliminar',                          [CotizacionesController::class, 'eliminar']);
    Route::post('/cotizaciones/cambiar-estado',                      [CotizacionesController::class, 'cambiarEstado']);
    Route::post('/cotizaciones/generar-pdf',                         [CotizacionesController::class, 'generarPdf']);
        
    // Rutas para evidencias de cotizaciones
    Route::post('/cotizaciones/cambiar-estado-con-evidencia',       [CotizacionesEvidenciasController::class, 'cambiarEstadoConEvidencia']);
    Route::post('/cotizaciones/evidencias/listar',                  [CotizacionesEvidenciasController::class, 'listarEvidencias']);
    Route::get('/cotizaciones/evidencias/obtener',                  [CotizacionesEvidenciasController::class, 'obtenerEvidencia']);
    Route::get('/cotizaciones/evidencias/descargar',                [CotizacionesEvidenciasController::class, 'descargarEvidencia']);
    Route::post('/cotizaciones/evidencias/subir',                   [CotizacionesEvidenciasController::class, 'subirEvidencia']);
    Route::post('/cotizaciones/evidencias/por-estado',              [CotizacionesEvidenciasController::class, 'evidenciasPorEstado']);


});

/*
|--------------------------------------------------------------------------
| ECOMMERCE API Routes
|--------------------------------------------------------------------------
*/

use App\Http\Controllers\Api\Ecommerce\AuthController as EcommerceAuthController;
use App\Http\Controllers\Api\Ecommerce\ProductoController;
use App\Http\Controllers\Api\Ecommerce\CategoriaController;
use App\Http\Controllers\Api\Ecommerce\CarritoController;
use App\Http\Controllers\Api\Ecommerce\PedidoController;
use App\Http\Controllers\Api\Ecommerce\WishlistController;
use App\Http\Controllers\Api\Ecommerce\DireccionController;
use App\Http\Controllers\Api\Ecommerce\BannerController;
use App\Http\Controllers\Api\Ecommerce\TrackingController;

Route::prefix('ecommerce')->group(function () {

    // ==========================================
    // RUTAS PÚBLICAS (Sin autenticación)
    //http://localhost:8000/api/ecommerce/metodos_envio
    // ==========================================

    // Autenticación
    Route::post('/auth/register', [EcommerceAuthController::class, 'register']);
    Route::post('/auth/login', [EcommerceAuthController::class, 'login']);
    Route::post('/auth/refresh', [EcommerceAuthController::class, 'refreshToken']);

    // Productos
    Route::get('/productos', [ProductoController::class, 'index']);
    Route::get('/productos/destacados', [ProductoController::class, 'destacados']);
    Route::get('/productos/nuevos', [ProductoController::class, 'nuevos']);
    Route::get('/productos/ofertas', [ProductoController::class, 'ofertas']);
    Route::get('/productos/buscar', [ProductoController::class, 'buscar']);
    Route::get('/productos/{id}', [ProductoController::class, 'show']);
    Route::get('/productos/{id}/relacionados', [ProductoController::class, 'relacionados']);
    Route::get('/productos/{id}/reviews', [ProductoController::class, 'reviews']);

    // Categorías
    Route::get('/categorias', [CategoriaController::class, 'index']);
    Route::get('/categorias/arbol', [CategoriaController::class, 'arbol']);
    Route::get('/categorias/{id}', [CategoriaController::class, 'show']);
    Route::get('/categorias/{id}/productos', [CategoriaController::class, 'productos']);

    // Banners
    Route::get('/banners', [BannerController::class, 'index']);
    Route::get('/banners/home', [BannerController::class, 'home']);
    Route::get('/banners/posicion/{posicion}', [BannerController::class, 'porPosicion']);

    // Carrito (público con session_id)
    Route::get('/carrito',                      [CarritoController::class, 'index']);
    Route::post('/carrito/agregar',             [CarritoController::class, 'agregar']);
    Route::put('/carrito/item/{itemId}',        [CarritoController::class, 'actualizar']);
    Route::delete('/carrito/item/{itemId}',     [CarritoController::class, 'eliminar']);
    Route::delete('/carrito/vaciar',            [CarritoController::class, 'vaciar']);
    Route::post('/carrito/cupon',               [CarritoController::class, 'aplicarCupon']);
    Route::delete('/carrito/cupon',             [CarritoController::class, 'removerCupon']);

    // Métodos de envío y pago (público)
    Route::get('/metodos_envio',                [PedidoController::class, 'metodosEnvio']);
    Route::get('/metodos_pago',                 [PedidoController::class, 'metodosPago']);

    // Tracking público (consulta por código)
    Route::get('/tracking/buscar',              [TrackingController::class, 'buscar']);
    Route::get('/tracking/estados',             [TrackingController::class, 'estados']);
    Route::get('/tracking/etapas',              [TrackingController::class, 'etapas']);

    // ==========================================
    // RUTAS PROTEGIDAS (Requieren autenticación JWT)
    // ==========================================
    Route::middleware('auth:api')->group(function () {

        // Tracking autenticado
        Route::get('/tracking/mis-pedidos',             [TrackingController::class, 'misPedidos']);
        Route::get('/tracking/pedido/{pedidoId}',       [TrackingController::class, 'obtenerPorPedido']);

        // Autenticación    
        Route::post('/auth/logout',             [EcommerceAuthController::class, 'logout']);
        Route::get('/auth/me',                  [EcommerceAuthController::class, 'me']);
        Route::put('/auth/profile',             [EcommerceAuthController::class, 'updateProfile']);
        Route::put('/auth/password',            [EcommerceAuthController::class, 'changePassword']);

        // Pedidos
        Route::get('/pedidos',                  [PedidoController::class, 'index']);
        Route::get('/pedidos/{id}',             [PedidoController::class, 'show']);
        Route::post('/pedidos',                 [PedidoController::class, 'crear']);
        Route::post('/pedidos/{id}/cancelar',   [PedidoController::class, 'cancelar']);

        // Wishlist
        Route::get('/wishlist',                             [WishlistController::class, 'index']);
        Route::post('/wishlist',                            [WishlistController::class, 'agregar']);
        Route::post('/wishlist/toggle',                     [WishlistController::class, 'toggle']);
        Route::get('/wishlist/verificar/{productoId}',      [WishlistController::class, 'verificar']);
        Route::delete('/wishlist/{productoId}',             [WishlistController::class, 'eliminar']);
        Route::delete('/wishlist',                          [WishlistController::class, 'vaciar']);

        // Direcciones
        Route::get('/direcciones',                          [DireccionController::class, 'index']);
        Route::get('/direcciones/{id}',                     [DireccionController::class, 'show']);
        Route::post('/direcciones',                         [DireccionController::class, 'crear']);
        Route::put('/direcciones/{id}',                     [DireccionController::class, 'actualizar']);
        Route::delete('/direcciones/{id}',                  [DireccionController::class, 'eliminar']);
        Route::put('/direcciones/{id}/principal',           [DireccionController::class, 'establecerPrincipal']);

    });

    // ==========================================
    // RUTAS ADMIN (Panel de administración)
    // ==========================================
    Route::prefix('admin')->middleware('auth:api')->group(function () {

        // Dashboard
        Route::get('/dashboard/estadisticas',           [DashboardAdminController::class, 'estadisticas']);
        Route::get('/dashboard/ventas',                 [DashboardAdminController::class, 'ventas']);
        Route::get('/dashboard/productos-mas-vendidos', [DashboardAdminController::class, 'productosMasVendidos']);
        Route::get('/dashboard/pedidos-recientes',      [DashboardAdminController::class, 'pedidosRecientes']);
        Route::get('/dashboard/clientes-nuevos',        [DashboardAdminController::class, 'clientesNuevos']);
        Route::get('/dashboard/productos-bajo-stock',   [DashboardAdminController::class, 'productosBajoStock']);
        Route::get('/dashboard/resumen-pedidos',        [DashboardAdminController::class, 'resumenPedidos']);

        // Productos Admin
        Route::get('/productos',                       [ProductoAdminController::class, 'index']);
        Route::get('/productos/{id}',                  [ProductoAdminController::class, 'show']);
        Route::post('/productos',                      [ProductoAdminController::class, 'store']);
        Route::put('/productos/{id}',                  [ProductoAdminController::class, 'update']);
        Route::delete('/productos/{id}',               [ProductoAdminController::class, 'destroy']);
        Route::put('/productos/{id}/activo',           [ProductoAdminController::class, 'toggleActivo']);
        Route::put('/productos/{id}/destacado',        [ProductoAdminController::class, 'toggleDestacado']);
        Route::put('/productos/{id}/stock',            [ProductoAdminController::class, 'updateStock']);

        // Categorías Admin
        Route::get('/categorias',                     [CategoriaController::class, 'index']);
        Route::get('/categorias/{id}',                [CategoriaController::class, 'show']);
        Route::post('/categorias',                    [CategoriaController::class, 'store']);
        Route::put('/categorias/{id}',                [CategoriaController::class, 'update']);
        Route::delete('/categorias/{id}',             [CategoriaController::class, 'destroy']);

        // Marcas Admin
        Route::get('/marcas',                       [MarcaAdminController::class, 'index']);
        Route::get('/marcas/{id}',                  [MarcaAdminController::class, 'show']);
        Route::post('/marcas',                      [MarcaAdminController::class, 'store']);
        Route::put('/marcas/{id}',                  [MarcaAdminController::class, 'update']);
        Route::delete('/marcas/{id}',               [MarcaAdminController::class, 'destroy']);
        Route::put('/marcas/{id}/activo',           [MarcaAdminController::class, 'toggleActivo']);

        // Pedidos Admin
        Route::get('/pedidos',                        [PedidoController::class, 'index']);
        Route::get('/pedidos/{id}',                   [PedidoController::class, 'show']);
        Route::post('/pedidos',                       [PedidoController::class, 'crear']); // Crear pedido (soporta items desde admin)
        // Admin: actualizar pedido (editar campos principales)
        Route::put('/pedidos/{id}',                   [PedidoAdminController::class, 'actualizar']);
        Route::put('/pedidos/{id}/estado',            [PedidoAdminController::class, 'cambiarEstado']);
        Route::put('/pedidos/{id}/pagado',            [PedidoAdminController::class, 'marcarPagado']);

        // Tracking Admin
        Route::get('/tracking',                        [TrackingAdminController::class, 'index']);
        Route::get('/tracking/resumen',                [TrackingAdminController::class, 'resumen']);
        Route::get('/tracking/{id}',                   [TrackingAdminController::class, 'show']);
        Route::post('/tracking/inicializar',           [TrackingAdminController::class, 'inicializar']);
        Route::put('/tracking/{id}/estado',            [TrackingAdminController::class, 'actualizarEstado']);
        Route::put('/tracking/{id}/transportista',     [TrackingAdminController::class, 'actualizarTransportista']);
        Route::post('/tracking/{id}/evento',           [TrackingAdminController::class, 'agregarEvento']);

    });

    // ==========================================
    // RUTAS DE TRACKING (Públicas y autenticadas)
    // ==========================================



});




// Rutas de autenticación (públicas)
Route::prefix('api')->group(function () {
    
    // Login
    Route::post('/login', [AuthController::class, 'login']);
    
    // Validar perfil (requiere autenticación JWT)
    Route::middleware('auth:api')->post('/validar-perfil', [AuthController::class, 'validarPerfil']);
});