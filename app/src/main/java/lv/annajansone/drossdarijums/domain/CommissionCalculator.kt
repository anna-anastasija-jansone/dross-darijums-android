package lv.annajansone.drossdarijums.domain

import java.math.BigDecimal
import java.math.RoundingMode

data class TransactionQuote(
    val itemAmount: BigDecimal,
    val commissionFee: BigDecimal,
    val buyerTotal: BigDecimal,
    val sellerReceives: BigDecimal,
)

object CommissionCalculator {
    val commissionRate: BigDecimal = BigDecimal("0.03")

    fun calculate(itemAmount: BigDecimal): TransactionQuote {
        val normalizedAmount = itemAmount.setScale(2, RoundingMode.HALF_UP)
        val commission = normalizedAmount
            .multiply(commissionRate)
            .setScale(2, RoundingMode.HALF_UP)

        return TransactionQuote(
            itemAmount = normalizedAmount,
            commissionFee = commission,
            buyerTotal = normalizedAmount.add(commission).setScale(2, RoundingMode.HALF_UP),
            sellerReceives = normalizedAmount,
        )
    }
}
