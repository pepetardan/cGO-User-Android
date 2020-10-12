package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import id.dtech.cgo.Adapter.PhotoImageAdapter
import id.dtech.cgo.Model.ExperienceDetailModel
import id.dtech.cgo.Model.PhotoModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil

import kotlinx.android.synthetic.main.activity_package_detail.*
import org.threeten.bp.LocalDate
import java.io.Serializable

class PackageDetailActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var intentMap : HashMap<String,Any>

    private lateinit var packageMap : HashMap<String,Any>
    private lateinit var guideList : ArrayList<HashMap<String,Any>>
    private lateinit var packageList : ArrayList<HashMap<String,Any>>
    private lateinit var experienceDetailModel : ExperienceDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_detail)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        val b = intent.extras
        b?.let {
            intentMap = it.getSerializable("intentMap") as HashMap<String, Any>

            experienceDetailModel = intentMap["experienceDetailModel"] as ExperienceDetailModel
            packageMap = intentMap["packageMap"] as HashMap<String, Any>

            guideList = intentMap["guideList"] as ArrayList<HashMap<String, Any>>
            packageList = intentMap["packageList"] as ArrayList<HashMap<String, Any>>

            val packageName = packageMap["packageName"] as String
            val packageDesc = packageMap["packageDesc"] as String
            val price = CurrencyUtil.decimal(packageMap["packagePrice"] as Long).replace(",",".")
            val paymentType = (packageMap["packageTypePayment"] as String).split(" ")[1]
            val packageCurrency = packageMap["packageCurrency"] as String
            val strPrice = "$packageCurrency $price/$paymentType"

            val packageImages = packageMap["packageImageList"] as ArrayList<PhotoModel>

            rvImages.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,
                false)
            rvImages.adapter = PhotoImageAdapter(this,packageImages,"")

            txtTitle.text = experienceDetailModel.exp_title ?: ""
            txtPrice.text = strPrice
            txtLocation.text = experienceDetailModel.harbors_name+", "+experienceDetailModel.province
            txtPackageName.text = packageName

            if (Build.VERSION.SDK_INT >= 24)
            {
                txtDesc.text = Html.fromHtml(packageDesc, Html.FROM_HTML_MODE_LEGACY)
            }
            else
            {
                txtDesc.text = Html.fromHtml(packageDesc)
            }
        }

        ivBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)
    }

    private fun intentNext(){
        intentMap["packageExpPayment"] = packageMap["packageExpPayment"] as ArrayList<HashMap<String,Any>>

        if (guideList.size > 1){
            intentSelectGuide()
        }
        else{
            if (guideList.size > 0){
                val guideMap = guideList[0]
                intentMap["selectedGuideMap"] = guideMap
            }
            intentAddOn()
        }
    }

    private fun intentSelectGuide(){
        val i = Intent(this, ActivitySelectGuide::class.java)
        i.putExtra("intentMap", intentMap as Serializable)
        startActivity(i)
        finish()
    }

    private fun intentAddOn(){
        val i = Intent(this, ActivityAddOn::class.java)
        i.putExtra("intentMap", intentMap as Serializable)
        startActivity(i)
        finish()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.btnNext -> {
                intentNext()
            }
        }
    }
}