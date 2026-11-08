package lv.annajansone.drossdarijums.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import lv.annajansone.drossdarijums.data.Transaction
import lv.annajansone.drossdarijums.data.TransactionRepository
import lv.annajansone.drossdarijums.domain.TransactionInput
import lv.annajansone.drossdarijums.domain.TransactionValidator

enum class AppScreen { LIST, CREATE, REVIEW, PROCESSING, SUCCESS, ERROR }

data class AppUiState(
    val screen: AppScreen = AppScreen.LIST,
    val item: String = "",
    val seller: String = "",
    val amountText: String = "",
    val validationError: String? = null,
    val transactions: List<Transaction> = emptyList(),
)

class DrossDarijumsViewModel : ViewModel() {
    private val repository = TransactionRepository()
    var uiState by mutableStateOf(AppUiState())
        private set

    fun startTransaction() {
        uiState = uiState.copy(screen = AppScreen.CREATE, validationError = null)
    }

    fun updateItem(value: String) { uiState = uiState.copy(item = value, validationError = null) }
    fun updateSeller(value: String) { uiState = uiState.copy(seller = value, validationError = null) }
    fun updateAmount(value: String) { uiState = uiState.copy(amountText = value, validationError = null) }

    fun review() {
        val result = TransactionValidator.validate(
            TransactionInput(uiState.item, uiState.seller, uiState.amountText),
        )
        uiState = if (result.isValid) {
            uiState.copy(screen = AppScreen.REVIEW, amountText = result.amount.toString())
        } else {
            uiState.copy(validationError = result.error)
        }
    }

    fun processPayment(simulateError: Boolean = false) {
        uiState = uiState.copy(screen = if (simulateError) AppScreen.ERROR else AppScreen.PROCESSING)
    }

    fun completePayment() {
        val result = TransactionValidator.validate(
            TransactionInput(uiState.item, uiState.seller, uiState.amountText),
        )
        val amount = result.amount ?: return
        repository.add(Transaction(item = uiState.item, seller = uiState.seller, amount = amount))
        uiState = uiState.copy(screen = AppScreen.SUCCESS, transactions = repository.all())
    }

    fun backToList() {
        uiState = AppUiState(transactions = repository.all())
    }
}

