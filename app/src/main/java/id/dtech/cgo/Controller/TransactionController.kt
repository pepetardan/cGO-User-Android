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
import java.lang.Exception

class TransactionController {
    fun createPaymentTransaction(body : HashMap<String,Any>, from : Int, createPaymentCallback :
    MyCallback.Companion.CreatePaymentCallback ){

        createPaymentCallback.onCreatePaymentPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.postCreatePaymentTransaction(body)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    try {

                        val dataMap = HashMap<String,Any>()

                        if (from != 1){
                            val paymentJsonObject = JSONObject(t.body()?.string() ?: "")
                            val redirect_url = paymentJsonObject.getString("redirect_url")
                            val token = paymentJsonObject.getString("token")

                            dataMap["redirect_url"] = redirect_url
                            dataMap["token"] = token
                        }

                        createPaymentCallback.onCreatePaymentSuccess(dataMap)
                    }
                    catch (e : Exception){
                        createPaymentCallback.onCreatePaymentError(e.message ?: "")
                        Log.d("create_booking_error",e.message ?: "")
                    }
                }
                else{
                    createPaymentCallback.onCreatePaymentError(t.message() ?: "")
                    Log.d("create_booking_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                createPaymentCallback.onCreatePaymentError(e.message ?: "")
                Log.d("create_booking_error",e.message ?: "")
            }
        })
    }
}