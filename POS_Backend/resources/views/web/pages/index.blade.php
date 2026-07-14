@extends('web.base')

<!-- Contenido en el Head de la pagina -->
@section('head_page')
<!-- extras here!!-->

@endsection
<!-- COOKIES AND POLICE : HEADER-->
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
                    <div class="lg:text-[140px] md:text-[80px] text-[42px] lg:leading-[152px] md:leading-[92px] leading-[52px] font-semibold">Stop</div>
                    <div class="heading2 mt-4">Página en mantemiento</div>
                    <div class="body1 text-secondary mt-4 pb-4">Estamos construyendo esta sección para una agradabele experiencia y contribucion al negocio empresarial en la belleza </div>
                    <a class="flex items-center gap-3" href="#">
                        <i class="ph ph-arrow-left"></i>
                        <div class="text-button">Volver</div>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection
<!-- COOKIES AND POLICE: FOOTER -->
@section('footer_page')
<script>
</script>
@endsection