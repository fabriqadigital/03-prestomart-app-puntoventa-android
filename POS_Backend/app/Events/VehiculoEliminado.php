<?php

namespace App\Events;

use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class VehiculoEliminado implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    public $vehiculoData;

    public function __construct(array $vehiculoData)
    {
        $this->vehiculoData = $vehiculoData;
    }

    public function broadcastOn()
    {
        return new Channel('vehiculos');
    }

    public function broadcastWith()
    {
        return [
            'id_vehiculo' => $this->vehiculoData['id_vehiculo'],
            'placa' => $this->vehiculoData['placa'],
            'mixer' => $this->vehiculoData['mixer'],
            'Activo' => $this->vehiculoData['Activo'],
            'action' => 'deleted'
        ];
    }
}