<?php

namespace App\Exports;

use App\Models\User;
use Maatwebsite\Excel\Concerns\FromCollection;
use Maatwebsite\Excel\Concerns\WithHeadings;
use App\Collector\Collector;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use App\Http\Controllers\Api\CarguioController;

class ReporteCarguioExport implements FromCollection, WithHeadings
{
    //Otro: https://stackoverflow.com/questions/30365169/access-controller-method-from-another-controller-in-laravel-5
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

    public function listaCarguio()
    {
        try {
            $printReport = new CarguioController();
            $result = $printReport->listarFiltrar(
                $this->FechaInicio,
                $this->FechaFin,
                $this->Activo,

                $this->id_programacion,
                $this->diseno
            );
            //convertiendo en toArrays
            $result = array_map(function ($value) {
                return (array)$value;
            }, $result);

         } catch (\Throwable $th) {
            //throw $th;
            Log::channel('stderr')->info($th);
        }

        return $result;
    }
    /**
    * @return \Illuminate\Support\Collection
    */
    public function collection()
    {
        $result = $this->listaCarguio();
        $outgoingcollection2 = new Collector();
        
            $mysArraus = array_keys($result[0]);
        
            $prmero2=[];
        foreach($result as $key => $results)
        {
            $prmero1=[];

            foreach($mysArraus as  $results1)
            {             
               $prmero1[]=$result[$key][$results1] ;
            }
            $prmero2[]=$prmero1;
        }
            $outgoingcollection2->push(
                $prmero2
            ); 
            // Log::channel('stderr')->info($outgoingcollection2);
        return  $outgoingcollection2;
    }

   /**
     * @return array
     */
    public function headings(): array
    {
        // $insert = "";
        // foreach ($rowData as $row => $tr) {
        //         $insert .= "('".$tr[0]."','".$tr[0]."','".$tr[0]."','".$tr[0]."'),";
        
        // }
        // $insert = substr($insert, 0, -1);
        // $sql = "Insert into YourTable values ".$insert;

        $result = $this->listaCarguio();
        $outgoingcollection1 = [];

        $my_array = array_diff(array_keys($result[0]), array(
            'RowIndex',"IdPedido","fecha","diseno","hora","volumen","operador","mixer"
            //,"por_cargar","created_at"
            //,"Activo",
        ));
        $outgoing = [
                    'N',
                    'ID_PEDIDO',
                    'FECHA',
                    'HORA',
                    'DISEÑO',

                    'OPERADOR',
                    'MIXER',
                    'VOLUMEN'
                ];
        foreach($my_array  as $results1)
        {
            array_push($outgoingcollection1,
                $results1
            );          
        }
     // Log::channel('stderr')->info(array_merge( $outgoing,$outgoingcollection1));

        return array_merge( $outgoing,$outgoingcollection1);
    }
}
