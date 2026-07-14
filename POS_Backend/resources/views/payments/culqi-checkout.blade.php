<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout Culqi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        #culqi-container {
            margin: 30px 0;
        }
        #pay-button {
            background: #00b0f0;
            color: white;
            border: none;
            padding: 15px 30px;
            font-size: 18px;
            border-radius: 5px;
            cursor: pointer;
            display: block;
            margin: 20px auto;
        }
        #pay-button:hover {
            background: #0088cc;
        }
        .payment-details {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .loader {
            border: 4px solid #f3f3f3;
            border-top: 4px solid #00b0f0;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            animation: spin 1s linear infinite;
            margin: 20px auto;
            display: none;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <h1>Checkout de Pago</h1>
    
    <div class="payment-details">
        <h3>Detalles del Pago</h3>
        <p><strong>Producto:</strong> Producto de Ejemplo</p>
        <p><strong>Monto:</strong> S/ 80.00</p>
    </div>

    <div id="culqi-container"></div>
    <button id="pay-button">Pagar ahora</button>
    <div id="loader" class="loader"></div>
    <script src="https://js.culqi.com/checkout-js"></script>
    <!-- Carga el script de Culqi al final del body -->
    <script src="https://checkout.culqi.com/js/v4"></script>
    
    <script>
        // Configuración desde Laravel
        const publicKey = "{{ $publicKey }}";
        const orderId = "{{ $orderId }}";
        const rsaPublicKey = "{{ $rsaPublicKey }}";
        let CulqiCheckoutInstance = null;

        // Configuración del Checkout
        const settings = {
            title: 'Mi Tienda Laravel',
            currency: 'PEN',
            amount: 8000, // 80.00 PEN en centimos
            order: orderId,
            xculqirsaid: rsaPublicKey,
            rsapublickey: rsaPublicKey,
        };

        const paymentMethods = {
            tarjeta: true,
            yape: true,
            billetera: true,
            bancaMovil: true,
            agente: true,
            cuotealo: true,    
        };

        const options = {
            lang: 'auto',
            installments: true,
            modal: true,
            container: "#culqi-container",
            paymentMethods: paymentMethods,
            paymentMethodsSort: Object.keys(paymentMethods),
        };

        const client = {
            email: 'cliente@example.com', // Puedes pasarlo desde Laravel si el usuario está logueado
        };

        const appearance = {
            theme: "default",
            logo: 'https://tu-logo.com/logo.png',
            defaultStyle: {
                bannerColor: "#00b0f0",
                buttonBackground: "#00b0f0",
                menuColor: "#00b0f0",
                linksColor: "#00b0f0",
                buttonTextColor: "#ffffff",
                priceColor: "#333333",
            },
            variables: {
                fontFamily: "'Arial', sans-serif",
                borderRadius: "8px",
                colorBackground: "#ffffff",
                colorPrimary: "#00b0f0",
                colorPrimaryText: "#ffffff",
                colorText: "#333333",
            },
            rules: {
                ".Culqi-Button": {
                    "font-weight": "bold",
                    "border-radius": "8px"
                },
            }
        };

        // Manejar la respuesta de Culqi
        function handleCulqiAction() {
            if (Culqi.token) {
                // Mostrar loader
                document.getElementById('pay-button').disabled = true;
                document.getElementById('loader').style.display = 'block';
                
                // Enviar datos al servidor
                fetch('{{ route("culqi.process") }}', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'X-CSRF-TOKEN': '{{ csrf_token() }}'
                    },
                    body: JSON.stringify({
                        token: Culqi.token.id,
                        order_id: orderId,
                        amount: 80, // Monto en PEN
                        email: client.email,
                        payment_method: Culqi.token.object === 'token' ? 'card' : Culqi.token.object
                    })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Pago exitoso!');
                        console.log('Pago completado:', data.charge);
                        // Redirigir a página de éxito
                        // window.location.href = '/pago-exitoso';
                    } else {
                        alert('Error: ' + (data.error || 'Ocurrió un error en el pago'));
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error al procesar el pago');
                })
                .finally(() => {
                    document.getElementById('pay-button').disabled = false;
                    document.getElementById('loader').style.display = 'none';
                });
            } else if (Culqi.error) {
                console.error('Error en Culqi:', Culqi.error);
                alert(Culqi.error.user_message || 'Error al procesar el pago');
                document.getElementById('loader').style.display = 'none';
            }
        }

        // Inicializar Culqi cuando el script esté cargado
        function initializeCulqi() {
            if (typeof CulqiCheckout === 'undefined') {
                console.error('CulqiCheckout no está disponible. Recarga la página.');
                return;
            }
 
            try {
                CulqiCheckoutInstance = new CulqiCheckout(publicKey, {
                    settings,
                    client,
                    options,
                    appearance,
                });

                CulqiCheckoutInstance.culqi = handleCulqiAction;

                document.getElementById('pay-button').addEventListener('click', () => {
                    if (CulqiCheckoutInstance) {
                        CulqiCheckoutInstance.open();
                    } else {
                        alert('El sistema de pagos no está listo. Recarga la página.');
                    }
                });

            } catch (error) {
                console.error('Error al inicializar Culqi:', error);
                alert('Error al configurar el sistema de pagos');
            }
        }

        // Verificar si Culqi está cargado
        function checkCulqiLoaded() {
            if (typeof CulqiCheckout !== 'undefined') {
                initializeCulqi();
            } else {
                // Reintentar después de un breve retraso
                setTimeout(checkCulqiLoaded, 100);
            }
        }

        // Iniciar cuando el DOM esté listo
        document.addEventListener('DOMContentLoaded', checkCulqiLoaded);


        console.log('CulqiCheckout disponible?', typeof CulqiCheckout);
console.log('Culqi disponible?', typeof Culqi);

    </script>
</body>
</html>