<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_pedido_items', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_pedido')->constrained('ecommerce_pedidos')->onDelete('cascade');
            $table->foreignId('id_producto')->constrained('ecommerce_productos')->onDelete('cascade');
            $table->foreignId('id_variante')->nullable()->constrained('ecommerce_producto_variantes')->onDelete('set null');
            $table->string('nombre_producto', 200);
            $table->string('sku', 50);
            $table->integer('cantidad');
            $table->decimal('precio_unitario', 10, 2);
            $table->decimal('subtotal', 10, 2);
            $table->timestamps();

            $table->index('id_pedido');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_pedido_items');
    }
};
