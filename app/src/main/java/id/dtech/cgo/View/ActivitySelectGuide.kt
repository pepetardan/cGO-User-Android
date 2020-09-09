package id.dtech.cgo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.dtech.cgo.Adapter.GuideAdapter
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_select_guide.*

class ActivitySelectGuide : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_guide)
        setView()
    }

    private fun setView(){
        ivBack.setOnClickListener(this)
        initiateGuide()
    }

    private fun initiateGuide(){
        rvGuide.layoutManager = LinearLayoutManager(this)
        rvGuide.adapter = GuideAdapter(this,1)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }
}