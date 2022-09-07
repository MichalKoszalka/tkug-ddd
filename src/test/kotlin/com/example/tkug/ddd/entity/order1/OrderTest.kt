package com.example.tkug.ddd.entity.order1

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals

internal class OrderTest {

    @Test
    fun `can accept received order`() {
        // given
        val receivedOrder = Order(Order.OrderStatus.received)

        // when
        receivedOrder.accept()

        // then
        assertEquals(receivedOrder.status, Order.OrderStatus.accepted)
    }

    @ParameterizedTest
    @EnumSource(
        value = Order.OrderStatus::class,
                names = [ "received" ],
        mode = EnumSource.Mode.EXCLUDE
    )
    fun `orders other than received cannot be accepted`(status: Order.OrderStatus) {
        // given
        val receivedOrder = Order(Order.OrderStatus.received)

        // when
        receivedOrder.accept()

        // then
        assertEquals(receivedOrder.status, Order.OrderStatus.accepted)
    }

    @Test
    fun `accepted order cannot be rejected`() {
        // given
        val acceptedOrder = Order(Order.OrderStatus.accepted)

        // when + then
        assertThrows<IllegalStateException> {
            acceptedOrder.reject()
        }
    }

}