package id.dtech.cgo.View

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.BookingController
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import kotlinx.android.synthetic.main.activity_review_detail.*

class ActivityReviewDetail : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.DetailBookingCallback{

    private lateinit var bookingController: BookingController
    private var order_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)
        setView()
    }

    private fun setView(){
        bookingController = BookingController()

        intent.extras?.let { bundle ->
            order_id = bundle.getString("order_id") ?: ""
            bookingController.getDetailBooking(order_id,this)
        }

        ivBack.setOnClickListener(this)
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
        }
        else if(from == 2){
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtGuideReview.text = "Standart"
        }
        else if(from == 3){
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtGuideReview.text = "Good"
        }
        else if(from == 4){
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtGuideReview.text = "Very Good"
        }
        else{
            imgGuideStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgGuideStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtGuideReview.text = "Excellent"
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
        }
        else if(from == 2){
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtActReview.text = "Standart"
        }
        else if(from == 3){
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtActReview.text = "Good"
        }
        else if(from == 4){
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtActReview.text = "Very Good"
        }
        else{
            imgActStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgActStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtActReview.text = "Excellent"
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
        }
        else if(from == 2){
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtServReview.text = "Standart"
        }
        else if(from == 3){
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtServReview.text = "Good"
        }
        else if(from == 4){
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtServReview.text = "Very Good"
        }
        else{
            imgServStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgServStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtServReview.text = "Excellent"
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
        }
        else if(from == 2){
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtCleanReview.text = "Standart"
        }
        else if(from == 3){
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtCleanReview.text = "Good"
        }
        else if(from == 4){
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtCleanReview.text = "Very Good"
        }
        else{
            imgCleanStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgCleanStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtCleanReview.text = "Excellent"
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
        }
        else if(from == 2){
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtValueReview.text = "Standart"
        }
        else if(from == 3){
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_empty)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtValueReview.text = "Good"
        }
        else if(from == 4){
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_empty)
            txtValueReview.text = "Very Good"
        }
        else{
            imgValueStar1.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar2.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar3.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar4.setImageResource(R.drawable.ic_star_yellow_full)
            imgValueStar5.setImageResource(R.drawable.ic_star_yellow_full)
            txtValueReview.text = "Excellent"
        }
    }

    override fun onDetailBookingPrepare() {

    }

    @SuppressLint("SetTextI18n")
    override fun onDetailBookingSuccess(data: HashMap<String, Any>) {
        val experienceMap = data["experience"] as HashMap<String,Any>
        val bookedByArray = data["booked_by"] as ArrayList<HashMap<String,Any>>
        val bookedByMap = bookedByArray[0]
        val total_price = data["total_price"] as Long
        val currency = data["currency"] as String
        val totalGuestList = data["guest_desc"] as ArrayList<HashMap<String,Any>>
        val total_guest = totalGuestList.size
        val merchant_picture = experienceMap["merchant_picture"] as String
        val merchant_name = experienceMap["merchant_name"] as String
        val merchant_phone = experienceMap["merchant_phone"] as String

        val guide_review = data["guide_review"] as Int
        val activities_review = data["activities_review"] as Int
        val service_review = data["service_review"] as Int
        val cleanliness_review = data["cleanliness_review"] as Int
        val value_review = data["value_review"] as Int

        setGuideReview(guide_review)
        setActReview(activities_review)
        setServReview(service_review)
        setCleanReview(cleanliness_review)
        setValueReview(value_review)

        txtTitle.text = experienceMap["exp_title"] as String
        // txtAddonName.text = addon_name
        // txtDate.text = booking_date
        txtName.text = bookedByMap["fullname"] as String
        txtOrderID.text = order_id
        txtBankName.text = data["payment_type"] as String

        val price = CurrencyUtil.decimal(total_price).replace(",",
            ".")
        val strPrice = "$currency $price"

        txtPrice.text = strPrice

        if (total_guest > 1){
            txtGuest.text = "$total_guest Guest(s)"
        }
        else{
            txtGuest.text = "$total_guest Guest"
        }

        if (merchant_picture.isNotEmpty()){
            Picasso.get().load(merchant_picture).into(imgMerchant)
        }

        txtMerchantName.text = merchant_name
        txtMerchantPhone.text = merchant_phone

        txtPlace.text = experienceMap["exp_pickup_place"] as String
        txtTimte.text = experienceMap["exp_pickup_time"] as String
    }

    override fun onDetailBookingError(message: String) {

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }
}
