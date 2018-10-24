package me.bayupaoh.qiscussdk.feature.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.qiscus.sdk.Qiscus
import me.bayupaoh.qiscussdk.R;
import me.bayupaoh.qiscussdk.base.ui.BaseActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class MainActivity : BaseActivity(), MainActivityView {
    override fun onSuccessCreateRoomChat(intent:Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private var presenter: MainActivityPresenter? = null

    override val resourceLayout: Int
        get() = R.layout.activity_main;

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupPresenter()
    }

    private fun setupPresenter() {
        presenter = MainActivityPresenter()
        presenter?.attachView(this)
        presenter?.authenticationUser(this)
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    fun getContext(): Context {
        return this
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}