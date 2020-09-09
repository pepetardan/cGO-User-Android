package id.dtech.cgo.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_package_detail.*

class PackageDetailActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_detail)
        setView()
    }

    private fun setView(){
        ivBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.btnNext -> {
                startActivity(Intent(this,ActivitySelectGuide::class.java))
            }
        }
    }
}