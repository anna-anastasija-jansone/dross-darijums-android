package lv.annajansone.drossdarijums.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lv.annajansone.drossdarijums.data.Transaction
import lv.annajansone.drossdarijums.data.TransactionStatus
import lv.annajansone.drossdarijums.domain.TransactionQuote

@Composable
fun DrossDarijumsApp(viewModel: DrossDarijumsViewModel) {
    val state = viewModel.uiState
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(":DD  Drošs Darījums", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            RoleSwitcher(state.currentRole, viewModel::switchRole)
            when (state.screen) {
                AppScreen.LIST -> TransactionList(
                    transactions = state.transactions,
                    role = state.currentRole,
                    statusMessage = state.statusMessage,
                    onCreate = viewModel::startTransaction,
                    onOpen = viewModel::openTransaction,
                )
                AppScreen.CREATE -> TransactionForm(state, viewModel)
                AppScreen.REVIEW -> TransactionReview(state, viewModel)
                AppScreen.PROCESSING -> Processing(viewModel::completePayment)
                AppScreen.SUCCESS -> ResultScreen(
                    title = "Maksājums ir aizsargāts",
                    message = "Pircēja maksājums ${state.quote?.buyerTotal?.money()} ir simulēti aizturēts.",
                    primaryLabel = "Skatīt manus darījumus",
                    onPrimary = viewModel::backToList,
                    secondaryLabel = "Izveidot citu darījumu",
                    onSecondary = viewModel::startTransaction,
                )
                AppScreen.ERROR -> ResultScreen(
                    title = "Maksājums neizdevās",
                    message = "Darījums nav izveidots un nauda nav ieturēta.",
                    primaryLabel = "Atgriezties un mēģināt vēlreiz",
                    onPrimary = viewModel::returnToReview,
                    secondaryLabel = "Skatīt manus darījumus",
                    onSecondary = viewModel::backToList,
                )
                AppScreen.DETAIL -> TransactionDetail(state, viewModel)
                AppScreen.COMPLETION_SUCCESS -> ResultScreen(
                    title = "Darījums pabeigts",
                    message = "Prece saņemta. Pārdevējam simulēti izmaksāti ${viewModel.selectedTransaction()?.sellerReceives?.money()}.",
                    primaryLabel = "Atgriezties pie darījumiem",
                    onPrimary = viewModel::backToList,
                )
            }
        }
    }
}

@Composable
private fun RoleSwitcher(role: UserRole, onRoleSelected: (UserRole) -> Unit) {
    Text("Demonstrācijas loma", style = MaterialTheme.typography.labelLarge)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (role == UserRole.BUYER) {
            Button(onClick = { onRoleSelected(UserRole.BUYER) }, modifier = Modifier.weight(1f).height(48.dp)) {
                Text("Pircējs")
            }
            OutlinedButton(onClick = { onRoleSelected(UserRole.SELLER) }, modifier = Modifier.weight(1f).height(48.dp)) {
                Text("Pārdevējs")
            }
        } else {
            OutlinedButton(onClick = { onRoleSelected(UserRole.BUYER) }, modifier = Modifier.weight(1f).height(48.dp)) {
                Text("Pircējs")
            }
            Button(onClick = { onRoleSelected(UserRole.SELLER) }, modifier = Modifier.weight(1f).height(48.dp)) {
                Text("Pārdevējs")
            }
        }
    }
}

@Composable
private fun ColumnScope.TransactionList(
    transactions: List<Transaction>,
    role: UserRole,
    statusMessage: String?,
    onCreate: () -> Unit,
    onOpen: (String) -> Unit,
) {
    Text(if (role == UserRole.BUYER) "Mani pirkumi" else "Mani pārdošanas darījumi", style = MaterialTheme.typography.headlineSmall)
    Text(
        if (role == UserRole.BUYER) {
            "Pircējs izveido un apmaksā darījumu, pēc nosūtīšanas apstiprina saņemšanu."
        } else {
            "Pārdevējs atver aizsargātu darījumu un atzīmē preci kā nosūtītu."
        },
    )
    statusMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    if (transactions.isEmpty()) {
        Text("Darījumu vēl nav.")
        Spacer(Modifier.weight(1f))
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(transactions) { transaction ->
                TransactionRow(transaction, onClick = { onOpen(transaction.id) })
            }
        }
    }
    if (role == UserRole.BUYER) {
        Button(onClick = onCreate, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Izveidot darījumu")
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.item, fontWeight = FontWeight.Bold)
                Text(transaction.seller)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(transaction.buyerTotal.money(), fontWeight = FontWeight.Bold)
                Text(transaction.status.label())
            }
        }
    }
}

