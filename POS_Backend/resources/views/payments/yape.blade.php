<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Pagar con Izipay</title>
  <script 
    src="https://sandbox-checkout.izipay.pe/payments/v1/js/index.js"
    onload="console.log('Script Izipay cargado')"
    onerror="console.error('No se pudo cargar el script de Izipay')">
  </script>
  <style>
    body { font-family: Arial, sans-serif; padding: 2rem; }
    #start-checkout-btn {
      padding: 1rem 2rem;
      font-size: 16px;
      background-color: #0057b7;
      color: white;
      border: none;
      border-radius: 8px;
      cursor: pointer;
    }
    #start-checkout-btn:hover {
      background-color: #003d82;
    }
    #izipay-button {
      margin-top: 2rem;
    }
  </style>
</head>
<body>

  <h2>Pagar con Izipay</h2>

  <!-- Botón personalizado -->
  <button id="start-checkout-btn">Iniciar pago</button>

  <!-- Contenedor donde se mostrará el botón de Izipay -->
  <div id="izipay-button"></div>

  <script>
    const iziConfig = {
      config: {
        transactionId: 'TEST_TRANS_ID_123456',
        action: 'pay_register',
        render: {
      typeForm: 'pop-up'
    },
        merchantCode: '4007701', // <-- Reemplaza con tu merchantCode real
        order: {
          orderNumber: 'R202211101518',
          currency: 'PEN',
          amount: '15.00',
          processType: 'AT',
          merchantBuyerId: '123456789',
          dateTimeTransaction: Date.now().toString()
        },
        billing: {
          firstName: 'Juan',
          lastName: 'Wick Quispe',
          email: 'jwickq@izi.com',
          phoneNumber: '958745896',
          street: 'Av. Jorge Chávez 275',
          city: 'Lima',
          state: 'Lima',
          country: 'PE',
          postalCode: '15038',
          documentType: 'DNI',
          document: '21458796'
        }
      }
    };


    const callbackResponsePayment = (response) => console.log(response);




    document.getElementById('start-checkout-btn').addEventListener('click', () => {
      if (window.Izipay) {
        const checkout = new Izipay(iziConfig);
        // checkout.render('izipay-button');


        try {
  checkout &&
    checkout.LoadForm({
      authorization: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtZXJjaGFudENvZGUiOiI0MDAwOTAxIiwiZmFjaWxpdGF0b3JDb2RlIjoiMCIsInRyYW5zYWN0aW9uSWQiOiJSMjAyMjExMTAxNTE4IiwiT3JkZXJOdW1iZXIiOiJSMjAyMjExMTAxNTE4IiwiQW1vdW50IjoiMC4wMCIsIlRva2VuSWQiOiJkOGVkNmM2NS05OGQ4LTQ3OWEtOGQ1MC05NWM0MmI0ZjhmNjAiLCJuYmYiOjE2NjU2MTYzMzYsImV4cCI6MTY2NTcwMjczNiwiaWF0IjoxNjY1NjE2MzM2fQ.pPe3HGxZwmxn9M90jSX_PWb5QeUhwDj_DeOJX59Q2Pk',
      keyRSA: 'VErethUtraQuxas57wuMuquprADrAHAb',
      callbackResponse: callbackResponsePayment
    });
} catch (error) {
  console.log(error.message, error.Errors, error.date);
}

        // checkout.on('paymentSuccess', function(response) {
        //   console.log('✅ Pago exitoso:', response);
        //   alert('¡Pago realizado con éxito!');
        // });

        // checkout.on('paymentError', function(error) {
        //   console.error('❌ Error en el pago:', error);
        //   alert('Hubo un error al procesar el pago.');
        // });
      } else {
        console.error('El script de Izipay no ha cargado aún.');
        alert('El script de Izipay no se ha cargado correctamente.');
      }
    });
  </script>

</body>
</html>
