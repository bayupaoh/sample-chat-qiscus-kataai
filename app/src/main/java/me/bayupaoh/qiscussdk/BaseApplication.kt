package me.bayupaoh.qiscussdk

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.drawee.backends.pipeline.Fresco
import com.qiscus.sdk.Qiscus
import me.bayupaoh.qiscussdk.di.component.ApplicationComponent
import me.bayupaoh.qiscussdk.di.module.ContextModule
import me.bayupaoh.qiscussdk.helper.CommonUtils
import me.bayupaoh.qiscussdk.helper.RxBus
import me.bayupaoh.qiscussdk.data.prefs.SuitSave
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import io.realm.Realm
import io.realm.RealmConfiguration
import me.bayupaoh.qiscussdk.di.component.DaggerApplicationComponent

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class BaseApplication : MultiDexApplication() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
        lateinit var bus: RxBus
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder().contextModule(ContextModule(this)).build()
        bus = RxBus()
        appContext = applicationContext
        Fresco.initialize(this)

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        Qiscus.init(this, "testsuitc-uqchm2cjzyt")
        Qiscus.setEnableLog(BuildConfig.DEBUG)

        // Initial Preferences
        SuitSave.init(applicationContext)

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        // Configure Twitter SDK
        val authConfig = TwitterAuthConfig(
                getString(R.string.CONSUMER_KEY),
                getString(R.string.CONSUMER_SECRET))

        val twitterConfig = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(authConfig)
                .build()

        Twitter.initialize(twitterConfig)

        CommonUtils.getKeyHash(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}