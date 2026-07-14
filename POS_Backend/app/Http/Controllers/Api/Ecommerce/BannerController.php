<?php

namespace App\Http\Controllers\Api\Ecommerce;

use App\Http\Controllers\Controller;
use App\Models\Ecommerce\Banner;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class BannerController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $query = Banner::activos()->ordenados();

        if ($request->has('posicion')) {
            $query->where('posicion', $request->posicion);
        }

        $banners = $query->get();

        return response()->json([
            'success' => true,
            'data' => $banners->map(function ($banner) {
                return $this->formatBanner($banner);
            })
        ]);
    }

    public function porPosicion($posicion): JsonResponse
    {
        $banners = Banner::activos()
                         ->where('posicion', $posicion)
                         ->ordenados()
                         ->get();

        return response()->json([
            'success' => true,
            'data' => $banners->map(function ($banner) {
                return $this->formatBanner($banner);
            })
        ]);
    }

    public function home(): JsonResponse
    {
        $hero = Banner::activos()
                      ->where('posicion', 'home_hero')
                      ->ordenados()
                      ->get();

        $secondary = Banner::activos()
                           ->where('posicion', 'home_secondary')
                           ->ordenados()
                           ->get();

        $banners = Banner::activos()
                         ->where('posicion', 'home_banner')
                         ->ordenados()
                         ->get();

        return response()->json([
            'success' => true,
            'data' => [
                'hero' => $hero->map(fn($b) => $this->formatBanner($b)),
                'secondary' => $secondary->map(fn($b) => $this->formatBanner($b)),
                'banners' => $banners->map(fn($b) => $this->formatBanner($b)),
            ]
        ]);
    }

    private function formatBanner($banner): array
    {
        return [
            'id' => $banner->id,
            'titulo' => $banner->titulo,
            'subtitulo' => $banner->subtitulo,
            'imagen_url' => $banner->imagen_url,
            'enlace' => $banner->enlace,
            'posicion' => $banner->posicion,
        ];
    }
}
