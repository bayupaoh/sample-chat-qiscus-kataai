package me.bayupaoh.qiscussdk.base.ui

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import me.bayupaoh.qiscussdk.R
import me.bayupaoh.qiscussdk.base.presenter.MvpView
import me.bayupaoh.qiscussdk.base.ui.recyclerview.BaseRecyclerView
import me.bayupaoh.qiscussdk.helper.CommonLoadingDialog

abstract class BaseActivity : AppCompatActivity(), MvpView {

    private var mContext: Context
        get() = this
        set(value) = TODO()

    var toolbar: Toolbar? = null
        protected set

    private var mInflater: LayoutInflater? = null
    private var mActionBar: ActionBar? = null
    private var mCommonLoadingDialog: CommonLoadingDialog? = null

    private val baseFragmentManager: FragmentManager
        get() = super.getSupportFragmentManager()

    protected abstract val resourceLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resourceLayout)
        mInflater = LayoutInflater.from(mContext)
        onViewReady(savedInstanceState)
    }

    protected fun setupToolbar(basetoolbar: Toolbar, needHomeButton: Boolean) {
        setupToolbar(basetoolbar, needHomeButton, null)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupToolbar(basetoolbar: Toolbar, needHomeButton: Boolean, onClickListener: View.OnClickListener?) {

        toolbar = basetoolbar
        setSupportActionBar(basetoolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(needHomeButton)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
        }

        if (onClickListener != null)
            basetoolbar.setNavigationOnClickListener(onClickListener)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun setupToolbarWithoutBackButton(toolbar: Toolbar) {

        this.toolbar = toolbar
        setSupportActionBar(toolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeButtonEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun setTitle(title: Int) {
        super.setTitle(title)
        if (mActionBar != null)
            mActionBar!!.title = getString(title)
    }

    override fun onBackPressed() {
        if (baseFragmentManager.backStackEntryCount > 0) {
            baseFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    protected fun showToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    override fun showLoading(isBackPressedCancelable: Boolean, message: String?) {
        mCommonLoadingDialog?.let {
            hideLoading()
        }
        mCommonLoadingDialog = CommonLoadingDialog.createLoaderDialog(msg = message)
        mCommonLoadingDialog?.show(supportFragmentManager, CommonLoadingDialog.TAG)
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
        val confirmDialog = AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.title_yes) { _, _ -> confirmCallback() }
                .setNegativeButton(R.string.title_no) { _, _ -> }
                .create()

        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        confirmDialog.show()
    }

    override fun showConfirmationDialog(message: Int, confirmCallback: () -> Unit) {
        val stringMessage = getString(message)
        showConfirmationDialog(stringMessage, confirmCallback)
    }

    override fun showAlertDialog(message: String) {
        val confirmDialog = AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.title_ok) { d, _ -> d.dismiss() }
                .create()

        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        confirmDialog.show()
    }

    override fun showAlertDialog(message: Int) {
        val stringMessage = getString(message)
        showAlertDialog(stringMessage)
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