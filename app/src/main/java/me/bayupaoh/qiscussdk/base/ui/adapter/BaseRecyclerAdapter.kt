package me.bayupaoh.qiscussdk.base.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.bayupaoh.qiscussdk.base.ui.adapter.viewholder.BaseItemViewHolder

import java.util.ArrayList

abstract class BaseRecyclerAdapter<Data, Holder : BaseItemViewHolder<Data>>(context: Context) : RecyclerView.Adapter<Holder>() {

    private var mContext: Context = context
    private var mDatas: ArrayList<Data> = ArrayList()
    protected var mItemClickListener: OnItemClickListener? = null
    protected var mLongItemClickListener: OnLongItemClickListener? = null

    val datas: List<Data>
        get() = mDatas

    init {
        mDatas = ArrayList()
    }

    protected fun getView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(mContext).inflate(getItemResourceLayout(viewType), parent, false)
    }

    protected abstract fun getItemResourceLayout(viewType: Int): Int

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (mDatas[position] != null) {
            holder.bind(mDatas[position])
        }
    }

    override fun getItemCount(): Int {
        return try {
            mDatas.size
        } catch (e: Exception) {
            0
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(view: View, position: Int)
    }

    fun setOnLongItemClickListener(longItemClickListener: OnLongItemClickListener) {
        this.mLongItemClickListener = longItemClickListener
    }

    fun add(item: Data) {
        mDatas.add(item)
        notifyItemInserted(mDatas.size - 1)
    }

    fun addAll(items: List<Data>) {
        add(items)
    }

    fun add(item: Data, position: Int) {
        mDatas.add(position, item)
        notifyItemInserted(position)
    }

    fun add(items: List<Data>) {
        val size = items.size
        for (i in 0 until size) {
            mDatas.add(items[i])
        }
        notifyDataSetChanged()
    }

    fun addOrUpdate(item: Data) {
        val i = mDatas.indexOf(item)
        if (i >= 0) {
            mDatas[i] = item
            notifyItemChanged(i)
        } else {
            add(item)
        }
    }

    fun addOrUpdate(items: List<Data>) {
        val size = items.size
        for (i in 0 until size) {
            val item = items[i]
            val x = mDatas.indexOf(item)
            if (x >= 0) {
                mDatas[x] = item
            } else {
                add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position >= 0 && position < mDatas.size) {
            mDatas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun remove(item: Data) {
        val position = mDatas.indexOf(item)
        remove(position)
    }

    fun clear() {
        mDatas.clear()
        notifyDataSetChanged()
    }
}
