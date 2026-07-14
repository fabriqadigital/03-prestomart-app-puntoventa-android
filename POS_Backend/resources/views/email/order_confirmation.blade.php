<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmación de Pedido - {{ $pedido['codigo_pedido'] }}</title>
</head>
<body style="margin: 0; padding: 0; font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f7f9fc;">
    <table width="100%" cellpadding="0" cellspacing="0" style="background-color: #f7f9fc; padding: 20px;">
        <tr>
            <td align="center">
                <table width="650" cellpadding="0" cellspacing="0" style="max-width: 650px; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);">
                    
                    <!-- HEADER -->
                    <tr>
                        <td style="background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%); padding: 30px 20px; text-align: center;">
                            <h1 style="margin: 0 0 20px 0; color: white; font-size: 28px; font-weight: 600;">Capre Perú</h1>
                            <h2 style="margin: 0 0 10px 0; color: white; font-size: 24px; font-weight: 600;">¡Gracias por tu compra!</h2>
                            <p style="margin: 0; color: white; font-size: 16px; opacity: 0.9;">Tu pedido ha sido recibido y está siendo procesado.</p>
                            <div style="background-color: rgba(255, 255, 255, 0.15); display: inline-block; padding: 8px 16px; border-radius: 20px; margin-top: 15px; font-weight: 500; color: white;">
                                Pedido #{{ $pedido['codigo_pedido'] }}
                            </div>
                        </td>
                    </tr>

                    <!-- CONTENIDO -->
                    <tr>
                        <td style="padding: 30px;">
                            
                            <!-- DETALLES DEL PEDIDO -->
                            <table width="100%" cellpadding="25" cellspacing="0" style="margin-bottom: 20px; background: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.03); border: 1px solid #eaeef2;">
                                <tr>
                                    <td>
                                        <h2 style="margin: 0 0 20px 0; font-size: 20px; font-weight: 600; color: #2d3748; padding-bottom: 12px; border-bottom: 2px solid #4b6cb7;">
                                            📦 Detalles del Pedido
                                        </h2>
                                        
                                        <table width="100%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Número de Pedido</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">{{ $pedido['codigo_pedido'] }}</div>
                                                </td>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Número de Boleta</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">{{ $pedido['codigo_boleta_o_factura'] }}</div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Fecha</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">{{ date('d/m/Y H:i', strtotime($pedido['created_at'])) }}</div>
                                                </td>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Estado</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">
                                                        @if($pedido['estado'] == 2)
                                                            <span style="display: inline-block; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: 500; background-color: #fff4e6; color: #f39c12;">Pendiente de pago</span>
                                                        @elseif($pedido['estado'] == 1)
                                                            <span style="display: inline-block; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: 500; background-color: #e1f0ff; color: #3498db;">En proceso</span>
                                                        @else
                                                            <span style="display: inline-block; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: 500;">Estado desconocido</span>
                                                        @endif
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Método de Pago</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">
                                                        @if($pedido['radio_metodo_pago'] == 1)
                                                            Tarjeta de crédito / débito, Yape, Plin
                                                        @elseif($pedido['radio_metodo_pago'] == 2)
                                                            Pago contra entrega
                                                        @else
                                                            No especificado
                                                        @endif
                                                    </div>
                                                </td>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Costo de envio</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">
                                                         @if($pedido['total'] >= 199.00)
                                                            <span style="display: inline-block; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: 500; background-color: #fff4e6; color: #f39c12;">Gratis</span>
                                                        @else
                                                            <span style="display: inline-block; padding: 6px 12px; border-radius: 20px; font-size: 14px; font-weight: 500;">{{$pedido['costo_envio']}}</span>
                                                        @endif
                                                    </div>
                                                </td>

                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                            <!-- INFORMACIÓN DEL CLIENTE -->
                            <table width="100%" cellpadding="25" cellspacing="0" style="margin-bottom: 20px; background: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.03); border: 1px solid #eaeef2;">
                                <tr>
                                    <td>
                                        <h2 style="margin: 0 0 20px 0; font-size: 20px; font-weight: 600; color: #2d3748; padding-bottom: 12px; border-bottom: 2px solid #4b6cb7;">
                                            👤 Información del Cliente
                                        </h2>
                                        
                                        <table width="100%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Nombre</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">{{ $pedido['direccion_envio_nombre'] }} {{ $pedido['direccion_envio_apellido'] }}</div>
                                                </td>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Email</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">{{ $pedido['direccion_envio_correo'] }}</div>
                                                </td>
                                            </tr>
                                             <tr>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">DNI</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">{{ $pedido['direccion_envio_dni'] }} {{ $pedido['direccion_envio_dni'] }}</div>
                                                </td>
                                                <td width="50%" style="padding: 8px 0;">
                                                    <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Teléfono</div>
                                                    <div style="font-weight: 600; color: #2d3748; font-size: 16px;">{{ $pedido['direccion_envio_telefono'] }}</div>
                                                </td>
                                            </tr>

                                        </table>
                                        
                                        <div style="height: 1px; background: #eaeef2; margin: 25px 0;"></div>
                                        
                                        <h3 style="margin: 0 0 15px 0; color: #2d3748; font-size: 16px;">📍 Dirección de Envío</h3>
                                        <div style="font-weight: 600; color: #2d3748; font-size: 16px; line-height: 1.8;">
                                            {{ $pedido['direccion_envio_ubicacion'] }}<br>
                                            {{ $pedido['direccion_envio_provincia'] }} - {{ $pedido['direccion_envio_distrito'] }} - Perú
                                        </div>
                                    </td>
                                </tr>
                            </table>

                            <!-- PRODUCTOS -->
                            @if(isset($pedido['productos']) && count($pedido['productos']) > 0)
                            <table width="100%" cellpadding="25" cellspacing="0" style="margin-bottom: 20px; background: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.03); border: 1px solid #eaeef2;">
                                <tr>
                                    <td>
                                        <h2 style="margin: 0 0 20px 0; font-size: 20px; font-weight: 600; color: #2d3748; padding-bottom: 12px; border-bottom: 2px solid #4b6cb7;">
                                            🛒 Productos
                                        </h2>
                                        
                                        <table width="100%" cellpadding="15" cellspacing="0" style="border-collapse: collapse; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);">
                                            <thead>
                                                <tr>
                                                    <th style="background-color: #4b6cb7; color: white; padding: 15px; text-align: left; font-weight: 500;">Producto</th>
                                                    <th style="background-color: #4b6cb7; color: white; padding: 15px; text-align: left; font-weight: 500;">Cantidad</th>
                                                    <th style="background-color: #4b6cb7; color: white; padding: 15px; text-align: left; font-weight: 500;">Precio Unit.</th>
                                                    <th style="background-color: #4b6cb7; color: white; padding: 15px; text-align: right; font-weight: 500;">Subtotal</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                @php
                                                    $subtotal = 0;
                                                    $igvTotal = 0;
                                                @endphp
                                                
                                                @foreach($pedido['productos'] as $producto)
                                                @php
                                                    $itemTotal = $producto->price * $producto->qty;
                                                    $itemSubtotal = $itemTotal / 1.18;
                                                    $itemIgv = $itemTotal - $itemSubtotal;
                                                    
                                                    $subtotal += $itemSubtotal;
                                                    $igvTotal += $itemIgv;
                                                @endphp
                                                <tr>
                                                    <td style="padding: 15px; border-bottom: 1px solid #eaeef2;">{{ $producto->name }}</td>
                                                    <td style="padding: 15px; border-bottom: 1px solid #eaeef2;">{{ $producto->qty }}</td>
                                                    <td style="padding: 15px; border-bottom: 1px solid #eaeef2;">S/ {{ number_format($producto->price, 2) }}</td>
                                                    <td style="padding: 15px; border-bottom: 1px solid #eaeef2; text-align: right;">S/ {{ number_format($producto->price * $producto->qty, 2) }}</td>
                                                </tr>
                                                @endforeach
                                                
                                                <tr style="background-color: #f8f9fa;">
                                                    <td colspan="3" style="padding: 15px; text-align: right; font-weight: 600;">Subtotal:</td>
                                                    <td style="padding: 15px; text-align: right; font-weight: 600;">S/ {{ number_format($subtotal, 2) }}</td>
                                                </tr>
                                                
                                                <tr style="background-color: #f8f9fa;">
                                                    <td colspan="3" style="padding: 15px; text-align: right; font-weight: 600;">IGV (18%):</td>
                                                    <td style="padding: 15px; text-align: right; font-weight: 600;">S/ {{ number_format($igvTotal, 2) }}</td>
                                                </tr>
                                                
                                                <tr style="background-color: #f8f9fa;">
                                                    <td colspan="3" style="padding: 15px; text-align: right; font-weight: 600;">Costo de Envío:</td>
                                                    <td style="padding: 15px; text-align: right; font-weight: 600;"> {{ $pedido['costo_envio']=='gratis' ? 'Gratis' : 'S/ ' . number_format($pedido['costo_envio'], 2) }}</td>
                                                </tr>
                                                
                                                <tr style="background-color: #e1f0ff;">
                                                    <td colspan="3" style="padding: 15px; text-align: right; font-weight: 700; font-size: 18px; color: #2d3748;">Total:</td>
                                                    <td style="padding: 15px; text-align: right; font-weight: 700; font-size: 18px; color: #2d3748;">S/ {{ number_format($pedido['total'], 2) }}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            @endif

                            <!-- INSTRUCCIONES DE PAGO -->
                            @if($pedido['radio_metodo_pago'] == 2)
                            <table width="100%" cellpadding="25" cellspacing="0" style="margin-bottom: 20px; background: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.03); border: 1px solid #eaeef2;">
                                <tr>
                                    <td>
                                        <h2 style="margin: 0 0 20px 0; font-size: 20px; font-weight: 600; color: #2d3748; padding-bottom: 12px; border-bottom: 2px solid #4b6cb7;">
                                            💳 Instrucciones de Pago
                                        </h2>
                                        <p style="margin: 0 0 15px 0; color: #333;">Por favor realiza la transferencia a nuestra cuenta bancaria:</p>
                                        
                                        <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 4px solid #4b6cb7;">
                                            <table width="100%" cellpadding="8" cellspacing="0">
                                                <tr>
                                                    <td width="50%">
                                                        <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Banco</div>
                                                        <div style="font-weight: 600; color: #2d3748; font-size: 16px;">Banco BCP</div>
                                                    </td>
                                                    <td width="50%">
                                                        <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Cuenta Soles</div>
                                                        <div style="font-weight: 600; color: #2d3748; font-size: 16px;">193-2440178-0-61</div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">CCI</div>
                                                        <div style="font-weight: 600; color: #2d3748; font-size: 16px;">002-193-002440178-0-61-14</div>
                                                    </td>
                                                    <td>
                                                        <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">Titular</div>
                                                        <div style="font-weight: 600; color: #2d3748; font-size: 16px;">Capre Perú</div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <div style="font-weight: 500; color: #4a5568; margin-bottom: 5px; font-size: 14px;">RUC</div>
                                                        <div style="font-weight: 600; color: #2d3748; font-size: 16px;">20602470882</div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        
                                        <p style="margin: 20px 0 0 0; color: #333;">Una vez realizado el pago, puedes subir tu comprobante registrándote en la web de <a href="https://capre.pe" style="color: #4b6cb7; text-decoration: none;">capre.pe</a></p>
                                    </td>
                                </tr>
                            </table>
                            @endif

                        </td>
                    </tr>

                    <!-- FOOTER -->
                    <tr>
                        <td style="text-align: center; padding: 25px; background: #2d3748; color: #cbd5e0; font-size: 14px;">
                            <p style="margin: 0 0 15px 0;">Si tienes alguna pregunta sobre tu pedido, no dudes en contactarnos</p>
                            
                            <p style="margin: 0 0 15px 0;">
                                Email: <a href="mailto:ventas@capre.pe" style="color: #fff; text-decoration: none;">ventas@capre.pe</a> | 
                                Teléfono: <a href="tel:+51901649384" style="color: #fff; text-decoration: none;">+51 901 649 384</a>
                            </p>
                            
                            <p style="margin: 15px 0;">
                                <a href="#" style="color: #cbd5e0; text-decoration: none; margin: 0 10px;">Facebook</a> • 
                                <a href="#" style="color: #cbd5e0; text-decoration: none; margin: 0 10px;">Instagram</a> • 
                                <a href="#" style="color: #cbd5e0; text-decoration: none; margin: 0 10px;">Web</a>
                            </p>
                            
                            <p style="margin: 0;">&copy; {{ date('Y') }} Capre Perú. Todos los derechos reservados.</p>
                        </td>
                    </tr>

                </table>
            </td>
        </tr>
    </table>
</body>
</html>