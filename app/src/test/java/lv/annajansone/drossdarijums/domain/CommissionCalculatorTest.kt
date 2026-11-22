package lv.annajansone.drossdarijums.domain

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test

class CommissionCalculatorTest {
    @Test
    fun `adds three percent commission to buyer total`() {
        val quote = CommissionCalculator.calculate(BigDecimal("100.00"))

        assertEquals(BigDecimal("100.00"), quote.itemAmount)
        assertEquals(BigDecimal("3.00"), quote.commissionFee)
        assertEquals(BigDecimal("103.00"), quote.buyerTotal)
        assertEquals(BigDecimal("100.00"), quote.sellerReceives)
    }

    @Test
    fun `rounds commission to cents using standard rounding`() {
        val quote = CommissionCalculator.calculate(BigDecimal("18.60"))

        assertEquals(BigDecimal("0.56"), quote.commissionFee)
        assertEquals(BigDecimal("19.16"), quote.buyerTotal)
    }
}
