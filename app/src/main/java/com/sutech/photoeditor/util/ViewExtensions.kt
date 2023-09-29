package com.sutech.photoeditor.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.SystemClock
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.sutech.photoeditor.R

fun View.showIfInv() {
    if (visibility == View.INVISIBLE)
        visibility = View.VISIBLE
}
fun Context.getActionBarHeight(): Int {
    val tv = TypedValue()
    if (this.theme?.resolveAttribute(
            android.R.attr.actionBarSize,
            tv,
            true
        ) == true
    ) {
        return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return 0
}

fun View.changeBackgroundColor(newColor: Int) {
    setBackgroundColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun Context?.replaceFragment(
    fragmentManager: FragmentManager,
    @IdRes id: Int = R.id.container,
    fragment: Fragment,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    fragmentManager.beginTransaction()
        .apply {
            add(id, fragment, tag)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
}

fun Drawable.applyColorFilter(color: Int) = mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun ImageView.setTintColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
}

fun TextView.changeTextColor(newColor: Int) {
    setTextColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun View.animRotation() {
    val anim = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    anim.interpolator = LinearInterpolator()
    anim.duration = 1500
    anim.isFillEnabled = true
    anim.repeatCount = Animation.INFINITE
    anim.fillAfter = true
    startAnimation(anim)
}

fun View.isShow() = visibility == View.VISIBLE
fun View.isGone() = visibility == View.GONE
fun View.isInvisible() = visibility == View.INVISIBLE

fun View.isVisible(isShow:Boolean) {
    if (isShow){
        show()
    } else{
        gone()
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inv() {
    visibility = View.INVISIBLE
}

fun View.setPreventDoubleClick(debounceTime: Long = 500 , action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.setPreventDoubleClickScaleView(debounceTime: Long ?=200, action: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        private var lastClickTime: Long = 0
        private var rect: Rect? = null
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            fun setScale(scale: Float) {
                v.scaleX = scale
                v.scaleY = scale
            }
            if (event.action == MotionEvent.ACTION_DOWN) {
                //action down: scale view down
                rect = Rect(v.left, v.top, v.right, v.bottom)
                setScale(0.9f)
            } else if (rect != null && !rect!!.contains(
                    v.left + event.x.toInt(),
                    v.top + event.y.toInt()
                )
            ) {
                //action moved out
                setScale(1f)
                return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                //action up
                setScale(1f)
                //handle click too fast
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime!!) {
                } else {
                    lastClickTime = SystemClock.elapsedRealtime()
                    action()
                }
            } else {
                //other
            }
            return true
        }
    })
}

private var lastClickTimeItem: Long = 0
fun View.setPreventDoubleClickItemScaleView(debounceTime: Long =500, action: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        private var rect: Rect? = null
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            fun setScale(scale: Float) {
                v.scaleX = scale
                v.scaleY = scale
            }
            if (event.action == MotionEvent.ACTION_DOWN) {
                //action down: scale view down
                rect = Rect(v.left, v.top, v.right, v.bottom)
                setScale(0.9f)
            } else if (rect != null && !rect!!.contains(
                    v.left + event.x.toInt(),
                    v.top + event.y.toInt()
                )
            ) {
                //action moved out
                setScale(1f)
                return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                //action up
                setScale(1f)
                //handle click too fast
                if (SystemClock.elapsedRealtime() - lastClickTimeItem < debounceTime) {
                } else {
                    lastClickTimeItem = SystemClock.elapsedRealtime()
                    action()
                }
            } else {
                //other
            }
            return true
        }
    })
}

fun View.setPreventDoubleClickItem(debounceTime: Long = 500 , action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTimeItem < debounceTime) return
            action()
            lastClickTimeItem = SystemClock.elapsedRealtime()
        }
    })
}
fun Fragment.displayToast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.displayToast(@StringRes msg: Int) {
    Toast.makeText(context, getString(msg), Toast.LENGTH_SHORT).show()
}

fun Fragment.convertDpToPx(dp: Int): Int {
    val dip = dp.toFloat()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()
}

fun Context.convertDpToPx(dp: Int): Int {
    val dip = dp.toFloat()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()
}


fun View.slideShowToLeft() {
    isVisible(true)
    YoYo.with(Techniques.SlideInUp)
        .duration(500)
        .playOn(this)
}

fun View.slideHideToLeft() {
    isVisible(false)
    YoYo.with(Techniques.SlideInRight)
        .duration(500)
        .playOn(this)

}

fun View.slideShowToRight(duration:Long=500) {
    isVisible(true)
    YoYo.with(Techniques.SlideInRight)
        .duration(duration)
        .playOn(this)
}

fun View.slideHideToRight(duration:Long=500) {
    YoYo.with(Techniques.SlideInLeft)
        .duration(duration)
        .playOn(this)
    isVisible(false)
}
fun View.slideShowToFade(duration:Long=500) {
    isVisible(true)
    YoYo.with(Techniques.FadeInUp)
        .duration(duration)
        .playOn(this)
}

fun View.slideHideToFade(duration:Long=500) {
    YoYo.with(Techniques.FadeInUp)
        .duration(duration)
        .playOn(this)
    isVisible(false)
}
fun View.slideShowToUp(duration:Long=500) {
    isVisible(true)
    YoYo.with(Techniques.SlideInUp)
        .duration(duration)
        .playOn(this)
}

fun View.slideHideToUp(duration:Long=500) {
    YoYo.with(Techniques.SlideInUp)
        .duration(duration)
        .playOn(this)
    isVisible(false)

}