package com.ecommerce.ecommerceposapp.util

import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object PhysicalScannerInput {

    private const val TAG = "PhysicalScanner"
    private const val MAX_CODE_LENGTH = 32
    private const val BURST_GAP_MS = 250L
    private const val DEDUP_MS = 500L

    private val buffer = StringBuilder()
    private var lastKeyTime = 0L
    private var lastEmittedCode = ""
    private var lastEmitTime = 0L

    private val _scans = MutableSharedFlow<String>(extraBufferCapacity = 16)
    val scans: SharedFlow<String> = _scans
    fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN || event.repeatCount > 0) return false
        val now = SystemClock.uptimeMillis()
        when (event.keyCode) {
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                if (buffer.isEmpty()) return false
                val code = buffer.toString()
                buffer.clear()
                lastKeyTime = 0L
                emit(code)
                return true
            }
            else -> {
                var ch = event.unicodeChar
                if (ch == 0) {
                    ch = when (event.keyCode) {
                        in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 ->
                            '0'.code + (event.keyCode - KeyEvent.KEYCODE_0)
                        in KeyEvent.KEYCODE_NUMPAD_0..KeyEvent.KEYCODE_NUMPAD_9 ->
                            '0'.code + (event.keyCode - KeyEvent.KEYCODE_NUMPAD_0)
                        else -> 0
                    }
                }
                if (ch in '0'.code..'9'.code) {
                    if (buffer.isNotEmpty() && now - lastKeyTime > BURST_GAP_MS) {
                        buffer.clear()
                    }
                    if (buffer.length < MAX_CODE_LENGTH) buffer.append(ch.toChar())
                    lastKeyTime = now
                    return true
                }
                if (buffer.isNotEmpty()) {
                    buffer.clear()
                    lastKeyTime = 0L
                }
                return false
            }
        }
    }

    private fun emit(code: String) {
        val now = SystemClock.uptimeMillis()
        if (code == lastEmittedCode && now - lastEmitTime < DEDUP_MS) return
        lastEmittedCode = code
        lastEmitTime = now
        Log.d(TAG, "SCANNER RAW: $code")
        _scans.tryEmit(code)
    }

    fun normalizeBarcode(raw: String, knownBarcodes: Collection<String> = emptyList()): String {
        val code = raw.trim()
        if (code.length == 12 && code.all { it.isDigit() }) {
            val ean13Candidate = "0$code"
            if (knownBarcodes.any { it == ean13Candidate }) return ean13Candidate
        }
        return code
    }
}
