package com.kulya.dev.hotdelivery.data

import java.io.Serializable

data class Order(val goodsList: MutableList<Good>, val orderId:String, val client:Client, val total:String): Serializable