<!DOCTYPE html>
<html>
<head>
    <title>Nuevo {{ $tipo_solicitud }} - {{ $numero_reclamo }}</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
        .header { background-color: #f8f9fa; padding: 15px; text-align: center; }
        .content { padding: 20px; background-color: #fff; }
        .section { margin-bottom: 20px; }
        .section-title { color: #2c3e50; border-bottom: 1px solid #eee; padding-bottom: 5px; }
        .footer { text-align: center; font-size: 12px; color: #777; margin-top: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Nuevo {{ $tipo_solicitud }} registrado</h2>
            <p>Número: <strong>{{ $numero_reclamo }}</strong></p>
        </div>
        
        <div class="content">
            <div class="section">
                <h3 class="section-title">Datos del Reclamante</h3>
                <p><strong>Nombres:</strong> {{ $nombres }} {{ $apellidos }}</p>
                <p><strong>Tipo Documento:</strong> {{ $tipo_documento }}</p>
                <p><strong>N° Documento:</strong> {{ $numero_documento }}</p>
                <p><strong>Razón Social:</strong> {{ $razon_social }}</p>
                <p><strong>Teléfono:</strong> {{ $telefono }}</p>
                <p><strong>Email:</strong> {{ $email }}</p>
            </div>
            
            <div class="section">
                <h3 class="section-title">Domicilio</h3>
                <p><strong>Departamento:</strong> {{ $departamento }}</p>
                <p><strong>Provincia:</strong> {{ $provincia }}</p>
                <p><strong>Distrito:</strong> {{ $distrito }}</p>
                <p><strong>Dirección:</strong> {{ $direccion }}</p>
            </div>
            
            <div class="section">
                <h3 class="section-title">Detalles de la {{ $tipo_solicitud }}</h3>
                <p>{{ $detalles_solicitud }}</p>
            </div>
            
            @if(!empty($archivos_adjuntos))
            <div class="section">
                <h3 class="section-title">Archivos Adjuntos</h3>
                <ul>
                    @foreach($archivos_adjuntos as $archivo)
                    <li>{{ $archivo['nombre_original'] }} ({{ round($archivo['tamanio'] / 1024, 2) }} KB)</li>
                    @endforeach
                </ul>
            </div>
            @endif
        </div>
        
        <div class="footer">
            <p>Fecha de registro: {{ $fecha_registro }}</p>
            <p>Este es un correo automático, por favor no responder.</p>
        </div>
    </div>
</body>
</html>