@Composable
private fun ColumnScope.TransactionForm(state: AppUiState, viewModel: DrossDarijumsViewModel) {
    Column(
        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Jauns darījums", style = MaterialTheme.typography.headlineSmall)
        Text("Komisija: 3% no preces cenas")
        OutlinedTextField(state.item, viewModel::updateItem, label = { Text("Prece") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.seller, viewModel::updateSeller, label = { Text("Pārdevējs") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.amountText, viewModel::updateAmount, label = { Text("Preces cena eiro") }, modifier = Modifier.fillMaxWidth())
        state.validationError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = viewModel::review, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Aprēķināt un turpināt")
        }
        OutlinedButton(onClick = viewModel::backToList, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Atcelt un skatīt darījumus")
        }
    }
}

@Composable
private fun ColumnScope.TransactionReview(state: AppUiState, viewModel: DrossDarijumsViewModel) {
    val quote = state.quote ?: return
    Column(
        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Pārbaudi darījumu", style = MaterialTheme.typography.headlineSmall)
        Text("Prece: ${state.item}")
        Text("Pārdevējs: ${state.seller}")
        CommissionBreakdown(quote)
        Text("Pēc maksājuma darījuma statuss būs “Aizsargāts”.")
        Button(onClick = { viewModel.processPayment(false) }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Apmaksāt ${quote.buyerTotal.money()}")
        }
        OutlinedButton(onClick = { viewModel.processPayment(true) }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Testēt kļūdas stāvokli")
        }
        OutlinedButton(onClick = viewModel::backToList, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Atcelt un skatīt darījumus")
        }
    }
}

@Composable
private fun CommissionBreakdown(quote: TransactionQuote) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Maksājuma sadalījums", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            MoneyLine("Preces cena", quote.itemAmount.money())
            MoneyLine("Komisija (3%)", quote.commissionFee.money())
            HorizontalDivider()
            MoneyLine("Pircējs maksā kopā", quote.buyerTotal.money(), bold = true)
            MoneyLine("Pārdevējs saņems", quote.sellerReceives.money(), bold = true)
        }
    }
}

@Composable
private fun MoneyLine(label: String, amount: String, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(amount, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun ColumnScope.TransactionDetail(state: AppUiState, viewModel: DrossDarijumsViewModel) {
    val transaction = viewModel.selectedTransaction()
    if (transaction == null) {
        Text("Darījums nav atrasts.")
        Button(onClick = viewModel::backToList, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Atgriezties pie darījumiem")
        }
        return
    }

    Column(
        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Darījuma detaļas", style = MaterialTheme.typography.headlineSmall)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(transaction.item, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Darījuma Nr.: ${transaction.id}")
                Text("Pārdevējs: ${transaction.seller}")
                Text("Datums: ${transaction.createdAt}")
                Text("Statuss: ${transaction.status.label()}", fontWeight = FontWeight.Bold)
                HorizontalDivider()
                MoneyLine("Preces cena", transaction.itemAmount.money())
                MoneyLine("Komisija", transaction.commissionFee.money())
                MoneyLine("Pircējs samaksāja", transaction.buyerTotal.money(), bold = true)
                MoneyLine("Pārdevējs saņems", transaction.sellerReceives.money(), bold = true)
            }
        }

        state.statusMessage?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        TransactionAction(state.currentRole, transaction, viewModel)

        OutlinedButton(onClick = viewModel::backToList, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Atgriezties pie darījumiem")
        }
    }

    if (state.showCompletionConfirmation) {
        AlertDialog(
            onDismissRequest = viewModel::dismissCompletion,
            title = { Text("Pabeigt darījumu?") },
            text = { Text("Apstiprini, ka prece ir saņemta. ${transaction.sellerReceives.money()} tiks simulēti izmaksāti pārdevējam.") },
            confirmButton = {
                Button(onClick = viewModel::completeSelectedTransaction) { Text("Jā, pabeigt") }
            },
            dismissButton = {
                OutlinedButton(onClick = viewModel::dismissCompletion) { Text("Atcelt") }
            },
        )
    }
}

@Composable
private fun TransactionAction(role: UserRole, transaction: Transaction, viewModel: DrossDarijumsViewModel) {
    when {
        transaction.status == TransactionStatus.COMPLETED -> {
            Text("Darījums ir pabeigts; maksājums pārdevējam ir simulēti izmaksāts.")
        }
        role == UserRole.SELLER && transaction.status == TransactionStatus.PROTECTED -> {
            Button(onClick = viewModel::markSelectedAsShipped, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text("Atzīmēt preci kā nosūtītu")
            }
        }
        role == UserRole.SELLER && transaction.status == TransactionStatus.SHIPPED -> {
            Text("Prece ir nosūtīta. Gaidi pircēja saņemšanas apstiprinājumu.")
        }
        role == UserRole.BUYER && transaction.status == TransactionStatus.PROTECTED -> {
            Text("Saņemšanu vēl nevar apstiprināt — pārdevējs nav atzīmējis preci kā nosūtītu.")
        }
        role == UserRole.BUYER && transaction.status == TransactionStatus.SHIPPED -> {
            Button(onClick = viewModel::requestCompletion, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text("Apstiprināt preces saņemšanu")
            }
        }
    }
}

@Composable
private fun Processing(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(900)
        onFinished()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text("Apstrādājam maksājumu")
    }
}

@Composable
private fun ResultScreen(
    title: String,
    message: String,
    primaryLabel: String,
    onPrimary: () -> Unit,
    secondaryLabel: String? = null,
    onSecondary: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(message)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onPrimary, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text(primaryLabel)
        }
        if (secondaryLabel != null && onSecondary != null) {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onSecondary, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text(secondaryLabel)
            }
        }
    }
}

private fun TransactionStatus.label(): String = when (this) {
    TransactionStatus.PROTECTED -> "Aizsargāts"
    TransactionStatus.SHIPPED -> "Nosūtīts"
    TransactionStatus.COMPLETED -> "Pabeigts"
}

private fun java.math.BigDecimal.money(): String = toPlainString() + " €"
