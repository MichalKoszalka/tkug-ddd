package com.example.tkug.ddd.valueobject.money2

import java.util.*

data class Money(private val currency: Currency, private val amount: Int) {

    fun add(other: Money): Money {
        validateCurrency(other.currency)
        return Money(currency, this.amount + other.amount)
    }

    private fun validateCurrency(currency: Currency) {
        if(this.currency.currencyCode != currency.currencyCode) {
            throw IllegalStateException("Cannot perform operation on different currencies: ${this.currency.currencyCode} and ${currency.currencyCode}")
        }
    }

}

data class Order(val id: UUID = UUID.randomUUID(), val totalAmount: Money)