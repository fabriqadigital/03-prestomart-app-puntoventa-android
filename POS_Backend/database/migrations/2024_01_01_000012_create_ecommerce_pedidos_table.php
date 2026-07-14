<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_pedidos', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_cliente')->constrained('ecommerce_clientes')->onDelete('cascade');
            $table->string('numero_pedido', 30)->unique();
            $table->enum('estado', ['pendiente', 'procesando', 'enviado', 'entregado', 'cancelado', 'reembolsado'])->default('pendiente');
            $table->json('envio_direccion');
            $table->foreignId('id_metodo_envio')->nullable()->constrained('ecommerce_metodos_envio')->onDelete('set null');
            $table->foreignId('id_metodo_pago')->nullable()->constrained('ecommerce_metodos_pago')->onDelete('set null');
            $table->decimal('subtotal', 10, 2);
            $table->decimal('descuento', 10, 2)->default(0);
            $table->foreignId('codigo_cupon')->nullable()->constrained('ecommerce_cupones')->onDelete('set null');
            $table->decimal('envio', 10, 2)->default(0);
            $table->decimal('impuestos', 10, 2)->default(0);
            $table->decimal('total', 10, 2);
            $table->text('notas')->nullable();
            $table->boolean('pagado')->default(false);
            $table->timestamp('fecha_pago')->nullable();
            $table->string('ip_address', 45)->nullable();
            $table->timestamps();
            $table->softDeletes();

            $table->index(['id_cliente', 'estado']);
            $table->index('numero_pedido');
            $table->index('estado');
            $table->index('created_at');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_pedidos');
    }
};
