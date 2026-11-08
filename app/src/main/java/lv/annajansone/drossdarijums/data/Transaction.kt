package lv.annajansone.drossdarijums.data

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

enum class TransactionStatus {
    PROTECTED,
    COMPLETED,
}

data class Transaction(
    val id: String = "DD-${UUID.randomUUID().toString().take(6).uppercase()}",
    val item: String,
    val seller: String,
    val amount: BigDecimal,
    val createdAt: LocalDate = LocalDate.now(),
    val status: TransactionStatus = TransactionStatus.PROTECTED,
)

