package com.example.hitstack

interface ItemTouchHelperAdapter {
    fun onItemMove( fromPosition : Int, toPosition :Int)
    fun onItemDissmiss(position: Int)
}