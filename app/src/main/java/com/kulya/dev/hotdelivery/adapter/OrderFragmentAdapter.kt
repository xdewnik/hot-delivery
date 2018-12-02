package com.kulya.dev.hotdelivery.adapter

import android.view.View
import com.kulya.dev.hotdelivery.R
import com.kulya.dev.hotdelivery.data.Order
import kotlinx.android.synthetic.main.item_order.view.*

class OrderFragmentAdapter constructor(
        orderList:MutableList<Order>,
        private val orderClickListener: OrderClickListener
)
    : AbstractAdapter<Order>(orderList, R.layout.item_order) {


    override fun onItemClick(itemView: View, position: Int) {
        orderClickListener.onOrderClick(itemList[position])


    }

    override fun View.bind(item: Order, position: Int, holder: Holder) {
        this.setOnClickListener{
            onItemClick(this, holder.adapterPosition)
        }
        item_order_address.text = item.client.address
        item_order_id.text = item.orderId
        item_order_phone_name.text = item.client.phone

    }

    interface OrderClickListener{
        fun onOrderClick(order:Order)
    }

}
