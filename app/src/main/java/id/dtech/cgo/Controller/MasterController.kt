package id.dtech.cgo.Controller

import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Model.ActivityTypeModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

class MasterController {
    fun getAccomodation(page : Int, size : Int, accomodationCallback: MyCallback.Companion.AccomodationCallback){

        accomodationCallback.onAccomodationPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getAccomodation(page,size)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    try {
                        val accomodationList = ArrayList<HashMap<String,Any>>()
                        val jsonObject = JSONObject(t.body()?.string() ?: "")
                        val jsonArray = jsonObject.getJSONArray("data")
                        val jsonSize = jsonArray.length()

                        for (i in 0 until jsonSize){
                           val accomodationObject = jsonArray[i] as JSONObject
                           val id = accomodationObject.getInt("id")
                           val name = accomodationObject.getString("name")

                            val accomodationMap = HashMap<String,Any>()
                            accomodationMap["id"] = id
                            accomodationMap["name"] = name

                            accomodationList.add(accomodationMap)
                        }

                        accomodationCallback.onAccomodationSuccess(accomodationList)
                    }
                    catch (e : Exception){
                        accomodationCallback.onAccomodationError()
                    }
                }
                else{
                    accomodationCallback.onAccomodationError()
                }
            }

            override fun onError(e: Throwable) {
                accomodationCallback.onAccomodationError()
            }
        })
    }

    fun getLanguage(page : Int, size : Int, languageCallback: MyCallback.Companion.LanguageCallback){

        languageCallback.onLanguagePrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getLanguage(page,size)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    try {
                        val languageList = ArrayList<HashMap<String,Any>>()
                        val jsonObject = JSONObject(t.body()?.string() ?: "")
                        val jsonArray = jsonObject.getJSONArray("data")
                        val jsonSize = jsonArray.length()

                        for (i in 0 until jsonSize){
                            val languageObject = jsonArray[i] as JSONObject
                            val id = languageObject.getInt("id")
                            val name = languageObject.getString("name")

                            val languageMap = HashMap<String,Any>()
                            languageMap["id"] = id
                            languageMap["name"] = name

                            languageList.add(languageMap)
                        }

                        languageCallback.onLanguageSuccess(languageList)
                    }
                    catch (e : Exception){
                        languageCallback.onLanguageError()
                    }
                }
                else{
                    languageCallback.onLanguageError()
                }
            }

            override fun onError(e: Throwable) {
                languageCallback.onLanguageError()
            }
        })
    }

    fun getCategories(categoriesCallback : MyCallback.Companion.CategoriesCallback){

        categoriesCallback.onCategoriesPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getCategories()

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                Observer<Response<ResponseBody>> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    val jsonArray = JSONArray(t.body()?.string())
                    val jsonSize = jsonArray.length()

                    val activityList = ArrayList<ActivityTypeModel>()
                    val booleanList = ArrayList<Boolean>()

                    for (i in 0 until jsonSize){
                        val jsonObject = jsonArray[i] as JSONObject
                        val exp_type_id = jsonObject.getInt("exp_type_id")
                        val exp_type_name = jsonObject.getString("exp_type_name")
                        val exp_type_icon = jsonObject.getString("exp_type_icon")
                        val exp_type_icon_mobile = jsonObject.getString("exp_type_icon_mobile")
                        val exp_sorting_id = jsonObject.getInt("sorting_id")

                        val activityTypeModel = ActivityTypeModel()
                        activityTypeModel.id = exp_type_id
                        activityTypeModel.name = exp_type_name
                        activityTypeModel.icon = exp_type_icon
                        activityTypeModel.icon_mobile = exp_type_icon_mobile
                        activityTypeModel.sorting_id = exp_sorting_id

                        activityList.add(activityTypeModel)
                        booleanList.add(false)
                    }

                    categoriesCallback.onCategoriesSuccess(activityList, booleanList)
                }
                else{
                    categoriesCallback.onCategoriesError()
                }
            }

            override fun onError(e: Throwable) {
                categoriesCallback.onCategoriesError()
            }

            override fun onComplete() {

            }

        })
    }
}