<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_carritos', function (Blueprint $table) {
            $table->id();
            $table->foreignId('id_cliente')->nullable()->constrained('ecommerce_clientes')->onDelete('cascade');
            $table->string('session_id', 100)->nullable();
            $table->foreignId('codigo_cupon')->nullable()->constrained('ecommerce_cupones')->onDelete('set null');
            $table->timestamps();

            $table->index('id_cliente');
            $table->index('session_id');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_carritos');
    }
};
