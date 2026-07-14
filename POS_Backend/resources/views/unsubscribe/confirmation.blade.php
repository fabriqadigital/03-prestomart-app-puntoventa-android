<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Suscripción cancelada</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; max-width: 600px; margin: 0 auto; padding: 20px; }
        .success { background: #d4edda; color: #155724; padding: 20px; border-radius: 8px; }
        .error { background: #f8d7da; color: #721c24; padding: 20px; border-radius: 8px; }
    </style>
</head>
<body>
    @if($success)
        <div class="success">
            <h2>Suscripción cancelada</h2>
            <p>El correo <strong>{{ $email }}</strong> ha sido eliminado de nuestra lista de suscriptores.</p>
            <p>Lamentamos que te vayas. Si fue un error, puedes volver a suscribirte en cualquier momento.</p>
            <a href="{{ url('/') }}">Volver al sitio</a>
        </div>
    @else
        <div class="error">
            <h2>Error al cancelar</h2>
            <p>{{ $message ?? 'Ocurrió un error al intentar cancelar tu suscripción.' }}</p>
            <p>Por favor intenta nuevamente o contacta a soporte.</p>
            <a href="mailto:ventas@capre.pe">Contactar soporte</a>
        </div>
    @endif
</body>
</html>