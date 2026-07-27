package com.ecommerce.ecommerceposapp.util

import android.content.Context
import android.hardware.input.InputManager
import android.view.InputDevice

/**
 * Detecta si hay un escáner físico conectado (USB o Bluetooth en modo HID).
 * Estos dispositivos se reportan al sistema como teclados externos, así que
 * se identifican revisando los InputDevice activos que NO sean el teclado
 * virtual ni un teclado integrado del propio dispositivo.
 */
object ScannerDetector {

    fun isPhysicalScannerConnected(context: Context): Boolean {
        val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager
        val result = InputDevice.getDeviceIds().any { id ->
            val device = inputManager.getInputDevice(id) ?: return@any false
            val isKeyboardLike = device.sources and InputDevice.SOURCE_KEYBOARD == InputDevice.SOURCE_KEYBOARD
            val isVirtual = device.isVirtual
            val isFullKeyboard = device.keyboardType == InputDevice.KEYBOARD_TYPE_ALPHABETIC
            android.util.Log.d(
                "ScannerDetector",
                "Device: ${device.name} | virtual=$isVirtual | keyboardLike=$isKeyboardLike | fullKeyboard=$isFullKeyboard | vendorId=${device.vendorId}",
            )
            isKeyboardLike && !isVirtual && isFullKeyboard
        }
        android.util.Log.d("ScannerDetector", "Resultado final: $result")
        return result
    }
}