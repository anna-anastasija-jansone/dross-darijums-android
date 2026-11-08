package lv.annajansone.drossdarijums.domain

import java.math.BigDecimal

data class TransactionInput(
    val item: String,
    val seller: String,
    val amountText: String,
)

data class ValidationResult(
    val amount: BigDecimal? = null,
    val error: String? = null,
) {
    val isValid: Boolean get() = error == null && amount != null
}

object TransactionValidator {
    fun validate(input: TransactionInput): ValidationResult {
        if (input.item.isBlank()) return ValidationResult(error = "Ievadi preces nosaukumu.")
        if (input.seller.isBlank()) return ValidationResult(error = "Ievadi pārdevēju.")

        val amount = input.amountText.trim().replace(',', '.').toBigDecimalOrNull()
            ?: return ValidationResult(error = "Ievadi derīgu summu.")

        if (amount < BigDecimal("1.00") || amount > BigDecimal("5000.00")) {
            return ValidationResult(error = "Summai jābūt no 1,00 € līdz 5000,00 €.")
        }
        if (amount.scale() > 2) {
            return ValidationResult(error = "Summai drīkst būt ne vairāk kā divas zīmes aiz komata.")
        }
        return ValidationResult(amount = amount.setScale(2))
    }
}

