<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_cupones', function (Blueprint $table) {
            $table->id();
            $table->string('codigo', 50)->unique();
            $table->enum('tipo', ['porcentaje', 'fijo', 'envio_gratis']);
            $table->decimal('valor', 10, 2);
            $table->decimal('minimo_compra', 10, 2)->nullable();
            $table->decimal('maximo_descuento', 10, 2)->nullable();
            $table->integer('usos_totales')->nullable();
            $table->integer('usos_por_usuario')->default(1);
            $table->integer('usos_actuales')->default(0);
            $table->timestamp('fecha_inicio');
            $table->timestamp('fecha_fin');
            $table->boolean('activo')->default(true);
            $table->timestamps();

            $table->index(['codigo', 'activo']);
            $table->index(['fecha_inicio', 'fecha_fin', 'activo']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_cupones');
    }
};
