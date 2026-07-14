<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_pedido_historial', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_pedido')->constrained('ecommerce_pedidos')->onDelete('cascade');
            $table->string('estado_anterior', 20)->nullable();
            $table->string('estado_nuevo', 20);
            $table->text('comentario')->nullable();
            $table->string('creado_por', 100)->nullable();
            $table->timestamps();

            $table->index(['id_pedido', 'created_at']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_pedido_historial');
    }
};
