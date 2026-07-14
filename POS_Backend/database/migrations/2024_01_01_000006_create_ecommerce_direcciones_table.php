<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_direcciones', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_cliente')->constrained('ecommerce_clientes')->onDelete('cascade');
            $table->enum('tipo', ['casa', 'oficina', 'otro'])->default('casa');
            $table->string('nombre_completo', 150);
            $table->string('telefono', 20)->nullable();
            $table->string('direccion', 255);
            $table->string('direccion_adicional', 255)->nullable();
            $table->string('ciudad', 100);
            $table->string('estado', 100)->nullable();
            $table->string('codigo_postal', 20)->nullable();
            $table->string('pais', 100)->default('Perú');
            $table->decimal('latitud', 10, 8)->nullable();
            $table->decimal('longitud', 11, 8)->nullable();
            $table->boolean('es_principal')->default(false);
            $table->boolean('activo')->default(true);
            $table->timestamps();

            $table->index(['id_cliente', 'es_principal']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_direcciones');
    }
};
