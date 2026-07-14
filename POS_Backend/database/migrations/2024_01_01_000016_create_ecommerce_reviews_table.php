<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_reviews', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_producto')->constrained('ecommerce_productos')->onDelete('cascade');
            $table->foreignId('id_cliente')->constrained('ecommerce_clientes')->onDelete('cascade');
            $table->foreignId('id_pedido')->nullable()->constrained('ecommerce_pedidos')->onDelete('set null');
            $table->tinyInteger('rating')->unsigned();
            $table->string('titulo', 200)->nullable();
            $table->text('comentario')->nullable();
            $table->json('imagenes')->nullable();
            $table->boolean('verificado')->default(false);
            $table->boolean('aprobado')->default(false);
            $table->timestamps();

            $table->index(['id_producto', 'aprobado']);
            $table->index(['id_cliente']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_reviews');
    }
};
