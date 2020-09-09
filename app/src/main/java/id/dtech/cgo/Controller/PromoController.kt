package id.dtech.cgo.Controller

import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Model.PromoModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

class PromoController {
    companion object{
        fun getPromo(promoCallback : MyCallback.Companion.PromoCallback){

            promoCallback.onPromoPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,null)
            val promo = retrofit.getPromo()

            promo.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){
                        try {

                            val jsonArray = JSONArray(t.body()?.string())
                            val totalLength = jsonArray.length()
                            val promoArray = ArrayList<PromoModel>()

                            for (i in 0 until totalLength){

                                val promoObject = jsonArray[i] as JSONObject
                                val promo_id = promoObject.getString("id")
                                val promo_code = promoObject.getString("promo_code")
                                val promo_name = promoObject.getString("promo_name")
                                val promo_desc = promoObject.getString("promo_desc")
                                val promo_value = promoObject.getLong("promo_value")
                                val promo_type = promoObject.getInt("promo_type")
                                val promo_image = promoObject.getString("promo_image")

                                val promoModel = PromoModel()
                                promoModel.id = promo_id
                                promoModel.promo_code = promo_code
                                promoModel.promo_name = promo_name
                                promoModel.promo_desc = promo_desc
                                promoModel.promo_value = promo_value
                                promoModel.promo_type = promo_type
                                promoModel.promo_image = promo_image

                                promoArray.add(promoModel)
                            }
                            Log.d("total_promo",""+promoArray.size)
                            promoCallback.onPromoLoaded(promoArray)
                        }
                        catch (e : Exception){
                            Log.d("promo_error","Failed to parse json object")
                            promoCallback.onPromoError()
                        }
                    }
                    else{
                        promoCallback.onPromoError()
                        Log.d("promo_error",t.message())
                    }
                }

                override fun onError(e: Throwable) {
                    promoCallback.onPromoError()
                    Log.d("promo_error",e.message)
                }
            })
        }

        fun getSpecialPromo(code : String, promoType : Int,
                            specialPromoCallback : MyCallback.Companion.SpecialPromoCallback){

            specialPromoCallback.onSpecialPromoPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,null)
            val promo = retrofit.getSpecialPromo(code,promoType)

            promo.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                    Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){
                        try {
                            val promoObject = JSONObject(t.body()?.string() ?: "")
                            val promo_id = promoObject.getString("id")
                            val promo_code = promoObject.getString("promo_code")
                            val promo_name = promoObject.getString("promo_name")
                            val promo_desc = promoObject.getString("promo_desc")
                            val promo_value = promoObject.getLong("promo_value")
                            val promo_type = promoObject.getInt("promo_type")
                            val promo_image = promoObject.getString("promo_image")

                            val promoModel = PromoModel()
                            promoModel.id = promo_id
                            promoModel.promo_code = promo_code
                            promoModel.promo_name = promo_name
                            promoModel.promo_desc = promo_desc
                            promoModel.promo_value = promo_value
                            promoModel.promo_type = promo_type
                            promoModel.promo_image = promo_image

                            specialPromoCallback.onSpecialPromoLoaded(promoModel)
                        }
                        catch (e : Exception){
                            specialPromoCallback.onSpecialPromoError(e.message ?: "")
                            Log.d("special_promo",e.message ?: "" )
                        }
                    }
                    else{
                        specialPromoCallback.onSpecialPromoError("Promo code wrong")
                        Log.d("special_promo",t.message())
                    }
                }

                override fun onError(e: Throwable) {
                    specialPromoCallback.onSpecialPromoError(e.message ?: "")
                    Log.d("special_promo",e.message ?: "" )
                }
            })
        }
    }
}


