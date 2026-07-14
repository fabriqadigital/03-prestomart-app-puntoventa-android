<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Presupuesto - {{ $cotizacion->codigo }}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: Arial, sans-serif;
            font-size: 9pt;
            line-height: 1.3;
            color: #000;
        }
        
        .container {
            padding: 15px;
        }
        
        /* Header */
        .header {
            display: table;
            width: 100%;
            margin-bottom: 10px;
        }
        
        .header-left {
            display: table-cell;
            width: 40%;
            vertical-align: top;
        }
        
        .logo {
            max-width: 180px;
            height: auto;
        }
        
        .header-right {
            display: table-cell;
            width: 60%;
            vertical-align: top;
            text-align: right;
            font-size: 8pt;
        }
        
        .company-info {
            line-height: 1.4;
        }
        
        .codigo-box {
            border: 2px solid #cc0000;
            padding: 5px 10px;
            display: inline-block;
            margin-top: 5px;
        }
        
        .codigo-box .numero {
            font-size: 11pt;
            font-weight: bold;
        }
        
        .codigo-box .version {
            font-size: 9pt;
        }
        
        /* Título */
        .titulo {
            text-align: center;
            font-size: 16pt;
            font-weight: bold;
            margin: 15px 0;
            text-decoration: underline;
        }
        
        /* Info boxes */
        .info-section {
            margin: 10px 0;
        }
        
        .info-row {
            display: table;
            width: 100%;
            margin-bottom: 3px;
        }
        
        .info-label {
            display: table-cell;
            width: 120px;
            font-weight: bold;
            padding-right: 10px;
        }
        
        .info-value {
            display: table-cell;
        }
        
        .bordered-value {
            border: 1px solid #cc0000;
            padding: 3px 8px;
            display: inline-block;
        }
        
        .location-row {
            text-align: right;
            margin-top: 5px;
        }
        
        /* Tabla de items */
        .items-table {
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
            font-size: 8pt;
        }
        
        .items-table thead th {
            background-color: #ffeb3b;
            border: 1px solid #000;
            padding: 5px;
            text-align: center;
            font-weight: bold;
        }
        
        .items-table tbody td {
            border: 1px solid #000;
            padding: 4px;
        }
        
        .items-table .col-item { width: 8%; text-align: center; }
        .items-table .col-descripcion { width: 42%; }
        .items-table .col-unidad { width: 8%; text-align: center; }
        .items-table .col-cantidad { width: 10%; text-align: center; }
        .items-table .col-precio { width: 12%; text-align: right; }
        .items-table .col-subtotal { width: 12%; text-align: right; }
        
        .seccion-header {
            background-color: #ffeb3b !important;
            font-weight: bold;
            text-align: center;
        }
        
        .item-destacado {
            color: #cc0000;
            font-weight: bold;
        }
        
        /* Totales */
        .totales-section {
            margin-top: 10px;
        }
        
        .totales-table {
            width: 400px;
            float: right;
            font-size: 9pt;
        }
        
        .totales-table td {
            padding: 3px 8px;
        }
        
        .totales-table .label {
            text-align: left;
            font-weight: bold;
        }
        
        .totales-table .value {
            text-align: right;
            min-width: 100px;
        }
        
        .total-row {
            border: 2px solid #cc0000;
            font-weight: bold;
            font-size: 10pt;
        }
        
        /* Condiciones */
        .condiciones-box {
            border: 2px solid #cc0000;
            padding: 10px;
            margin: 15px 0;
        }
        
        .condiciones-title {
            font-weight: bold;
            text-decoration: underline;
            margin-bottom: 8px;
        }
        
        .condicion-item {
            margin: 3px 0;
            padding-left: 5px;
        }
        
        /* Datos del oferante */
        .oferante-section {
            margin-top: 15px;
            font-size: 8pt;
        }
        
        .oferante-title {
            font-weight: bold;
            text-decoration: underline;
            margin-bottom: 5px;
        }
        
        /* Firmas */
        .firmas-section {
            display: table;
            width: 100%;
            margin-top: 30px;
        }
        
        .firma-box {
            display: table-cell;
            width: 50%;
            text-align: center;
        }
        
        .firma-line {
            border-top: 1px solid #000;
            width: 200px;
            margin: 40px auto 5px;
        }
        
        .page-break {
            page-break-after: always;
        }
        
        /* Observaciones */
        .observaciones {
            margin: 10px 0;
            font-size: 8pt;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="header">
            <div class="header-left">
                <!-- Logo - Puedes agregar tu logo aquí -->
                {{-- <img src="{{ public_path('images/logo-maesc.png') }}" alt="MAESC" class="logo"> --}}
                <h3 style="color: #0066cc;">MAESC</h3>
            </div>
            <div class="header-right">
                <div class="company-info">
                    Av. Circunvalación del Golf Los Incas Nº298 - Int. 3038 - Santiago de Surco - Lima<br>
                    www.maescperu.com<br>
                    info@maescperu.com<br>
                    716 3340 / 715 3161
                </div>
                <div class="codigo-box">
                    <div class="numero">{{ $cotizacion->codigo ?? 'COT-XXX-2025' }}</div>
                    <div class="version">Ver. {{ $cotizacion->version ?? '2.0' }}</div>
                </div>
            </div>
        </div>
        
        <!-- Título -->
        <div class="titulo">PRESUPUESTO</div>
        
        <!-- Información del proyecto -->
        <div class="info-section">
            <div class="info-row">
                <div class="info-label">PROYECTO:</div>
                <div class="info-value">
                    <span class="bordered-value">{{ strtoupper($cotizacion->proyecto) }}</span>
                </div>
            </div>
            
            @if($cotizacion->etapa)
            <div class="info-row">
                <div class="info-label">ETAPA:</div>
                <div class="info-value">{{ $cotizacion->etapa }}</div>
            </div>
            @endif
            
            <div class="info-row">
                <div class="info-label">CLIENTE:</div>
                <div class="info-value">
                    <span class="bordered-value">{{ strtoupper($cotizacion->cliente) }} / RUC: {{ $cotizacion->ruc }}</span>
                </div>
            </div>
            
            <div class="info-row">
                <div class="info-label">FECHA:</div>
                <div class="info-value">{{ date('d/m/Y', strtotime($cotizacion->fecha)) }}</div>
            </div>
            
            <div class="info-row">
                <div class="info-label">DIRECCIÓN:</div>
                <div class="info-value">{{ strtoupper($cotizacion->direccion) }}</div>
            </div>
            
            <div class="location-row">
                <strong>CIUDAD:</strong> {{ $cotizacion->ciudad }}<br>
                <strong>PROVINCIA:</strong> {{ $cotizacion->provincia }}
            </div>
        </div>
        
        <!-- Tabla de items -->
        <table class="items-table">
            <thead>
                <tr>
                    <th class="col-item">ÍTEM</th>
                    <th class="col-descripcion">DESCRIPCIÓN</th>
                    <th class="col-unidad">UND.</th>
                    <th class="col-cantidad">CANTIDAD</th>
                    <th class="col-precio">PU - S/.</th>
                    <th class="col-subtotal">SUB TOTAL - S/.</th>
                </tr>
            </thead>
            <tbody>
                @php
                    $currentSection = null;
                    $subtotalGeneral = 0;
                @endphp
                
                @foreach($items as $item)
                    @if($item->seccion && $item->seccion != $currentSection)
                        <tr>
                            <td colspan="6" class="seccion-header">{{ strtoupper($item->seccion) }}</td>
                        </tr>
                        @php $currentSection = $item->seccion; @endphp
                    @endif
                    
                    <tr>
                        <td class="col-item">{{ $item->item }}</td>
                        <td class="col-descripcion">{{ strtoupper($item->descripcion) }}</td>
                        <td class="col-unidad">{{ $item->unidad }}</td>
                        <td class="col-cantidad">{{ number_format($item->cantidad, 2) }}</td>
                        <td class="col-precio">S/. {{ number_format($item->precio_unitario, 2) }}</td>
                        <td class="col-subtotal @if($item->cantidad >= 5) item-destacado @endif">
                            S/. {{ number_format($item->subtotal, 2) }}
                        </td>
                    </tr>
                    
                    @php $subtotalGeneral += $item->subtotal; @endphp
                @endforeach
            </tbody>
        </table>
        
        <!-- Observaciones si existen -->
        @if($cotizacion->observaciones)
        <div class="observaciones">
            <strong>CONSIDERACIONES DE OFERTA:</strong><br>
            {{ $cotizacion->observaciones }}
        </div>
        @endif
        
        <!-- Totales -->
        <div class="totales-section">
            <table class="totales-table">
                <tr>
                    <td class="label">Costo Directo</td>
                    <td class="value">S/. {{ number_format($cotizacion->costo_directo, 2) }}</td>
                </tr>
                <tr>
                    <td class="label">Gtos. Grles. -{{ number_format($cotizacion->gastos_generales, 0) }}%</td>
                    <td class="value">S/. {{ number_format($cotizacion->costo_directo * $cotizacion->gastos_generales / 100, 2) }}</td>
                </tr>
                <tr>
                    <td class="label">Utilidad -{{ number_format($cotizacion->utilidad, 0) }}%</td>
                    <td class="value">S/. {{ number_format($cotizacion->costo_directo * $cotizacion->utilidad / 100, 2) }}</td>
                </tr>
                <tr>
                    <td class="label">Sub Total</td>
                    <td class="value">S/. {{ number_format($cotizacion->subtotal, 2) }}</td>
                </tr>
                <tr>
                    <td class="label">IGV - {{ number_format($cotizacion->igv, 0) }}%</td>
                    <td class="value">S/. {{ number_format($cotizacion->subtotal * $cotizacion->igv / 100, 2) }}</td>
                </tr>
                <tr class="total-row">
                    <td class="label">Total</td>
                    <td class="value">S/. {{ number_format($cotizacion->total, 2) }}</td>
                </tr>
            </table>
            <div style="clear: both;"></div>
        </div>
        
        <!-- Condiciones de oferta -->
        <div class="condiciones-box">
            <div class="condiciones-title">CONDICIONES DE OFERTA:</div>
            <div class="condicion-item">- VALIDEZ DE OFERTA: {{ $cotizacion->validez_oferta }}</div>
            <div class="condicion-item">- TIEMPO DE EJECUCIÓN: {{ $cotizacion->tiempo_ejecucion }}</div>
            <div class="condicion-item">- FORMAS DE PAGO: {{ $cotizacion->forma_pago }}</div>
            <div class="condicion-item">- ADELANTOS: {{ $cotizacion->adelanto }}</div>
            @if($cotizacion->garantia)
            <div class="condicion-item">- GARANTÍA: {{ $cotizacion->garantia }}</div>
            @endif
        </div>
        
        <!-- Datos del oferante -->
        <div class="oferante-section">
            <div class="oferante-title">DATOS DEL OFERANTE:</div>
            <div>- RAZÓN SOCIAL: MAQUINARIAS ARROYO EQUIPOS SERVICIOS Y CONSTRUCCIÓN S.A.C.</div>
            <div>- RUC: 20544116591</div>
            <div>- CTAS BANCARIAS: BCP Soles: 191-1953670-0-39 / CCI 00219100195367003956</div>
            <div style="margin-left: 100px;">BCP Dólares: 191-2238615-1-93 / CCI 00219100223861519339</div>
            <div style="margin-left: 100px;">BBVA Soles: 0011-0750-0100007914 / CCI 01110700001000079148</div>
            <div>- CTA. DETRACCIÓN: 05-091-012737</div>
        </div>
        
        <!-- Firmas -->
        <div class="firmas-section">
            <div class="firma-box">
                <div class="firma-line"></div>
                <div>Elaborado por:</div>
                <div><strong>Nombre: Christian Vivanco</strong></div>
            </div>
            <div class="firma-box">
                <div class="firma-line"></div>
                <div>Aprobado por:</div>
                <div><strong>Nombre:</strong></div>
            </div>
        </div>
    </div>
</body>
</html>