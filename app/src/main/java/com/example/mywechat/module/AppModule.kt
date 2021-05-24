package com.example.mywechat.module

import android.app.Application
import com.example.mywechat.BuildConfig
import com.example.mywechat.repository.MyWeChatService
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun createHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
                //.addInterceptor(noConnectionInterceptor)
                .readTimeout(5, TimeUnit.MILLISECONDS)
                .writeTimeout(5, TimeUnit.MILLISECONDS)
                .followRedirects(false)
                .connectTimeout(5, TimeUnit.MILLISECONDS)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideScarlet(
            app: Application,
            httpClient: OkHttpClient,
            @Named("apiBaseUrl") baseUrl: String
    ): Scarlet = Scarlet.Builder()
            .webSocketFactory(httpClient.newWebSocketFactory("ws://8.140.133.34:7263/ws"))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .lifecycle(AndroidLifecycle.ofApplicationForeground(app))
            .backoffStrategy(LinearBackoffStrategy(5000))
            .build()

    @Provides
    @Singleton
    fun createMyWeChatService(scarlet: Scarlet): MyWeChatService {
        return scarlet.create(MyWeChatService::class.java)
    }
}