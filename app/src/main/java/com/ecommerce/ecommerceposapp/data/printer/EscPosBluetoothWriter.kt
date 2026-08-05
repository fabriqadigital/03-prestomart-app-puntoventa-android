package com.ecommerce.ecommerceposapp.data.printer

import java.io.OutputStream

/** Envía el ticket al ritmo que soporta el búfer de la ticketera Bluetooth. */
internal object EscPosBluetoothWriter {
    const val CHUNK_SIZE = 64
    const val CHUNK_DELAY_MS = 35L

    fun writePaced(
        output: OutputStream,
        payload: ByteArray,
        pause: (Long) -> Unit = Thread::sleep,
    ) {
        var offset = 0
        while (offset < payload.size) {
            val length = minOf(CHUNK_SIZE, payload.size - offset)
            output.write(payload, offset, length)
            output.flush()
            offset += length
            if (offset < payload.size) pause(CHUNK_DELAY_MS)
        }
    }
}
