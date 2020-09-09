package id.dtech.cgo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import id.dtech.cgo.Adapter.GuestDetailAdapter
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_guest_detail.*

class ActivityGuestDetail : AppCompatActivity(), View.OnClickListener{

    private lateinit var guestList : ArrayList<HashMap<String,Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_detail)
        setView()
    }

    private fun setView(){
        intent.extras?.let {
            guestList = it.getSerializable("guest_list") as  ArrayList<HashMap<String,Any>>
            rvGuestDetail.layoutManager = LinearLayoutManager(this)
            rvGuestDetail.adapter = GuestDetailAdapter(this,guestList)
        }

        ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }
}
