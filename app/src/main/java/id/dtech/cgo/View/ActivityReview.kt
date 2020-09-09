package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.ExperienceController
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_review.*

class ActivityReview : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.CreateReviewCallback {

    private var order_id = ""
    private var exp_id = ""
    private var desc = ""

    private var guideScore = 0
    private var activityScore = 0
    private var serviceScore = 0
    private var cleanScore = 0
    private var valueScore = 0

    private lateinit var loadingDialog : AlertDialog
    private lateinit var userSession: UserSession
    private lateinit var experienceController: ExperienceController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setView()
    }

    private fun setView(){

        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        loadingDialog = ViewUtil.laodingDialog(this)
        experienceController = ExperienceController()

        intent.extras?.let { bundle ->
            order_id = bundle.getString("order_id") ?: ""
            exp_id = bundle.getString("exp_id") ?: ""
        }

        imgGuideStar1.setOnClickListener(this)
        imgGuideStar2.setOnClickListener(this)
        imgGuideStar3.setOnClickListener(this)
        imgGuideStar4.setOnClickListener(this)
        imgGuideStar5.setOnClickListener(this)

        imgActStar1.setOnClickListener(this)
        imgActStar2.setOnClickListener(this)
        imgActStar3.setOnClickListener(this)
        imgActStar4.setOnClickListener(this)
        imgActStar5.setOnClickListener(this)

        imgServStar1.setOnClickListener(this)
        imgServStar2.setOnClickListener(this)
        imgServStar3.setOnClickListener(this)
        imgServStar4.setOnClickListener(this)
        imgServStar5.setOnClickListener(this)

        imgCleanStar1.setOnClickListener(this)
        imgCleanStar2.setOnClickListener(this)
        imgCleanStar3.setOnClickListener(this)
        imgCleanStar4.setOnClickListener(this)
        imgCleanStar5.setOnClickListener(this)

        imgValueStar1.setOnClickListener(this)
        imgValueStar2.setOnClickListener(this)
        imgValueStar3.setOnClickListener(this)
        imgValueStar4.setOnClickListener(this)
        imgValueStar5.setOnClickListener(this)

        ivBack.setOnClickListener(this)
        btnSend.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setGuideReview(from : Int){
        if (from == 1){
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtGuideReview.text = "Bad"
            guideScore = 1
        }
        else if(from == 2){
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtGuideReview.text = "Standart"
            guideScore = 2
        }
        else if(from == 3){
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtGuideReview.text = "Good"
            guideScore = 3
        }
        else if(from == 4){
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtGuideReview.text = "Very Good"
            guideScore = 4
        }
        else{
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtGuideReview.text = "Excellent"
            guideScore = 5
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setActReview(from : Int){
        if (from == 1){
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtActReview.text = "Bad"
            activityScore = 1
        }
        else if(from == 2){
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtActReview.text = "Standart"
            activityScore = 2
        }
        else if(from == 3){
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtActReview.text = "Good"
            activityScore = 3
        }
        else if(from == 4){
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtActReview.text = "Very Good"
            activityScore = 4
        }
        else{
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtActReview.text = "Excellent"
            activityScore = 5
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setServReview(from : Int){
        if (from == 1){
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtServReview.text = "Bad"
            serviceScore = 1
        }
        else if(from == 2){
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtServReview.text = "Standart"
            serviceScore = 2
        }
        else if(from == 3){
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtServReview.text = "Good"
            serviceScore = 3
        }
        else if(from == 4){
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtServReview.text = "Very Good"
            serviceScore = 4
        }
        else{
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtServReview.text = "Excellent"
            serviceScore = 5
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCleanReview(from : Int){
        if (from == 1){
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtCleanReview.text = "Bad"
            cleanScore = 1
        }
        else if(from == 2){
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtCleanReview.text = "Standart"
            cleanScore = 2
        }
        else if(from == 3){
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtCleanReview.text = "Good"
            cleanScore = 3
        }
        else if(from == 4){
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtCleanReview.text = "Very Good"
            cleanScore = 4
        }
        else{
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtCleanReview.text = "Excellent"
            cleanScore = 5
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setValueReview(from : Int){
        if (from == 1){
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtValueReview.text = "Bad"
            valueScore = 1
        }
        else if(from == 2){
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtValueReview.text = "Standart"
            valueScore = 2
        }
        else if(from == 3){
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtValueReview.text = "Good"
            valueScore = 3
        }
        else if(from == 4){
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtValueReview.text = "Very Good"
            valueScore = 4
        }
        else{
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtValueReview.text = "Excellent"
            valueScore = 5
        }
    }

    private fun createReview(){
        if (guideScore == 0){
            ViewUtil.showBlackToast(this,"Please select guide review",0).show()
            return
        }

        if (activityScore == 0){
            ViewUtil.showBlackToast(this,"Please select activity review",0).show()
            return
        }

        if (serviceScore == 0){
            ViewUtil.showBlackToast(this,"Please select service review",0).show()
            return
        }

        if (cleanScore == 0){
            ViewUtil.showBlackToast(this,"Please select clean review",0).show()
            return
        }

        if (valueScore == 0){
            ViewUtil.showBlackToast(this,"Please select value review",0).show()
            return
        }

        desc = edtReview.text.toString().trim()

        val bodyMap = HashMap<String,Any>()
        bodyMap["id"] = ""
        bodyMap["exp_id"] = exp_id
        bodyMap["desc"] = desc
        bodyMap["guide_review"] = guideScore
        bodyMap["activities_review"] = activityScore
        bodyMap["service_review"] = serviceScore
        bodyMap["cleanliness_review"] = cleanScore
        bodyMap["value_review"] = valueScore

        val token = userSession.access_token ?: ""

        experienceController.createReview(token,bodyMap,this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.imgGuideStar1 -> {
                setGuideReview(1)
            }

            R.id.imgGuideStar2 -> {
                setGuideReview(2)
            }

            R.id.imgGuideStar3 -> {
                setGuideReview(3)
            }

            R.id.imgGuideStar4 -> {
                setGuideReview(4)
            }

            R.id.imgGuideStar5 -> {
                setGuideReview(5)
            }

            R.id.imgActStar1 -> {
                setActReview(1)
            }

            R.id.imgActStar2 -> {
                setActReview(2)
            }

            R.id.imgActStar3 -> {
                setActReview(3)
            }

            R.id.imgActStar4 -> {
                setActReview(4)
            }

            R.id.imgActStar5 -> {
                setActReview(5)
            }

            R.id.imgServStar1 -> {
                setServReview(1)
            }

            R.id.imgServStar2 -> {
                setServReview(2)
            }

            R.id.imgServStar3 -> {
                setServReview(3)
            }

            R.id.imgServStar4 -> {
                setServReview(4)
            }

            R.id.imgServStar5 -> {
                setServReview(5)
            }

            R.id.imgCleanStar1 -> {
                setCleanReview(1)
            }

            R.id.imgCleanStar2 -> {
                setCleanReview(2)
            }

            R.id.imgCleanStar3 -> {
                setCleanReview(3)
            }

            R.id.imgCleanStar4 -> {
                setCleanReview(4)
            }

            R.id.imgCleanStar5 -> {
                setCleanReview(5)
            }

            R.id.imgValueStar1 -> {
                setValueReview(1)
            }

            R.id.imgValueStar2 -> {
                setValueReview(2)
            }

            R.id.imgValueStar3 -> {
                setValueReview(3)
            }

            R.id.imgValueStar4 -> {
                setValueReview(4)
            }

            R.id.imgValueStar5 -> {
                setValueReview(5)
            }

            R.id.btnSend -> {
               createReview()
            }
        }
    }

    override fun onCreateReviewPrepare() {
        loadingDialog.show()
    }

    override fun onCreateReviewSuccess() {
        loadingDialog.dismiss()
        val i = Intent(this,ActivityReviewDetail::class.java)
        i.putExtra("order_id",order_id)
        startActivity(i)
        finish()
        ViewUtil.showBlackToast(this,"Review success",0).show()
    }

    override fun onCreateReviewError(message: String, code: Int) {
        loadingDialog.dismiss()
        if (code != 401){
            ViewUtil.showBlackToast(this,"Session expired, please login again",0).show()
        }
        else{
            ViewUtil.showBlackToast(this,message,0).show()
        }
    }
}
