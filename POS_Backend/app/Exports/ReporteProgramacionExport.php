<?php

namespace App\Exports;

use App\Models\User;
use Maatwebsite\Excel\Concerns\FromCollection;
use Maatwebsite\Excel\Concerns\WithHeadings;
use App\Collector\Collector;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Maatwebsite\Excel\Concerns\WithColumnFormatting;
use PhpOffice\PhpSpreadsheet\Style\NumberFormat;

class ReporteProgramacionExport implements FromCollection, WithHeadings,WithColumnFormatting
{
    protected $FechaInicio;
    protected $FechaFin;
    protected $Activo;

 function __construct($FechaInicio, $FechaFin, $Activo,$id_programacion,$diseno) {
        $this->FechaInicio = $FechaInicio;
        $this->FechaFin = $FechaFin;
        $this->Activo = $Activo;
        $this->id_programacion = $id_programacion;
        $this->diseno = $diseno;
 }


    /**
    * @return \Illuminate\Support\Collection
    */
    public function collection()
    {
//         Log::channel('stderr')->info($this->FechaInicio);
//    Log::channel('stderr')->info($this->FechaFin);
//    Log::channel('stderr')->info($this->Activo);
        $result = DB::select('CALL USP_ADMINISTRACION_PROGRAMACION_LISTAR_FILTRAR(?,?,?,?,?);', [
            $this->FechaInicio,
            $this->FechaFin,
            $this->Activo,
            $this->id_programacion,
            $this->diseno
        ]);
      
        $outgoingcollection = new Collector();
        foreach($result as $results)
        {
               // Si tu fecha viene en Y-m-d
            $fechaExcel = $results->fecha ? \PhpOffice\PhpSpreadsheet\Shared\Date::stringToExcel($results->fecha) : null;


            // var_dump(get_object_vars($results)["RowIndex"]); 
            $outgoingcollection->push([
                $results->RowIndex,
                $results->id_programacion,
                $fechaExcel, // <-- ahora como número de Excel
                $results->hora,
                $results->dia,
                $results->ubicacion,
                $results->nombre,

                $results->v_guia,
                $results->v_real,
                $results->v_carga,

                $results->nombre_bomba,
                $results->comentario,
                $results->nombre_vendedor,
                $results->comprobante,
                //$results->nombre_cliente,
                $results->razon_social,
                $results->ruc,

            ]);
        }
        return  $outgoingcollection;
    }

   /**
     * @return array
     */
    public function headings(): array
    {
        return [
            'N°',
            'Codigo',
            'Fecha',
            'Hora',
            'Dia',
            'Ubicacion',
            'Nombre',
            'V. Guia',
            'V. Real',
            'V. Carga',

            'Bomba',
            'Comentario',
            'Vendedor',
            'Comprobante',
            'Razon Social',
            'RUC',


        ];
    }

     public function columnFormats(): array
    {
        return [
            'C' => NumberFormat::FORMAT_DATE_DDMMYYYY, // Columna C es la fecha
        ];
    }
}
