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
import kotlinx.android.synthetic.main.activity_waiting_confirmation.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityWaitingConfirmation : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.DetailBookingCallback{

    private lateinit var bookingController: BookingController
    private var order_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_confirmation)
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

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun onDetailBookingPrepare() {

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onDetailBookingSuccess(data: HashMap<String, Any>) {
        val transactionStatus = data["transaction_status"] as Int
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

        val expiredDate = data["booking_date"] as String
        val expiredAmount = experienceMap["exp_payment_deadline_amount"] as Int
        val expiredType = experienceMap["exp_payment_deadline_type"] as String

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        val sdfDayDate = SimpleDateFormat("dd MMMM yyyy")
        val date = sdf.parse(expiredDate) ?: Date()

        val calendar = Calendar.getInstance()
        calendar.time = date

        if (expiredType == "Days"){
            calendar.add(Calendar.DAY_OF_YEAR, -expiredAmount)
        }
        else if (expiredType == "Week"){
            val totalWeek = expiredAmount * 7
            calendar.add(Calendar.DAY_OF_YEAR, -totalWeek)
        }
        else{
            val totalMonth = expiredAmount * 30
            calendar.add(Calendar.DAY_OF_YEAR, -totalMonth)
        }

        val strExpired = sdfDayDate.format(calendar.time)

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

        if (transactionStatus == 1){
            txtBottomContent.text = "Please wait for your guide confirmation maximum within 24 hour. An email confirmation will be sent to your email"

            if (data["name"] as String == "Down Payment"){
                val remainingPayment = data["remaining_payment"] as Long
                txtDpPrice.text =  CurrencyUtil.decimal(total_price).replace(",",
                    ".")
                txtRemainingPrice.text = CurrencyUtil.decimal(remainingPayment).replace(",",
                    ".")
            }
            else{
                linearDP.visibility = View.GONE
                linearRemaining.visibility = View.GONE
                txtWaiting.text = "WAITING FOR GUIDE CONFIRMATION"
            }
        }
        else{
            val remainingPayment = data["remaining_payment"] as Long
            txtWaiting.text = "INCOMPLETE PAYMENT"
            txtDpPrice.text =  CurrencyUtil.decimal(total_price).replace(",",
                ".")
            txtRemainingPrice.text = CurrencyUtil.decimal(remainingPayment).replace(",",
                ".")
            txtBottomContent.text = "Please contact your guide for payment instructions. Payment must be made before $strExpired"
        }
    }

    override fun onDetailBookingError(message: String) {

    }
}
