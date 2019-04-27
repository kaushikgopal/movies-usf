package co.kaush.msusf

import android.content.Context
import co.kaush.msusf.movies.MSMovieActivity
import co.kaush.msusf.movies.MSMovieApi
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MSAppModule(private val app: MSApp) {

    @Provides
    @Singleton
    fun provideContext(): Context = app


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val interceptor = (HttpLoggingInterceptor())
                .apply { level = HttpLoggingInterceptor.Level.BODY }

        val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MSMovieApi {
        return retrofit.create(MSMovieApi::class.java)
    }
}

@Singleton
@Component(modules = [MSAppModule::class])
interface MSAppComponent {
    fun inject(activity: MSMovieActivity)
}
