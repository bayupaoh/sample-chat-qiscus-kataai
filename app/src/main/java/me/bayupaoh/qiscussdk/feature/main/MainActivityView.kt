package me.bayupaoh.qiscussdk.feature.main;

import android.content.Intent
import me.bayupaoh.qiscussdk.base.presenter.MvpView;

/**
 * Created by SuitTemplate
 */

interface MainActivityView : MvpView {
    fun onSuccessCreateRoomChat(intent:Intent)
}
