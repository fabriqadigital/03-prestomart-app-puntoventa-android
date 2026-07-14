<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_producto_variantes', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_producto')->constrained('ecommerce_productos')->onDelete('cascade');
            $table->string('nombre', 100);
            $table->string('sku', 50)->unique();
            $table->decimal('precio_adicional', 10, 2)->default(0);
            $table->integer('stock')->default(0);
            $table->json('atributos')->nullable();
            $table->boolean('activo')->default(true);
            $table->timestamps();

            $table->index(['id_producto', 'activo']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_producto_variantes');
    }
};
