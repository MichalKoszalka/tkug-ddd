package com.example.tkug.ddd.valueobject.money1

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MoneyTest {

    @Test
    fun `can sum up amounts from 2 orders`() {
        // given
        val order = Order(totalAmount = 10)
        val another = Order(totalAmount = 10)

        // when
        val sum = order.totalAmount + another.totalAmount

        // then
        assertEquals(sum, 20)
    }

    @Test
    fun `can do whatever I want`() {
        // given
        val order = Order(totalAmount = 10)
        val another = Order(totalAmount = 10)

        // when
        Order(totalAmount =  order.totalAmount * another.totalAmount)
        Order(totalAmount =  order.totalAmount / another.totalAmount)

    }

}