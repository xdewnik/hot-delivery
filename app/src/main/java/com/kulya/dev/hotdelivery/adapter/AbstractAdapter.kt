package com.kulya.dev.hotdelivery.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.kulya.dev.hotdelivery.DiffUtilCallback
import com.kulya.dev.hotdelivery.ex.inflate

abstract class AbstractAdapter<ITEM> constructor(protected var itemList: MutableList<ITEM>,
                                                 private val layoutResId: Int)
    : RecyclerView.Adapter<AbstractAdapter.Holder>(){
    override fun getItemCount() = itemList.size


    protected lateinit var itemView: View

    protected lateinit var viewHolder: Holder

    private var isCoverAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = parent inflate layoutResId
        viewHolder = Holder(view)
        itemView = viewHolder.itemView
        itemView.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
//                onItemClick(itemView, adapterPosition)
            }
        }
        return viewHolder
    }


    fun coverPageDeleted(){
        isCoverAdded = false
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]
        holder.itemView.bind(item, position, holder)
    }

    fun update(items: MutableList<ITEM>) {
        updateAdapterWithDiffResult(calculateDiff(items))

    }

    private fun updateAdapterWithDiffResult(result: DiffUtil.DiffResult) {

        result.dispatchUpdatesTo(this)
    }

    private fun calculateDiff(newItems: MutableList<ITEM>) =
            DiffUtil.calculateDiff(DiffUtilCallback(itemList, newItems))

    fun add(item: ITEM) {
        itemList.add(item)
        notifyItemInserted(itemList.size)
    }

    fun remove(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear(){
        itemList.clear()
        notifyDataSetChanged()
    }

    fun getList() = itemList.toMutableList()


    fun updateData(newItems:MutableList<ITEM>){
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    final override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        onViewRecycled(holder.itemView)
    }

    protected open fun onViewRecycled(itemView: View) {
    }

    protected open fun onItemClick(itemView: View, position: Int) {
    }

    protected open fun View.bind(item: ITEM, position: Int, holder: Holder) {
    }

    public fun getCurrentPosition() = viewHolder.adapterPosition

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}