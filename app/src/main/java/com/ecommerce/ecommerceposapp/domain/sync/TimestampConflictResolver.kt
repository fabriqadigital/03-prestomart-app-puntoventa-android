package com.ecommerce.ecommerceposapp.domain.sync

/**
 * Creation timestamps are immutable ownership markers. The oldest record wins;
 * a missing timestamp or exact tie keeps the server record to avoid duplicates.
 */
object TimestampConflictResolver {
    fun serverWins(serverCreatedAt: Long, localCreatedAt: Long): Boolean =
        serverCreatedAt <= 0L ||
            localCreatedAt <= 0L ||
            serverCreatedAt <= localCreatedAt
}
