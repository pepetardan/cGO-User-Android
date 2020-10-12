package id.dtech.cgo.Connection

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class MyConnection {
    companion object {

        @Throws(IOException::class)
        private fun buildClient(token : String?) : OkHttpClient {

            val logging =  HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(60, TimeUnit.SECONDS)
            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            httpClient.retryOnConnectionFailure(false)

            httpClient.addInterceptor {

                val original = it.request()
                val request : Request

                if (token != null){
                    request = original.newBuilder()
                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .addHeader("X-Requested-With","XMLHttpRequest")
                        .addHeader("Authorization", "$token")
                        .build()
                }
                else{
                    request = original.newBuilder()
                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .addHeader("X-Requested-With"," XMLHttpRequest")
                        .build()
                }

                it.proceed(request)
            }

            httpClient.addInterceptor(logging)

            return httpClient.build()
        }

        private fun buildRetrofit(token : String?) : Retrofit {
            // http://cgo-web-api-dev.azurewebsites.net -> Dev
            // http://cgo-web-api.azurewebsites.net -> Staging
            // https://api-cgo-prod.azurewebsites.net -> Prod
            return Retrofit.Builder()
                .baseUrl("http://cgo-web-api.azurewebsites.net/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient(token))
                .build()
        }

        fun <T> myClient(myClass : Class<T>,token : String?) : T {
            return buildRetrofit(token).create(myClass)
        }
    }
}

