package me.bayupaoh.qiscussdk.base.ui.adapter.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

import me.bayupaoh.qiscussdk.base.ui.adapter.BaseRecyclerAdapter

abstract class BaseItemViewHolder<in Data>(
        itemView: View,
        private val mItemClickListener: BaseRecyclerAdapter.OnItemClickListener?,
        private val mLongItemClickListener: BaseRecyclerAdapter.OnLongItemClickListener?
) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{
    protected var mBaseContext: Context? = null
    var isHasHeader = false

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    abstract fun bind(data: Data?)

    override fun onClick(v: View) {
        mItemClickListener?.onItemClick(v, if (isHasHeader) adapterPosition - 1 else adapterPosition)
    }

    override fun onLongClick(v: View): Boolean {
        if (mLongItemClickListener != null) {
            mLongItemClickListener.onLongItemClick(v, if (isHasHeader) adapterPosition - 1 else adapterPosition)
            return true
        }
        return false
    }
}
