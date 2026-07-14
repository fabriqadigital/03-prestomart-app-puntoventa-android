<!DOCTYPE html>
<html>

<head>
    <title>Pago con Yape</title>
</head>

<body>
    <h1>Escanea el QR con Yape</h1>

    <form action="https://payment.micuentaweb.pe" method="POST" id="payment-form">
        @csrf
        <input type="hidden" name="formToken" value="{{ $formToken }}">
        <button type="submit">Pagar con Izipay</button>
    </form>

    <script>
        document.getElementById('payment-form').submit();
    </script>
</body>

</html>