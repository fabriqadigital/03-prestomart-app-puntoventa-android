package com.ecommerce.ecommerceposapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat

/**
 * Integración con el escáner interno de dispositivos Zebra mediante DataWedge.
 *
 * Flujo:
 *   Botón físico SCAN → DataWedge (app de sistema com.symbol.datawedge)
 *   → Broadcast Intent con acción [SCAN_ACTION]
 *   → BroadcastReceiver registrado en PosScreen
 *   → código reutilizado por la MISMA lógica de procesamiento existente
 *     (tryProcessScan en CatalogPane: busca producto y lo agrega al carrito).
 *
 * La app SOLO escucha el broadcast; la configuración del perfil (simbiologías,
 * salida por Intent, teclado desactivado, etc.) se hace en la app DataWedge del
 * dispositivo o, de forma automática y best-effort, con [setupProfile].
 */
object DataWedgeScanner {

    private const val TAG = "DataWedge"
    const val DATAWEDGE_PACKAGE = "com.symbol.datawedge"
    const val SCAN_ACTION = "com.symbol.datawedge.scan"
    const val EXTRA_DATA_STRING = "com.symbol.datawedge.data_string"
    const val EXTRA_LABEL_TYPE = "com.symbol.datawedge.label_type"
    const val EXTRA_SOURCE = "com.symbol.datawedge.source"
    private const val API_ACTION = "com.symbol.datawedge.api.ACTION"
    private const val API_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG"
    private const val API_RESULT_ACTION = "com.symbol.datawedge.api.RESULT_ACTION"
    private const val API_RESULT_CATEGORY = "android.intent.category.DEFAULT"
    const val PROFILE_NAME = "PrestoMartPOS"
    const val DATAWEDGE_DEDUP_MS = 2000L
    data class DataWedgeScan(val sequence: Int, val code: String)

    fun isDataWedgeInstalled(context: Context): Boolean = runCatching {
        context.packageManager.getPackageInfo(DATAWEDGE_PACKAGE, 0)
    }.isSuccess

    fun extractBarcode(intent: Intent?): String? {
        if (intent == null || intent.action != SCAN_ACTION) return null
        val rawData = intent.getStringExtra(EXTRA_DATA_STRING)?.trim() ?: return null
        val labelType = intent.getStringExtra(EXTRA_LABEL_TYPE).orEmpty()
        val normalizedLabel = labelType.replace("-", "").replace("_", "").uppercase()
        val data = if (normalizedLabel.contains("UPCA") && rawData.length == 12) {
            val restored = "0$rawData"
            Log.d(
                "BarcodeDebug",
                "DataWedge UPC-A (12 dígitos): [$rawData] labelType=$labelType → 0 inicial restaurado: [$restored] (length=${restored.length})",
            )
            restored
        } else {
            rawData
        }
        Log.d(
            "BarcodeDebug",
            "DataWedge recibido: [$data] (length=${data.length}) labelType=$labelType",
        )
        return data.ifBlank { null }
    }

    fun setupProfile(context: Context) {
        if (!isDataWedgeInstalled(context)) {
            Log.d(TAG, "DataWedge no está instalado; se omite la configuración de perfil.")
            return
        }
        Log.d(TAG, "DataWedge instalado. Configurando perfil '$PROFILE_NAME'...")
        runCatching {
            val barcodeConfig = Bundle().apply {
                putString("PLUGIN_NAME", "BARCODE")
                putString("RESET_CONFIG", "true")
                putBundle(
                    "PARAM_LIST",
                    Bundle().apply {
                        putString("scanner_selection", "auto")
                        putString("scanner_input_enabled", "true")
                        putString("decoder_ean13", "true")
                        putString("decoder_ean8", "true")
                        putString("decoder_upca", "true")
                        putString("decoder_upce0", "true")
                        putString("decoder_code128", "true")
                        putString("decoder_qrcode", "true")
                    },
                )
            }
            val intentConfig = Bundle().apply {
                putString("PLUGIN_NAME", "INTENT")
                putString("RESET_CONFIG", "true")
                putBundle(
                    "PARAM_LIST",
                    Bundle().apply {
                        putString("intent_output_enabled", "true")
                        putString("intent_action", SCAN_ACTION)
                        putString("intent_category", API_RESULT_CATEGORY)
                        putString("intent_delivery", "2") // 2 = Broadcast intent
                    },
                )
            }
            val keystrokeConfig = Bundle().apply {
                putString("PLUGIN_NAME", "KEYSTROKE")
                putString("RESET_CONFIG", "true")
                putBundle(
                    "PARAM_LIST",
                    Bundle().apply {
                        putString("keystroke_output_enabled", "false")
                    },
                )
            }

            val profileConfig = Bundle().apply {
                putString("PROFILE_NAME", PROFILE_NAME)
                putString("PROFILE_ENABLED", "true")
                putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST")
                putParcelableArrayList(
                    "PLUGIN_CONFIG",
                    arrayListOf(barcodeConfig, intentConfig, keystrokeConfig),
                )

                // APP_LIST como Parcelable[] (array de Bundles), NUNCA un Bundle.
                val appListEntry = Bundle().apply {
                    putString("PACKAGE_NAME", context.packageName)
                    putStringArray("ACTIVITY_LIST", arrayOf("*"))
                }
                putParcelableArray("APP_LIST", arrayOf(appListEntry))
            }
            val resultReceiver = object : BroadcastReceiver() {
                override fun onReceive(ctx: Context?, intent: Intent?) {
                    Log.d(
                        TAG,
                        "RESULT SET_CONFIG: result=${intent?.getStringExtra("RESULT")} " +
                            "command=${intent?.getStringExtra("COMMAND")} " +
                            "cid=${intent?.getStringExtra("COMMAND_IDENTIFIER")}",
                    )
                    runCatching { ctx?.unregisterReceiver(this) }
                }
            }
            ContextCompat.registerReceiver(
                context,
                resultReceiver,
                IntentFilter(API_RESULT_ACTION).apply { addCategory(API_RESULT_CATEGORY) },
                ContextCompat.RECEIVER_EXPORTED,
            )
            Handler(Looper.getMainLooper()).postDelayed({
                runCatching { context.unregisterReceiver(resultReceiver) }
            }, 3000)

            context.sendBroadcast(
                Intent(API_ACTION).setPackage(DATAWEDGE_PACKAGE)
                    .putExtra(API_SET_CONFIG, profileConfig)
                    // SEND_RESULT y COMMAND_IDENTIFIER van como extras del Intent.
                    .putExtra("SEND_RESULT", "true")
                    .putExtra("COMMAND_IDENTIFIER", "SETUP_PRESTO_POS"),
            )
            Log.d(TAG, "SET_CONFIG enviado (CREATE_IF_NOT_EXIST). Esperando RESULT...")
        }.onFailure { error ->
            Log.w(TAG, "No se pudo configurar el perfil de DataWedge: ${error.message}", error)
        }
    }
}
