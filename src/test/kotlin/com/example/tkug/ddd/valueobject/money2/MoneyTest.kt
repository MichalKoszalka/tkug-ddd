package com.example.tkug.ddd.valueobject.money2

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

internal class MoneyTest {

    @Test
    fun `can sum up amounts from 2 orders when currencies match`() {
        // given
        val order = Order(totalAmount = Money(Currency.getInstance("PLN"), 20))
        val another = Order(totalAmount = Money(Currency.getInstance("PLN"), 20))

        // when
        val sum = order.totalAmount.add(another.totalAmount)

        // then
        assertEquals(sum, Money(Currency.getInstance("PLN"), 40))
    }

    @Test
    fun `cannot sum up amounts from 2 orders when currencies don't match`() {
        // given
        val order = Order(totalAmount = Money(Currency.getInstance("GBP"), 20))
        val another = Order(totalAmount = Money(Currency.getInstance("PLN"), 20))

        assertThrows<IllegalStateException> {
            order.totalAmount.add(another.totalAmount)
        }
    }

}