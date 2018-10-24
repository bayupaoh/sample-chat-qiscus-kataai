package me.bayupaoh.qiscussdk.di.module

import android.content.Context
import me.bayupaoh.qiscussdk.di.scope.SuitCoreApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

@Module(includes = [(ContextModule::class)])
class NetworkModule {

    @Provides
    @SuitCoreApplicationScope
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val inteceptor = HttpLoggingInterceptor()
        inteceptor.level = HttpLoggingInterceptor.Level.BODY
        return inteceptor
    }

    @Provides
    @SuitCoreApplicationScope
    fun cache(file: File): Cache = Cache(file, 10 * 1000 * 1000)

    @Provides
    @SuitCoreApplicationScope
    fun file(context: Context): File = File(context.cacheDir, "okhttp_cache")

    @Provides
    @SuitCoreApplicationScope
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache) =
            OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .cache(cache).build()
}