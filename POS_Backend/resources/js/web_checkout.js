$(document).ready(function () {
    // ============================================
    // VARIABLES GLOBALES
    // ============================================
    let totalCarrito = 0;
    let cantidadInicialCargada = 1;

    // ============================================
    // FUNCIÓN PARA EXTRAER PRECIO
    // ============================================
    function extraerPrecio(selectorOElemento) {
        const elemento = typeof selectorOElemento === 'string'
            ? document.querySelector(selectorOElemento)
            : selectorOElemento;

        if (!elemento) return 0;

        const texto = elemento.textContent || elemento.innerText;
        const precio = texto.replace(/[^\d.]/g, '');

        return parseFloat(precio) || 0;
    }

    // ============================================
    // OBTENER TOTAL DEL CARRITO DESDE EL SERVIDOR
    // ============================================
    function obtenerTotalCarrito() {
        return fetch('/web_shopDetail_total_carrito', {
            method: 'GET',
            headers: {
                'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute('content'),
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                totalCarrito = parseFloat(data.total) || 0;
                return totalCarrito;
            })
            .catch(error => {
                console.error('Error al obtener total del carrito:', error);
                return 0;
            });
    }

    // ============================================
    // VALIDAR ENVÍO GRATIS (FUNCIÓN MEJORADA)
    // ============================================
    function validarEnvioGratis() {
        const txtPrecio = document.querySelector('#txtPrecio');
        const cantidadInput = document.querySelector('#txt_cantidad');

        if (!txtPrecio || !cantidadInput) return;

        const precioUnitario = extraerPrecio(txtPrecio);
        const cantidadActual = parseInt(cantidadInput.value) || 1;
        const precioProductoActual = precioUnitario * cantidadActual;

        let totalGeneral;
        console.log('%c [test]-60', 'font-size:13px; background:pink; color:#bf2c9f;', totalGeneral)

        if (totalCarrito === 0) {
            totalGeneral = precioProductoActual;
        } else if (cantidadActual === cantidadInicialCargada) {
            totalGeneral = totalCarrito;
        } else {
            totalGeneral = totalCarrito + precioProductoActual;
        }

        const umbralEnvioGratis = 199;
        const mensajeContainer = $('.mensajeLlevaloDesde');

        if (totalGeneral > 0 && totalGeneral < umbralEnvioGratis) {
            const faltante = umbralEnvioGratis - totalGeneral;
            let htmlMensaje = `
                <div class="free-shipping mt-2">
                    <div class="free-shipping-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                            <circle cx="12" cy="12" r="10" fill="#FF9800" />
                            <path d="M12 8v4M12 16h.01" stroke="white" stroke-width="2" stroke-linecap="round" />
                        </svg>
                    </div>
                    <div class="free-shipping-text">
                        <strong>¡Envío gratis</strong> en compras a partir de <strong>S/199</strong>
                        <br>
                        <small>Te faltan <strong>S/${faltante.toFixed(2)}</strong></small>
                        <br>
                        <small style="color: #666;">Total: <strong>S/${totalGeneral.toFixed(2)}</strong></small>
                    </div>
                </div>
            `;

            mensajeContainer.html(htmlMensaje);

        } else if (totalGeneral >= umbralEnvioGratis) {
            let htmlMensaje = `
                <div class="free-shipping mt-2">
                    <div class="free-shipping-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                            <circle cx="12" cy="12" r="10" fill="#4CAF50" />
                            <path d="M8 12l2 2 4-4" stroke="white" stroke-width="2" fill="none" />
                        </svg>
                    </div>
                    <div class="free-shipping-text">
                        <strong>🎉 ¡Envío GRATIS!</strong>
                        <br>
                        <small>Tu compra califica para envío sin costo</small>
                        <br>
                        <small style="color: #4CAF50;">Total: <strong>S/${totalGeneral.toFixed(2)}</strong></small>
                    </div>
                </div>
            `;

            mensajeContainer.html(htmlMensaje);
        } else {
            mensajeContainer.html('');
        }
    }

    // ============================================
    // EVENTOS PARA VALIDAR ENVÍO GRATIS
    // ============================================
    obtenerTotalCarrito().then((response) => {
        cantidadInicialCargada = response || 1;
        validarEnvioGratis();
    });

    // ============================================
    // CÁLCULO DE ENVÍO
    // ============================================
    $('#txt_listar_distritos, #txt_listar_departamentos').select2({
        theme: 'bootstrap-5',
        width: '100%'
    });

    function cargarDistritos(idDepartamento) {
        const $districtSelect = $('#txt_listar_distritos');

        if (!idDepartamento) {
            $districtSelect.prop('disabled', true).val('').trigger('change');
            return;
        }

        if (idDepartamento === 'Lima Metropolitana') {
            $('#idTiempoEntrega').show();
        } else {
            $('#idTiempoEntrega').hide();
        }

        $districtSelect.prop('disabled', true);
        $districtSelect.html('<option value="">Cargando distritos...</option>');

        $.ajax({
            url: `/web_calcular_envio/${idDepartamento}/lista_distritos`,
            method: 'GET',
            success: function (result) {
                let arrays = JSON.parse(result);
                let options = '<option value="" selected disabled>Seleccione...</option>';
                arrays.forEach(dataRow => {
                    options += `<option value="${dataRow.address_distrito}">${dataRow.address_distrito}</option>`;
                });
                $districtSelect.html(options).prop('disabled', false);
            },
            error: function () {
                showError('No se pudieron cargar los distritos');
                $districtSelect.html('<option value="" selected disabled>Error al cargar</option>');
            }
        });
    }

    $('#txt_listar_departamentos').on('change', function () {
        cargarDistritos($(this).val());
    });

    const initialDepto = $('#txt_listar_departamentos').val();
    if (initialDepto) {
        cargarDistritos(initialDepto);
    }

    function toggleLoading(show) {
        const $loadingIndicator = $('.loading-shipping-cost');
        if (show) {
            if ($loadingIndicator.length === 0) {
                // $('.costoEnvio').html(`
                //     <div class="loading-shipping-cost" style="
                //         display: flex;
                //         align-items: center;
                //         gap: 10px;
                //         padding: 12px;
                //         background: #f8f9fa;
                //         border-radius: 8px;
                //         margin-top: 12px;
                //     ">
                //         <div class="spinner-border spinner-border-sm text-primary" role="status">
                //             <span class="visually-hidden">Cargando...</span>
                //         </div>
                //         <span style="color: #6c757d; font-size: 14px;">Calculando costo de envío...</span>
                //     </div>
                // `);
            }
        } else {
            $loadingIndicator.remove();
        }
    }

    // ============================================
    // EVENTO ONCHANGE DEL DISTRITO (AUTOMÁTICO)
    // ============================================
    $('#txt_listar_distritos').on('change', function () {
        const departamento = $('#txt_listar_departamentos').val();
        const distrito = $(this).val();

        if (!departamento || !distrito) {
            return;
        }

        toggleLoading(true);

        let formData = new FormData();
        formData.append('_token', $('meta[name="csrf-token"]').attr('content'));
        formData.append('address_departamento', departamento);
        formData.append('address_distrito', distrito);
        formData.append('txt_peso_kilogramo', $('input[name="txt_peso_kilogramo"]').val() || '');
        formData.append('txt_paqueta_medidas', $('input[name="txt_paqueta_medidas"]').val() || '');
        formData.append('txt_paquete_dimencion', $('input[name="txt_paquete_dimencion"]').val() || '');

        axios.post('/web_calcular_envio/calcular_envio', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        })
            .then(function (response) {
                updateShippingResults(response.data);
                toggleLoading(false);
            })
            .catch(function (error) {
                console.error('Error:', error.response ? error.response.data : error);
                showError('Ocurrió un error al calcular el envío');
                toggleLoading(false);
            });
    });

    // Variable global para guardar el total BASE (sin envío)
    let totalBase = 0;
    let totalBaseGuardado = false;

    function updateShippingResults(data) {
        const distrito = data.distrito;
        const address_departamento = data.address_departamento;
        const pago_contra_entrega = data.pago_contra_entrega;
        const hora_regresiva = data.hora_regresiva;
        const hora_regresiva_descripcion = data.hora_regresiva_descripcion;
        let precio_envio = parseFloat(data.precio_envio) || 0;
        const contacto_direccion = data.contacto_direccion;
        const totalElement = document.getElementById('total-price');

        // 1. Guardar el total base SOLO LA PRIMERA VEZ
        if (!totalBaseGuardado) {
            totalBase = parseFloat(totalElement.textContent.replace(/,/g, '')) || 0;
            totalBaseGuardado = true;
            console.log('%c Total Base Guardado:', 'background:purple; color:white;', totalBase);
        }

        // 2. Si el total base es >= 199, el envío es GRATIS
        if (totalBase >= 199.00) {
            precio_envio = 0;
            console.log('%c 🎉 Envío GRATIS (total >= 199)', 'background:green; color:white;');
        }

        // 3. Calcular el nuevo total desde el base
        const nuevoTotal = totalBase + precio_envio;

        console.log('%c Total Base:', 'background:orange; color:white;', totalBase);
        console.log('%c Envío:', 'background:green; color:white;', precio_envio);
        console.log('%c Nuevo Total:', 'background:blue; color:white;', nuevoTotal);

        // 4. Actualizar el total
        totalElement.textContent = nuevoTotal.toFixed(2);











        // Limpiar contenedores
        $('.costoEnvio').empty();
        $('.lugarEnvio').empty();

        let htmlResultados = '';

        // Badge amarillo (si aplica)
        if (hora_regresiva_descripcion && hora_regresiva_descripcion !== 'Retírelo por shalom y marvisur') {
            htmlResultados += `
                <div style="
                    background: #FFF9E6;
                    border: 1px solid #FFE082;
                    border-radius: 8px;
                    padding: 8px 12px;
                    display: inline-block;
                    margin-bottom: 12px;
                    font-size: 13px;
                    color: #856404;
                    font-weight: 500;
                ">
                    ${hora_regresiva_descripcion}
                    ${hora_regresiva ? `<span id="corte_tiempo_promocion" style="margin-left: 5px; font-weight: 600;">${hora_regresiva}</span>` : ''}
                </div>
            `;
        }

        // ENVÍO A DOMICILIO
        if (distrito) {
            if (cantidadInicialCargada >= 199.00) {

                htmlResultados += `
                <div style="display: flex; align-items: flex-start; gap: 12px; margin-bottom: 12px;">
                    <div style="
                        width: 24px;
                        height: 24px;
                        flex-shrink: 0;
                        margin-top: 2px;
                    ">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#6c757d" stroke-width="2">
                            <rect x="1" y="3" width="15" height="13" />
                            <polygon points="16,8 20,8 23,11 23,16 16,16 16,8" />
                            <circle cx="5.5" cy="18.5" r="2.5" />
                            <circle cx="18.5" cy="18.5" r="2.5" />
                        </svg>
                    </div>
                    <div style="flex: 1;">
                        <p style="
                            margin: 0;
                            font-size: 14px;
                            color: #212529;
                            line-height: 1.5;
                        ">
                            <strong>Envío a domicilio</strong> a <span style="color: #6c757d;">${distrito}</span> - <strong style="color: #75c278;"> Envío Gratis </strong>
                        </p>
                    </div>
                </div>
            `;

            } else {
                htmlResultados += `
                <div style="display: flex; align-items: flex-start; gap: 12px; margin-bottom: 12px;">
                    <div style="
                        width: 24px;
                        height: 24px;
                        flex-shrink: 0;
                        margin-top: 2px;
                    ">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#6c757d" stroke-width="2">
                            <rect x="1" y="3" width="15" height="13" />
                            <polygon points="16,8 20,8 23,11 23,16 16,16 16,8" />
                            <circle cx="5.5" cy="18.5" r="2.5" />
                            <circle cx="18.5" cy="18.5" r="2.5" />
                        </svg>
                    </div>   
                    <div style="flex: 1;">
                        <p style="
                            margin: 0;
                            font-size: 14px;
                            color: #212529;
                            line-height: 1.5;
                        ">
                            <strong>Envío a domicilio</strong> a <span style="color: #6c757d;">${distrito}</span> por <strong style="color: #212529;">S/${precio_envio.toFixed(2)}</strong>
                        </p>
                    </div>
                </div>
            `;
            }
        }



        // RETIRO INMEDIATO EN TIENDA
        if (contacto_direccion && hora_regresiva_descripcion !== 'Retírelo por shalom y marvisur') {
            htmlResultados += `
                <div style="display: flex; align-items: flex-start; gap: 12px; margin-bottom: 12px;">
                    <div style="
                        width: 24px;
                        height: 24px;
                        flex-shrink: 0;
                        margin-top: 2px;
                    ">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#0d6efd" stroke-width="2">
                            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                            <circle cx="12" cy="10" r="3"></circle>
                        </svg>
                    </div>
                    <div style="flex: 1;">
                        <p style="
                            margin: 0;
                            font-size: 14px;
                            color: #212529;
                            line-height: 1.5;
                        ">
                            <strong>Retiro inmediato</strong> en <span style="color: #6c757d;">${contacto_direccion}</span>
                        </p>
                    </div>
                </div>
            `;
        }

        // OPCIONES DE RECOJO SHALOM Y MARVISUR
        if (hora_regresiva_descripcion === 'Retírelo por shalom y marvisur') {
            htmlResultados += `
                <div style="display: flex; align-items: flex-start; gap: 12px; margin-bottom: 12px;">
                    <div style="
                        width: 24px;
                        height: 24px;
                        flex-shrink: 0;
                        margin-top: 2px;
                    ">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#0d6efd" stroke-width="2">
                            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                            <circle cx="12" cy="10" r="3"></circle>
                        </svg>
                    </div>
                    <div style="flex: 1;">
                        <p style="
                            margin: 0 0 4px 0;
                            font-size: 14px;
                            color: #212529;
                            line-height: 1.5;
                        ">
                            <strong>Retiro inmediato</strong> en <span style="color: #6c757d;">Jr. Daniel Hernández 1304, Pueblo Libre.</span>
                        </p>
                    </div>
                </div>
            `;
        }

        $('.costoEnvio').html(htmlResultados);

        // Iniciar countdown si existe
        if (hora_regresiva) {
            initCountdown(hora_regresiva);
        }
    }

    function initCountdown(targetTimeStr) {
        if (window.countdownInterval) {
            clearInterval(window.countdownInterval);
        }

        function updateCountdown() {
            const countdownElement = document.getElementById('corte_tiempo_promocion');
            if (!countdownElement) return;

            let [time, modifier] = targetTimeStr.split(/(am|pm)/i);
            let [hours, minutes] = time.split(':').map(Number);

            if (modifier) {
                hours = modifier.toLowerCase() === 'pm' && hours < 12 ? hours + 12 : hours;
                hours = modifier.toLowerCase() === 'am' && hours === 12 ? 0 : hours;
            }

            const now = new Date();
            let targetTime = new Date();
            targetTime.setHours(hours, minutes || 0, 0, 0);

            if (targetTime <= now) {
                targetTime.setDate(targetTime.getDate() + 1);
            }

            const diff = targetTime - now;
            const totalMinutes = Math.floor(diff / (1000 * 60));
            const hoursLeft = Math.floor(totalMinutes / 60);
            const minutesLeft = totalMinutes % 60;

            let countdownText = '';
            if (hoursLeft > 0) {
                countdownText += `${hoursLeft}h `;
            }
            countdownText += `${minutesLeft}m`;

            countdownElement.textContent = countdownText;
        }

        updateCountdown();
        window.countdownInterval = setInterval(updateCountdown, 30000);
    }

    function showError(message) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: message,
            confirmButtonText: 'Entendido',
            toast: true,
            position: 'top-end',
            timer: 3000,
            timerProgressBar: true,
            showConfirmButton: false
        });
    }
});