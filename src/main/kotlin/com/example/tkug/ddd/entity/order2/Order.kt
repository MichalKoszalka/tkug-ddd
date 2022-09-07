package com.example.tkug.ddd.entity.order2

import java.util.*

sealed class Order(val id: UUID = UUID.randomUUID()) {

}

class ReceivedOrder : Order() {

    fun accept() = AcceptedOrder(this.id)
    fun reject() = RejectedOrder(this.id)

}

class RejectedOrder(id: UUID) : Order(id)

class AcceptedOrder(id: UUID) : Order(id)

