package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.dtech.cgo.Adapter.PackageAdapter
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExperienceDetailModel
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_package_list.*
import org.threeten.bp.LocalDate
import java.util.HashMap

class PackageListActivity : AppCompatActivity(), View.OnClickListener,
    ApplicationListener.Companion.PackageListener {

    companion object{
        val PACKAGE_RESULT_CODE = 715
    }

    private lateinit var intentMap : HashMap<String,Any>
    private lateinit var experienceDetailModel : ExperienceDetailModel
    private lateinit var packageList : ArrayList<HashMap<String,Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_list)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        val b = intent.extras

        b?.let {
            intentMap = it.getSerializable("intentMap") as HashMap<String, Any>
            experienceDetailModel = intentMap["experienceDetailModel"] as ExperienceDetailModel
            packageList = intentMap["packageList"] as ArrayList<HashMap<String, Any>>
            val selectedMapId = selectedPosition()
            rvPackage.layoutManager = LinearLayoutManager(this)
            rvPackage.adapter = PackageAdapter(this,1, packageList,
                selectedMapId,this)

            txtLocation.text = experienceDetailModel.harbors_name+", "+experienceDetailModel.province
            txtTitle.text = experienceDetailModel.exp_title ?: ""
        }

        ivBack.setOnClickListener(this)
    }

    private fun selectedPosition() : Int {
        val packageMap = intentMap["packageMap"] as HashMap<String, Any>
        val currentpackageId = packageMap["packageId"] as Int
        var selectedMapPosition = 0

        for (i in 0 until packageList.size){
            val packageListMap = packageList[i]
            val packageListMapId = packageListMap["packageId"] as Int

            if (packageListMapId == currentpackageId){
                selectedMapPosition = i
                break
            }
        }

        return selectedMapPosition
    }

    private fun setPackageResult(packageMap: HashMap<String, Any>){
        val packageAvaibility = packageMap["packageAvaibility"] as ArrayList<LocalDate>
        intentMap["avaibility"] = packageAvaibility
        intentMap["packageMap"] = packageMap

        val i = Intent(this,ActivityAddOn::class.java)
        i.putExtra("intentMap",intentMap)
        setResult(PACKAGE_RESULT_CODE,i)
        finish()
    }
    
    override fun onPackageClicked(packageMap: HashMap<String, Any>) {
        setPackageResult(packageMap)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }
}