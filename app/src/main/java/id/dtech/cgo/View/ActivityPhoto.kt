package id.dtech.cgo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.dtech.cgo.Adapter.PhotoAdapter
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_photo.*

class ActivityPhoto : AppCompatActivity(), View.OnClickListener{

    private lateinit var photoList : ArrayList<HashMap<String,Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        setView()
    }

    private fun setView(){

        photoList = intent.getSerializableExtra("photos") as ArrayList<HashMap<String, Any>>

        rvPhoto.layoutManager = LinearLayoutManager(this)
        rvPhoto.adapter = PhotoAdapter(this,photoList)
        imgClose.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgClose -> {
                finish()
            }
        }
    }
}
