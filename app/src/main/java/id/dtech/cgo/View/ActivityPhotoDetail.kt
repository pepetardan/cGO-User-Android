package id.dtech.cgo.View

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import id.dtech.cgo.Adapter.ImagePagerAdapter
import id.dtech.cgo.Model.PhotoModel
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_photo_detail.*
import java.util.ArrayList

class ActivityPhotoDetail : AppCompatActivity(), View.OnClickListener {

    private var folderName = ""
    private var clickedPosotion = 0
    private lateinit var photoList : ArrayList<PhotoModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        val b = intent.extras
        b?.let { bundle ->
            folderName = bundle.getString("folder_name") ?: ""
            clickedPosotion = bundle.getInt("clicked_position")
            photoList = bundle.getParcelableArrayList("photos") ?: ArrayList()

            txtFolderName.text = folderName
            setImagePager()
        }

        imgBack.setOnClickListener(this)
        imgClose.setOnClickListener(this)
    }

    private fun setImagePager(){
        photoPager.adapter = ImagePagerAdapter(this,photoList,"")
        photoPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                txtPage.text =  ""+(photoPager.currentItem+1)+" / "+photoList.size
            }

            override fun onPageSelected(p0: Int) {
            }
        })
        photoPager.currentItem = clickedPosotion
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgBack -> {
                finish()
            }
            R.id.imgClose -> {
                finish()
            }
        }
    }
}
