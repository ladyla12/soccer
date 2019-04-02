package com.dicoding.soccer.db

import android.content.Context
import android.net.ConnectivityManager
import com.dicoding.soccer.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object RestApiClient {
    private var client: Retrofit? = null
    private const val dc = 15

    fun create(): SportsDBApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        if (client == null){
            client = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL + BuildConfig.TSDB_API_KEY + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient())
                .build()
        }

        return client?.create(SportsDBApi::class.java) as SportsDBApi
    }

    fun networkCheck(context: Context?): Boolean{
        return try {
            val networkManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = networkManager.activeNetworkInfo

            networkInfo != null && networkInfo.isConnected
        } catch (e: Exception){
            false
        }
    }

    private fun httpClient(): OkHttpClient{
        val builder = OkHttpClient.Builder()
            .connectTimeout(dc.toLong(), TimeUnit.SECONDS)
            .readTimeout(dc.toLong(), TimeUnit.SECONDS)

        builder.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        val x = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
            .tlsVersions(TlsVersion.TLS_1_0, TlsVersion.TLS_1_1, TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
            .cipherSuites(
                CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA,
                CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256
            )
            .build()

        builder.connectionSpecs(Collections.singletonList(x))
        return builder.build()
    }
}