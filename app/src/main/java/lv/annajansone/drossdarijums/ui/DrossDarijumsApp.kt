package lv.annajansone.drossdarijums.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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

@Composable
fun DrossDarijumsApp(viewModel: DrossDarijumsViewModel) {
    val state = viewModel.uiState
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(":DD  Drošs Darījums", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            when (state.screen) {
                AppScreen.LIST -> TransactionList(state.transactions, viewModel::startTransaction)
                AppScreen.CREATE -> TransactionForm(state, viewModel)
                AppScreen.REVIEW -> TransactionReview(state, viewModel)
                AppScreen.PROCESSING -> Processing(viewModel::completePayment)
                AppScreen.SUCCESS -> ResultScreen(
                    title = "Maksājums ir aizsargāts",
                    message = "${state.amountText} € ir simulēti aizturēti līdz preces saņemšanai.",
                    onContinue = viewModel::backToList,
                )
                AppScreen.ERROR -> ResultScreen(
                    title = "Maksājums neizdevās",
                    message = "Darījums nav izveidots un nauda nav ieturēta.",
                    onContinue = { viewModel.processPayment(false) },
                )
            }
        }
    }
}

@Composable
private fun TransactionList(transactions: List<Transaction>, onCreate: () -> Unit) {
    Text("Mani darījumi", style = MaterialTheme.typography.headlineSmall)
    if (transactions.isEmpty()) {
        Text("Darījumu vēl nav. Izveido pirmo aizsargāto pirkumu.")
        Spacer(Modifier.weight(1f))
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(transactions) { TransactionRow(it) }
        }
    }
    Button(onClick = onCreate, modifier = Modifier.fillMaxWidth().height(48.dp)) {
        Text("Izveidot darījumu")
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(transaction.item, fontWeight = FontWeight.Bold)
                Text(transaction.seller)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${transaction.amount} €", fontWeight = FontWeight.Bold)
                Text("Aizsargāts")
            }
        }
    }
}

@Composable
private fun TransactionForm(state: AppUiState, viewModel: DrossDarijumsViewModel) {
    Text("Jauns darījums", style = MaterialTheme.typography.headlineSmall)
    OutlinedTextField(state.item, viewModel::updateItem, label = { Text("Prece") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(state.seller, viewModel::updateSeller, label = { Text("Pārdevējs") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(state.amountText, viewModel::updateAmount, label = { Text("Summa eiro") }, modifier = Modifier.fillMaxWidth())
    state.validationError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    Spacer(Modifier.weight(1f))
    Button(onClick = viewModel::review, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Turpināt") }
}

@Composable
private fun TransactionReview(state: AppUiState, viewModel: DrossDarijumsViewModel) {
    Text("Pārbaudi darījumu", style = MaterialTheme.typography.headlineSmall)
    Text("Prece: ${state.item}")
    Text("Pārdevējs: ${state.seller}")
    Text("Summa: ${state.amountText} €", style = MaterialTheme.typography.titleLarge)
    Text("Nauda tiks simulēti aizturēta līdz preces saņemšanai.")
    Spacer(Modifier.weight(1f))
    Button(onClick = { viewModel.processPayment(false) }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
        Text("Apstiprināt maksājumu")
    }
    OutlinedButton(onClick = { viewModel.processPayment(true) }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
        Text("Testēt kļūdas stāvokli")
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
private fun ResultScreen(title: String, message: String, onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(message)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Turpināt") }
    }
}

