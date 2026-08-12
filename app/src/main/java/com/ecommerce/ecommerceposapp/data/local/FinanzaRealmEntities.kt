package com.ecommerce.ecommerceposapp.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/** finanza_cajas */
open class FinanzaCajaRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var nombreCaja: String = ""
    var descripcion: String = ""
    var activo: Boolean = true
    var createdAt: Long = 0
    var updatedAt: Long = 0
}

/** finanza_sesiones_caja */
open class FinanzaSesionCajaRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var idCaja: Long = 0
    var idUsuario: Long = 0
    var fechaApertura: Long = 0
    var montoApertura: Double = 0.0
    var fechaCierre: Long = 0
    var montoCierre: Double = 0.0
    /** A=abierta, C=cerrada */
    var estado: String = "A"
    var observaciones: String = ""
}

/** finanza_arqueos */
open class FinanzaArqueoRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var idSesion: Long = 0
    var idUsuario: Long = 0
    var fechaUtc: Long = 0
    var totalEfectivo: Double = 0.0
    var totalSistema: Double = 0.0
    var diferencia: Double = 0.0
    var observaciones: String = ""
}

/** finanza_ventas */
open class FinanzaVentaRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var numeroComprobante: String = ""
    var tipoComprobante: String = ""
    var idSesion: Long = 0
    var idUsuario: Long = 0
    var idCliente: Long = 0
    var fechaVenta: Long = 0
    var subtotal: Double = 0.0
    var igv: Double = 0.0
    var descuento: Double = 0.0
    var descuentoPorcentaje: Double = 0.0
    var total: Double = 0.0
    var tipoPago: String = ""
    var montoRecibido: Double = 0.0
    var vuelto: Double = 0.0
    /** A=activa, N=anulada */
    var estado: String = "A"
    var motivoAnulacion: String = ""
}

/** finanza_ventas_detalle */
open class FinanzaVentaDetalleRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var idVenta: Long = 0
    var idProducto: Long = 0
    var nombreProducto: String = ""
    var codigoBarras: String = ""
    var cantidad: Double = 0.0
    var precioUnitario: Double = 0.0
    var descuento: Double = 0.0
    var subtotal: Double = 0.0
}

/** finanza_comprobantes_series */
open class FinanzaComprobanteSerieRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var tipoComprobante: String = ""
    var serie: String = ""
    var correlativoActual: Int = 0
    var activo: Boolean = true
}

/** finanza_emisor_config */
open class FinanzaEmisorConfigRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var ruc: String = ""
    var razonSocial: String = ""
    var direccion: String = ""
    var ubigeo: String = "150101"
    var urlOpenInvoice: String = ""
    var proveedorCpeActivo: String = "SMARTPSE"
    var smartpseUsuarioSecundaria: String = ""
    var smartpseTokenAcceso: String = ""
    var smartpseBaseUrl: String = "https://panel.smartpse.pe"
    var usarServicioLocal: Boolean = true
    var ambienteSol: String = "BETA"
    var activo: Boolean = true
}

/** finanza_comprobantes (cabecera SUNAT / local) */
open class FinanzaComprobanteRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var tipoComprobante: String = ""
    var serie: String = ""
    var correlativo: Int = 0
    var numeroCompleto: String = ""
    var idVenta: Long = 0
    var idComprobanteRef: Long = 0
    var serieRef: String = ""
    var correlativoRef: Int = 0
    var tipoNotaCredito: String = ""
    var motivoNota: String = ""
    var emisorRuc: String = ""
    var emisorRazonSocial: String = ""
    var emisorDireccion: String = ""
    var emisorUbigeo: String = ""
    var receptorTipoDoc: String = "0"
    var receptorNumDoc: String = ""
    var receptorRazonSocial: String = ""
    var receptorDireccion: String = ""
    var receptorEmail: String = ""
    var subtotalGravado: Double = 0.0
    var subtotalInafecto: Double = 0.0
    var subtotalExonerado: Double = 0.0
    var totalIgv: Double = 0.0
    var totalDescuentos: Double = 0.0
    var total: Double = 0.0
    var totalLetras: String = ""
    var formaPago: String = "Contado"
    var montoPagado: Double = 0.0
    var estadoSunat: String = "PENDIENTE"
    var codigoRespuesta: String = ""
    var mensajeRespuesta: String = ""
    var hashCdr: String = ""
    var nombreXml: String = ""
    var rutaXml: String = ""
    var rutaCdr: String = ""
    var rutaPdf: String = ""
    var codigoHash: String = ""
    var fechaEmision: String = ""
    var fechaEnvioSunat: Long = 0
    var fechaRespuestaSunat: Long = 0
    var idUsuario: Long = 0
}

/** finanza_comprobantes_detalle */
open class FinanzaComprobanteDetalleRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var idComprobante: Long = 0
    var orden: Int = 0
    var codigoProducto: String = ""
    var descripcion: String = ""
    var unidadMedida: String = "NIU"
    var cantidad: Double = 0.0
    var valorUnitario: Double = 0.0
    var precioUnitario: Double = 0.0
    var descuentoMonto: Double = 0.0
    var subtotalLinea: Double = 0.0
    var igvLinea: Double = 0.0
    var totalLinea: Double = 0.0
    var tipoAfectacionIgv: String = "10"
}

/** finanza_comprobantes_log */
open class FinanzaComprobanteLogRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var idComprobante: Long = 0
    var tipoAccion: String = ""
    var requestJson: String = ""
    var responseJson: String = ""
    var codigoHttp: Int = 0
    var exito: Boolean = false
    var mensajeError: String = ""
    var fecha: Long = 0
}
