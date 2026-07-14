<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_carrito_items', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_carrito')->constrained('ecommerce_carritos')->onDelete('cascade');
            $table->foreignId('id_producto')->constrained('ecommerce_productos')->onDelete('cascade');
            $table->foreignId('id_variante')->nullable()->constrained('ecommerce_producto_variantes')->onDelete('set null');
            $table->integer('cantidad')->default(1);
            $table->decimal('precio_unitario', 10, 2);
            $table->timestamps();

            $table->unique(['id_carrito', 'id_producto', 'id_variante'], 'carrito_producto_variante_unique');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_carrito_items');
    }
};
