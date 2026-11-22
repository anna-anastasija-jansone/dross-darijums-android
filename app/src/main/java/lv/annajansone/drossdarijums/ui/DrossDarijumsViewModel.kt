package lv.annajansone.drossdarijums.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import lv.annajansone.drossdarijums.data.Transaction
import lv.annajansone.drossdarijums.data.TransactionRepository
import lv.annajansone.drossdarijums.data.TransactionStatus
import lv.annajansone.drossdarijums.domain.CommissionCalculator
import lv.annajansone.drossdarijums.domain.TransactionInput
import lv.annajansone.drossdarijums.domain.TransactionQuote
import lv.annajansone.drossdarijums.domain.TransactionValidator

enum class UserRole { BUYER, SELLER }

enum class AppScreen {
    LIST,
    CREATE,
    REVIEW,
    PROCESSING,
    SUCCESS,
    ERROR,
    DETAIL,
    COMPLETION_SUCCESS,
}

data class AppUiState(
    val screen: AppScreen = AppScreen.LIST,
    val currentRole: UserRole = UserRole.BUYER,
    val item: String = "",
    val seller: String = "",
    val amountText: String = "",
    val quote: TransactionQuote? = null,
    val validationError: String? = null,
    val statusMessage: String? = null,
    val transactions: List<Transaction> = emptyList(),
    val selectedTransactionId: String? = null,
    val showCompletionConfirmation: Boolean = false,
)

class DrossDarijumsViewModel : ViewModel() {
    private val repository = TransactionRepository()
    var uiState by mutableStateOf(AppUiState())
        private set

    fun switchRole(role: UserRole) {
        val buyerOnlyScreen = uiState.screen in setOf(
            AppScreen.CREATE,
            AppScreen.REVIEW,
            AppScreen.PROCESSING,
            AppScreen.SUCCESS,
            AppScreen.ERROR,
        )
        uiState = if (role == UserRole.SELLER && buyerOnlyScreen) {
            AppUiState(currentRole = role, transactions = repository.all())
        } else {
            uiState.copy(
                currentRole = role,
                showCompletionConfirmation = false,
                statusMessage = null,
                transactions = repository.all(),
            )
        }
    }

    fun startTransaction() {
        if (uiState.currentRole != UserRole.BUYER) {
            uiState = uiState.copy(statusMessage = "Darījumu var izveidot tikai pircējs.")
            return
        }
        uiState = AppUiState(
            screen = AppScreen.CREATE,
            currentRole = uiState.currentRole,
            transactions = repository.all(),
        )
    }

    fun updateItem(value: String) {
        uiState = uiState.copy(item = value, validationError = null, quote = null)
    }

    fun updateSeller(value: String) {
        uiState = uiState.copy(seller = value, validationError = null, quote = null)
    }

    fun updateAmount(value: String) {
        uiState = uiState.copy(amountText = value, validationError = null, quote = null)
    }

    fun review() {
        if (uiState.currentRole != UserRole.BUYER) return
        val result = TransactionValidator.validate(
            TransactionInput(uiState.item, uiState.seller, uiState.amountText),
        )
        uiState = if (result.isValid) {
            val quote = CommissionCalculator.calculate(requireNotNull(result.amount))
            uiState.copy(
                screen = AppScreen.REVIEW,
                amountText = quote.itemAmount.toPlainString(),
                quote = quote,
                validationError = null,
            )
        } else {
            uiState.copy(validationError = result.error)
        }
    }

    fun processPayment(simulateError: Boolean = false) {
        if (uiState.currentRole != UserRole.BUYER || uiState.quote == null) return
        uiState = uiState.copy(screen = if (simulateError) AppScreen.ERROR else AppScreen.PROCESSING)
    }

    fun returnToReview() {
        uiState = uiState.copy(screen = AppScreen.REVIEW)
    }

    fun completePayment() {
        if (uiState.currentRole != UserRole.BUYER) return
        val quote = uiState.quote ?: return
        repository.add(
            Transaction(
                item = uiState.item,
                seller = uiState.seller,
                itemAmount = quote.itemAmount,
                commissionFee = quote.commissionFee,
                buyerTotal = quote.buyerTotal,
                sellerReceives = quote.sellerReceives,
            ),
        )
        uiState = uiState.copy(screen = AppScreen.SUCCESS, transactions = repository.all())
    }

    fun backToList() {
        uiState = AppUiState(
            currentRole = uiState.currentRole,
            transactions = repository.all(),
        )
    }

    fun openTransaction(id: String) {
        if (repository.find(id) == null) return
        uiState = uiState.copy(
            screen = AppScreen.DETAIL,
            selectedTransactionId = id,
            showCompletionConfirmation = false,
            statusMessage = null,
            transactions = repository.all(),
        )
    }

    fun markSelectedAsShipped() {
        val transaction = selectedTransaction() ?: return
        if (uiState.currentRole != UserRole.SELLER) {
            uiState = uiState.copy(statusMessage = "Tikai pārdevējs var atzīmēt preci kā nosūtītu.")
            return
        }
        if (transaction.status != TransactionStatus.PROTECTED) {
            uiState = uiState.copy(statusMessage = "Šo statusa pāreju vairs nevar veikt.")
            return
        }
        val shipped = repository.markShipped(transaction.id) ?: return
        uiState = uiState.copy(
            selectedTransactionId = shipped.id,
            statusMessage = "Prece atzīmēta kā nosūtīta.",
            transactions = repository.all(),
        )
    }

    fun requestCompletion() {
        val transaction = selectedTransaction() ?: return
        when {
            uiState.currentRole != UserRole.BUYER -> {
                uiState = uiState.copy(statusMessage = "Tikai pircējs var apstiprināt preces saņemšanu.")
            }
            transaction.status != TransactionStatus.SHIPPED -> {
                uiState = uiState.copy(statusMessage = "Vispirms pārdevējam jāatzīmē prece kā nosūtīta.")
            }
            else -> uiState = uiState.copy(showCompletionConfirmation = true, statusMessage = null)
        }
    }

    fun dismissCompletion() {
        uiState = uiState.copy(showCompletionConfirmation = false)
    }

    fun completeSelectedTransaction() {
        val transaction = selectedTransaction() ?: return
        if (uiState.currentRole != UserRole.BUYER || transaction.status != TransactionStatus.SHIPPED) {
            dismissCompletion()
            return
        }
        val completed = repository.complete(transaction.id) ?: return
        uiState = uiState.copy(
            screen = AppScreen.COMPLETION_SUCCESS,
            selectedTransactionId = completed.id,
            showCompletionConfirmation = false,
            statusMessage = null,
            transactions = repository.all(),
        )
    }

    fun selectedTransaction(): Transaction? =
        uiState.selectedTransactionId?.let(repository::find)
}
