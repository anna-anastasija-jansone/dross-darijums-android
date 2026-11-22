package lv.annajansone.drossdarijums.data

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Test

class TransactionRepositoryTest {
    private fun transaction() = Transaction(
        id = "DD-TEST",
        item = "Austiņas",
        seller = "Pārdevējs",
        itemAmount = BigDecimal("50.00"),
        commissionFee = BigDecimal("1.50"),
        buyerTotal = BigDecimal("51.50"),
        sellerReceives = BigDecimal("50.00"),
    )

    @Test
    fun `protected transaction cannot be completed before it is shipped`() {
        val repository = TransactionRepository()
        repository.add(transaction())

        assertNull(repository.complete("DD-TEST"))
        assertEquals(TransactionStatus.PROTECTED, repository.find("DD-TEST")?.status)
    }

    @Test
    fun `transaction follows protected shipped completed sequence`() {
        val repository = TransactionRepository()
        repository.add(transaction())

        val shipped = repository.markShipped("DD-TEST")
        val completed = repository.complete("DD-TEST")

        assertNotNull(shipped)
        assertEquals(TransactionStatus.SHIPPED, shipped?.status)
        assertNotNull(completed)
        assertEquals(TransactionStatus.COMPLETED, completed?.status)
    }

    @Test
    fun `shipped transaction cannot be marked shipped again`() {
        val repository = TransactionRepository()
        repository.add(transaction())
        repository.markShipped("DD-TEST")

        assertNull(repository.markShipped("DD-TEST"))
        assertEquals(TransactionStatus.SHIPPED, repository.find("DD-TEST")?.status)
    }
}
