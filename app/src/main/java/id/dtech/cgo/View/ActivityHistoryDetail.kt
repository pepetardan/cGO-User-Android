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
import kotlinx.android.synthetic.main.activity_history_detail.*

class ActivityHistoryDetail : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.DetailBookingCallback {

    private lateinit var bookingController: BookingController
    private var order_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)
        setView()
    }

    private fun setView(){
        bookingController = BookingController()
        intent.extras?.let {
            order_id = it.getString("order_id") ?: ""
            bookingController.getDetailBooking(order_id,this)
        }
        ivBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
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

        txtTitle.text = experienceMap["exp_title"] as String

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

    }

    override fun onDetailBookingError(message: String) {

    }
}
