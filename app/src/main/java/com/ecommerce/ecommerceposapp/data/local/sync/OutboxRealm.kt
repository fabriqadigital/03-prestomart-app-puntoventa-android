package com.ecommerce.ecommerceposapp.data.local.sync

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Durable queue for every operation that must eventually reach the backend.
 * Payloads are immutable JSON snapshots so a process restart cannot lose work.
 */
open class OutboxRealm : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var moduleKey: String = ""
    var operation: String = ""
    var aggregateType: String = ""
    var aggregateLocalId: Long = 0L
    var payloadJson: String = ""
    var createdAt: Long = 0L
    var updatedAt: Long = 0L
    var attemptCount: Int = 0
    var nextAttemptAt: Long = 0L
    /** PENDING, PROCESSING or FAILED. Rows are deleted after acknowledgement. */
    var state: String = "PENDING"
    var lastError: String = ""
}

/** Keeps references valid when a negative local ID is replaced by a server ID. */
open class SyncIdMapRealm : RealmObject() {
    @PrimaryKey
    var key: String = ""
    var entityType: String = ""
    var localId: Long = 0L
    var remoteId: Long = 0L
    var createdAt: Long = 0L
}
