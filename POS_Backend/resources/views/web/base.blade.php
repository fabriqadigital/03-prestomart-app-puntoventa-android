<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Google Tag Manager -->
    <!-- End Google Tag Manager -->

    <!-- Mobile Metas -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Site Metas -->
   <title>404 - Page Not Found</title>
    <meta name="keywords" content="Template Capre">
    <meta name="description" content="">
    <meta name="author" content="p-themes">

    <!-- Site Icons -->
    <link rel="shortcut icon" href="assets/images/fav.png" type="image/x-icon" />
    <link rel="apple-touch-icon" href="#">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    
    <!-- Default CSS -->
    @section('head')
    @include('web.partials.head')
    @show

    <!-- CSS Page para las paginas a instanciar -->
    @yield('head_page')

</head>

<body class="clsFondoGris">
    <!-- Google Tag Manager (noscript) -->
    
    <!-- End Google Tag Manager (noscript) -->

    <!-- Header para todas las paginas -->
    @include('web.partials.header.menu')

    @yield('content')
    <!-- Footer para todas las paginas -->
    @include('web.partials.footer')

    <!-- Codigo JS para todas las paginas -->
    @include('web.partials.footer_js')
    @stack('scripts')
    @yield('footer_page')


</body>

</html>