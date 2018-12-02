package com.kulya.dev.hotdelivery

import android.support.v4.app.Fragment
import com.kulya.dev.hotdelivery.data.Order
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kulya.dev.hotdelivery.adapter.DetailFragmentAdapter
import com.kulya.dev.hotdelivery.data.Good
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_order.*


class DetailFragment: AppCompatDialogFragment() {
    private var order: Order? = null
    private var detailFragmentAdapter: DetailFragmentAdapter? = null

    companion object {
        fun getInstance(order: Order): DetailFragment {
            val detailFragment = DetailFragment()

            val args = Bundle()
            args.putSerializable("order", order)
            detailFragment.arguments = args

            return detailFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        order = arguments?.getSerializable("order") as Order
        order?.let {
            detail_address.text = it.client.address
            detail_name.text = it.client.name
            detail_phone.text = it.client.phone
            detail_total.text = it.total
            updateAdapter(it.goodsList)
        }

    }


    private fun updateAdapter(goodList: MutableList<Good>) {
        detailFragmentAdapter?.update(goodList) ?: this setUpRecyclerView goodList
    }


    private infix fun setUpRecyclerView(goodList:MutableList<Good>){
        detailFragmentAdapter = DetailFragmentAdapter(goodList)
        with(detail_recycler){
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            adapter = detailFragmentAdapter
            scheduleLayoutAnimation()
        }
    }
}