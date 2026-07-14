<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_notificaciones', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_cliente')->constrained('ecommerce_clientes')->onDelete('cascade');
            $table->enum('tipo', ['pedido', 'promocion', 'sistema', 'otro'])->default('sistema');
            $table->string('titulo', 200);
            $table->text('mensaje');
            $table->json('datos')->nullable();
            $table->boolean('leido')->default(false);
            $table->timestamps();

            $table->index(['id_cliente', 'leido', 'created_at']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_notificaciones');
    }
};
