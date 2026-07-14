<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_wishlist', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_cliente')->constrained('ecommerce_clientes')->onDelete('cascade');
            $table->foreignId('id_producto')->constrained('ecommerce_productos')->onDelete('cascade');
            $table->timestamps();

            $table->unique(['id_cliente', 'id_producto']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_wishlist');
    }
};
