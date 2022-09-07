package com.example.tkug.ddd.policy.discount1

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PurchaseOrderTest {

    private val normalCustomer = CustomerOrder.CustomerData(UUID.randomUUID(), "John Doe")
    private val loyalCustomer = CustomerOrder.CustomerData(UUID.randomUUID(), "Jimmy Doe", isInLoyaltyProgram = true)

    private val orderLineWithOneItem =  CustomerOrder.OrderLine(UUID.randomUUID(), 1, 100)
    private val orderLineWithTwoItems =  CustomerOrder.OrderLine(UUID.randomUUID(), 2, 100)

    @Test
    fun `total amount for order not eligible for discount`() {
        // given
        val draftOrder = DraftOrder(normalCustomer, listOf(orderLineWithOneItem))

        // when
        val purchaseOrder = draftOrder.purchase()

        // then
        assertEquals(purchaseOrder.totalAmounts, 100)
    }

    @Test
    fun `total amounts for order with more than 2 items`() {
        // given
        val draftOrder = DraftOrder(normalCustomer, listOf(orderLineWithOneItem, orderLineWithTwoItems))

        // when
        val purchaseOrder = draftOrder.purchase()

        // then
        assertEquals(purchaseOrder.totalAmounts, (300*0.95).toInt())
    }

    @Test
    fun `total amounts for order from customer with loyalty program`() {
        // given
        val draftOrder = DraftOrder(loyalCustomer, listOf(orderLineWithOneItem))

        // when
        val purchaseOrder = draftOrder.purchase()

        // then
        assertEquals(purchaseOrder.totalAmounts, 90)
    }

    @Test
    fun `calculates total amount for loyal customer if both discount applicable`() {
        // given
        val draftOrder = DraftOrder(loyalCustomer, listOf(orderLineWithOneItem, orderLineWithTwoItems))

        // when
        val purchaseOrder = draftOrder.purchase()

        // then
        assertEquals(purchaseOrder.totalAmounts, (300*0.90).toInt())
    }


}