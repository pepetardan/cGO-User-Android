package id.dtech.cgo.Controller

import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class MicsController {
    fun loadExchangeRates(from : String, to : String, exchangeRatesCallback :
    MyCallback.Companion.ExchangeRatesCallback){

        exchangeRatesCallback.onExchangeRatesPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getExhangeRates(from,to)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    val jsonObject = JSONObject(t.body()?.string() ?: "")
                    val rates = jsonObject.getDouble("rates")
                    exchangeRatesCallback.onExchangeRatesSuccess(rates)
                }
                else{
                    Log.d("exchange_rate_error",t.message() ?: "")
                    val errorCode = t.code()
                    exchangeRatesCallback.onExchangeRatesError(t.message() ?: "",errorCode)
                }
            }

            override fun onError(e: Throwable) {
                Log.d("exchange_rate_error",e.message ?: "")
                exchangeRatesCallback.onExchangeRatesError(e.message ?: "",0)
            }
        })
    }
}