package com.kulya.dev.hotdelivery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kulya.dev.hotdelivery.data.Order
import kotlinx.android.synthetic.main.dialog_delivery.*
import kotlinx.android.synthetic.main.fragment_detail.*

class DeliveryDialog: AppCompatDialogFragment() {
    private var order: Order? = null

    companion object {
        fun getInstance(order: Order): DeliveryDialog {
            val detailFragment = DeliveryDialog()

            val args = Bundle()
            args.putSerializable("order", order)
            detailFragment.arguments = args

            return detailFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_delivery, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        order = arguments?.getSerializable("order") as Order
        order?.let {
            delivery_total.text = order!!.total
        }

    }

}