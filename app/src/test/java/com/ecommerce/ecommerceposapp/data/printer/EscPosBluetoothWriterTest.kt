package com.ecommerce.ecommerceposapp.data.printer

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream

class EscPosBluetoothWriterTest {
    @Test
    fun `un ticket largo se envia completo y en bloques`() {
        val payload = ByteArray(2_117) { (it % 251).toByte() }
        val output = CountingOutputStream()
        val pauses = mutableListOf<Long>()

        EscPosBluetoothWriter.writePaced(output, payload) { pauses += it }

        assertArrayEquals(payload, output.toByteArray())
        val chunks = (payload.size + EscPosBluetoothWriter.CHUNK_SIZE - 1) / EscPosBluetoothWriter.CHUNK_SIZE
        assertEquals(chunks, output.writeCalls)
        assertEquals(chunks, output.flushCalls)
        assertEquals(chunks - 1, pauses.size)
        assertEquals(List(chunks - 1) { EscPosBluetoothWriter.CHUNK_DELAY_MS }, pauses)
    }

    @Test
    fun `un ticket pequeno se envia completo sin pausas`() {
        val payload = "ticket corto con qr y corte".toByteArray()
        val output = CountingOutputStream()
        var pauses = 0

        EscPosBluetoothWriter.writePaced(output, payload) { pauses++ }

        assertArrayEquals(payload, output.toByteArray())
        assertEquals(1, output.writeCalls)
        assertEquals(1, output.flushCalls)
        assertEquals(0, pauses)
    }

    private class CountingOutputStream : ByteArrayOutputStream() {
        var writeCalls = 0
        var flushCalls = 0

        override fun write(buffer: ByteArray, offset: Int, length: Int) {
            writeCalls++
            super.write(buffer, offset, length)
        }

        override fun flush() {
            flushCalls++
            super.flush()
        }
    }
}
