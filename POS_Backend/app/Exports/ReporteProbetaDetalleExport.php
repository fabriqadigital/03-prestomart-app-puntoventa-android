<?php

namespace App\Exports;

use Maatwebsite\Excel\Concerns\FromCollection;
use Maatwebsite\Excel\Concerns\WithHeadings;
use Maatwebsite\Excel\Concerns\WithMapping;
use Maatwebsite\Excel\Concerns\WithColumnFormatting;
use Maatwebsite\Excel\Concerns\WithStyles;
use PhpOffice\PhpSpreadsheet\Worksheet\Worksheet;
use PhpOffice\PhpSpreadsheet\Style\NumberFormat;
use Illuminate\Support\Facades\Log;
use Carbon\Carbon;
use App\Http\Controllers\Api\CalidadProbetaDetalleController;

class ReporteProbetaDetalleExport implements
    FromCollection,
    WithHeadings,
    WithMapping,
    WithColumnFormatting,
    WithStyles
{
    /**
     * Obtener todos los registros instanciando el controlador
     */
    public function collection()
    {
        try {
            $printReport = new CalidadProbetaDetalleController();
            $response = $printReport->listar();

            // Extraer los datos del JSON response
            $data = $response->getData();

            if ($data->success && !empty($data->result)) {
                return collect($data->result);
            }

            return collect([]);
        } catch (\Exception $e) {
            Log::error('Error al listar probetas para exportación: ' . $e->getMessage());
            return collect([]);
        }
    }

    /**
     * Mapear cada fila de datos
     */
    public function map($row): array
    {
                       // Si tu fecha viene en Y-m-d
            $detalle_fecha_carguio =$row->detalle_fecha_carguio ? \PhpOffice\PhpSpreadsheet\Shared\Date::stringToExcel($row->detalle_fecha_carguio) : null;

                           // Si tu fecha viene en Y-m-d
            $replica_fecha_rotura = $row->replica_fecha_rotura ? \PhpOffice\PhpSpreadsheet\Shared\Date::stringToExcel($row->replica_fecha_rotura) : null;

        return [
            $row->RowIndex ?? '',
            $row->id_calidad_probeta ?? '',
            $row->id_calidad_probeta_detalle ?? '',
            $row->id_testigo ?? '',
            $row->detalle_ruc ?? '',
            $row->detalle_razon_social ?? '',
            $row->detalle_diseno ?? '',
            !empty($row->detalle_fecha_carguio) ?  $detalle_fecha_carguio : '',

            $row->detalle_numero_guia ?? '',
            $row->replica_cantidad_veces ?? '',
            $row->replica_cantidad_dias ?? '',
            !empty($row->replica_fecha_rotura) ?  $replica_fecha_rotura : '',


            $row->m_carga_maxima ?? '',
            $row->m_tipo_falla ?? '',
            $row->m_diametro ?? '',
            $row->m_area_seccion ?? '',
            $row->m_esfuerzo_comprension ?? '',
            isset($row->estado_probeta) ? ($row->estado_probeta === 'E' ? 'Ensayado' : ($row->estado_probeta === 'P' ? 'Por Ensayar' : '')) : '',
            $row->numero_orden ?? '',
            $row->m_resistencia ?? '',
        ];
    }

    /**
     * Encabezados de las columnas
     */
    public function headings(): array
    {
        return [
            'N°',
            'ID Calidad Probeta',
            'ID Probeta Detalle',
            'ID Testigo',
            'RUC',
            'Razón Social',
            'Diseño',
            'Fecha Carguío',
            'N° Guía',
            'Cantidad Veces',
            'Cantidad Días',
            'Fecha Rotura',
            'Carga Máxima',
            'Tipo Falla',
            'Diámetro',
            'Área Sección',
            'Esfuerzo Compresión',
            'Estado Probeta',
            'N° Orden',
            'Resistencia',
        ];
    }

    /**
     * Formato de columnas
     */
    public function columnFormats(): array
    {
        return [
            'H' => NumberFormat::FORMAT_DATE_DDMMYYYY, // Fecha Carguío
            'L' => NumberFormat::FORMAT_DATE_DDMMYYYY, // Fecha Rotura
            'M' => NumberFormat::FORMAT_NUMBER_00, // Carga Máxima
            'O' => NumberFormat::FORMAT_NUMBER_00, // Diámetro
            'P' => NumberFormat::FORMAT_NUMBER_00, // Área Sección
            'Q' => NumberFormat::FORMAT_NUMBER_00, // Esfuerzo Compresión
            'T' => NumberFormat::FORMAT_NUMBER_00, // Resistencia
        ];
    }

    /**
     * Estilos para el encabezado
     */
    public function styles(Worksheet $sheet)
    {
        return [
            1 => [
                'font' => ['bold' => true],
                'fill' => [
                    'fillType' => \PhpOffice\PhpSpreadsheet\Style\Fill::FILL_SOLID,
                    'startColor' => ['rgb' => 'E2E8F0']
                ]
            ],
        ];
    }
}
