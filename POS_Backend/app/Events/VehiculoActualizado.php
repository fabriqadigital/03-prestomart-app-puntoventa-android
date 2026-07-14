<?php

namespace App\Events;

use App\Models\Vehiculo;
use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class VehiculoActualizado implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    public $vehiculo;

    public function __construct(Vehiculo $vehiculo)
    {
        $this->vehiculo = $vehiculo;
    }

    public function broadcastOn()
    {
        return new Channel('vehiculos');
    }

    public function broadcastWith()
    {
        return [
            'id_vehiculo' => $this->vehiculo->id_vehiculo,
            'placa' => $this->vehiculo->placa,
            'mixer' => $this->vehiculo->mixer,
            'Activo' => $this->vehiculo->Activo,
            'created_at' => $this->vehiculo->created_at,
            'updated_at' => $this->vehiculo->updated_at,
            'action' => 'updated'
        ];
    }
}