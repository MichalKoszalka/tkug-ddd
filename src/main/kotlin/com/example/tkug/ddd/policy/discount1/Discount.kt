package com.example.tkug.ddd.policy.discount1

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

class DraftOrder(customerData: CustomerData, orderLines: List<OrderLine>) : CustomerOrder(
    customerData,
    orderLines
) {

    fun purchase(): PurchaseOrder {
        var totalAmounts = orderLines.sumOf { it.unitPrice * it.quantity }
        if (customerData.isInLoyaltyProgram) {
            totalAmounts = (totalAmounts * 0.9).toInt()
            return PurchaseOrder(totalAmounts, customerData, orderLines)
        } else if (totalQuantity() >= 3) {
            totalAmounts = (totalAmounts * 0.95).toInt()
            return PurchaseOrder(totalAmounts, customerData, orderLines)
        }
        return PurchaseOrder(totalAmounts, customerData, orderLines)

    }

}

class PurchaseOrder(val totalAmounts: Int, customerData: CustomerData, orderLines: List<OrderLine>) :
    CustomerOrder(
        customerData,
        orderLines
    )