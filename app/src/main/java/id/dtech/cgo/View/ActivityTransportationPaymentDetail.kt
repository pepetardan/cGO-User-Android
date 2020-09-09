package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.squareup.picasso.Picasso

import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.BookingController
import id.dtech.cgo.Model.TransportationModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.Util.ViewUtil

import kotlinx.android.synthetic.main.activity_payment_detail.imgMerchant
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.*
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.btnHome
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.btnTicket
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.ivBack
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.linearBacktoHome
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.txtBankName
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.txtGuest
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.txtName
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.txtOrderID
import kotlinx.android.synthetic.main.activity_transportation_payment_detail.txtPrice

import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityTransportationPaymentDetail : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.DetailBookingCallback {

    private lateinit var transportList: ArrayList<TransportationModel>
    private lateinit var bookingController: BookingController

    private var order_id = ""

    private var adultCount = 0
    private var childrenCount = 0
    private var infantCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transportation_payment_detail)
        setView()
    }

    private fun setView() {
        bookingController = BookingController()

        intent.extras?.let { bundle ->

            order_id = bundle.getString("order_id") ?: ""
            bookingController.getDetailBooking(order_id, this)

        }

        ivBack.setOnClickListener(this)
        btnTicket.setOnClickListener(this)
        btnDepartureSeeTicket.setOnClickListener(this)
        btnReturnSeeTicket.setOnClickListener(this)
        btnHome.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setReturnState() {
        linearBacktoHome.visibility = View.GONE
        linearDepartureSeeTicket.visibility = View.VISIBLE
        linearReturn.visibility = View.VISIBLE
        btnTicket.text = "BACK TO HOME"
    }

    @SuppressLint("SetTextI18n")
    private fun setGuestText(textView: TextView) {
        if (adultCount != 0 && childrenCount == 0 && infantCount == 0) {
            if (adultCount > 1) {
                textView.text = "$adultCount Adults"
            } else {
                textView.text = "$adultCount Adult"
            }
        } else if (adultCount != 0 && childrenCount != 0 && infantCount == 0) {
            if (adultCount > 1) {
                if (childrenCount > 1) {
                    textView.text = "$adultCount Adults, $childrenCount Children"
                } else {
                    textView.text = "$adultCount Adults, $childrenCount Children"
                }
            } else {
                if (childrenCount > 1) {
                    textView.text = "$adultCount Adult, $childrenCount Children"
                } else {
                    textView.text = "$adultCount Adult, $childrenCount Children"
                }
            }
        } else {
            if (adultCount > 1) {
                if (childrenCount > 1) {
                    if (infantCount > 1) {
                        textView.text =
                            "$adultCount Adults, $childrenCount Children, $infantCount Infants"
                    } else {
                        textView.text =
                            "$adultCount Adults, $childrenCount Children, $infantCount Infant"
                    }
                } else {
                    if (infantCount > 1) {
                        textView.text =
                            "$adultCount Adults, $childrenCount Children, $infantCount Infants"
                    } else {
                        textView.text =
                            "$adultCount Adults, $childrenCount Children, $infantCount Infant"
                    }
                }
            } else {
                if (childrenCount > 1) {
                    if (infantCount > 1) {
                        textView.text =
                            "$adultCount Adult, $childrenCount Children, $infantCount Infants"
                    } else {
                        textView.text =
                            "$adultCount Adult, $childrenCount Children, $infantCount Infant"
                    }
                } else {
                    if (infantCount > 1) {
                        textView.text =
                            "$adultCount Adult, $childrenCount Children, $infantCount Infants"
                    } else {
                        textView.text =
                            "$adultCount Adult, $childrenCount Children, $infantCount Infant"
                    }
                }
            }
        }
    }

    private fun intentTicket(from: Int) {
        val i = Intent(this, ActivityTransportationTicket::class.java)
        i.putExtra("order_id", order_id)
        i.putExtra("from", from)
        startActivity(i)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ivBack -> {
                finish()
            }

            R.id.btnTicket -> {
                if (transportList.size == 1) {
                    intentTicket(0)
                } else {
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }
            }

            R.id.btnDepartureSeeTicket -> {
                intentTicket(0)
            }

            R.id.btnReturnSeeTicket -> {
                intentTicket(1)
            }

            R.id.btnHome -> {
                val i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        }
    }

    override fun onDetailBookingPrepare() {

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onDetailBookingSuccess(data: HashMap<String, Any>) {

        val bookedByArray = data["booked_by"] as ArrayList<HashMap<String, Any>>
        val bookedByMap = bookedByArray[0]
        val totalGuestList = data["guest_desc"] as ArrayList<HashMap<String, Any>>

        val totalPrice = data["total_price"] as Long
        val currency = data["currency"] as String
        val strPrice = "$currency " + CurrencyUtil.decimal(totalPrice).replace(
            ",", "."
        )
        val totalGuest = totalGuestList.size

        for (i in 0 until totalGuestList.size){
            val guest = totalGuestList[i]
            val type = guest["type"] as String

            if (type == "Adult"){
                adultCount += 1
            }
            else if (type == "Children"){
                childrenCount += 1
            }
            else{
                infantCount += 1
            }
        }

        setGuestText(txtDepartureGuest)
        setGuestText(txtReturnGuest)

        txtName.text = bookedByMap["fullname"] as String
        txtOrderID.text = order_id
        txtPrice.text = strPrice
        txtBankName.text = data["payment_type"] as String

        if (totalGuest > 1) {
            txtGuest.text = "$totalGuest Guest(s)"
        } else {
            txtGuest.text = "$totalGuest Guest"
        }

        transportList = data["transportation"] as ArrayList<TransportationModel>

        if (transportList.size > 1) {

                val transportationModel = transportList[1]
                val merchantName = transportationModel.merchant_name ?: ""
                val merchantImg = transportationModel.merchant_picture ?: ""
                val strClass = transportationModel.transportClass ?: ""

                val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                val dateSdf = SimpleDateFormat("EEE, dd MMM")
                val sdf = SimpleDateFormat("HH:mm:ss")
                val sdfs = SimpleDateFormat("HH:mm a")

                val departureTime = sdf.parse(transportationModel.departure_time ?: "") ?: Date()
                val departureDate = sdfDate.parse(transportationModel.departure_date ?: "") ?: Date()

                val strDate = dateSdf.format(departureDate)
                val strTime = sdfs.format(departureTime)
                val strDepartureDate = "$strDate - Departure $strTime"

                txtDepartureOrigin.text = transportationModel.harbor_source_name ?: ""
                txtDepartureDestination.text = transportationModel.harbor_destination_name ?: ""
                txtDepartureDate.text = strDepartureDate

                if (merchantImg.isNotEmpty()) {
                    Picasso.get().load(merchantImg).into(imgDepartureMerchant)
                }

                txtDepartureMerchantName.text = merchantName
                txtDepartureClass.text = strClass

                val returnTransportationModel = transportList[0]
                val returnMerchantName = returnTransportationModel.merchant_name ?: ""
                val returnMerchantImg = returnTransportationModel.merchant_picture ?: ""
                val returnStrClass = returnTransportationModel.transportClass ?: ""

                val sdfDate1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                val dateSdf1 = SimpleDateFormat("EEE, dd MMM")

                val sdfTime1 = SimpleDateFormat("HH:mm:ss")
                val sdfsTime2 = SimpleDateFormat("HH:mm a")

                val returnDateTime = sdfTime1.parse(returnTransportationModel.departure_time ?: "") ?: Date()
                val returnDate = sdfDate1.parse(returnTransportationModel.departure_date ?: "") ?: Date()

                val strDate1 = dateSdf1.format(returnDate)
                val strTime1 = sdfsTime2.format(returnDateTime)
                val strDepartureDate1 = "$strDate1 - Departure $strTime1"

                txtReturnOrigin.text = returnTransportationModel.harbor_source_name ?: ""
                txtReturnDestination.text = returnTransportationModel.harbor_destination_name ?: ""
                txtReturnDate.text = strDepartureDate1

                if (returnMerchantImg.isNotEmpty()) {
                    Picasso.get().load(returnMerchantImg).into(imgReturnMerchant)
                }

                txtReturnMerchantName.text = returnMerchantName
                txtReturnClass.text = returnStrClass

                setReturnState()
        }
        else{
            val transportationModel = transportList[0]
            val merchantName = transportationModel.merchant_name ?: ""
            val merchantImg = transportationModel.merchant_picture ?: ""
            val strClass = transportationModel.transportClass ?: ""

            val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            val dateSdf = SimpleDateFormat("EEE, dd MMM")
            val sdf = SimpleDateFormat("HH:mm:ss")
            val sdfs = SimpleDateFormat("HH:mm a")

            val departureTime = sdf.parse(transportationModel.departure_time ?: "") ?: Date()
            val departureDate = sdfDate.parse(transportationModel.departure_date ?: "") ?: Date()

            val strDate = dateSdf.format(departureDate)
            val strTime = sdfs.format(departureTime)
            val strDepartureDate = "$strDate - Departure $strTime"

            txtDepartureOrigin.text = transportationModel.harbor_source_name ?: ""
            txtDepartureDestination.text = transportationModel.harbor_destination_name ?: ""
            txtDepartureDate.text = strDepartureDate

            if (merchantImg.isNotEmpty()){
                Picasso.get().load(merchantImg).into(imgDepartureMerchant)
            }

            txtDepartureMerchantName.text = merchantName
            txtDepartureClass.text = strClass
        }
    }

    override fun onDetailBookingError(message: String) {
        ViewUtil.showBlackToast(this,message,0).show()
    }
}
