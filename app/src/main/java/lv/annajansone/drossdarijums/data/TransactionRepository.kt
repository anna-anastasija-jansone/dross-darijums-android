package lv.annajansone.drossdarijums.data

class TransactionRepository {
    private val transactions = mutableListOf<Transaction>()

    fun all(): List<Transaction> = transactions.toList()

    fun add(transaction: Transaction) {
        transactions.add(0, transaction)
    }
}

