package com.telemed.interfaces

import android.view.View


interface OnItemMultipleClickListener{
    fun onSingleTapClick(view: View, position:Int)
    fun onDoubleTapClick(view: View, position:Int)
    fun onLongPressClick(view: View, position:Int)
}