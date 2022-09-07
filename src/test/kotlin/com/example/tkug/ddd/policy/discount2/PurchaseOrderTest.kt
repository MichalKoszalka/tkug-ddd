package com.example.tkug.ddd.policy.discount2

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


private val normalCustomer = CustomerOrder.CustomerData(UUID.randomUUID(), "John Doe")
private val loyalCustomer = CustomerOrder.CustomerData(UUID.randomUUID(), "Jimmy Doe", isInLoyaltyProgram = true)

private val orderLineWithOneItem =  CustomerOrder.OrderLine(UUID.randomUUID(), 1, 100)
private val orderLineWithTwoItems =  CustomerOrder.OrderLine(UUID.randomUUID(), 2, 100)

internal class LoyaltyProgramDiscountPolicyTest {

    private val loyaltyProgramDiscountPolicy = LoyaltyProgramDiscountPolicy()

    @Test
    fun `is applicable for loyal customer order`() {
        // given
        val draftOrderForLoyalCustomer = DraftOrder(loyalCustomer, listOf(orderLineWithOneItem))

        // when + then
        assertTrue(loyaltyProgramDiscountPolicy.isApplicable(draftOrderForLoyalCustomer))
    }

    @Test
    fun `is not applicable for normal customer order`() {
        // given
        val draftOrderForLoyalCustomer = DraftOrder(normalCustomer, listOf(orderLineWithOneItem))

        // when + then
        assertFalse(loyaltyProgramDiscountPolicy.isApplicable(draftOrderForLoyalCustomer))
    }

    @Test
    fun `applies 10 percent discounts`() {
        // given
        val draftOrderForLoyalCustomer = DraftOrder(loyalCustomer, listOf(orderLineWithOneItem))

        // when
        val totalAmountsAfterDiscount = loyaltyProgramDiscountPolicy.apply(draftOrderForLoyalCustomer).totalAmounts

        // then
        assertEquals(totalAmountsAfterDiscount, 90)
    }

}

internal class TotalItemsQuantityDiscountPolicyTest {

    private val totalItemsQuantityDiscountPolicy = TotalItemsQuantityDiscountPolicy()

    @Test
    fun `is applicable for order with 3 items`() {
        // given
        val draftOrderWith3Items = DraftOrder(normalCustomer, listOf(orderLineWithTwoItems, orderLineWithOneItem))

        // when + then
        assertTrue(totalItemsQuantityDiscountPolicy.isApplicable(draftOrderWith3Items))
    }

    @Test
    fun `is not applicable for order with 2 items`() {
        // given
        val draftOrderWith2Items = DraftOrder(normalCustomer, listOf(orderLineWithTwoItems))

        // when + then
        assertFalse(totalItemsQuantityDiscountPolicy.isApplicable(draftOrderWith2Items))
    }

    @Test
    fun `applies 5 percent discounts`() {
        // given
        val draftOrderWith3Items = DraftOrder(normalCustomer, listOf(orderLineWithTwoItems, orderLineWithOneItem))

        // when
        val totalAmountsAfterDiscount = totalItemsQuantityDiscountPolicy.apply(draftOrderWith3Items).totalAmounts

        // then
        assertEquals(totalAmountsAfterDiscount, 285)
    }

}

internal class DiscountPolicyFactoryTest {

    private val discountPolicyFactory = DiscountPolicyFactory()

    @Test
    fun `loyalty program discount policy more important than total quantity discount policy`() {
        // given
        val draftOrderWith3ItemsFromLoyalCustomer = DraftOrder(loyalCustomer, listOf(orderLineWithTwoItems, orderLineWithOneItem))

        // when
        val policy = discountPolicyFactory.createDiscountPolicy(draftOrderWith3ItemsFromLoyalCustomer)

        // then
        assertTrue(policy is LoyaltyProgramDiscountPolicy)
    }

    @Test
    fun `no discount policy when not applicable`() {
        // given
        val draftOrderWith2ItemsFromNormalCustomer = DraftOrder(normalCustomer, listOf(orderLineWithTwoItems))

        // when
        val policy = discountPolicyFactory.createDiscountPolicy(draftOrderWith2ItemsFromNormalCustomer)

        // then
        assertTrue(policy is DiscountPolicy.NoDiscountPolicy)
    }

}

internal class DraftOrderTest {

    @Test
    fun `order can be purchased with discount policy`() {
        // given
        val draftOrderWith3ItemsFromLoyalCustomer = DraftOrder(loyalCustomer, listOf(orderLineWithTwoItems, orderLineWithOneItem))
        draftOrderWith3ItemsFromLoyalCustomer.discountPolicy = LoyaltyProgramDiscountPolicy()

        // when
        val purchaseOrder = draftOrderWith3ItemsFromLoyalCustomer.purchase()

        // then
        assertEquals(purchaseOrder.totalAmounts, 270)
    }

}