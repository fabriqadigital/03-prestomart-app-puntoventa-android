<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cancelar suscripción</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; max-width: 600px; margin: 0 auto; padding: 20px; }
        .unsubscribe-form { background: #f9f9f9; padding: 30px; border-radius: 8px; }
        .btn { background: #dc3545; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
        .btn:hover { background: #c82333; }
    </style>
</head>
<body>
    <div class="unsubscribe-form">
        <h2>Cancelar suscripción</h2>
        
        @if(session('status'))
            <div style="color: green; margin-bottom: 20px;">
                {{ session('status') }}
            </div>
        @endif

        <p>¿Estás seguro que deseas cancelar tu suscripción con el correo <strong>{{ $email }}</strong>?</p>
        
        <form method="POST" action="{{ route('unsubscribe') }}">
            @csrf
                <input type="hidden" name="email" value="{{ $email }}">
                <!-- Cambia _token por token para evitar conflicto -->
                <input type="hidden" name="token" value="{{ $token }}">
            
            <button type="submit" class="btn">
                Confirmar cancelación
            </button>
            
            <a href="{{ url('/') }}" style="margin-left: 15px;">
                Volver al sitio
            </a>
        </form>
    </div>
</body>
</html>