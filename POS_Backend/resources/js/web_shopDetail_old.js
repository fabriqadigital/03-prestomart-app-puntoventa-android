$(document).ready(function() {
    // ============================================
    // VARIABLES GLOBALES
    // ============================================
    let totalCarrito = 0;
    let cantidadInicialCargada = 1; // Guardar la cantidad inicial al cargar la página

    // ============================================
    // BOTONES AUMENTAR/DISMINUIR CANTIDAD
    // ============================================
    $(document).on('click', '.btn-plus', function() {
        const input = $('#txt_cantidad');
        let valor = parseInt(input.val()) || 1;
        if (valor < 1000) {
            input.val(valor + 1).trigger('change');
            // Llamar inmediatamente para actualizar
            validarEnvioGratis();
        }
    });
    
    $(document).on('click', '.btn-minus', function() {
        const input = $('#txt_cantidad');
        let valor = parseInt(input.val()) || 1;
        if (valor > 1) {
            input.val(valor - 1).trigger('change');
            // Llamar inmediatamente para actualizar
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
            
            // Actualizar envío gratis después de cambiar producto
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

    // // ============================================
    // // ACTUALIZAR DIV DEL HEADER
    // // ============================================
    // function updateDiv() {
    //     $("#idActualizarDivMenuHeader").load(window.location.href + " #idActualizarDivMenuHeader");
    // }

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
        
        // LÓGICA CORREGIDA:
        // 1. Si el carrito está VACÍO → SIEMPRE sumar el producto actual
        // 2. Si el carrito tiene productos Y la cantidad NO cambió → Solo mostrar el carrito
        // 3. Si el carrito tiene productos Y la cantidad SÍ cambió → Sumar producto actual
        
        if (totalCarrito === 0) {
            // Carrito vacío: SIEMPRE mostrar el producto actual
            totalGeneral = precioProductoActual;
        } else if (cantidadActual === cantidadInicialCargada) {
            // Carrito con productos y cantidad sin cambiar: Solo carrito
            totalGeneral = totalCarrito;
        } else {
            // Carrito con productos y cantidad cambió: Sumar producto actual
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
    // Evento cuando cambias la cantidad directamente en el input
    $('#txt_cantidad').on('input change keyup', function() {
        validarEnvioGratis();
    });
    
    // Evento cuando cambias de variación del producto
    $('.btnImagenPrecio').on('click', function() {
        setTimeout(() => {
            // Al cambiar de producto, resetear la cantidad inicial de referencia
            const cantidadInput = document.querySelector('#txt_cantidad');
            if (cantidadInput) {
                cantidadInicialCargada = parseInt(cantidadInput.value) || 1;
            }
            validarEnvioGratis();
        }, 200);
    });

    // Inicializar al cargar la página
    obtenerTotalCarrito().then(() => {
        // Guardar la cantidad inicial al cargar
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
                // Actualizar el total del carrito con la respuesta del servidor
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
                
                // Resetear cantidad a 1 después de agregar
                $('#txt_cantidad').val(1);
                cantidadInicialCargada = 1; // Actualizar la cantidad inicial de referencia
                
                $('.clsDescripcion_SubTotal').text(response.data.result["descripcion_t_subtotal"]);
                $('.clsDescripcion_Igv').text('S/' + response.data.result["descripcion_t_igv"]);
                $('.clsDetalle_Total').text('S/' + response.data.result["descripcion_t_total"]);
                
                updateDiv();
                
                // Revalidar envío gratis con el nuevo total
                // Como reseteamos la cantidad a 1, esto mostrará el total correcto
                validarEnvioGratis();
            }
        })
        .catch(function (error) {
            console.log('Error al agregar producto:', error);
        });
    });

    // // ============================================
    // // ELIMINAR DEL CARRITO
    // // ============================================
    // document.addEventListener('click', function (e) {
    //     const button = e.target.closest('.btn-remove');
    //     if (button) {
    //         e.preventDefault();
    //         const rowId = button.id.replace('idRemoveCart', '');
    //         const formData = new FormData();
    //         formData.append('id_producto', rowId);

    //         fetch('/web_shopDetail_eliminar', {
    //             method: 'POST',
    //             body: formData,
    //             headers: {
    //                 'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute('content'),
    //                 'Accept': 'application/json'
    //             }
    //         })
    //         .then(response => response.json())
    //         .then(data => {
    //             if (data.success) {
    //                 // Actualizar el total del carrito
    //                 if (data.total_carrito !== undefined) {
    //                     totalCarrito = parseFloat(data.total_carrito);
    //                 } else {
    //                     // Si no viene en la respuesta, consultarlo nuevamente
    //                     obtenerTotalCarrito().then(() => {
    //                         validarEnvioGratis();
    //                     });
    //                 }
                    
    //                 updateDiv();
    //                 validarEnvioGratis();
    //             } else {
    //                 alert('Error al eliminar el producto.');
    //             }
    //         })
    //         .catch(error => {
    //             console.error('Error:', error);
    //             alert('Ocurrió un error al procesar la solicitud.');
    //         });
    //     }
    // });

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
            updateShippingResults(response.data);
            toggleLoading(false);
        })
        .catch(function (error) {
            console.error('Error:', error.response ? error.response.data : error);
            showError('Ocurrió un error al calcular el envío');
            toggleLoading(false);
        });
    });

    function updateShippingResults(data) {
        const distrito = data.distrito;
        const address_departamento = data.address_departamento;
        const pago_contra_entrega = data.pago_contra_entrega;
        const hora_regresiva = data.hora_regresiva;
        const hora_regresiva_descripcion = data.hora_regresiva_descripcion;
        const precio_envio = parseFloat(data.precio_envio);
        const contacto_direccion = data.contacto_direccion;

        let html = '';
        html += `${pago_contra_entrega ? `<p class="clsMensajePageAlEntrega mb-1">${pago_contra_entrega}</p>` : ""}`;

        if (hora_regresiva) {
            html += `
            ${hora_regresiva_descripcion ? `<div class="clsComprandoLasProximasHoras"><p>${hora_regresiva_descripcion}</p>` : ""}
             ${hora_regresiva ? `<span id="corte_tiempo_promocion" style="padding-top: 15px;">${hora_regresiva}</span></div>` : ""}
        `;
        } else {
            html += `
            ${hora_regresiva_descripcion ? `<p class="clsMensajeRetireAgencia mb-1">${hora_regresiva_descripcion}</p>` : ""}
        `;
        }

        if (distrito != null) {
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
                        <h3 style="margin-bottom: -5px;">Envío a domicilio <span class="location costoEnvio">a ${distrito} por S/${precio_envio.toFixed(2)}</span>
                        </h3>
                    </div>
                </div>`;
        }
        
        let html_Lugar = '';
        let html_domicilio_empresa = '';

        html_Lugar += ` <div class="pickup-section">
                        <div class="pickup-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none"
                                stroke="currentColor" stroke-width="2">
                                <rect x="2" y="3" width="20" height="14" rx="2" ry="2" />
                                <line x1="8" y1="21" x2="16" y2="21" />
                                <line x1="12" y1="17" x2="12" y2="21" />
                            </svg>
                        </div>
                        <div class="pickup-content">
                            <h3>Recoge y paga el envío en la agencia <strong>Shalom</strong> <span class="location">${address_departamento} </span></h3>
                            <a href="https://agencias.shalom.pe/" class="location-link">Ver ubicaciones y horarios</a>
                        </div>
                    </div>`;
        html_Lugar += ` <div class="pickup-section">
                        <div class="pickup-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none"
                                stroke="currentColor" stroke-width="2">
                                <rect x="2" y="3" width="20" height="14" rx="2" ry="2" />
                                <line x1="8" y1="21" x2="16" y2="21" />
                                <line x1="12" y1="17" x2="12" y2="21" />
                            </svg>
                        </div>
                        <div class="pickup-content">
                            <h3>Recoge y paga el envío en la agencia <strong>Marvisur </strong> <span class="location">${address_departamento} </span></h3>
                            <a href="https://www.expresomarvisur.com/sucursales" class="location-link">Ver ubicaciones y horarios</a>
                        </div>
                    </div>`;

        html_domicilio_empresa += `<div class="shipping-section">
                    <div class="shipping-icon">
                      <svg xmlns="http://www.w3.org/2000/svg" height="25px" viewBox="0 0 24 24" id="map-marker-question"><path fill="#6563FF" d="M12.44,13.11,12.27,13a1,1,0,0,0-1.09.22.87.87,0,0,0-.22.32,1,1,0,0,0-.08.39,1,1,0,0,0,.08.38,1.07,1.07,0,0,0,.54.54,1,1,0,0,0,.38.08,1.09,1.09,0,0,0,.39-.08,1,1,0,0,0,.32-.22,1,1,0,0,0,0-1.41ZM11.88,6A2.75,2.75,0,0,0,9.5,7.32a1,1,0,1,0,1.73,1A.77.77,0,0,1,11.88,8a.75.75,0,1,1,0,1.5,1,1,0,1,0,0,2,2.75,2.75,0,1,0,0-5.5Zm8.58,3.68A8.5,8.5,0,0,0,7.3,3.36,8.56,8.56,0,0,0,3.54,9.63,8.46,8.46,0,0,0,6,16.46l5.3,5.31a1,1,0,0,0,1.42,0L18,16.46A8.46,8.46,0,0,0,20.46,9.63ZM16.6,15.05,12,19.65l-4.6-4.6A6.49,6.49,0,0,1,5.53,9.83,6.57,6.57,0,0,1,8.42,5a6.47,6.47,0,0,1,7.16,0,6.57,6.57,0,0,1,2.89,4.81A6.49,6.49,0,0,1,16.6,15.05Z"></path></svg>
                    </div>
                    <div class="shipping-content">
                        <h3 style="margin-bottom: -5px;">Retiro inmediato en <span class="location costoEnvio"> ${contacto_direccion}</span>
                        </h3>
                    </div>
                </div>`;

        $('.costoEnvio').html(html);

        if (data.hora_regresiva_descripcion !== 'Retírelo por shalom y marvisur') {
            $('.lugarEnvio').html(html_domicilio_empresa);
        } else {
            $('.lugarEnvio').html(html_Lugar);
        }

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

            const countdownBadge = countdownElement.querySelector('.promo-countdown');
            if (countdownBadge) {
                countdownBadge.innerHTML = `${countdownText}`;
            } else {
                countdownElement.innerHTML = `${countdownText}`;
            }
        }

        updateCountdown();
        window.countdownInterval = setInterval(updateCountdown, 30000);
    }

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