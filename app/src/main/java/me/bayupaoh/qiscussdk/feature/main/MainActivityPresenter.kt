package me.bayupaoh.qiscussdk.feature.main;

import android.content.Context
import com.qiscus.sdk.Qiscus
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import me.bayupaoh.qiscussdk.base.presenter.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.util.Log
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Created by SuitTemplate
 */

class MainActivityPresenter : BasePresenter<MainActivityView> {
    private var view: MainActivityView? = null
    private val mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: MainActivityView) {
        this.view = view
    }

    override fun detachView() {
        view = null
        mCompositeDisposable?.clear()
    }

    fun authenticationUser(context: Context) {
        Qiscus.setUser("user-guest", "user-guest")
                .withUsername("Guest")
                .withAvatarUrl("https://upload.wikimedia.org/wikipedia/id/thumb/d/d5/Aang_.jpg/300px-Aang_.jpg")
                .save()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({qiscusAccount ->
                    createRoomToChatWithChatBot(context)
                },{throwable ->
                    Log.e("qiscus-apps",throwable?.localizedMessage)
                })
    }

    fun createRoomToChatWithChatBot(context: Context) {
        Qiscus.buildChatWith("bayupaoh@gmail.com")
                .build(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ intent ->
                    view?.onSuccessCreateRoomChat(intent)
                }, { throwable ->
                    Log.d("qiscus-apps", throwable?.localizedMessage)
                })
    }
}