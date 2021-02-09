package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import com.squareup.picasso.Picasso
import id.dtech.cgo.Adapter.AddonNameAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.BookingController
import id.dtech.cgo.Model.AddOnModel

import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.Util.ViewUtil

import kotlinx.android.synthetic.main.activity_payment_detail.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityPaymentDetail : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.DetailBookingCallback {

    private lateinit var bookingController: BookingController
    private lateinit var userSession : UserSession

    private var order_id = ""
    private var from = 0
    private var transactionStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        bookingController = BookingController()

        intent.extras?.let { bundle ->

            order_id = bundle.getString("order_id") ?: ""
            from = bundle.getInt("from")
            bookingController.getDetailBooking(order_id,this)
        }

        ivBack.setOnClickListener(this)
        btnTicket.setOnClickListener(this)
        imgCopy.setOnClickListener(this)
        btnHome.setOnClickListener(this)
    }

    private fun intentTicketActivity(){
        val i = Intent(this,ActivityTicket::class.java)
        i.putExtra("order_id", order_id)
        startActivity(i)
    }

    private fun intentMainActivity(){
        val i = Intent(this,MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    @SuppressLint("SetTextI18n")
    private fun checkBookingType(){
          if (transactionStatus != 1){
                linearMerchant.visibility = View.VISIBLE
                linearCheckEmail.visibility = View.GONE
                linearBacktoHome.visibility = View.VISIBLE
                btnTicket.text = "SEE TICKET"
            }
            else{
                linearMerchant.visibility = View.GONE
                linearBacktoHome.visibility = View.GONE
                linearCheckEmail.visibility = View.VISIBLE
                btnTicket.text = "BACK TO HOME"

              if (userSession.access_token != null){
                  txtRegister.visibility = View.GONE
              }
        }
    }

    private fun copyToClipBoard(){
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text",  txtMerchantPhone.text ?: "")
        myClipboard.setPrimaryClip(myClip)
        ViewUtil.showBlackToast(this,"Phone number copied",0).show()
    }

    private fun intentWaitingCnfirmation(){
        val i = Intent(this,ActivityWaitingConfirmation::class.java)
        i.putExtra("order_id", order_id)
        startActivity(i)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }
            R.id.btnTicket -> {
                if (from == 0){

                    if (transactionStatus != 1){
                        if (transactionStatus != 5){
                            intentTicketActivity()
                        }
                        else{
                            intentWaitingCnfirmation()
                        }
                    }
                    else{
                        intentMainActivity()
                    }
                }
                else{
                    intentTicketActivity()
                }
            }
            R.id.imgCopy -> {
                copyToClipBoard()
            }
            R.id.btnHome -> {
                intentMainActivity()
            }
        }
    }

    override fun onDetailBookingPrepare() {

    }

    @SuppressLint("SetTextI18n")
    override fun onDetailBookingSuccess(data: HashMap<String, Any>) {

        val experienceMap = data["experience"] as HashMap<String,Any>
        val bookedByArray = data["booked_by"] as ArrayList<HashMap<String,Any>>
        val totalGuestList = data["guest_desc"] as ArrayList<HashMap<String,Any>>
        val bookedByMap = bookedByArray[0]
        val addOnlist = experienceMap["addOnList"] as ArrayList<AddOnModel>

        val total_price = data["total_price"] as Long
        val currency = data["currency"] as String
        val expDuration = experienceMap["exp_duration"] as Int
        val bookingDate = data["booking_date"] as String
        val total_guest = totalGuestList.size
        val merchant_picture = experienceMap["merchant_picture"] as String
        val merchant_name = experienceMap["merchant_name"] as String
        val merchant_phone = experienceMap["merchant_phone"] as String

        var packageId = 0
        val packageName = experienceMap["package_name"] as String

        var isPackageIdZero = false
        var isAddOnEmpty = false

        if (experienceMap["package_id"] != null){
            packageId = experienceMap["package_id"] as Int
        }

        if (packageId != 0){
            linearPackage.visibility = View.VISIBLE
            txtPackageName.text = packageName
            isPackageIdZero = false
        }
        else{
            linearPackage.visibility = View.GONE
            isPackageIdZero = true
        }

        if (addOnlist.size > 0){
            isAddOnEmpty = false
            rvAddonName.layoutManager = LinearLayoutManager(this)
            rvAddonName.adapter = AddonNameAdapter(this,addOnlist)
            linearAddOn.visibility = View.VISIBLE
        }
        else{
            isAddOnEmpty = true
            linearAddOn.visibility = View.GONE
        }

        if (isPackageIdZero && isAddOnEmpty){
            viewPackage.visibility = View.GONE
        }

        transactionStatus = data["transaction_status"] as Int

        applyDate(bookingDate,expDuration)

        txtTitle.text = experienceMap["exp_title"] as String
        txtName.text = bookedByMap["fullname"] as String
        txtOrderID.text = order_id
        txtBankName.text = data["payment_type"] as String

        val price = CurrencyUtil.decimal(total_price).replace(",",
            ".")
        val strPrice = "$currency $price"

        txtPrice.text = strPrice

        if (total_guest > 1){
            txtGuests.text = "$total_guest Guest(s)"
            txtGuest.text = "$total_guest Guest(s)"
        }
        else{
            txtGuests.text = "$total_guest Guest"
            txtGuest.text = "$total_guest Guest"
        }

        if (merchant_picture.isNotEmpty()){
            Picasso.get().load(merchant_picture).into(imgMerchant)
        }

        txtMerchantName.text = merchant_name
        txtMerchantPhone.text = merchant_phone

        checkBookingType()
    }

    override fun onDetailBookingError(message: String) {

    }

    @SuppressLint("SimpleDateFormat")
    private fun applyDate(bookingDate : String, duration : Int){
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        val sdfDayDate = SimpleDateFormat("dd MMM yyyy")
        val sdfDay = SimpleDateFormat("dd")
        val sdfDayMonth = SimpleDateFormat("dd MMMM yyyy")

        val strDate = bookingDate
        val date = sdf.parse(strDate)

        if (duration > 1){
            val calendar = Calendar.getInstance()
            calendar.time = date ?: Date()
            calendar.add(Calendar.DATE,duration)

            val strDay = sdfDay.format(date ?: Date())
            val strDayDate = sdfDayDate.format(calendar.time)

            val strDayDateYear = "$strDay - $strDayDate"
            txtDate.text = strDayDateYear
        }
        else{
            val strDay = sdfDayMonth.format(date ?: Date())
            txtDate.text = strDay
        }
    }
}

