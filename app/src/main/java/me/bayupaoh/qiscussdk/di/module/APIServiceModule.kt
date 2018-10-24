package me.bayupaoh.qiscussdk.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.bayupaoh.qiscussdk.BuildConfig
import me.bayupaoh.qiscussdk.data.api.APIService
import me.bayupaoh.qiscussdk.di.scope.SuitCoreApplicationScope
import me.bayupaoh.qiscussdk.model.User
import me.bayupaoh.qiscussdk.model.deserelializer.UserDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [(NetworkModule::class)])
class APIServiceModule {

    @Provides
    @SuitCoreApplicationScope
    fun apiService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)

    @Provides
    @SuitCoreApplicationScope
    fun rxJavaCallAdapter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    @SuitCoreApplicationScope
    fun gson(): Gson =
            GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .registerTypeAdapter(User::class.java, UserDeserializer())
                    .create()

    @Provides
    @SuitCoreApplicationScope
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()

}