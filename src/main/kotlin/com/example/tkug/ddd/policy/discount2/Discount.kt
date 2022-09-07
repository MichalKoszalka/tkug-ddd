package com.example.tkug.ddd.policy.discount2

import java.util.*


// Imagine we want to apply some discounts based on
// 1. no of items in order - e.g. 3 items and more, means 5% discount
// 2. customer status - if he is in loyalty program, then 10% discount
// 3. discounts cannot be combined
// 4. loyalty discount is more important

sealed class CustomerOrder(
    val customerData: CustomerData,
    val orderLines: List<OrderLine>
) {

    fun totalQuantity() = orderLines.sumOf { it.quantity }

    data class CustomerData(val id: UUID, val name: String, val isInLoyaltyProgram: Boolean = false)
    data class OrderLine(val productId: UUID, val quantity: Int, val unitPrice: Int)

}

class DraftOrder(customerData: CustomerData, orderLines: List<OrderLine>, val totalAmounts: Int = 0) : CustomerOrder(
    customerData,
    orderLines
) {

    var discountPolicy: DiscountPolicy =  DiscountPolicy.NoDiscountPolicy

    fun purchase(): PurchaseOrder {
        val discountedOrder = discountPolicy.apply(this)
        return PurchaseOrder(discountedOrder.totalAmounts, discountedOrder.customerData, discountedOrder.orderLines)
    }

}

fun List<CustomerOrder.OrderLine>.getTotalAmounts() = sumOf { it.quantity * it.unitPrice }

interface DiscountPolicy {

    fun apply(draftOrder: DraftOrder): DraftOrder

    object NoDiscountPolicy : DiscountPolicy {
        override fun apply(draftOrder: DraftOrder): DraftOrder {
            return draftOrder
        }

    }
}

class TotalItemsQuantityDiscountPolicy : DiscountPolicy {

    fun isApplicable(draftOrder: DraftOrder): Boolean {
        return draftOrder.totalQuantity() > 2
    }

    override fun apply(draftOrder: DraftOrder): DraftOrder {
        return DraftOrder(
            draftOrder.customerData,
            draftOrder.orderLines,
            (draftOrder.orderLines.getTotalAmounts() * 0.95).toInt()
        )
    }

}

class LoyaltyProgramDiscountPolicy : DiscountPolicy {

    fun isApplicable(draftOrder: DraftOrder): Boolean {
        return draftOrder.customerData.isInLoyaltyProgram
    }

    override fun apply(draftOrder: DraftOrder): DraftOrder {
        return DraftOrder(
            draftOrder.customerData,
            draftOrder.orderLines,
            (draftOrder.orderLines.getTotalAmounts() * 0.9).toInt()
        )
    }

}

class DiscountPolicyFactory {

    private val loyaltyProgramDiscountPolicy = LoyaltyProgramDiscountPolicy()
    private val totalItemsQuantityDiscountPolicy = TotalItemsQuantityDiscountPolicy()

    fun createDiscountPolicy(draftOrder: DraftOrder): DiscountPolicy {
        return when {
            loyaltyProgramDiscountPolicy.isApplicable(draftOrder) -> loyaltyProgramDiscountPolicy
            totalItemsQuantityDiscountPolicy.isApplicable(draftOrder) -> loyaltyProgramDiscountPolicy
            else -> DiscountPolicy.NoDiscountPolicy
        }

    }

}

class PurchaseOrder(val totalAmounts: Int, customerData: CustomerData, orderLines: List<OrderLine>) :
    CustomerOrder(
        customerData,
        orderLines
    )