package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.model.sales.paginateSalesHistoryLocal
import com.ecommerce.ecommerceposapp.domain.model.sales.mergePendingSalesWithRemotePage
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryPage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SalesHistoryPaginationTest {
    private val rows = (1..125).map { id ->
        SalesHistoryRow(id.toLong(), "B001-$id", "03", id.toLong(), "Cliente $id", "Caja", "EFE", id.toDouble(), "Pagada", 0)
    }

    @Test fun `pagina offline respeta 20 50 y 100 registros`() {
        assertEquals(20, paginateSalesHistoryLocal(rows, 1, 20, "").rows.size)
        assertEquals(50, paginateSalesHistoryLocal(rows, 2, 50, "").rows.size)
        assertEquals(25, paginateSalesHistoryLocal(rows, 2, 100, "").rows.size)
    }

    @Test fun `pagina fuera del rango queda vacia y conserva el total`() {
        val page = paginateSalesHistoryLocal(rows, 10, 20, "")
        assertTrue(page.rows.isEmpty())
        assertEquals(125, page.total)
    }

    @Test fun `busqueda se aplica antes de paginar`() {
        val page = paginateSalesHistoryLocal(rows, 1, 20, "Cliente 12")
        assertEquals(7, page.total)
        assertTrue(page.rows.all { it.clienteNombre.contains("Cliente 12") })
    }

    @Test fun `valores invalidos usan pagina uno y veinte registros`() {
        val page = paginateSalesHistoryLocal(rows, 0, 999, "")
        assertEquals(1, page.page)
        assertEquals(20, page.perPage)
        assertEquals(20, page.rows.size)
    }

    @Test fun `ventas pendientes aparecen solo en la primera pagina`() {
        val pending = row(-1, "Pendiente offline")
        val first = mergePendingSalesWithRemotePage(
            SalesHistoryPage(listOf(row(20)), 40, 1, 20),
            listOf(pending),
            "",
        )
        val second = mergePendingSalesWithRemotePage(
            SalesHistoryPage(listOf(row(19)), 40, 2, 20),
            listOf(pending),
            "",
        )

        assertTrue(first.rows.any { it.ventaId == -1L })
        assertTrue(second.rows.none { it.ventaId == -1L })
        assertEquals(41, first.total)
        assertEquals(41, second.total)
    }

    @Test fun `mezcla elimina duplicados y conserva orden descendente`() {
        val duplicated = row(-1, "Pendiente")
        val page = mergePendingSalesWithRemotePage(
            SalesHistoryPage(listOf(duplicated, row(3)), 2, 1, 20),
            listOf(duplicated, duplicated),
            "",
        )

        assertEquals(2, page.rows.size)
        assertEquals(2, page.total)
        assertTrue(page.rows.zipWithNext().all { (left, right) -> left.fechaMillis >= right.fechaMillis })
    }

    @Test fun `busqueda excluye pendientes que no coinciden`() {
        val page = mergePendingSalesWithRemotePage(
            SalesHistoryPage(emptyList(), 0, 1, 20),
            listOf(row(-1, "Maria"), row(-2, "Jose")),
            "Maria",
        )

        assertEquals(listOf(-1L), page.rows.map { it.ventaId })
        assertEquals(1, page.total)
    }

    private fun row(id: Int, client: String = "Cliente $id") = SalesHistoryRow(
        id.toLong(), "B001-$id", "03", id.toLong(), client, "Caja", "EFE", 10.0, "Pagada", 0,
    )
}
