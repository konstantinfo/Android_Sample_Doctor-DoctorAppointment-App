package com.telemed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.telemed.R
import com.telemed.api.response.SlotsResponse
import com.telemed.databinding.ItemSlotsBinding
import com.telemed.databinding.ItemSlotsBookingBinding
import com.telemed.interfaces.*
import com.telemed.models.BaseModel
import com.telemed.models.FaqModel


class RecyclerViewAdapter<T : BaseModel>(
    var list: List<T>,
    val onItemClickListener: OnItemClickListener<T>?,
    var layout: Int
) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    var onTextChangeListener: OnTextChangeListener? = null
    var onItemClickListenerWithTag: OnItemClickListenerWithTag<T>? = null
    var onTextChangeWithTagListener: OnTextChangeWithTagListener? = null
    var onItemRecyclerViewListener: OnItemRecyclerViewListener? = null
    var tag: Int? = null
    var maxUnit: Int? = null
    var unitSelection: String? = null
    var menuId: String? = null
    var isViewItem: Boolean = false
    var isMoreShow: Boolean = false
    var moreText: String? = null
    private var emptyTextView: TextView? = null
    private var emptyViewText = R.string.no_data_found

    fun setTextChangeListener(onTextChangeListener: OnTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener
    }

    fun setTextChangeWithTagListener(onTextChangeWithTagListener: OnTextChangeWithTagListener?) {
        this.onTextChangeWithTagListener = onTextChangeWithTagListener
    }

    fun setItemRecyclerViewListener(onItemRecyclerViewListener: OnItemRecyclerViewListener?) {
        this.onItemRecyclerViewListener = onItemRecyclerViewListener
    }


    fun setMaxUnit(maxUnit: Int, unitSelection: String?) {
        this.maxUnit = maxUnit
        this.unitSelection = unitSelection
    }

    fun setMenuIid(menuId: String) {
        this.menuId = menuId
    }

    fun setOnlyViewModeItem(isViewItem: Boolean) {
        this.isViewItem = isViewItem
    }

    fun setShowMore(isMoreShow: Boolean, moreText: String) {
        this.isMoreShow = isMoreShow
        this.moreText = moreText
    }

    fun setItemClickListenerWithTag(
        onItemClickListenerWithTag: OnItemClickListenerWithTag<T>,
        tag: Int
    ) {
        this.onItemClickListenerWithTag = onItemClickListenerWithTag
        this.tag = tag
    }


    fun setEmptyTextView(emptyTextView: TextView, @StringRes emptyViewText: Int) {
        this.emptyTextView = emptyTextView
        this.emptyViewText = emptyViewText
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = getItem(position)
        if (holder.binding is ItemSlotsBinding) {
            if (item != null) {
                if ((item as SlotsResponse) != null) {
                    if (onItemRecyclerViewListener != null) {
                        onItemRecyclerViewListener!!.onItemRender(
                            (holder.binding as ItemSlotsBinding).recyclerView,
                            item
                        )
                    }
                }

            }
        }else if (holder.binding is ItemSlotsBookingBinding) {
            if (item != null) {
                if ((item as SlotsResponse) != null) {
                    if (onItemRecyclerViewListener != null) {
                        onItemRecyclerViewListener!!.onItemRender(
                            (holder.binding as ItemSlotsBookingBinding).recyclerView,
                            item
                        )
                    }
                }

            }
        }


        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.setVariable(BR.itemClickListener, onItemClickListener)
    }

    override fun getItemCount(): Int {
        if (emptyTextView != null) {
            if (list.size == 0) {
                emptyTextView!!.setVisibility(View.VISIBLE)
                emptyTextView!!.setText(emptyViewText)
            } else {
                emptyTextView!!.setVisibility(View.INVISIBLE)
            }
        }

        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                viewType,
                parent,
                false
            );
        return MyViewHolder(binding);
    }

    class MyViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        var binding: ViewDataBinding
            internal set

        init {
            this.binding = binding
            this.binding.executePendingBindings()
        }
    }

    fun getItem(position: Int): BaseModel {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getPosition(@Nullable item: T): Int {
        return list.indexOf(item)
    }

    override fun getItemViewType(position: Int): Int {
        return layout
    }

    fun setSearchList(list: List<T>) {
        this.list = list
    }

    fun changeSelection(dataModel: BaseModel) {
        if(dataModel is FaqModel){
            val expanded: Boolean = dataModel.expanded
            dataModel.expanded=!expanded
        }

        notifyDataSetChanged()
    }


}
