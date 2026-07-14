<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;
use App\Models\Ecommerce\TrackingNotificacion;
use Illuminate\Support\Facades\Log;

class SendTrackingNotifications extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'tracking:send-notifications';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Procesa y envía notificaciones pendientes de tracking';

    public function handle()
    {
        $this->info('Procesando notificaciones pendientes...');

        $pendientes = TrackingNotificacion::pendientes()->get();

        foreach ($pendientes as $n) {
            try {
                // Aquí se implementaría el envío real (email/push/sms/whatsapp).
                // Por ahora lo simulamos escribiendo en logs.
                Log::info("Enviando notificación [id={$n->id}] tipo={$n->tipo} pedido={$n->id_pedido} cliente={$n->id_cliente}");

                // Marcar como enviada
                $n->marcarEnviada();

                $this->info("Notificación enviada id={$n->id}");
            } catch (\Exception $e) {
                Log::error("Error enviando notificación id={$n->id}: " . $e->getMessage());
                $n->marcarError($e->getMessage());
            }
        }

        $this->info('Proceso finalizado.');
        return 0;
    }
}

