<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_categorias', function (Blueprint $table) {
            $table->id();
            $table->string('nombre', 100);
            $table->string('slug', 120)->unique();
            $table->text('descripcion')->nullable();
            $table->string('imagen_url', 500)->nullable();
            $table->unsignedBigInteger('padre_id')->nullable();
            $table->integer('orden')->default(0);
            $table->boolean('activo')->default(true);
            $table->timestamps();

            $table->foreign('padre_id')
                  ->references('id_categoria')
                  ->on('ecommerce_categorias')
                  ->onDelete('set null');

            $table->index(['activo', 'orden']);
            $table->index('padre_id');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_categorias');
    }
};
