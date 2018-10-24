package me.bayupaoh.qiscussdk.base.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import me.bayupaoh.qiscussdk.base.presenter.MvpView
import me.bayupaoh.qiscussdk.base.ui.recyclerview.BaseRecyclerView
import me.bayupaoh.qiscussdk.helper.CommonLoadingDialog

abstract class BaseFragment : Fragment(), MvpView {

    private var mContext: Context? = null
    private  lateinit var mBaseActivity: BaseActivity
    private var mInflater: LayoutInflater? = null
    private var mCommonLoadingDialog: CommonLoadingDialog? = null
    protected abstract val resourceLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        if (context is BaseActivity) {
            mBaseActivity = mContext as BaseActivity
            mInflater = LayoutInflater.from(mBaseActivity)
        } else {
            throw ClassCastException("The activity is not child of BaseActivity")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return mInflater?.inflate(resourceLayout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onViewReady(savedInstanceState)
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    protected fun showToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(isBackPressedCancelable: Boolean, message: String?) {
        mBaseActivity.showLoading(isBackPressedCancelable, message)
    }

    override fun showLoadingWithText(msg: String) {
        showLoading(message = msg)
    }

    override fun showLoadingWithText(msg: Int) {
        showLoading(message = getString(msg))
    }

    override fun hideLoading() {
        mCommonLoadingDialog?.dismiss()
    }

    override fun showConfirmationDialog(message: String, confirmCallback: () -> Unit) {
        mBaseActivity.showConfirmationDialog(message, confirmCallback)
    }

    override fun showConfirmationDialog(message: Int, confirmCallback: () -> Unit) {
        mBaseActivity.showConfirmationDialog(message, confirmCallback)
    }

    override fun showAlertDialog(message: String) {
        mBaseActivity.showAlertDialog(message)
    }

    override fun showAlertDialog(message: Int) {
        mBaseActivity.showAlertDialog(message)
    }

    fun finishLoad(recycler: BaseRecyclerView?){
        recycler?.let {
            it.refreshComplete()
            it.loadMoreComplete()
        }
    }

    fun showEmptyState(value: Boolean, list: View?, emptyView: View?){
        when(value){
            false -> {
                list?.visibility = View.VISIBLE
                emptyView?.visibility = View.GONE
            }
            true -> {
                list?.visibility = View.GONE
                emptyView?.visibility = View.VISIBLE
            }
        }
    }

}
