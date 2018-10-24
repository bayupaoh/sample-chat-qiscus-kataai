package me.bayupaoh.qiscussdk.di.module

import android.content.Context
import me.bayupaoh.qiscussdk.di.scope.SuitCoreApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(context: Context) {
    private val appContext = context.applicationContext

    @Provides
    @SuitCoreApplicationScope
    fun context(): Context {
        return this.appContext
    }
}