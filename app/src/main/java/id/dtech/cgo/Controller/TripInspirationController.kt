package id.dtech.cgo.Controller

import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.BlogConnection
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Model.TripInspirationModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

class TripInspirationController {
    companion object{
        fun getTripInspiration(inspirationCallback : MyCallback.Companion.TripInspirationCallback){

            inspirationCallback.onTripInspirationPrepare()

            val retrofit = BlogConnection.myClient(ApiService::class.java,null)
            val inspiration = retrofit.getTripInspiration()

            inspiration.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
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
                            val inspirationArray = ArrayList<TripInspirationModel>()

                            for (i in 0 until totalLength){

                                val inspirationObject = jsonArray[i] as JSONObject
                                val exp_inspiration_id  = inspirationObject.getString("id")
                                val exp_title  = inspirationObject.getString("title")
                                val exp_desc  = inspirationObject.getString("description")
                                val exp_cover_photo  = inspirationObject.getString("url_file")

                                val inspirationModel = TripInspirationModel()
                                inspirationModel.exp_inspiration_id = exp_inspiration_id
                                inspirationModel.exp_title = exp_title
                                inspirationModel.exp_desc = exp_desc
                                inspirationModel.exp_cover_photo = exp_cover_photo

                                if (exp_inspiration_id != "6"){
                                    inspirationArray.add(inspirationModel)
                                }
                            }

                            inspirationCallback.onTripInspirationLoaded(inspirationArray)
                        }
                        catch (e : Exception){
                            Log.d("inspiration_error","Failed to parse json object")
                            inspirationCallback.onTripInspirationError()
                        }
                    }
                    else{
                        inspirationCallback.onTripInspirationError()
                        Log.d("inspiration_error",t.message())
                    }
                }

                override fun onError(e: Throwable) {
                    inspirationCallback.onTripInspirationError()
                    Log.d("inspiration_error",e.message)
                }
            })
        }
    }
}
