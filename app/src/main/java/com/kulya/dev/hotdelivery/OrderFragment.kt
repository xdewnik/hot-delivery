package com.kulya.dev.hotdelivery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kulya.dev.hotdelivery.adapter.OrderFragmentAdapter
import com.kulya.dev.hotdelivery.data.Client
import com.kulya.dev.hotdelivery.data.Good
import com.kulya.dev.hotdelivery.data.Order
import com.kulya.dev.hotdelivery.ex.hide
import com.kulya.dev.hotdelivery.ex.show
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment: Fragment(), OrderFragmentAdapter.OrderClickListener {


    private var orderList: MutableList<Order>? = null

    private var orderFragmentAdapter: OrderFragmentAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.title = "Orders"
        val igor = Client("Igor","Dobr 14124 321", "+380677577827")
        orderList = mutableListOf(
            Order(mutableListOf(Good("https://images.pizza33.ua/products_for_catalog/F87hxnCbFeBIeznrX4rJZtvODfoMLMuD.jpg","Bavarskaya","100 grn","1")
                ), "1541241", igor,"100 grn"
            ),   Order(mutableListOf(Good("https://images.pizza33.ua/products_for_catalog/F87hxnCbFeBIeznrX4rJZtvODfoMLMuD.jpg","Bavarskaya","100 grn","1")
            ), "1541241", igor,"100 grn"
            ),  Order(mutableListOf(Good("https://images.pizza33.ua/products_for_catalog/F87hxnCbFeBIeznrX4rJZtvODfoMLMuD.jpg","Bavarskaya","100 grn","1")
            ), "1541241", igor,"100 grn"
            )
        )

        if (orderList != null) {
            order_recycler.show()
            order_empty.hide()
            updateAdapter(orderList!!)
        }else{
            order_recycler.hide()
            order_empty.show()
        }
    }


    private fun updateAdapter(orderList: MutableList<Order>) {
        orderFragmentAdapter?.update(orderList) ?: this setUpRecyclerView orderList
    }


    private infix fun setUpRecyclerView(orderList:MutableList<Order>){
        orderFragmentAdapter = OrderFragmentAdapter(orderList, this)
        with(order_recycler){
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            adapter = orderFragmentAdapter
            scheduleLayoutAnimation()
        }
    }

    override fun onOrderClick(order: Order) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.container, DetailFragment.getInstance(order))
        fragmentTransaction?.commit()
    }
}