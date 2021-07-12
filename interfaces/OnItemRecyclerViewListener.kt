package com.telemed.interfaces

import android.view.View
import com.telemed.models.BaseModel


interface OnItemRecyclerViewListener {
    fun onItemRender(view: View, `object`: BaseModel)
}