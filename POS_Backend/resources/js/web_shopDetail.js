$(document).ready(function () {
    // ============================================
    // VARIABLES GLOBALES
    // ============================================
    let totalCarrito = 0;
    let cantidadInicialCargada = 1;
    let countdownInterval = null;
    let horaCorteData = null;

    // ============================================
    // BOTONES AUMENTAR/DISMINUIR CANTIDAD
    // ============================================
    $(document).on('click', '.btn-plus', function () {
        const input = $('#txt_cantidad');
        let valor = parseInt(input.val()) || 1;
        if (valor < 1000) {
            input.val(valor + 1).trigger('change');
            validarEnvioGratis();
        }
    });

    $(document).on('click', '.btn-minus', function () {
        const input = $('#txt_cantidad');
        let valor = parseInt(input.val()) || 1;
        if (valor > 1) {
            input.val(valor - 1).trigger('change');
            validarEnvioGratis();
        }
    });

    // ============================================
    // RESPONSIVE - SIDEBAR
    // ============================================
    function aplicarResponsive() {
        if ($(window).width() > 768) {
            $(".clsShopDetallProductoSider").addClass("slide-track");
        } else {
            $(".clsShopDetallProductoSider").removeClass("slide-track");
            $('.clsShopDetallProductoSider').removeAttr('style');
        }
    }
    aplicarResponsive();
    $(window).resize(function () {
        aplicarResponsive();
    });

    // ============================================
    // SCROLL SIDEBAR
    // ============================================
    var $cart = $('.slide-track'),
        $cartOffset = $('.sider').offset().top,
        $cartHeight = $cart.outerHeight(),
        $win = $(window),
        $footer = $('footer'),
        $footerOffset = $('footer').offset().top;

    $win.scroll(function (e) {
        var $scrollTop = $win.scrollTop();
        if ($scrollTop >= $cartOffset) {
            $cart.css({
                position: 'fixed',
                top: '5rem',
                marginRight: '5rem',
                heigth: '35rem'
            });
        } else if ($cart.css('position') === 'fixed') {
            $cart.css({
                position: '',
                marginRight: '0',
            });
        }
        if ($scrollTop >= $footerOffset - $cartHeight) {
            $cart.css({
                position: '',
                bottom: '0',
                top: '',
                marginRight: '5rem',
            });
        }
    });

    // ============================================
    // CAMBIO DE VARIACIÓN DE PRODUCTO
    // ============================================
    let btnImagenPrecio = document.querySelectorAll('.btnImagenPrecio');
    btnImagenPrecio.forEach(element => {
        element.addEventListener('click', function () {
            let txtCodigo = document.querySelector('#txtCodigo');
            let txtPrecio = document.querySelector('#txtPrecio') || 0;
            let codigo_producto = document.querySelector('#codigo_producto');
            let txtDescripcion = document.querySelector('#txtDescripcion');
            let txtTitulo = document.querySelector('#txtTitulo');

            txtPrecio.innerHTML = element.getAttribute('isPrecio');
            codigo_producto.value = element.getAttribute('isCodigoProducto');
            txtCodigo.innerHTML = element.getAttribute('isCodigoProducto');
            txtDescripcion.innerHTML = element.getAttribute('isDescripcion');
            txtTitulo.innerHTML = element.getAttribute('isTitulo');

            const rating = element.getAttribute('isCantidadEstrellas') || 0;
            const label = `${parseFloat(rating).toFixed(1)} Vendido por Capre. <span style="margin-left:10px;color:#1976d2;cursor:pointer;" title="¡Disponible en stock!"><i class="fas fa-info-circle"></i></span>`;
            renderStarRating(rating, label, '#txtEstrellas');

            setTimeout(validarEnvioGratis, 200);
        });
    });

    // ============================================
    // RENDER ESTRELLAS
    // ============================================
    function renderStarRating(rating, label, containerSelector) {
        rating = parseFloat(rating) || 0;
        const fullStars = Math.floor(rating);
        const halfStar = (rating - fullStars) >= 0.5 ? 1 : 0;
        const emptyStars = 5 - fullStars - halfStar;

        let html = '<div class="product-rating">';
        for (let i = 0; i < fullStars; i++) {
            html += '<i class="fas fa-star text-warning"></i>';
        }
        if (halfStar) {
            html += '<i class="fas fa-star-half-alt text-warning"></i>';
        }
        for (let i = 0; i < emptyStars; i++) {
            html += '<i class="far fa-star"></i>';
        }
        if (label) {
            html += `<span class="icon-tooltip">${label}</span>`;
        }
        html += '</div>';
        document.querySelector(containerSelector).innerHTML = html;
    }

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
    // VALIDAR ENVÍO GRATIS
    // ============================================
    function validarEnvioGratis() {
        const txtPrecio = document.querySelector('#txtPrecio');
        const cantidadInput = document.querySelector('#txt_cantidad');

        if (!txtPrecio || !cantidadInput) return;

        const precioUnitario = extraerPrecio(txtPrecio);
        const cantidadActual = parseInt(cantidadInput.value) || 1;
        const precioProductoActual = precioUnitario * cantidadActual;

        let totalGeneral;

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

    $('#txt_cantidad').on('input change keyup', function () {
        validarEnvioGratis();
    });

    $('.btnImagenPrecio').on('click', function () {
        setTimeout(() => {
            const cantidadInput = document.querySelector('#txt_cantidad');
            if (cantidadInput) {
                cantidadInicialCargada = parseInt(cantidadInput.value) || 1;
            }
            validarEnvioGratis();
        }, 200);
    });

    obtenerTotalCarrito().then(() => {
        const cantidadInput = document.querySelector('#txt_cantidad');
        if (cantidadInput) {
            cantidadInicialCargada = parseInt(cantidadInput.value) || 1;
        }
        validarEnvioGratis();
    });

    // ============================================
    // AGREGAR AL CARRITO
    // ============================================
    $('#formAgragarProducto').on('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        formData.append('codigo_producto', $('#codigo_producto').val());
        formData.append('cantidad', $('#txt_cantidad').val());

        axios.post('/web_shopDetail_agragar', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(function (response) {
                if (response.data.status == "error") {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: response.data.message,
                    });
                } else {
                    if (response.data.result && response.data.result.total_carrito) {
                        totalCarrito = parseFloat(response.data.result.total_carrito);
                    }

                    $('.clsShopDetallProductoSider').css('z-index', '99');
                    $('#idOpenModalDetalle').modal(true);

                    $('.clsDescripcion_CountCart').text(response.data.result["descripcion_cantidad"]);
                    $('.clsDescripcion_Precio').text(response.data.result["descripcion_precio"]);
                    $('.clsDescripcion_Nombre').text(response.data.result["descripcion_nombre"]);
                    $('#idDescripcion_Imagen').attr('src', '../' + response.data.result["descripcion_imagen"]);
                    $('.clsDetalle_CountCart').text(response.data.result["detalle_cantidad"]);

                    $('#txt_cantidad').val(1);
                    cantidadInicialCargada = 1;

                    $('.clsDescripcion_SubTotal').text(response.data.result["descripcion_t_subtotal"]);
                    $('.clsDescripcion_Igv').text('S/' + response.data.result["descripcion_t_igv"]);
                    $('.clsDetalle_Total').text('S/' + response.data.result["descripcion_t_total"]);

                    validarEnvioGratis();
                }
            })
            .catch(function (error) {
                console.log('Error al agregar producto:', error);
            });
    });

    // ============================================
    // COMPRA RÁPIDA
    // ============================================
    $('.clsComprarRapido').on('click', function (e) {
        e.preventDefault();
        let formData = new FormData();
        formData.append('codigo_producto', $('#codigo_producto').val());
        formData.append('cantidad', $('#txt_cantidad').val());

        axios.post('/web_shopDetail_agragar', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(function (response) {
                window.location.href = '/web_checkout/index';
            })
            .catch(function (error) {
                console.log('Error en compra rápida:', error);
            });
    });

    // ============================================
    // GALERÍA DE IMÁGENES
    // ============================================
    const $thumbWrapper = $('.rounded-thumbs-wrapper');
    const $thumbItems = $('.rounded-thumb');
    const thumbHeight = $thumbItems.outerHeight(true);
    const wrapperHeight = $thumbWrapper.height();
    const visibleThumbs = Math.floor(wrapperHeight / thumbHeight);
    let currentPosition = 0;

    $thumbItems.click(function () {
        const largeSrc = $(this).data('large-src');
        $('#product-zoom').attr('src', largeSrc);
        $thumbItems.removeClass('active-thumb');
        $(this).addClass('active-thumb');
    });

    $('.round-nav-up').click(function () {
        if (currentPosition > 0) {
            currentPosition--;
            $thumbWrapper.stop().animate({
                scrollTop: currentPosition * thumbHeight
            }, 300);
        }
    });

    $('.round-nav-down').click(function () {
        const maxPosition = $thumbItems.length - visibleThumbs;
        if (currentPosition < maxPosition) {
            currentPosition++;
            $thumbWrapper.stop().animate({
                scrollTop: currentPosition * thumbHeight
            }, 300);
        }
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
        if (show) {
            $('#calculateBtn').prop('disabled', true);
            $('#calculateBtn span').removeClass('d-none');
        } else {
            $('#calculateBtn').prop('disabled', false);
            $('#calculateBtn span').addClass('d-none');
        }
    }

    $('#formCalcularEnvio').on('submit', function (e) {
        e.preventDefault();

        if (!this.checkValidity()) {
            e.stopPropagation();
            this.classList.add('was-validated');
            return;
        }

        toggleLoading(true);
        $('#shippingResults').addClass('d-none');
        $('#shippingResultsPlaceholder').removeClass('d-none');

        let formData = new FormData(this);
        formData.append('address_departamento', $('#txt_listar_departamentos').val() || null);
        formData.append('address_distrito', $('#txt_listar_distritos').val() || null);

        axios.post('/web_calcular_envio/calcular_envio', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        })
            .then(function (response) {
                console.log('Respuesta del servidor:', response.data);
                updateShippingResults(response.data);
                toggleLoading(false);
            })
            .catch(function (error) {
                console.error('Error:', error.response ? error.response.data : error);
                showError('Ocurrió un error al calcular el envío');
                toggleLoading(false);
            });
    });
// ============================================
    // ACTUALIZAR RESULTADOS DE ENVÍO (CORREGIDO)
    // ============================================
    function updateShippingResults(data) {
        console.log('Actualizando resultados con:', data);
        
        // ✅ LIMPIAR CONTENIDO PREVIO PARA EVITAR DUPLICACIÓN
        $('.costoEnvio').empty();
        $('.lugarEnvio').empty();
        
        let html = '';
        let html_Lugar = '';
        let html_domicilio_recibelo_hoy = '';
        let html_domicilio_empresa = '';
        let contacto_direccion = data.contacto_direccion;
        let distrito = data.distrito || '';
        let precio_envio = parseFloat(data.precio_envio) || 0;
        let pago_contra_entrega = data.pago_contra_entrega || '';
        let address_departamento = data.address_departamento || '';

        // ============================================
        // ✅ HTML BASE CON SECCIÓN DE ENVÍO A DOMICILIO
        // ============================================
        html += `<div class="shipping-section">
                    <div class="shipping-icon">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none"
                            stroke="currentColor" stroke-width="2">
                            <rect x="1" y="3" width="15" height="13" />
                            <polygon points="16,8 20,8 23,11 23,16 16,16 16,8" />
                            <circle cx="5.5" cy="18.5" r="2.5" />
                            <circle cx="18.5" cy="18.5" r="2.5" />
                        </svg>
                    </div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Envío a domicilio <span class="location ">a ${distrito} por S/${precio_envio.toFixed(2)}</span>
                        </h3>
                    </div>
                </div>`;

        // ============================================
        // ✅ MENSAJE DE PAGO CONTRAENTREGA
        // ============================================
        html += `${pago_contra_entrega ? `<p class="clsMensajePageAlEntrega mb-1">${pago_contra_entrega}</p>` : ""}`;

        // ============================================
        // ✅ HTML PARA UBICACIÓN - VALIDACIÓN DE PROVINCIA
        // ============================================
        // Opción 1: Si es Lima Metropolitana
        if (address_departamento === 'Lima Metropolitana') {
            html_Lugar = `<div class="shipping-section">
                    <div class="shipping-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" height="25px" viewBox="0 0 24 24" id="map-marker-question"><path fill="#6563FF" d="M12.44,13.11,12.27,13a1,1,0,0,0-1.09.22.87.87,0,0,0-.22.32,1,1,0,0,0-.08.39,1,1,0,0,0,.08.38,1.07,1.07,0,0,0,.54.54,1,1,0,0,0,.38.08,1.09,1.09,0,0,0,.39-.08,1,1,0,0,0,.32-.22,1,1,0,0,0,0-1.41ZM11.88,6A2.75,2.75,0,0,0,9.5,7.32a1,1,0,1,0,1.73,1A.77.77,0,0,1,11.88,8a.75.75,0,1,1,0,1.5,1,1,0,1,0,0,2,2.75,2.75,0,1,0,0-5.5Zm8.58,3.68A8.5,8.5,0,0,0,7.3,3.36,8.56,8.56,0,0,0,3.54,9.63,8.46,8.46,0,0,0,6,16.46l5.3,5.31a1,1,0,0,0,1.42,0L18,16.46A8.46,8.46,0,0,0,20.46,9.63ZM16.6,15.05,12,19.65l-4.6-4.6A6.49,6.49,0,0,1,5.53,9.83,6.57,6.57,0,0,1,8.42,5a6.47,6.47,0,0,1,7.16,0,6.57,6.57,0,0,1,2.89,4.81A6.49,6.49,0,0,1,16.6,15.05Z"></path></svg>
                    </div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">${contacto_direccion}</h3>
                    </div>
                </div>`;
        } 
        // Opción 2: Si es FUERA de Lima Metropolitana (Provincia)
        else {
            // ✅ PROVINCIA: Solo mostrar agencias (SIN dirección de Lima)
            html_Lugar = `<div class="pickup-section">
                        <div class="pickup-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none"
                                stroke="currentColor" stroke-width="2">
                                <rect x="2" y="3" width="20" height="14" rx="2" ry="2" />
                                <line x1="8" y1="21" x2="16" y2="21" />
                                <line x1="12" y1="17" x2="12" y2="21" />
                            </svg>
                        </div>
                        <div class="pickup-content">
                            <h3>Recoge y paga el envío en la agencia <strong>Shalom</strong> <span class="location">${address_departamento}</span></h3>
                            <a href="https://agencias.shalom.pe/" target="_blank" class="location-link">Ver ubicaciones y horarios</a>
                        </div>
                    </div>`;

            html_Lugar += `<div class="pickup-section">
                        <div class="pickup-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none"
                                stroke="currentColor" stroke-width="2">
                                <rect x="2" y="3" width="20" height="14" rx="2" ry="2" />
                                <line x1="8" y1="21" x2="16" y2="21" />
                                <line x1="12" y1="17" x2="12" y2="21" />
                            </svg>
                        </div>
                        <div class="pickup-content">
                            <h3>Recoge y paga el envío en la agencia <strong>Marvisur</strong> <span class="location">${address_departamento}</span></h3>
                            <a href="https://www.expresomarvisur.com/sucursales" target="_blank" class="location-link">Ver ubicaciones y horarios</a>
                        </div>
                    </div>`;
        }

        // ============================================
        // ✅ LÓGICA DE ENVÍO A DOMICILIO (CON VALIDACIÓN)
        // ============================================
        if (data.hora_corte_procesada) {
            const tipoEntrega = data.hora_corte_procesada.tipo;
            const svgIcon = `<svg xmlns="http://www.w3.org/2000/svg"  height="25px" viewBox="0 0 24 24" id="rocket"><path fill="#3d007eff" d="M22.601 2.062a1 1 0 0 0-.713-.713A11.252 11.252 0 0 0 10.47 4.972L9.354 6.296 6.75 5.668a2.777 2.777 0 0 0-3.387 1.357l-2.2 3.9a1 1 0 0 0 .661 1.469l3.073.659a13.42 13.42 0 0 0-.555 2.434 1 1 0 0 0 .284.836l3.1 3.1a1 1 0 0 0 .708.293c.028 0 .057-.001.086-.004a12.169 12.169 0 0 0 2.492-.49l.644 3.004a1 1 0 0 0 1.469.661l3.905-2.202a3.035 3.035 0 0 0 1.375-3.304l-.668-2.76 1.237-1.137A11.204 11.204 0 0 0 22.6 2.062ZM3.572 10.723l1.556-2.76a.826.826 0 0 1 1.07-.375l1.718.416-.65.772a13.095 13.095 0 0 0-1.59 2.398Zm12.47 8.222-2.715 1.532-.43-2.005a11.34 11.34 0 0 0 2.414-1.62l.743-.683.404 1.664a1.041 1.041 0 0 1-.416 1.112Zm1.615-6.965-3.685 3.386a9.773 9.773 0 0 1-5.17 2.304l-2.405-2.404a10.932 10.932 0 0 1 2.401-5.206l1.679-1.993a.964.964 0 0 0 .078-.092L11.99 6.27a9.278 9.278 0 0 1 8.81-3.12 9.218 9.218 0 0 1-3.143 8.829Zm-.923-6.164a1.5 1.5 0 1 0 1.5 1.5 1.5 1.5 0 0 0-1.5-1.5Z"></path></svg>`;

            if (tipoEntrega === 'hoy') {
                // Caso 1: Recíbelo HOY
                html_domicilio_recibelo_hoy = `<div class="shipping-section">
                    <div class="shipping-icon">${svgIcon}</div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Recíbelo Hoy por S/${precio_envio.toFixed(2)}</h3>
                    </div>
                </div>`;
            } else if (tipoEntrega === 'manana') {
                // Caso 2: Llega MAÑANA
                html_domicilio_recibelo_hoy = `<div class="shipping-section">
                    <div class="shipping-icon">${svgIcon}</div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Llega Mañana por S/${precio_envio.toFixed(2)}</h3>
                    </div>
                </div>`;
            } else if (data.hora_corte_procesada.fecha_entrega) {
                // Caso 3: Llega en FECHA ESPECÍFICA (ej: Miércoles)
                html_domicilio_recibelo_hoy = `<div class="shipping-section">
                    <div class="shipping-icon">${svgIcon}</div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Llega el ${data.hora_corte_procesada.fecha_entrega} por S/${precio_envio.toFixed(2)}</h3>
                    </div>
                </div>`;
            }
        }

        // ============================================
        // ✅ LÓGICA DE RETIRO EN TIENDA (CON VALIDACIÓN)
        // ============================================
        if (data.hora_corte_procesada) {
            const tipoRetiro = data.hora_corte_procesada.tipo;
            const svgIcon = `<svg xmlns="http://www.w3.org/2000/svg" height="25px" viewBox="0 0 24 24" id="map-marker-question"><path fill="#6563FF" d="M12.44,13.11,12.27,13a1,1,0,0,0-1.09.22.87.87,0,0,0-.22.32,1,1,0,0,0-.08.39,1,1,0,0,0,.08.38,1.07,1.07,0,0,0,.54.54,1,1,0,0,0,.38.08,1.09,1.09,0,0,0,.39-.08,1,1,0,0,0,.32-.22,1,1,0,0,0,0-1.41ZM11.88,6A2.75,2.75,0,0,0,9.5,7.32a1,1,0,1,0,1.73,1A.77.77,0,0,1,11.88,8a.75.75,0,1,1,0,1.5,1,1,0,1,0,0,2,2.75,2.75,0,1,0,0-5.5Zm8.58,3.68A8.5,8.5,0,0,0,7.3,3.36,8.56,8.56,0,0,0,3.54,9.63,8.46,8.46,0,0,0,6,16.46l5.3,5.31a1,1,0,0,0,1.42,0L18,16.46A8.46,8.46,0,0,0,20.46,9.63ZM16.6,15.05,12,19.65l-4.6-4.6A6.49,6.49,0,0,1,5.53,9.83,6.57,6.57,0,0,1,8.42,5a6.47,6.47,0,0,1,7.16,0,6.57,6.57,0,0,1,2.89,4.81A6.49,6.49,0,0,1,16.6,15.05Z"></path></svg>`;

            if (tipoRetiro === 'hoy') {
                // Caso 1: Retíralo INMEDIATAMENTE (hoy)
                html_domicilio_empresa = `<div class="shipping-section">
                    <div class="shipping-icon">${svgIcon}</div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Retíralo inmediatamente en ${contacto_direccion}</h3>
                    </div>
                </div>`;
            } else if (tipoRetiro === 'manana') {
                // Caso 2: Retíralo MAÑANA
                html_domicilio_empresa = `<div class="shipping-section">
                    <div class="shipping-icon">${svgIcon}</div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Retíralo mañana en ${contacto_direccion}</h3>
                    </div>
                </div>`;
            } else if (data.hora_corte_procesada.dia_nombre) {
                // Caso 3: Retíralo el [DÍA ESPECÍFICO] (ej: Miércoles)
                html_domicilio_empresa = `<div class="shipping-section">
                    <div class="shipping-icon">${svgIcon}</div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Retíralo el ${data.hora_corte_procesada.dia_nombre} en ${contacto_direccion}</h3>
                    </div>
                </div>`;
            } else {
                // Fallback: Retiro sin fecha específica
                html_domicilio_empresa = `<div class="shipping-section">
                    <div class="shipping-icon">${svgIcon}</div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Retíralo en ${contacto_direccion}</h3>
                    </div>
                </div>`;
            }
        }

        // ============================================
        // ✅ AGREGAR HTML AL DOM (SIN DUPLICACIÓN)
        // ============================================
        $('.costoEnvio').html(html);

        // ============================================
        // ✅ LÓGICA MEJORADA: Mostrar opciones según departamento
        // ============================================
        if (address_departamento === 'Lima Metropolitana') {
            // LIMA METROPOLITANA: Mostrar retiro en tienda según hora_regresiva_descripcion
            if (data.hora_regresiva_descripcion !== 'Retírelo por shalom y marvisur') {
                // Lima + hora de corte activa: Mostrar "Recíbelo Hoy" + "Retíralo"
                $('.lugarEnvio').html(html_domicilio_recibelo_hoy + html_domicilio_empresa);
            } else {
                // Lima + sin hora de corte: Solo ubicación genérica
                $('.lugarEnvio').html(html_Lugar);
            }
        } else {
            // PROVINCIA: SIEMPRE mostrar agencias (Shalom + Marvisur)
            // También incluir las opciones de envío si existen
            if (data.hora_regresiva_descripcion !== 'Retírelo por shalom y marvisur') {
                // Provincia con hora de corte: Mostrar "Recíbelo" + "Retíralo" + Agencias
                $('.lugarEnvio').html(html_domicilio_recibelo_hoy + html_domicilio_empresa + html_Lugar);
            } else {
                // Provincia sin hora de corte: Solo agencias
                $('.lugarEnvio').html(html_Lugar);
            }
        }

        // ============================================
        // ✅ PROCESAR HORA DE CORTE (CONTADOR)
        // ============================================
        if (data.hora_corte_procesada) {
            console.log('Datos de hora de corte:', data.hora_corte_procesada);
            inicializarCountdown(data.hora_corte_procesada, data.hora_regresiva_descripcion);
        }
    }

    // ============================================
    // SISTEMA DE COUNTDOWN CON VENTANA DE 12 HORAS
    // ============================================
    function inicializarCountdown(datosCorte, descripcion) {
        console.log('Inicializando countdown:', datosCorte, descripcion);

        if (!datosCorte) {
            console.log('No hay datos de corte');
            return;
        }

        horaCorteData = datosCorte;

        if (countdownInterval) {
            clearInterval(countdownInterval);
        }

        const container = $('.costoEnvio');
        let html = '';

        if (datosCorte.tipo === 'hoy') {
            console.log('Mostrando contador para HOY');
            html = `
                <div class="clsComprandoLasProximasHoras">
                    ${descripcion ? `<p>${descripcion}</p>` : '<p>Pídelo en las próximas</p>'}
                    <span id="corte_tiempo_promocion" style="padding-top: 15px; font-weight: bold; color: #FF5722;">
                        <span id="countdown-horas">00</span>h <span id="countdown-minutos">00</span>m
                    </span>
                </div>
            `;
        } else {
            console.log('Mostrando LLEGA MAÑANA');
            html = `
                <label class="clsMensajeRetireAgencia mb-1" style="font-weight: bold; color: #4CAF50;">
                    Llega Mañana ${datosCorte.dia} de ${datosCorte.mes}
                </label>
            `;
        }

        container.prepend(html);
 

        actualizarContador();
        countdownInterval = setInterval(actualizarContador, 1000);
    }

    function actualizarContador() {
        const ahora = Math.floor(Date.now() / 1000);

        if (!horaCorteData) return;

        if (horaCorteData.tipo === 'hoy') {
            const tiempoRestante = horaCorteData.timestamp_corte - ahora;

            if (tiempoRestante <= 0) {
                cambiarAManana();
                return;
            }

            const horas = Math.floor(tiempoRestante / 3600);
            const minutos = Math.floor((tiempoRestante % 3600) / 60);

            const horasElement = document.getElementById('countdown-horas');
            const minutosElement = document.getElementById('countdown-minutos');

            if (horasElement && minutosElement) {
                horasElement.textContent = horas.toString().padStart(2, '0');
                minutosElement.textContent = minutos.toString().padStart(2, '0');
            }

        } else if (horaCorteData.tipo === 'manana') {
            if (ahora >= horaCorteData.timestamp_proxima_ventana) {
                recargarDatosEnvio();
            }
        }
    }

    function cambiarAManana() {
        if (countdownInterval) {
            clearInterval(countdownInterval);
        }

        const ahora = new Date();
        const manana = new Date(ahora);
        manana.setDate(manana.getDate() + 1);

        const meses = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
        const dia = manana.getDate();
        const mes = meses[manana.getMonth()];

        horaCorteData = {
            tipo: 'manana',
            dia: dia,
            mes: mes,
            fecha_entrega: `${dia} de ${mes}`,
            timestamp_proxima_ventana: Math.floor(Date.now() / 1000) + (12 * 3600)
        };

        const container = $('.costoEnvio');
        const countdownDiv = container.find('.clsComprandoLasProximasHoras');

        if (countdownDiv.length) {
            countdownDiv.replaceWith(`
                <label class="clsMensajeRetireAgencia mb-1" style="font-weight: bold; color: #4CAF50;">
                    Llega Mañana ${dia} de ${mes}
                </label>
            `);
        }

        countdownInterval = setInterval(actualizarContador, 1000);
    }

    function recargarDatosEnvio() {
        const departamento = $('#txt_listar_departamentos').val();
        const distrito = $('#txt_listar_distritos').val();
        const peso = $('input[name="txt_peso_kilogramo"]').val();
        const medidas = $('input[name="txt_paqueta_medidas"]').val();
        const dimension = $('input[name="txt_paquete_dimencion"]').val();

        if (departamento && distrito) {
            let formData = new FormData();
            formData.append('address_departamento', departamento);
            formData.append('address_distrito', distrito);
            formData.append('txt_peso_kilogramo', peso);
            formData.append('txt_paqueta_medidas', medidas);
            formData.append('txt_paquete_dimencion', dimension);

            axios.post('/web_calcular_envio/calcular_envio', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            })
                .then(function (response) {
                    updateShippingResults(response.data);
                })
                .catch(function (error) {
                    console.error('Error al recargar datos de envío:', error);
                });
        }
    }

    $(window).on('beforeunload', function () {
        if (countdownInterval) {
            clearInterval(countdownInterval);
        }
    });

    // ============================================
    // FUNCIÓN PARA MOSTRAR ERRORES
    // ============================================
    function showError(message) {
        const toast = `<div class="toast show align-items-center text-white bg-danger position-fixed bottom-0 end-0 m-3" role="alert">
                <div class="d-flex">
                    <div class="toast-body">${message}</div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>
            </div>`;

        $('body').append(toast);
        setTimeout(() => $('.toast').remove(), 5000);
    }
});