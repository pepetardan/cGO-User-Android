package id.dtech.cgo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_t_n_c.*

class ActivityTNC : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_t_n_c)
        setView()
    }

    private fun setView(){
        cardMore.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.cardMore -> {
                txtContent.maxLines = Integer.MAX_VALUE
                txtContent.ellipsize = null
                cardMore.visibility = View.GONE
            }
        }
    }
}
