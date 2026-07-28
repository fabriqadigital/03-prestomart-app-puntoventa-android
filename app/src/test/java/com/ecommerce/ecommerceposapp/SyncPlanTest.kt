package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.sync.SyncPlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncPlanTest {
    @Test
    fun `product images include only their catalog prerequisites`() {
        val selected = SyncPlan.expand(setOf("imagenes_productos"))

        assertEquals(
            listOf("categorias", "subcategorias", "productos", "imagenes_productos"),
            selected.toList(),
        )
        assertFalse("proveedores" in selected)
        assertFalse("clientes" in selected)
        assertFalse("caja" in selected)
    }

    @Test
    fun `sales include cash clients and complete product hierarchy`() {
        val selected = SyncPlan.expand(setOf("ventas"))

        assertEquals(
            listOf("categorias", "subcategorias", "clientes", "productos", "caja", "ventas"),
            selected.toList(),
        )
    }

    @Test
    fun `tickets include the complete sales hierarchy`() {
        val selected = SyncPlan.expand(setOf("tickets"))

        assertTrue("ventas" in selected)
        assertTrue("caja" in selected)
        assertTrue("clientes" in selected)
        assertTrue("productos" in selected)
    }

    @Test
    fun `removing category also removes all selected dependents`() {
        val selected = SyncPlan.expand(setOf("imagenes_productos", "tickets", "proveedores"))
        val remaining = SyncPlan.removeWithDependents(selected, "categorias")

        assertEquals(setOf("proveedores", "clientes", "caja"), remaining)
    }
}
