package lv.annajansone.drossdarijums.data

class TransactionRepository {
    private val transactions = mutableListOf<Transaction>()

    fun all(): List<Transaction> = transactions.toList()

    fun find(id: String): Transaction? = transactions.find { it.id == id }

    fun add(transaction: Transaction) {
        transactions.add(0, transaction)
    }

    fun markShipped(id: String): Transaction? {
        val index = transactions.indexOfFirst { it.id == id }
        if (index == -1) return null
        if (transactions[index].status != TransactionStatus.PROTECTED) return null

        val shipped = transactions[index].copy(status = TransactionStatus.SHIPPED)
        transactions[index] = shipped
        return shipped
    }

    fun complete(id: String): Transaction? {
        val index = transactions.indexOfFirst { it.id == id }
        if (index == -1) return null
        if (transactions[index].status != TransactionStatus.SHIPPED) return null

        val completed = transactions[index].copy(status = TransactionStatus.COMPLETED)
        transactions[index] = completed
        return completed
    }
}
