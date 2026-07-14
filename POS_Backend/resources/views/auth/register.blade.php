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
                    <span>Empresa S.A.C.</span>
                </div>
            </div>

            <main>
                <div class="form-container">
                    <div id="loginForm" class="form active text-center">
                        <h1>Crear una cuenta</h1>
                        <p class="subtitle">Regístrate para comenzar</p>

                        <form id="registerFormElement" method="POST" action="{{ route('register.post') }}">
                            @csrf
                        <div class="input-group">
                                <img src="https://api.iconify.design/mdi:account-outline.svg" alt="Name" class="input-icon">

                                <input id="name" name="name" type="text" class="@error('name') is-invalid @enderror"
                                    value="{{ old('name') }}" placeholder="Usuario" autocomplete="name" autofocus>
                                @if ($errors->has('name'))
                                <span class="text-danger">{{ $errors->first('name') }}</span>
                                @endif

                            </div>

                            <div class="input-group">
                                <img src="https://api.iconify.design/mdi:email-outline.svg" alt="Email" class="input-icon">
                                <input id="email" type="email" class="@error('email') is-invalid @enderror"
                                    name="email" value="{{ old('email') }}"  placeholder="Correo Electronico" autocomplete="email">
                                @if ($errors->has('email'))
                                <span class="text-danger">{{ $errors->first('email') }}</span>
                                @endif
                            </div>

                            <div class="input-group">
                                <img src="https://api.iconify.design/mdi:lock-outline.svg" alt="Password" class="input-icon">
                                <input id="password" type="password"
                                    class="@error('password') is-invalid @enderror" placeholder="crear contraseña" name="password" required
                                    autocomplete="new-password">
                                @if ($errors->has('password'))
                                <span class="text-danger">{{ $errors->first('password') }}</span>
                                @endif
                            </div>

                            <div class="input-group">
                                <img src="https://api.iconify.design/mdi:lock-check-outline.svg" alt="Confirm Password" class="input-icon">
                                <input type="password" id="password-confirm" name="password_confirmation" placeholder="Confirme contraseña" required>
                            </div>

                            <button type="submit" class="signup-btn">Crear cuenta</button>

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
                                ¿Ya tienes una cuenta? <a href="/login" class="switch-form">Iniciar sesión</a>
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