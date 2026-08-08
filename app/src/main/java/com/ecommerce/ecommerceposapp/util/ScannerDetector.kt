package com.ecommerce.ecommerceposapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.core.content.ContextCompat

object ScannerDetector {

    private const val TAG = "ScannerDetector"
    private val ZEBRA_VENDOR_IDS = setOf(
        0x05E0,
        0x064D,
        0x0624,
        0x0681,
        0x05F9,
        0x0525,
    )

    private val INTERNAL_VENDOR_IDS = setOf(
        0x05C6,
        0x2C7C,
        0x0A5C,
        0x18D1,
        0x04E8,
        0x12D1,
        0x10A9,
        0x0000,
    )

    fun isPhysicalScannerConnected(context: Context): Boolean {
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
            isKeyboardLike && !isVirtual && (isFullKeyboard || isZebraVendor)
        }
        if (inputBased) {
            Log.d(TAG, "Resultado final: true (InputDevice)")
            return true
        }
        val usbBased = hasUsbHidScanner(context)
        if (usbBased) {
            Log.d(TAG, "Resultado final: true (USB HID)")
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

        val usbReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                when (intent?.action) {
                    UsbManager.ACTION_USB_DEVICE_ATTACHED,
                    UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                        Log.d("ScannerDetector", "Evento USB: ${intent.action} → re-evaluando lector físico")
                        connected.value = ScannerDetector.isPhysicalScannerConnected(context)
                    }
                }
            }
        }
        val usbFilter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        ContextCompat.registerReceiver(
            context,
            usbReceiver,
            usbFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )

        onDispose {
            inputManager.unregisterInputDeviceListener(listener)
            runCatching { context.unregisterReceiver(usbReceiver) }
        }
    }

    return connected
}