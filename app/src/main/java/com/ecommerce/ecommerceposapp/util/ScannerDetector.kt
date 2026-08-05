package com.ecommerce.ecommerceposapp.util

import android.content.Context
import android.hardware.input.InputManager
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbManager
import android.util.Log
import android.view.InputDevice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Detecta si hay un escáner físico conectado (USB o Bluetooth en modo HID).
 *
 * Un escáner de barras se comporta como un teclado, así que se identifica por
 * TRES vías independientes:
 *
 *  1) InputDevice: los teclados HID (USB/Bluetooth) que Android enumera como
 *     dispositivos de entrada. Se excluyen el teclado virtual y los periféricos
 *     integrados del propio dispositivo (botones de volumen, headset jack,
 *     touchscreen, gpio-keys, etc.).
 *
 *  2) UsbManager: algunos escáneres conectados por USB (host/OTG) NO se
 *     enumeran como InputDevice en ciertos dispositivos (p. ej. Zebra TC26 con
 *     Android 10), pero SÍ aparecen como dispositivo USB con interfaz HID
 *     (clase 3). En modo teclado traen subclass=1 (boot) y protocol=1
 *     (keyboard). Se excluyen los vendors internos del SoC y se da prioridad a
 *     los vendor IDs de Symbol/Zebra (0x05E0, 0x064D, 0x0681...).
 *
 *  3) DataWedge instalado: en dispositivos Zebra el lector es HARDWARE
 *     INTEGRADO: no se enumera como InputDevice NI aparece en el bus USB
 *     (confirmado por Logcat en TC26 Android 10). Su única interfaz es la app
 *     de sistema DataWedge (com.symbol.datawedge). Si DataWedge está instalado,
 *     el dispositivo tiene lector físico y el escáner funciona vía broadcast.
 */
object ScannerDetector {

    private const val TAG = "ScannerDetector"

    /** Vendor IDs USB de Symbol Technologies / Zebra Technologies (escáneres). */
    private val ZEBRA_VENDOR_IDS = setOf(
        0x05E0, // Symbol Technologies (escáneres clásicos y Zebra actuales)
        0x064D, // Symbol Technologies
        0x0624, // Symbol Technologies
        0x0681, // Zebra Technologies (modelos nuevos)
        0x05F9, // Symbol/Zebra
        0x0525, // NetChip (adaptadores USB-serial usados en productos Symbol/Zebra)
    )

    /** Vendor IDs de periféricos integrados del dispositivo (NUNCA son escáner). */
    private val INTERNAL_VENDOR_IDS = setOf(
        0x05C6, // Qualcomm (modem/SoC)
        0x2C7C, // Qualcomm
        0x0A5C, // Broadcom
        0x18D1, // Google
        0x04E8, // Samsung
        0x12D1, // Huawei
        0x10A9, // Skyworks
        0x0000,
    )

    fun isPhysicalScannerConnected(context: Context): Boolean {
        // 1) Detección por InputDevice (teclados HID reales enumerados por Android).
        val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager
        val inputBased = InputDevice.getDeviceIds().any { id ->
            val device = inputManager.getInputDevice(id) ?: return@any false
            val isKeyboardLike = device.sources and InputDevice.SOURCE_KEYBOARD == InputDevice.SOURCE_KEYBOARD
            val isVirtual = device.isVirtual
            val isFullKeyboard = device.keyboardType == InputDevice.KEYBOARD_TYPE_ALPHABETIC
            val isZebraVendor = device.vendorId in ZEBRA_VENDOR_IDS
            Log.d(
                TAG,
                "Device: ${device.name} | virtual=$isVirtual | keyboardLike=$isKeyboardLike | " +
                    "fullKeyboard=$isFullKeyboard | vendorId=${device.vendorId} | zebraVendor=$isZebraVendor",
            )
            // Escáner válido: teclado-like, NO virtual, y teclado completo o vendor Zebra.
            isKeyboardLike && !isVirtual && (isFullKeyboard || isZebraVendor)
        }
        if (inputBased) {
            Log.d(TAG, "Resultado final: true (InputDevice)")
            return true
        }

        // 2) Detección por USB: escáner que NO se enumera como InputDevice pero
        //    aparece como dispositivo USB con interfaz HID.
        val usbBased = hasUsbHidScanner(context)
        if (usbBased) {
            Log.d(TAG, "Resultado final: true (USB HID)")
            return true
        }

        // 3) Detección por DataWedge: en Zebra (TC26, TC5x, etc.) el lector es
        //    hardware integrado — no aparece como InputDevice ni como USB, y su
        //    única interfaz es DataWedge. DataWedge instalado ⟹ hay lector
        //    físico conectado (y el escáner funciona vía broadcast).
        if (DataWedgeScanner.isDataWedgeInstalled(context)) {
            Log.d(TAG, "Resultado final: true (DataWedge/Zebra integrado)")
            return true
        }

        Log.d(TAG, "Resultado final: false")
        return false
    }

    private fun hasUsbHidScanner(context: Context): Boolean {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        return usbManager.deviceList.values.any { device ->
            val interfaces = (0 until device.interfaceCount).map { device.getInterface(it) }
            val isHid = interfaces.any { it.interfaceClass == UsbConstants.USB_CLASS_HID }
            val isHidBootKeyboard = interfaces.any {
                it.interfaceClass == UsbConstants.USB_CLASS_HID &&
                    it.interfaceSubclass == 1 && // boot interface
                    it.interfaceProtocol == 1    // keyboard
            }
            val isZebraVendor = device.vendorId in ZEBRA_VENDOR_IDS
            val isInternalVendor = device.vendorId in INTERNAL_VENDOR_IDS
            Log.d(
                TAG,
                "USB: ${device.productName ?: device.deviceName} | vid=${device.vendorId} " +
                    "(0x${device.vendorId.toString(16)}) | pid=${device.productId} | " +
                    "hid=$isHid | bootKeyboard=$isHidBootKeyboard | zebraVendor=$isZebraVendor",
            )
            isHid && (isZebraVendor || (isHidBootKeyboard && !isInternalVendor))
        }
    }
}

/**
 * Observa dinámicamente la presencia de un escáner físico (HID) usando
 * InputManager.InputDeviceListener. A diferencia de isPhysicalScannerConnected(),
 * que es una foto fija, esto se actualiza automáticamente cuando el
 * dispositivo se conecta o desconecta mientras la pantalla está abierta.
 */
@Composable
fun rememberPhysicalScannerConnected(context: Context): State<Boolean> {
    val connected = remember { mutableStateOf(ScannerDetector.isPhysicalScannerConnected(context)) }

    DisposableEffect(context) {
        val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager
        val listener = object : InputManager.InputDeviceListener {
            override fun onInputDeviceAdded(deviceId: Int) {
                connected.value = ScannerDetector.isPhysicalScannerConnected(context)
            }

            override fun onInputDeviceRemoved(deviceId: Int) {
                connected.value = ScannerDetector.isPhysicalScannerConnected(context)
            }

            override fun onInputDeviceChanged(deviceId: Int) {
                connected.value = ScannerDetector.isPhysicalScannerConnected(context)
            }
        }
        inputManager.registerInputDeviceListener(listener, null)
        onDispose { inputManager.unregisterInputDeviceListener(listener) }
    }

    return connected
}