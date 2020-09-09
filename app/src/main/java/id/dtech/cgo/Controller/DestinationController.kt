package id.dtech.cgo.Controller

import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Model.ExpDestinationModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

class DestinationController {
    companion object {
        fun loadExperienceDestination(experienceDestinationCallback : MyCallback.Companion.ExperienceDestinationCallback
        , query : String
        ) {

            experienceDestinationCallback.onExperienceDestinationPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,null)
            val expdestination = retrofit.getDestination(query)

            expdestination.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                    Observer<Response<ResponseBody>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        if (t.isSuccessful){
                            try {

                                val jsonArray = JSONArray(t.body()?.string())
                                val jsonSize = jsonArray.length()
                                val destinationArray = ArrayList<ExpDestinationModel>()

                                    for (i in 0 until jsonSize) {

                                        val jsonObject = jsonArray[i] as JSONObject

                                        val id = jsonObject.getString("id")
                                        val harbors_name = jsonObject.getString("harbors_name")
                                        val harbors_longitude =
                                            jsonObject.getDouble("harbors_longitude")
                                        val harbors_latitude =
                                            jsonObject.getDouble("harbors_latitude")
                                        val harbors_image = jsonObject.getString("harbors_image")
                                        val city_id = jsonObject.getInt("city_id")
                                        val province_id = jsonObject.getInt("province_id")
                                        val city = jsonObject.getString("city")
                                        val province = jsonObject.getString("province")
                                        val country = jsonObject.getString("country")

                                        val destinationModel = ExpDestinationModel()
                                        destinationModel.id = id
                                        destinationModel.harbors_name = harbors_name
                                        destinationModel.harbors_longitude = harbors_longitude
                                        destinationModel.harbors_latitude = harbors_latitude
                                        destinationModel.harbors_image = harbors_image
                                        destinationModel.city_id = city_id
                                        destinationModel.province_id = province_id
                                        destinationModel.city = city
                                        destinationModel.province = province
                                        destinationModel.country = country
                                        destinationModel.type = 2

                                        var isProvinceExist = false
                                        var existPosition = 0

                                        for (j in 0 until destinationArray.size) {

                                            val model = destinationArray[j]

                                            if (model.type == 1 && model.province == province) {
                                                isProvinceExist = true
                                                existPosition = j
                                                break
                                            }
                                        }

                                        if (isProvinceExist) {
                                            destinationArray.add(
                                                existPosition + 1,
                                                destinationModel
                                            )
                                        } else {
                                            val destinationModel1 = ExpDestinationModel()
                                            destinationModel1.id = id
                                            destinationModel1.harbors_name = harbors_name
                                            destinationModel1.harbors_longitude = harbors_longitude
                                            destinationModel1.harbors_latitude = harbors_latitude
                                            destinationModel1.harbors_image = harbors_image
                                            destinationModel1.city_id = city_id
                                            destinationModel1.city = city
                                            destinationModel1.province = province
                                            destinationModel.province_id = province_id
                                            destinationModel1.country = country
                                            destinationModel1.type = 1

                                            destinationArray.add(destinationModel1)
                                            destinationArray.add(destinationModel)
                                        }
                                    }

                                experienceDestinationCallback.onExperienceDestinationLoaded(destinationArray)
                            }
                            catch (e : Exception){
                                Log.d("destination_error",e.message ?: "")
                                experienceDestinationCallback.onExperienceDestinationLoaded(ArrayList<ExpDestinationModel>())
                            }
                        }
                        else{
                            Log.d("destination_error",t.message())
                            experienceDestinationCallback.onExperienceDestinationError()
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("destination_error",e.message ?: "")
                        experienceDestinationCallback.onExperienceDestinationError()
                    }
                })
        }
    }
}