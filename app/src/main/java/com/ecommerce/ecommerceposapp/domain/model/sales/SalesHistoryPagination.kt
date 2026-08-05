package com.ecommerce.ecommerceposapp.domain.model.sales

internal fun paginateSalesHistoryLocal(
    rows: List<SalesHistoryRow>,
    page: Int,
    perPage: Int,
    search: String,
): SalesHistoryPage {
    val safePage = page.coerceAtLeast(1)
    val safePerPage = perPage.takeIf { it in setOf(20, 50, 100) } ?: 20
    val term = search.trim()
    val filtered = rows.filter {
        term.isBlank() || it.numeroComprobante.contains(term, true) ||
            it.clienteNombre.contains(term, true) || it.cajeroNombre.contains(term, true)
    }
    return SalesHistoryPage(
        rows = filtered.drop((safePage - 1) * safePerPage).take(safePerPage),
        total = filtered.size,
        page = safePage,
        perPage = safePerPage,
    )
}

internal fun mergePendingSalesWithRemotePage(
    remote: SalesHistoryPage,
    localRows: List<SalesHistoryRow>,
    search: String,
): SalesHistoryPage {
    val term = search.trim()
    val pending = localRows.filter {
        it.ventaId < 0L && (term.isBlank() || it.numeroComprobante.contains(term, true) ||
            it.clienteNombre.contains(term, true) || it.cajeroNombre.contains(term, true))
    }.distinctBy { it.ventaId }
    val visiblePending = if (remote.page == 1) pending else emptyList()
    return remote.copy(
        rows = (visiblePending + remote.rows)
            .distinctBy { it.ventaId }
            .sortedByDescending { it.fechaMillis },
        total = remote.total + pending.count { pendingRow -> remote.rows.none { it.ventaId == pendingRow.ventaId } },
    )
}
