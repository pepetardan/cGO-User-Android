package id.dtech.cgo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.dtech.cgo.Adapter.PackageAdapter
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_package_list.*

class PackageListActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_list)
        setView()
    }

    private fun setView(){
        initiateRvPackage()
        ivBack.setOnClickListener(this)
    }

    private fun initiateRvPackage(){
        val packagesList = ArrayList<String>()
        packagesList.add("Kota Tua Reguler")
        packagesList.add("Kota Tua Reguler + Lunch")

        rvPackage.layoutManager = LinearLayoutManager(this)
        rvPackage.adapter = PackageAdapter(this,1,packagesList)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }
}