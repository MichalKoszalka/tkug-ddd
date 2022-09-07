package com.example.tkug.ddd.entity.order1

import java.util.*

//cohesion, identity, lifecycle
class Order(
    var status: OrderStatus
) {

    val id: UUID = UUID.randomUUID()

    enum class OrderStatus {
        received, accepted, rejected
    }


    //invariants

    // e.g. order cannot be rejected if was accepted
    fun reject() {
        if (status == OrderStatus.accepted) {
            throw IllegalStateException("Cannot reject already accepted order!!!")
        }
        status = OrderStatus.rejected
    }

    // e.g. only received order can be accepted
    fun accept() {
        if (status != OrderStatus.received) {
            throw IllegalStateException("Cannot accept order not in received status")
        }
        status = OrderStatus.accepted
    }
}