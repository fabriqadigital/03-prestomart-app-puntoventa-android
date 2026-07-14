composer require tymon/jwt-auth
Tymon\JWTAuth\Providers\LaravelServiceProvider::class,

php artisan vendor:publish --provider="Tymon\JWTAuth\Providers\LaravelServiceProvider"
php artisan jwt:secret

*************** OTRO *********************
https://jwt-auth.readthedocs.io/en/develop/laravel-installation/
https://medium.com/@a3rxander/how-to-implement-jwt-authentication-in-laravel-11-26e6d7be5a41
https://jurin.medium.com/securing-laravel-10-api-using-jwt-a5b6dca58fd7

INSTALANDO SWAGGER
====================
No se pudo
https://medium.com/@mark.tabletpc/set-up-laravel-with-swagger-for-comprehensive-api-documentation-step-by-step-instructions-d30552ca8051
composer require zircote/swagger-php
composer require darkaonline/l5-swagger
php artisan vendor:publish --provider "L5Swagger\L5SwaggerServiceProvider"
php artisan l5-swagger:generate
http://localhost:8000/api/documentation
 
 Template web
 =============
https://github.com/uilibrary/matx-react

LIMPIAR PORQUE SE PEGA LA BD
==============================
php artisan cache:clear
php artisan route:clear
php artisan config:clear
php artisan view:clear

Instalar reporte excel
==========================
https://docs.laravel-excel.com/3.1/getting-started/installation.html
composer require maatwebsite/excel

retornar la version anteior de composer
===================
composer self-update


composer install
php artisan key:generate
php artisan migrate --seed (it has some seeded data for your testing)

Comando para crear reporte
========================
https://docs.laravel-excel.com/3.1/exports/
php artisan make:export UsersExport --model=User

Para que arranque laravel
========================
En la version nueva
Habilitar  en php.ini
extension=gd
Instalar composer y darle comoposer install
Instalar en la documentacion el laravel

instalando greeter 
======================================
https://greenter.dev/starter/
<Repositorio>
https://github.com/codersfree/curso-greenter

INSTALANDO GREETER REPORT
=============================
https://greenter.dev/packages/report/
https://github.com/lecano/php-numero-a-letras

composer require greenter/lite
composer require greenter/report
composer require luecano/numero-a-letras "^3.0"
composer require greenter/htmltopdf
https://github.com/thegreenter/consulta-cpe
composer require greenter/ws
composer require greenter/greenter

INSTALANDO
================
composer require barryvdh/laravel-dompdf

IMPLEMENTANDO BRODCAST
==========================
php artisan make:event VehiculoCreado 


Seria landing para la parte de captar reclutamiento area de belleza