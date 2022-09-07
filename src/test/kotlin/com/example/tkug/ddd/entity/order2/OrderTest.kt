package com.example.tkug.ddd.entity.order2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class OrderTest {

    @Test
    fun `can accept received order`() {
        // given
        val receivedOrder = ReceivedOrder()

        // when
        val result = receivedOrder.accept()

        // then
        assertEquals(result::class.java, AcceptedOrder::class.java)
    }

    //    We got covered on compilation time
//    @Test
    fun `orders other than received cannot be accepted`() {
    }

    //    We got covered on compilation time
//    @Test
    fun `accepted order cannot be rejected`() {
    }

}