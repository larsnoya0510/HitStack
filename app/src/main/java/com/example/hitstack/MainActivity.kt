package com.example.hitstack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.os.SystemClock
import android.view.MotionEvent.ACTION_DOWN
import android.view.View



class MainActivity : AppCompatActivity() {
    lateinit var woodsList:MutableList<Int>
    lateinit var woodRecyclerViewAdapter : WoodRecyclerViewAdapter
    var TOLEFT : Int =99
    var TORIGHT : Int =98
    var USER_TOUCH_TYPE_1 : Int =97

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        woodsList = mutableListOf<Int>()
        initWood()
        buttonReset.setOnClickListener { resetRecyclerView() }
        leftHitConstraintLayout.setOnClickListener {  leftHit() }
        rightHitConstraintLayout.setOnClickListener {  rightHit() }
    }
    private fun leftHit(){
        analogUserScroll(hitAreaConstraintLayout,0,100F,hitAreaConstraintLayout.bottom*1F-100,hitAreaConstraintLayout.right*1F-50,hitAreaConstraintLayout.bottom*1F-100)
    }
    private fun rightHit(){
        analogUserScroll(hitAreaConstraintLayout,0,hitAreaConstraintLayout.right*1F-50,hitAreaConstraintLayout.bottom*1F-100,100F,hitAreaConstraintLayout.bottom*1F-100)
    }
    private fun resetRecyclerView() {
        woodsList.clear()
        for (i in 0 until 10) {
            woodsList.add(i)
        }
        woodRecyclerViewAdapter.updateData(woodsList)
//        woodRecyclerView.smoothScrollToPosition(woodRecyclerView.bottom)
        woodRecyclerView.smoothScrollBy(0,woodRecyclerView.bottom)
    }

    private fun initWood() {

        woodsList.clear()
        for(i in 0 until 10){
            woodsList.add(i)
        }
        woodRecyclerViewAdapter = WoodRecyclerViewAdapter(this,woodsList)
        woodRecyclerView.adapter = woodRecyclerViewAdapter

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        woodRecyclerView.layoutManager = layoutManager

        val callback = ItemTouchHelperCallback(woodRecyclerViewAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(woodRecyclerView)

        woodRecyclerView.smoothScrollToPosition(woodRecyclerView.bottom)
    }
    private fun analogUserScroll(view: View?, type: Int, p1x: Float, p1y: Float, p2x: Float, p2y: Float) {
        if (view == null) {
            return
        }
        val downTime = SystemClock.uptimeMillis()//模拟按下去的时间

        var eventTime = downTime

        var pX = p1x
        var pY = p1y
        var speed = 0//快速滑动
        var touchTime = 100f//模拟滑动时发生的触摸事件次数

        //平均每次事件要移动的距离
        val perX = (p2x - p1x) / touchTime
        val perY = (p2y - p1y) / touchTime

        val isReversal = perX < 0 || perY < 0//判断是否反向：手指从下往上滑动，或者手指从右往左滑动
        val isHandY = Math.abs(perY) > Math.abs(perX)//判断是左右滑动还是上下滑动

        if (type == USER_TOUCH_TYPE_1) {//加速滑动
            touchTime = 10f//如果是快速滑动，则发生的触摸事件比均匀滑动更少
            speed = if (isReversal) -20 else 20//反向移动则坐标每次递减
        }

        //模拟用户按下
        val downEvent = MotionEvent.obtain(
            downTime, eventTime,
            ACTION_DOWN, pX, pY, 0
        )
//        view!!.onTouchEvent(downEvent)
        view.dispatchTouchEvent(downEvent)
        //模拟移动过程中的事件
        val moveEvents = mutableListOf<MotionEvent>()
        var isSkip = false
        run {
            var i = 0
            while (i < touchTime) {

                pX += perX + speed
                pY += perY + speed
                if (isReversal && pX < p2x || !isReversal && pX > p2x) {
                    pX = p2x
                    isSkip = !isHandY
                }

                if (isReversal && pY < p2y || !isReversal && pY > p2y) {
                    pY = p2y
                    isSkip = isHandY
                }
                eventTime += 50000//事件发生的时间要不断递增
                val moveEvent = getMoveEvent(downTime, eventTime, pX, pY)
                moveEvents.add(moveEvent)
//                view!!.onTouchEvent(moveEvent)
                view.dispatchTouchEvent(moveEvent)
                if (type == USER_TOUCH_TYPE_1) {//加速滑动
                    speed += if (isReversal) -70 else 70
                }
                if (isSkip) {
                    break
                }
                i++
            }
        }

        //模拟手指离开屏幕
        val upEvent = MotionEvent.obtain(
            downTime, eventTime,
            MotionEvent.ACTION_UP, pX, pY, 0
        )
//        view!!.onTouchEvent(upEvent)
        view.dispatchTouchEvent(upEvent)
        //回收触摸事件
        downEvent.recycle()
        for (i in moveEvents.indices) {
            moveEvents.get(i).recycle()
        }
        upEvent.recycle()
    }
    private  fun getMoveEvent( downTime :Long,  evntTime :Long,  x : Float,  y: Float) : MotionEvent {
            var mdownTime=downTime
            var mevntTime=evntTime
            var mx=x
            var my=y
        var mMotionEvent =  MotionEvent.obtain(mdownTime, mevntTime,MotionEvent.ACTION_MOVE, mx, my, 0)
        return mMotionEvent
    }
}
