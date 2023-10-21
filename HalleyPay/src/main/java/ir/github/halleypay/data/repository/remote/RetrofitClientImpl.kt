package ir.github.halleypay.data.repository.remote

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ir.github.halleypay.core.utils.AuxiliaryFunctionsManager
import ir.github.halleypay.data.dataSource.local.sharedPrefrences.SharedPreferencesManager
import ir.github.halleypay.data.dataSource.remote.ApiService
import ir.github.halleypay.presentation.utils.Constants
import ir.github.halleypay.presentation.utils.Constants.CACHE_SIZE_FOR_RETROFIT
import ir.github.halleypay.presentation.utils.Constants.HEADER_CACHE_CONTROL
import ir.github.halleypay.presentation.utils.Constants.HEADER_PRAGMA
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitClientImpl {

    private fun getRetrofitNonCaching(application: Context, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASIC_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getUnsafeOkHttpClientNonBasicAuthInterceptor(application).build())
            .build()
    }

    private fun getUnsafeOkHttpClientNonBasicAuthInterceptor(application: Context): OkHttpClient.Builder {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
//                .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
//                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
//                .addInterceptor(offlineInterceptor(application))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun getRetrofitCaching(application: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASIC_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getUnsafeOkHttpClientNonBasicAuthInterceptorCaching(application).build())
            .build()
    }

    private fun getUnsafeOkHttpClientNonBasicAuthInterceptorCaching(application: Context): OkHttpClient.Builder {
        return try {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                .cache(cache(application))
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor(application))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .hostnameVerifier { _, _ -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun offlineInterceptor(context: Context): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->

            val originalResponse: Response = chain.proceed(chain.request())
            return@Interceptor if (AuxiliaryFunctionsManager(SharedPreferencesManager(context))
                    .isNetworkConnected(context)
            ) {
                val maxAge = 60 * 60 * 24 // read from cache for 1 day
                originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
            } else {
                val maxStale = 60 * 60 * 24 * 7 // tolerate one weeks stale
                originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
        }
    }

    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val response = chain.proceed(chain.request())
            val cacheControl: CacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.DAYS)
                .build()
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(
                    HEADER_CACHE_CONTROL,
                    cacheControl.toString()
                )
                .build()
        }
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Log.d("Data On Http:", message) }
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        return httpLoggingInterceptor
    }

    private fun cache(application: Context): Cache {
        return Cache(File(application.cacheDir, "retrofitCache"), CACHE_SIZE_FOR_RETROFIT)
    }

    companion object {

        private var mInstance: RetrofitClientImpl? = null
        private var gson = GsonBuilder().serializeNulls().create()

        private fun getInstance(): RetrofitClientImpl {
            if (mInstance == null) {
                synchronized(RetrofitClientImpl::class) {
                    mInstance = RetrofitClientImpl()
                }
            }
            return mInstance!!
        }

        fun buildApiNonCache(application: Context): ApiService? {
            val gson = GsonBuilder().serializeNulls().create()
            return getInstance().getRetrofitNonCaching(application, gson)
                .create(ApiService::class.java)
        }

        fun buildApiCaching(application: Context): ApiService? {
            return getInstance().getRetrofitCaching(application)
                .create(ApiService::class.java)
        }

    }
}