<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gracias por suscribirte</title>
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
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
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
        <h1>{{ config('app.name') }}</h1>
    </div>
    
    <div class="content">
        <h2>¡Gracias por suscribirte!</h2>
        <p>Hola,</p>
        <p>Te damos las gracias por suscribirte a nuestro boletín. Ahora recibirás nuestras últimas noticias, promociones y actualizaciones directamente en tu correo electrónico.</p>
        
        <p>Esto es lo que puedes esperar:</p>
        <ul>
            <li>Noticias y actualizaciones exclusivas</li>
            <li>Ofertas especiales para suscriptores</li>
            <li>Contenido relevante y de valor</li>
        </ul>
        
        <p>Si en algún momento deseas dejar de recibir nuestros correos, puedes darte de baja fácilmente haciendo clic en el enlace que aparece al pie de cada mensaje.</p>
        
        <p>¡Gracias nuevamente por unirte a nuestra comunidad!</p>
        
        <p>Saludos cordiales,<br>
        El equipo de {{ config('app.name') }}</p>
    </div>
    
    <div class="footer">
        <p>© {{ date('Y') }} {{ config('app.name') }}. Todos los derechos reservados.</p>
        <p>
            <a href="{{ url('/web_politica_privacidad') }}">Política de Privacidad</a> | 
            <a href="{{ url('/web_terminos_condiciones') }}">Términos de Servicio</a>
        </p>
        <p>
            <small>
                Recibes este correo porque te suscribiste en nuestro sitio web. 
                <a href="{{ $unsubscribeLink }}">Cancelar suscripción</a>
            </small>
        </p>
    </div>
</body>
</html>