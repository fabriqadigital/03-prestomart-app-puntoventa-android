<aside id="cookies-policy" class="cookies cookies--no-js">
    <div class="cookies__alert">
        <div class="cookies__container">
            <div class="cookies__wrapper">
                <h2 class="cookies__title">Utilizamos cookies</h2>
                <div class="cookies__intro">
                    <p>Utilizamos cookies propias y de terceros para mejorar nuestros servicios y mostrarle publicidad relacionada con sus preferencias mediante el analisis de sus habitos de navegacion.</p>
                    @if(isset($policy) && $policy)
                        <p>Puede obtener mas informacion en nuestra <a href="{{ $policy }}">Politica de Cookies</a>.</p>
                    @endif
                </div>
                <div class="cookies__actions">
                    @cookieconsentbutton(action: 'accept.essentials', label: 'Solo esenciales', attributes: ['class' => 'cookiesBtn cookiesBtn--essentials'])
                    @cookieconsentbutton(action: 'accept.all', label: 'Aceptar todas', attributes: ['class' => 'cookiesBtn cookiesBtn--accept'])
                </div>
            </div>
        </div>
        <a href="#cookies-policy-customize" class="cookies__btn cookies__btn--customize">
            <span>Personalizar</span>
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                <path d="M14.7559 11.9782C15.0814 11.6527 15.0814 11.1251 14.7559 10.7996L10.5893 6.63297C10.433 6.47669 10.221 6.3889 10 6.38889C9.77899 6.38889 9.56703 6.47669 9.41075 6.63297L5.24408 10.7996C4.91864 11.1251 4.91864 11.6527 5.24408 11.9782C5.56951 12.3036 6.09715 12.3036 6.42259 11.9782L10 8.40074L13.5774 11.9782C13.9028 12.3036 14.4305 12.3036 14.7559 11.9782Z" fill="#2C2E30"/>
            </svg>
        </a>
        <div class="cookies__expandable cookies__expandable--custom" id="cookies-policy-customize">
            <form action="{{ route('cookieconsent.accept.configuration') }}" method="post" class="cookies__customize">
                @csrf
                <div class="cookies__sections">
                    @foreach($cookies->getCategories() as $category)
                    <div class="cookies__section">
                        <label for="cookies-policy-check-{{ $category->key() }}" class="cookies__category">
                            @if ($category->key() === 'essentials')
                                <input type="hidden" name="categories[]" value="{{ $category->key() }}" />
                                <input type="checkbox" name="categories[]" value="{{ $category->key() }}" id="cookies-policy-check-{{ $category->key() }}" checked="checked" disabled="disabled" />
                            @else
                                <input type="checkbox" name="categories[]" value="{{ $category->key() }}" id="cookies-policy-check-{{ $category->key() }}" />
                            @endif
                            <span class="cookies__box">
                                <strong class="cookies__label">
                                    @if($category->key() === 'essentials')
                                        Cookies de funcionamiento
                                    @elseif($category->key() === 'analytics')
                                        Cookies de analisis
                                    @elseif($category->key() === 'marketing')
                                        Cookies de marketing
                                    @else
                                        Cookies
                                    @endif
                                </strong>
                            </span>
                            <p class="cookies__info">
                                @if($category->key() === 'essentials')
                                    Necesitamos usar ciertas cookies para que ciertas paginas web funcionen. Por eso no requieren tu consentimiento.
                                @elseif($category->key() === 'analytics')
                                    Permiten analizar el comportamiento de los usuarios para mejorar nuestros servicios.
                                @elseif($category->key() === 'marketing')
                                    Utilizadas para mostrar publicidad personalizada segun tus intereses.
                                @endif
                            </p>
                        </label>

                        <div class="cookies__expandable cookies__details-content" id="cookies-policy-{{ $category->key() }}" style="display: none;">
                            <ul class="cookies__definitions">
                                @foreach($category->getCookies() as $cookie)
                                <li class="cookies__cookie">
                                    <p class="cookies__name"><strong>{{ $cookie->name }}</strong></p>
                                    <p class="cookies__duration">
                                        @php
                                            $minutes = $cookie->duration;
                                            $days = floor($minutes / 1440);
                                            $hours = floor(($minutes % 1440) / 60);
                                            
                                            if ($days >= 365) {
                                                $years = floor($days / 365);
                                                $remainingDays = $days % 365;
                                                $months = floor($remainingDays / 30);
                                                $finalDays = $remainingDays % 30;
                                                
                                                if ($years > 0) echo $years . ' ano' . ($years > 1 ? 's' : '') . ' ';
                                                if ($months > 0) echo $months . ' mes' . ($months > 1 ? 'es' : '') . ' ';
                                                if ($finalDays > 0) echo $finalDays . ' dia' . ($finalDays > 1 ? 's' : '');
                                            } elseif ($days >= 30) {
                                                echo floor($days / 30) . ' mes(es)';
                                            } elseif ($days >= 1) {
                                                echo $days . ' dia(s)';
                                            } elseif ($hours >= 1) {
                                                echo $hours . ' hora(s)';
                                            } else {
                                                echo $minutes . ' minuto(s)';
                                            }
                                        @endphp
                                    </p>
                                    
                                </li>
                                @endforeach
                            </ul>
                        </div>
                      
                    </div>
                    @endforeach
                </div>
                <div class="cookies__save">
                    <button type="submit" class="cookiesBtn__link">Guardar configuracion</button>
                </div>
            </form>
        </div>
    </div>
</aside>

{{-- STYLES & SCRIPT --}}
<script data-cookie-consent>
    {!! file_get_contents(LCC_ROOT . '/dist/script.js') !!}
    
    // Script personalizado para toggle de detalles
    document.addEventListener('DOMContentLoaded', function() {
        const toggleLinks = document.querySelectorAll('.cookies__toggle-details');
        
        toggleLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                const targetId = this.getAttribute('data-target');
                const targetElement = document.getElementById(targetId);
                const showText = this.querySelector('.show-text');
                const hideText = this.querySelector('.hide-text');
                
                if (targetElement.style.display === 'none' || targetElement.style.display === '') {
                    targetElement.style.display = 'block';
                    showText.style.display = 'none';
                    hideText.style.display = 'inline';
                } else {
                    targetElement.style.display = 'none';
                    showText.style.display = 'inline';
                    hideText.style.display = 'none';
                }
            });
        });
    });
</script>
<style data-cookie-consent>
    {!! file_get_contents(LCC_ROOT . '/dist/style.css') !!}
    
    /* Estilos adicionales */
    .cookies__details-content {
        margin-top: 15px;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 8px;
    }
    
    .cookies__definitions {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    
    .cookies__cookie {
        padding: 12px;
        margin-bottom: 10px;
        background: white;
        border-radius: 6px;
        border-left: 3px solid #667eea;
    }
    
    .cookies__cookie:last-child {
        margin-bottom: 0;
    }
    
    .cookies__name {
        margin-bottom: 8px;
        color: #333;
        font-size: 14px;
    }
    
    .cookies__duration {
        margin-bottom: 8px;
        color: #999;
        font-size: 13px;
    }
    
    .cookies__description {
        margin: 0;
        color: #999;
        font-size: 13px;
        line-height: 1.5;
    }
    
    .cookies__toggle-details {
        cursor: pointer;
        color: #667eea;
        font-weight: 500;
        text-decoration: none;
        display: inline-block;
        margin-top: 10px;
        font-size: 14px;
    }
    
    .cookies__toggle-details:hover {
        text-decoration: underline;
    }
</style>