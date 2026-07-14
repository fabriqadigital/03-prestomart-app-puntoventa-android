<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('ecommerce_productos', function (Blueprint $table) {
            $table->id();
            $table->foreignId('categoria_id')->constrained('ecommerce_categorias')->onDelete('cascade');
            $table->foreignId('marca_id')->nullable()->constrained('ecommerce_marcas')->onDelete('set null');
            $table->string('nombre', 200);
            $table->string('slug', 220)->unique();
            $table->text('descripcion')->nullable();
            $table->string('descripcion_corta', 500)->nullable();
            $table->decimal('precio', 10, 2);
            $table->decimal('precio_oferta', 10, 2)->nullable();
            $table->decimal('costo', 10, 2)->nullable();
            $table->string('sku', 50)->unique();
            $table->integer('stock')->default(0);
            $table->integer('stock_minimo')->default(0);
            $table->decimal('peso', 8, 3)->nullable();
            $table->string('dimensiones', 50)->nullable();
            $table->string('imagen_principal', 500)->nullable();
            $table->json('imagenes')->nullable();
            $table->json('especificaciones')->nullable();
            $table->boolean('destacado')->default(false);
            $table->boolean('nuevo')->default(false);
            $table->boolean('activo')->default(true);
            $table->timestamps();
            $table->softDeletes();

            $table->index(['activo', 'destacado']);
            $table->index(['activo', 'nuevo']);
            $table->index('categoria_id');
            $table->index('marca_id');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('ecommerce_productos');
    }
};
