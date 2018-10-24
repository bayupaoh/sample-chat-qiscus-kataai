package me.bayupaoh.qiscussdk.base.presenter

import android.support.annotation.StringRes

interface MvpView {

    fun showLoading(isBackPressedCancelable: Boolean = true, message: String? = null)

    fun showLoadingWithText(msg: String)

    fun showLoadingWithText(@StringRes msg: Int)

    fun hideLoading()

    fun showConfirmationDialog(message: String, confirmCallback: () -> Unit)

    fun showConfirmationDialog(@StringRes message: Int, confirmCallback: () -> Unit)

    fun showAlertDialog(message: String)

    fun showAlertDialog(@StringRes message: Int)

}