<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo suscriptor</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background-color: #f8f9fa;
            padding: 20px;
            text-align: center;
            border-radius: 5px 5px 0 0;
        }
        .content {
            padding: 20px;
            background-color: #fff;
            border-left: 1px solid #ddd;
            border-right: 1px solid #ddd;
        }
        .footer {
            padding: 15px;
            text-align: center;
            background-color: #f8f9fa;
            border-radius: 0 0 5px 5px;
            font-size: 12px;
            color: #666;
        }
        .info-box {
            background-color: #f0f8ff;
            padding: 15px;
            border-radius: 5px;
            margin: 15px 0;
        }
        .logo {
            max-width: 150px;
            height: auto;
        }
    </style>
</head>
<body>
    <div class="header">
        @if(isset($logo))
            <img src="{{ $logo }}" alt="Logo" class="logo">
        @else
            <h1>{{ config('app.name') }}</h1>
        @endif
    </div>
    
    <div class="content">
        <h2>¡Nuevo suscriptor en el sitio web!</h2>
        
        <div class="info-box">
            <p><strong>Correo electrónico del suscriptor:</strong> {{ $txt_email }}</p>
            <p><strong>Fecha de suscripción:</strong> {{ now()->format('d/m/Y H:i') }}</p>
        </div>
        
        <p>Este es un correo automático para notificarte que un nuevo usuario se ha suscrito a través del formulario de tu sitio web.</p>
        
        <!-- <p>Puedes ver todos los suscriptores en el panel de administración:</p>
        
        <p><a href="{{ url('/admin/suscriptores') }}" style="color: #007bff;">Ver lista de suscriptores</a></p>
        
        <p>Total de suscriptores actuales: {{ \App\Models\WebSuscriptores::count() }}</p> -->
    </div>
    
    <div class="footer">
        <p>© {{ date('Y') }} {{ config('app.name') }}. Todos los derechos reservados.</p>
        <p>Este es un correo automático, por favor no respondas directamente a este mensaje.</p>
    </div>
</body>
</html>