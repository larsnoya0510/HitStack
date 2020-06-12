package com.example.hitstack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class WoodRecyclerViewAdapter(
    private val context: Context,
    private val mOutList: MutableList<Int>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  , ItemTouchHelperAdapter{


    var mInList: MutableList<Int>
    val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        mInList = mOutList
    }
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(mInList,fromPosition,toPosition)
        notifyItemMoved(fromPosition,toPosition)
    }

    override fun onItemDissmiss(position: Int) {
        mInList.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        view = inflater.inflate(R.layout.viewholder_wood_recyclerview, parent, false)
        return WoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mInList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var mHolder = holder as WoodViewHolder
        mHolder.bind(position)
    }

    inner class WoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var woodNumberTextView = view.findViewById<TextView>(R.id.woodNumberTextView)
        fun bind(position: Int) {
            woodNumberTextView.text = mInList[position].toString()
        }
    }
    fun updateData(mOutList: MutableList<Int>) {
        mInList = mOutList
        notifyDataSetChanged()
    }
}