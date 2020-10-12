package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.dtech.cgo.Adapter.GuideAdapter
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExperienceDetailModel
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_select_guide.*
import java.io.Serializable

class ActivitySelectGuide : AppCompatActivity(), View.OnClickListener,
    ApplicationListener.Companion.GuideListener {

    companion object {
        val SELECT_GUIDE_RESULT = 215
    }

    private lateinit var intentMap : HashMap<String,Any>

    private lateinit var packageMap : HashMap<String,Any>
    private lateinit var experienceDetailModel : ExperienceDetailModel
    private lateinit var guideList : ArrayList<HashMap<String,Any>>
    private lateinit var packageList : ArrayList<HashMap<String,Any>>

    private var intentFrom = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_guide)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        val b = intent.extras

        b?.let {
            intentMap = it.getSerializable("intentMap") as HashMap<String, Any>
            intentFrom = it.getInt("intent_from")

            experienceDetailModel = intentMap["experienceDetailModel"] as ExperienceDetailModel
            packageMap = intentMap["packageMap"] as HashMap<String, Any>

            guideList = intentMap["guideList"] as ArrayList<HashMap<String, Any>>
            packageList = intentMap["packageList"] as ArrayList<HashMap<String, Any>>

            txtLocation.text = experienceDetailModel.harbors_name+", "+experienceDetailModel.province
            txtTitle.text = experienceDetailModel.exp_title ?: ""

            initiateGuide()
        }

        ivBack.setOnClickListener(this)
        initiateGuide()
    }

    private fun initiateGuide(){
        var selectedPosition = 0

        if (intentMap["selectedGuideMap"] != null){
            selectedPosition = selectedPosition()
        }

        rvGuide.layoutManager = LinearLayoutManager(this)
        rvGuide.adapter = GuideAdapter(this,1, guideList, selectedPosition,
            this)
    }

    private fun selectedPosition() : Int {
        val selectedMap = intentMap["selectedGuideMap"] as HashMap<String,Any>
        val currentSelectedId = selectedMap["guide_id"] as String
        var selectedMapPosition = 0

        for (i in 0 until guideList.size){
            val guideListMap = guideList[i]
            val guideListMapId = guideListMap["guide_id"] as String

            if (guideListMapId == currentSelectedId){
                selectedMapPosition = i
                break
            }
        }

        return selectedMapPosition
    }

    private fun intentAddOn(){
        val i = Intent(this, ActivityAddOn::class.java)
        i.putExtra("intentMap", intentMap as Serializable)
        startActivity(i)
        finish()
    }

    private fun setAddOnResult(){
        val i = Intent(this, ActivityAddOn::class.java)
        i.putExtra("intentMap", intentMap as Serializable)
        setResult(SELECT_GUIDE_RESULT,i)
        finish()
    }

    override fun onGuideClicked(guideMap: java.util.HashMap<String, Any>) {
        intentMap["selectedGuideMap"] = guideMap
        if (intentFrom == 0){
            intentAddOn()
        }
        else{
            setAddOnResult()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }
}