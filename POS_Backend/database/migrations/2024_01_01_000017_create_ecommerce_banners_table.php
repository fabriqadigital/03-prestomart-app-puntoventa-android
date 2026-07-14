<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_banners', function (Blueprint $table) {
            $table->id();
            $table->string('titulo', 200)->nullable();
            $table->string('subtitulo', 300)->nullable();
            $table->string('imagen_url', 500);
            $table->string('enlace', 500)->nullable();
            $table->string('posicion', 50)->default('home_hero');
            $table->integer('orden')->default(0);
            $table->timestamp('fecha_inicio')->nullable();
            $table->timestamp('fecha_fin')->nullable();
            $table->boolean('activo')->default(true);
            $table->timestamps();

            $table->index(['posicion', 'activo', 'orden']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_banners');
    }
};
