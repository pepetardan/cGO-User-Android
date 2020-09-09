package id.dtech.cgo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_package_list.*

class PackageListActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_list)
        setView()
    }

    private fun setView(){
        ivBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }
}