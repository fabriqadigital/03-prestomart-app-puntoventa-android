@extends('web.base')

<!-- Contenido en el Head de la pagina -->
@section('head_page')
<!-- extras here!!-->

@vite(['resources/sass/web_about.scss'])
@endsection
<!-- COOKIES AND POLICE : HEADER-->
@cookieconsentscripts
<!-- Contenido en el Body -->
@section('content')

<!-- Hero Section -->
@include('web.partials.breadcrumb')
<div class="page-not-found md:py-20 py-10 bg-linear md:mt-[74px] mt-14">
    <div class="container">
        <div class="flex items-center justify-between max-sm:flex-col gap-y-8">
            <img src="assets/images/other/404-img.png" alt="bg-img" class="sm:w-1/2 w-3/4" />
            <div class="text-content sm:w-1/2 w-full flex items-center justify-center sm:pl-10">
                <div class="">
                    <div class="lg:text-[140px] md:text-[80px] text-[42px] lg:leading-[152px] md:leading-[92px] leading-[52px] font-semibold">404</div>
                    <div class="heading2 mt-4">Something is Missing.</div>
                    <div class="body1 text-secondary mt-4 pb-4">The page you are looking for cannot be found. <br class="max-xl:hidden" />Take a break before trying again</div>
                    <a class="flex items-center gap-3" href="index.html">
                        <i class="ph ph-arrow-left"></i>
                        <div class="text-button">Back To Homepage</div>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection
<!-- COOKIES AND POLICE: FOOTER -->
@cookieconsentview
@section('footer_page')
<script>
</script>
@endsection