package lv.annajansone.drossdarijums.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class TransactionValidatorTest {
    @Test
    fun commaAmountIsAccepted() {
        val result = TransactionValidator.validate(TransactionInput("Fotoaparāts", "Laura_K", "245,00"))
        assertTrue(result.isValid)
        assertEquals(BigDecimal("245.00"), result.amount)
    }

    @Test
    fun blankSellerIsRejected() {
        val result = TransactionValidator.validate(TransactionInput("Fotoaparāts", "", "245,00"))
        assertFalse(result.isValid)
        assertEquals("Ievadi pārdevēju.", result.error)
    }

    @Test
    fun amountOutsideLimitsIsRejected() {
        val result = TransactionValidator.validate(TransactionInput("Fotoaparāts", "Laura_K", "0,50"))
        assertFalse(result.isValid)
    }

    @Test
    fun amountWithMoreThanTwoDecimalsIsRejected() {
        val result = TransactionValidator.validate(TransactionInput("Fotoaparāts", "Laura_K", "245,001"))
        assertFalse(result.isValid)
        assertEquals("Summai drīkst būt ne vairāk kā divas zīmes aiz komata.", result.error)
    }
}

