<style>
.bg-surface {
    --tw-bg-opacity: 1;
    background-color: rgb(0 0 0);
    color: white;
}
.text-secondary {
    --tw-text-opacity: 1;
    color: rgb(255 255 255);
}
</style>
    
    <div id="top-nav" class="top-nav style-one bg-black md:h-[44px] h-[30px]" style="background: #6b4aae;">
        <div class="container mx-auto h-full">
            <div class="top-nav-main flex justify-between max-md:justify-center h-full">
                <div class="left-content flex items-center gap-5 max-md:hidden">
                    <div class="choose-type choose-language flex items-center gap-1.5">
                        <div class="select relative">
                            <p class="selected caption2 text-white">English</p>
                            <ul class="list-option bg-white">
                                <li data-item="English" class="caption2 active">English</li>
                                <li data-item="Espana" class="caption2">Espana</li>
                                <li data-item="France" class="caption2">France</li>
                            </ul>
                        </div>
                        <i class="ph ph-caret-down text-xs text-white"></i>
                    </div>
                    <div class="choose-type choose-currency flex items-center gap-1.5">
                        <div class="select relative">
                            <p class="selected caption2 text-white">USD</p>
                            <ul class="list-option bg-white">
                                <li data-item="USD" class="caption2 active">USD</li>
                                <li data-item="EUR" class="caption2">EUR</li>
                                <li data-item="GBP" class="caption2">GBP</li>
                            </ul>
                        </div>
                        <i class="ph ph-caret-down text-xs text-white"></i>
                    </div>
                </div>
                <div class="text-center text-button-uppercase text-white flex items-center">Los nuevos clientes ahorran 10% con el código GET10</div>

                <div class="right-content flex items-center gap-5 max-md:hidden">
                    <a href="https://www.facebook.com/" target="_blank">
                        <i class="icon-facebook text-white"></i>
                    </a>
                    <a href="https://www.instagram.com/" target="_blank">
                        <i class="icon-instagram text-white"></i>
                    </a>
                    <a href="https://www.youtube.com/" target="_blank">
                        <i class="icon-youtube text-white"></i>
                    </a>
                    <a href="https://twitter.com/" target="_blank">
                        <i class="icon-twitter text-white"></i>
                    </a>
                    <a href="https://pinterest.com/" target="_blank">
                        <i class="icon-pinterest text-white"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div id="header" class="relative w-full">
        <div class="header-menu style-one absolute top-0 left-0 right-0 w-full md:h-[74px] h-[56px] bg-transparent">
            <div class="container mx-auto h-full">
                <div class="header-main flex justify-between h-full">
                    <div class="menu-mobile-icon lg:hidden flex items-center">
                        <i class="icon-category text-2xl"></i>
                    </div>
                    <div class="left flex items-center gap-16">
                        <a href="index.html" class="flex items-center max-lg:absolute max-lg:left-1/2 max-lg:-translate-x-1/2">
                            <div class="heading4">Anvogue</div>
                        </a>
                        <div class="menu-main h-full max-lg:hidden">
                            <ul class="flex items-center gap-8 h-full">
                                <li class="h-full relative">
                                    <a href="#!" class="text-button-uppercase duration-300 h-full flex items-center justify-center gap-1"> Inicio </a>
                                </li>
                                <li class="h-full">
                                    <a href="#!" class="text-button-uppercase duration-300 h-full flex items-center justify-center"> Quienes Somos </a>
                                </li>
                                <li class="h-full">
                                    <a href="#!" class="text-button-uppercase duration-300 h-full flex items-center justify-center"> Ofertas </a>
                                </li>
                                <li class="h-full">
                                    <a href="#!" class="text-button-uppercase duration-300 h-full flex items-center justify-center"> Productos </a>
                                </li>
                                <li class="h-full relative">
                                    <a href="#!" class="text-button-uppercase duration-300 h-full flex items-center justify-center"> Blog </a>
                                </li>
                                <li class="h-full relative">
                                    <a href="#!" class="text-button-uppercase duration-300 h-full flex items-center justify-center active"> Beneficios </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="right flex gap-12">
                        <div class="max-md:hidden search-icon flex items-center cursor-pointer relative">
                            <i class="ph-bold ph-magnifying-glass text-2xl"></i>
                            <div class="line absolute bg-line w-px h-6 -right-6"></div>
                        </div>
                        <div class="list-action flex items-center gap-4">
                            <div class="user-icon flex items-center justify-center cursor-pointer">
                                <i class="ph-bold ph-user text-2xl"></i>
                                <div class="login-popup absolute top-[74px] w-[320px] p-7 rounded-xl bg-white box-shadow-sm">
                                    <a href="login.html" class="button-main w-full text-center">Login</a>
                                    <div class="text-secondary text-center mt-3 pb-4">
                                        Don’t have an account?
                                        <a href="register.html" class="text-black pl-1 hover:underline">Register </a>
                                    </div>
                                    <a href="my-account.html" class="button-main bg-white text-black border border-black w-full text-center">Dashboard</a>
                                    <div class="bottom mt-4 pt-4 border-t border-line"></div>
                                    <a href="#!" class="body1 hover:underline">Support</a>
                                </div>
                            </div>
                            <div class="max-md:hidden wishlist-icon flex items-center relative cursor-pointer">
                                <i class="ph-bold ph-heart text-2xl"></i>
                                <span class="quantity wishlist-quantity absolute -right-1.5 -top-1.5 text-xs text-white bg-black w-4 h-4 flex items-center justify-center rounded-full">0</span>
                            </div>
                            <div class="max-md:hidden cart-icon flex items-center relative cursor-pointer">
                                <i class="ph-bold ph-handbag text-2xl"></i>
                                <span class="quantity cart-quantity absolute -right-1.5 -top-1.5 text-xs text-white bg-black w-4 h-4 flex items-center justify-center rounded-full">0</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Menu Mobile -->
        <div id="menu-mobile" class="">
            <div class="menu-container bg-white h-full">
                <div class="container h-full">
                    <div class="menu-main h-full overflow-hidden">
                        <div class="heading py-2 relative flex items-center justify-center">
                            <div class="close-menu-mobile-btn absolute left-0 top-1/2 -translate-y-1/2 w-6 h-6 rounded-full bg-surface flex items-center justify-center">
                                <i class="ph ph-x text-sm"></i>
                            </div>
                            <a href="index.html" class="logo text-3xl font-semibold text-center">Anvogue</a>
                        </div>
                        <div class="form-search relative mt-2">
                            <i class="ph ph-magnifying-glass text-xl absolute left-3 top-1/2 -translate-y-1/2 cursor-pointer"></i>
                            <input type="text" placeholder="What are you looking for?" class="h-12 rounded-lg border border-line text-sm w-full pl-10 pr-4" />
                        </div>
                        <div class="list-nav mt-6">
                            <ul>
                                <li>
                                    <a href="#!" class="text-xl font-semibold flex items-center justify-between">Inicio
                                        <span class="text-right">
                                            <i class="ph ph-caret-right text-xl"></i>
                                        </span>
                                    </a>
                                    <div class="sub-nav-mobile">
                                        <div class="back-btn flex items-center gap-3">
                                            <i class="ph ph-caret-left text-xl"></i>
                                            Back
                                        </div>
                                        <div class="list-nav-item w-full grid grid-cols-2 pt-2 pb-6">
                                            <ul>
                                                <li>
                                                    <a href="index-2.html" class="nav-item-mobile link text-secondary duration-300"> Home Fashion 1 </a>
                                                </li>
                                                <li>
                                                    <a href="fashion2.html" class="nav-item-mobile link text-secondary duration-300"> Home Fashion 2 </a>
                                                </li>
                                                <li>
                                                    <a href="fashion3.html" class="nav-item-mobile link text-secondary duration-300"> Home Fashion 3 </a>
                                                </li>

                                            </ul>
                                            <ul>
                                                <li>
                                                    <a href="underwear.html" class="nav-item-mobile link text-secondary duration-300"> Home Underwear </a>
                                                </li>
                                                <li>
                                                    <a href="cosmetic1.html" class="nav-item-mobile link text-secondary duration-300"> Home Cosmetic 1 </a>
                                                </li>
                                                <li>
                                                    <a href="cosmetic2.html" class="nav-item-mobile link text-secondary duration-300"> Home Cosmetic 2 </a>
                                                </li>

                                            </ul>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <a href="#!" class="text-xl font-semibold flex items-center justify-between mt-5">Features
                                        <span class="text-right">
                                            <i class="ph ph-caret-right text-xl"></i>
                                        </span>
                                    </a>
                                    <div class="sub-nav-mobile">
                                        <div class="back-btn flex items-center gap-3">
                                            <i class="ph ph-caret-left text-xl"></i>
                                            Back
                                        </div>
                                        <div class="list-nav-item w-full pt-2 pb-6">
                                            <div class="nav-link grid grid-cols-2 gap-5 gap-y-6">
                                                <div class="nav-item">
                                                    <div class="text-button-uppercase pb-1">For Men</div>
                                                    <ul>
                                                        <li>
                                                            <a href="shop-breadcrumb1.html" class="link text-secondary duration-300 cursor-pointer"> Starting From 50% Off </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-breadcrumb1.html" class="link text-secondary duration-300 cursor-pointer"> Outerwear | Coats </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-breadcrumb1.html" class="link text-secondary duration-300 cursor-pointer"> Sweaters | Cardigans </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-breadcrumb1.html" class="link text-secondary duration-300 cursor-pointer"> Shirt | Sweatshirts </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-breadcrumb1.html" class="link text-secondary duration-300 view-all-btn"> View All </a>
                                                        </li>
                                                    </ul>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <a href="#!" class="text-xl font-semibold flex items-center justify-between mt-5">Shop
                                        <span class="text-right">
                                            <i class="ph ph-caret-right text-xl"></i>
                                        </span>
                                    </a>
                                    <div class="sub-nav-mobile">
                                        <div class="back-btn flex items-center gap-3">
                                            <i class="ph ph-caret-left text-xl"></i>
                                            Back
                                        </div>
                                        <div class="list-nav-item w-full pt-2 pb-6">
                                            <div class="nav-link grid grid-cols-2 gap-5 gap-y-6 justify-between">
                                                <div class="nav-item">
                                                    <div class="text-button-uppercase pb-1">Shop Features</div>
                                                    <ul>
                                                        <li>
                                                            <a href="shop-breadcrumb-img.html" class="link text-secondary duration-300"> Shop Breadcrumb IMG </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-breadcrumb1.html" class="link text-secondary duration-300"> Shop Breadcrumb 1 </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-breadcrumb2.html" class="link text-secondary duration-300"> Shop Breadcrumb 2 </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-collection.html" class="link text-secondary duration-300"> Shop Collection </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                                <div class="nav-item">
                                                    <div class="text-button-uppercase pb-1">Shop Features</div>
                                                    <ul>
                                                        <li>
                                                            <a href="shop-filter-canvas.html" class="link text-secondary duration-300"> Shop Filter Canvas </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-filter-options.html" class="link text-secondary duration-300"> Shop Filter Options </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-filter-dropdown.html" class="link text-secondary duration-300"> Shop Filter Dropdown </a>
                                                        </li>
                                                        <li>
                                                            <a href="shop-sidebar-list.html" class="link text-secondary duration-300"> Shop Sidebar List </a>
                                                        </li>
                                                    </ul>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <a href="#!" class="text-xl font-semibold flex items-center justify-between mt-5">Product
                                        <span class="text-right">
                                            <i class="ph ph-caret-right text-xl"></i>
                                        </span>
                                    </a>
                                    <div class="sub-nav-mobile">
                                        <div class="back-btn flex items-center gap-3">
                                            <i class="ph ph-caret-left text-xl"></i>
                                            Back
                                        </div>
                                        <div class="list-nav-item w-full pt-2 pb-6">
                                            <div class="nav-link grid grid-cols-2 gap-5 gap-y-6 justify-between">
                                                <div class="nav-item">
                                                    <div class="text-button-uppercase pb-1">Products Features</div>
                                                    <ul>
                                                        <li>
                                                            <a href="product-default.html" class="link text-secondary duration-300"> Products Defaults </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-sale.html" class="link text-secondary duration-300"> Products Sale </a>
                                                        </li>

                                                    </ul>
                                                </div>
                                                <div class="nav-item">
                                                    <div class="text-button-uppercase pb-1">Products Features</div>
                                                    <ul>
                                                        <li>
                                                            <a href="product-external.html" class="link text-secondary duration-300"> Products External </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-on-sale.html" class="link text-secondary duration-300"> Products On Sale </a>
                                                        </li>

                                                    </ul>
                                                </div>
                                                <div class="nav-item">
                                                    <div class="text-button-uppercase pb-1">Products Layout</div>
                                                    <ul>
                                                        <li>
                                                            <a href="product-thumbnail-left.html" class="link text-secondary duration-300 cursor-pointer"> Products Thumbnails Left </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-thumbnail-bottom.html" class="link text-secondary duration-300 cursor-pointer"> Products Thumbnails Bottom </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-one-scrolling.html" class="link text-secondary duration-300 cursor-pointer"> Products Grid 1 Scrolling </a>
                                                        </li>

                                                    </ul>
                                                </div>
                                                <div class="nav-item">
                                                    <div class="text-button-uppercase pb-1">Products Styles</div>
                                                    <ul>
                                                        <li>
                                                            <a href="product-style1.html" class="link text-secondary duration-300 cursor-pointer"> Products Style 01 </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-style2.html" class="link text-secondary duration-300 cursor-pointer"> Products Style 02 </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-style3.html" class="link text-secondary duration-300 cursor-pointer"> Products Style 03 </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-style4.html" class="link text-secondary duration-300 cursor-pointer"> Products Style 04 </a>
                                                        </li>
                                                        <li>
                                                            <a href="product-style5.html" class="link text-secondary duration-300 cursor-pointer"> Products Style 05 </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <a href="#!" class="text-xl font-semibold flex items-center justify-between mt-5">Blog
                                        <span class="text-right">
                                            <i class="ph ph-caret-right text-xl"></i>
                                        </span>
                                    </a>
                                    <div class="sub-nav-mobile">
                                        <div class="back-btn flex items-center gap-3">
                                            <i class="ph ph-caret-left text-xl"></i>
                                            Back
                                        </div>
                                        <div class="list-nav-item w-full pt-2 pb-6">
                                            <ul class="w-full">
                                                <li>
                                                    <a href="blog-default.html" class="link text-secondary duration-300"> Blog Default </a>
                                                </li>
                                                <li>
                                                    <a href="blog-list.html" class="link text-secondary duration-300"> Blog List </a>
                                                </li>
                                                <li>
                                                    <a href="blog-grid.html" class="link text-secondary duration-300"> Blog Grid </a>
                                                </li>
                                                <li>
                                                    <a href="blog-detail1.html" class="link text-secondary duration-300"> Blog Detail 1 </a>
                                                </li>
                                                <li>
                                                    <a href="blog-detail2.html" class="link text-secondary duration-300"> Blog Detail 2 </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <a href="#!" class="text-xl font-semibold flex items-center justify-between mt-5">Pages
                                        <span class="text-right">
                                            <i class="ph ph-caret-right text-xl"></i>
                                        </span>
                                    </a>
                                    <div class="sub-nav-mobile">
                                        <div class="back-btn flex items-center gap-3">
                                            <i class="ph ph-caret-left text-xl"></i>
                                            Back
                                        </div>
                                        <div class="list-nav-item w-full pt-2 pb-6">
                                            <ul class="w-full">
                                                <li>
                                                    <a href="about.html" class="link text-secondary duration-300"> About Us </a>
                                                </li>
                                                <li>
                                                    <a href="contact.html" class="link text-secondary duration-300"> Contact Us </a>
                                                </li>
                                                <li>
                                                    <a href="store-list.html" class="link text-secondary duration-300"> Store List </a>
                                                </li>

                                            </ul>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>