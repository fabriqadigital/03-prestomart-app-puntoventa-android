@extends('web.base')

<!-- Contenido en el Head de la pagina -->
@section('head_page')
<!-- extras -->

@endsection

<!-- Contenido en el Body -->
@section('content')
<style>

</style>
<div class="">

    <div class="overlay ">
        <div class="popup-container mt-5 mb-5">

            <div class="popup-header">
                <div class="logo">
                    <img src="https://api.iconify.design/mdi:shaker-outline.svg" alt="CaIN Logo" class="logo-icon">
                   <span>{{ config('app.name') }} {{ config('app.version') }}</span>
                </div>
            </div>

            <main>
                <div class="form-container">
                    <div id="loginForm" class="form active text-center" >
                        <h3>Bievenido a Capre</h3>
                        <p class="subtitle">Sign in to continue to your account</p>

                        @if(session('success'))
                        <div class="alert alert-success">
                            {{ session('success') }}
                        </div>
                        @endif
                        @if ($errors->any())
                        <div class="alert alert-danger">
                            @foreach ($errors->all() as $error)
                            <div>{{ $error }}</div>
                            @endforeach
                        </div>
                        @endif

                        <form id="signupForm" method="POST" action="{{ route('login') }}">
                            @csrf
                            <div class="input-group">
                                <img src="https://api.iconify.design/mdi:email-outline.svg" alt="Email" class="input-icon">
                                <input id="email" type="email" class="@error('email') is-invalid @enderror" name="email" value="{{ old('email') }}" placeholder="Correo Electrónico" required autocomplete="email" autofocus>
                                @if ($errors->has('email'))
                                <span class="text-danger">{{ $errors->first('email') }}</span>
                                @endif
                            </div>

                            <div class="input-group">
                                <img src="https://api.iconify.design/mdi:lock-outline.svg" alt="Password" class="input-icon">
                                <input id="password" type="password" class="@error('password') is-invalid @enderror" name="password" placeholder="Contraseña" required autocomplete="current-password">
                                @if ($errors->has('password'))
                                <span class="text-danger">{{ $errors->first('password') }}</span>
                                @endif

                            </div>

                            <div class="remember-forgot">
                                <label class="remember">
                                    @if (Route::has('forget.password.get'))
                                    <a href="{{ route('forget.password.get') }}" class="forgot-link" data-form="forgotForm">Forgot Password?</a>
                                    @endif
                                </label>
                            </div>

                            <button type="submit" class="signup-btn">Sign In</button>

                            <div class="divider">
                                <span>or continue with</span>
                            </div>

                            <a href=" {{ route('auth.google') }}">
                                <button type="button" class="google-btn">
                                    <img src="https://api.iconify.design/flat-color-icons:google.svg" alt="Google">
                                    <span>Google </span>
                                </button>
                            </a>

                            <p class="login-link"> 
                               ¿No tienes una cuenta? <a href="/registration" class="switch-form">Regístrate</a>
                            </p>
                        </form>
                    </div>
                </div>
            </main>

        </div>
    </div>

</div>

@endsection

@section('footer_page')
@if(session('messageOlvidoContrasena'))
<script>
    toastr.success("{{ session('messageOlvidoContrasena') }}");
</script>
@endif

@endsection