package com.ecommerce.ecommerceposapp.data.sync

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.sync.OutboxRealm
import io.realm.Realm
import java.io.File
import java.util.UUID
import org.json.JSONObject

class OfflineReceiptDeliveryQueue(private val context: Context) {
    fun enqueueEmail(
        email: String,
        receiptNumber: String,
        customerName: String,
        sourcePdf: File,
    ): Result<Unit> = runCatching {
        require(sourcePdf.exists() && sourcePdf.length() > 0L) { "El PDF local no esta disponible." }
        val destination = File(context.filesDir, "queued_receipts").apply { mkdirs() }
            .resolve("${UUID.randomUUID()}.pdf")
        sourcePdf.copyTo(destination, overwrite = true)
        val now = System.currentTimeMillis()
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.insert(
                    OutboxRealm().apply {
                        id = UUID.randomUUID().toString()
                        moduleKey = "tickets"
                        operation = "SEND_RECEIPT_EMAIL"
                        aggregateType = "receipt_delivery"
                        payloadJson = JSONObject()
                            .put("email", email.trim())
                            .put("receipt_number", receiptNumber.trim())
                            .put("customer_name", customerName.trim())
                            .put("pdf_path", destination.absolutePath)
                            .toString()
                        createdAt = now
                        updatedAt = now
                        state = "PENDING"
                    },
                )
            }
        }
    }
}
