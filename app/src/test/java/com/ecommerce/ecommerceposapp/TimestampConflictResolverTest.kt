package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.sync.TimestampConflictResolver
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimestampConflictResolverTest {
    @Test
    fun serverWinsWhenItsCreationIsOlder() {
        assertTrue(TimestampConflictResolver.serverWins(serverCreatedAt = 100, localCreatedAt = 200))
    }

    @Test
    fun localWinsWhenItsCreationIsOlder() {
        assertFalse(TimestampConflictResolver.serverWins(serverCreatedAt = 200, localCreatedAt = 100))
    }

    @Test
    fun exactTieKeepsExistingServerRecord() {
        assertTrue(TimestampConflictResolver.serverWins(serverCreatedAt = 100, localCreatedAt = 100))
    }

    @Test
    fun missingTimestampKeepsExistingServerRecord() {
        assertTrue(TimestampConflictResolver.serverWins(serverCreatedAt = 0, localCreatedAt = 100))
        assertTrue(TimestampConflictResolver.serverWins(serverCreatedAt = 100, localCreatedAt = 0))
    }
}
