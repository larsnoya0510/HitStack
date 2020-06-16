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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import android.R.attr.resource
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import android.R.attr.resource
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat.registerAnimationCallback
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.ViewTarget


class MainActivity : AppCompatActivity() {
    lateinit var woodsList:MutableList<Int>
    lateinit var woodRecyclerViewAdapter : WoodRecyclerViewAdapter
    var TOLEFT : Int =99
    var TORIGHT : Int =98
    var USER_TOUCH_TYPE_1 : Int =97
    var hitState=false
    var initGifLock=true
    lateinit var gifLeft : ViewTarget<ImageView,GifDrawable>
    lateinit var gifRight : ViewTarget<ImageView,GifDrawable>
    lateinit var gifDust : ViewTarget<ImageView,GifDrawable>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        woodsList = mutableListOf<Int>()
        initWood()
        initHit()
        buttonReset.setOnClickListener { resetRecyclerView() }
        leftHitConstraintLayout.setOnClickListener {  leftHit() }
        rightHitConstraintLayout.setOnClickListener {  rightHit() }
    }

    private fun initHit() {
        initHitManLeft(R.drawable.hitrleftfix,imageViewHitLeft)
        initHitManRight(R.drawable.hitrightfix,imageViewHitRight)
        initDust()
    }
    private fun initHitManLeft(mDrawable : Int,mView:ImageView){
        gifLeft=Glide.with(this).asGif().load(mDrawable).listener(object :
            RequestListener<GifDrawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false;
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.setLoopCount(1)
                resource?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                           if(initGifLock==false) swipeFromLeft()
                    }
                })
                return false
            }

        }).into(mView)

    }
    private fun initHitManRight(mDrawable : Int,mView:ImageView){
        gifRight=Glide.with(this).asGif().load(mDrawable).listener(object :
            RequestListener<GifDrawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false;
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.setLoopCount(1)
                resource?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        if(initGifLock==false)  swipeFromRight()
                    }
                })
                return false
            }

        }).into(mView)

    }
    private fun initDust() {
//        Glide.with(this).asGif().load(R.drawable.dust).into(imageViewDust)
        gifDust=Glide.with(this).asGif().load(R.drawable.dust).listener(object :
            RequestListener<GifDrawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false;
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.setLoopCount(1)
                resource?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        hitState = false
                    }
                })
//                Thread(Runnable {
//                    while (true) {
//                        if (!resource!!.isRunning()) {
//                            break
//                        }
//                    }
//                }).start()
                return false
            }

        }).into(imageViewDust)
//        hitState = false
    }
    private fun leftHit(){
        initGifLock=false
        if(hitState!=true) {
            hitState = true
            gifLeft.onStart()
        }
    }

    private fun swipeFromLeft() {
        analogUserScroll(
            hitAreaConstraintLayout,
            0,
            100F,
            hitAreaConstraintLayout.bottom * 1F - 100,
            hitAreaConstraintLayout.right * 1F - 50,
            hitAreaConstraintLayout.bottom * 1F - 100
        ) {
//            initDust()
            gifDust.onStart()
        }
    }

    private fun rightHit(){
        initGifLock=false
        if(hitState!=true) {
            hitState = true
            gifRight.onStart()
        }
    }

    private fun swipeFromRight() {
        analogUserScroll(
            hitAreaConstraintLayout,
            0,
            hitAreaConstraintLayout.right * 1F - 50,
            hitAreaConstraintLayout.bottom * 1F - 100,
            100F,
            hitAreaConstraintLayout.bottom * 1F - 100
        ) {
//            initDust()
            gifDust.onStart()
        }
    }

    private fun resetRecyclerView() {
        woodsList.clear()
        for (i in 0 until 10) {
            woodsList.add(i)
        }
        woodRecyclerViewAdapter.updateData(woodsList)
        woodRecyclerView.post {
            woodRecyclerView.smoothScrollToPosition(0)
        }
//        woodRecyclerView.post {
////            woodRecyclerView.smoothScrollToPosition(woodRecyclerView.bottom)
//            woodRecyclerView.smoothScrollBy(0,woodRecyclerView.bottom)
//        }

//        woodRecyclerView.smoothScrollBy(0,woodRecyclerView.bottom)
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
    private fun analogUserScroll(view: View?, type: Int, p1x: Float, p1y: Float, p2x: Float, p2y: Float,callback: () -> Unit) {
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
        callback.invoke()
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
