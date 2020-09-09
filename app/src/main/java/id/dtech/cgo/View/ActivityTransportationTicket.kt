package id.dtech.cgo.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import com.squareup.picasso.Picasso
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.BookingController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.TransportationModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.DownloadFileFromURL
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_ticket.*
import kotlinx.android.synthetic.main.activity_transportation_ticket.*
import kotlinx.android.synthetic.main.activity_transportation_ticket.imgQrCode
import kotlinx.android.synthetic.main.activity_transportation_ticket.ivBack
import kotlinx.android.synthetic.main.activity_transportation_ticket.ivDownload
import kotlinx.android.synthetic.main.activity_transportation_ticket.linearGuest
import kotlinx.android.synthetic.main.activity_transportation_ticket.txtGuest
import kotlinx.android.synthetic.main.activity_transportation_ticket.txtMerchantName
import kotlinx.android.synthetic.main.activity_transportation_ticket.txtOrderID
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityTransportationTicket : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.DetailBookingCallback, MyCallback.Companion.PdfFileCallback {

    private lateinit var guestList : java.util.ArrayList<java.util.HashMap<String, Any>>
    private lateinit var transportList : ArrayList<TransportationModel>
    private lateinit var bookingController: BookingController

    private lateinit var loadingDialog : AlertDialog
    private lateinit var storageDialog : AlertDialog

    private lateinit var imgShareCLose : ImageView
    private lateinit var txtCancel : MyTextView
    private lateinit var txtSetting : MyTextView

    private var order_id = ""
    private var from = 0

    private var adultCount = 0
    private var childrenCount = 0
    private var infantCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transportation_ticket)
        setView()
    }

    private fun setView(){
        loadingDialog = ViewUtil.laodingDialog(this)
        bookingController = BookingController()

        intent.extras?.let { bundle ->
            order_id = bundle.getString("order_id") ?: ""
            from = bundle.getInt("from")
            bookingController.getDetailBooking(order_id,this)
        }

        initiateStorageDialog()

        linearGuest.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        ivDownload.setOnClickListener(this)
    }

    private fun initiateStorageDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_setting,null)
        storageDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        storageDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        imgShareCLose = view.findViewById(R.id.imgShareCLose)
        txtCancel = view.findViewById(R.id.txtCancel)
        txtSetting = view.findViewById(R.id.txtSetting)

        imgShareCLose.setOnClickListener(this)
        txtCancel.setOnClickListener(this)
        txtSetting.setOnClickListener(this)
    }

    private fun intentSetting(){
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun downloadPDF(){
        Dexter.withActivity(this).withPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    bookingController.getPDF(order_id,"transportation",this@ActivityTransportationTicket)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    storageDialog.show()
                }
            })
            .check()
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

    @SuppressLint("CheckResult")
    private fun downloadFilePDF(storageUrl : String){
        loadingDialog.dismiss()
        storageUrl.replace("\"","")
        val file = File(Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
                + "/cGO/")

        if (file.exists() && file.isDirectory){
            DownloadFileFromURL(this,order_id).execute(storageUrl)
        }
        else{
            file.mkdir()
            DownloadFileFromURL(this,order_id).execute(storageUrl)
        }
    }

    override fun onDetailBookingPrepare() {

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onDetailBookingSuccess(data: HashMap<String, Any>) {
        guestList = data["guest_desc"] as java.util.ArrayList<java.util.HashMap<String, Any>>

        val qrUrl = data["ticket_qr_code"] as String
        val order_id = data["order_id"] as String

        if (qrUrl.isNotEmpty()){
            Picasso.get().load(qrUrl).into(imgQrCode)
        }

        txtOrderID.text = order_id

        for (i in 0 until guestList.size){
            val guest = guestList[i]
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
        
        setGuestText(txtGuestDetail)

        transportList = data["transportation"] as ArrayList<TransportationModel>

        if (transportList.size > 1){
            if (from == 0){
                val transportationModel = transportList[1]
                val totalGuest = transportationModel.totalGuest
                val merchantName = transportationModel.merchant_name ?: ""
                val merchantImg = transportationModel.merchant_picture ?: ""
                val strClass = transportationModel.transportClass ?: ""

                val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                val dateSdf = SimpleDateFormat("dd MMM")

                val sdf = SimpleDateFormat("HH:mm:ss")
                val sdfs = SimpleDateFormat("HH:mm")

                val departureDate = sdfDate.parse(transportationModel.departure_date ?: "")
                val startDateTime = sdf.parse(transportationModel.departure_time ?: "")
                val endDateTime = sdf.parse(transportationModel.arrival_time ?: "")

                txtOrigin.text = transportationModel.harbor_source_name ?: ""
                txtDestination.text = transportationModel.harbor_destination_name ?: ""

                txtDepartureTime.text = sdfs.format(startDateTime ?: Date())
                txtArrivalTime.text = sdfs.format(endDateTime ?: Date())

                txtDate1.text = dateSdf.format(departureDate ?: Date())
                txtDate2.text = dateSdf.format(departureDate ?: Date())

                txtDuration.text = transportationModel.trip_duration ?: ""

                if (merchantImg.isNotEmpty()){
                    Picasso.get().load(merchantImg).into(imgMerchant)
                }

                txtMerchantName.text = merchantName
                txtClass.text = strClass


                if (totalGuest > 1){
                    txtGuest.text = "$totalGuest Guest(s)"
                }
                else{
                    txtGuest.text = "$totalGuest Guest"
                }
            }
            else{
                val transportationModel = transportList[0]
                val totalGuest = transportationModel.totalGuest
                val merchantName = transportationModel.merchant_name ?: ""
                val merchantImg = transportationModel.merchant_picture ?: ""
                val strClass = transportationModel.transportClass ?: ""

                val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                val dateSdf = SimpleDateFormat("dd MMM")

                val sdf = SimpleDateFormat("HH:mm:ss")
                val sdfs = SimpleDateFormat("HH:mm")

                val departureDate = sdfDate.parse(transportationModel.departure_date ?: "")
                val startDateTime = sdf.parse(transportationModel.departure_time ?: "")
                val endDateTime = sdf.parse(transportationModel.arrival_time ?: "")

                txtOrigin.text = transportationModel.harbor_source_name ?: ""
                txtDestination.text = transportationModel.harbor_destination_name ?: ""

                txtDepartureTime.text = sdfs.format(startDateTime ?: Date())
                txtArrivalTime.text = sdfs.format(endDateTime ?: Date())

                txtDate1.text = dateSdf.format(departureDate ?: Date())
                txtDate2.text = dateSdf.format(departureDate ?: Date())

                txtDuration.text = transportationModel.trip_duration ?: ""

                if (merchantImg.isNotEmpty()){
                    Picasso.get().load(merchantImg).into(imgMerchant)
                }

                txtMerchantName.text = merchantName
                txtClass.text = strClass


                if (totalGuest > 1){
                    txtGuest.text = "$totalGuest Guest(s)"
                }
                else{
                    txtGuest.text = "$totalGuest Guest"
                }
            }
        }
        else{
            val transportationModel = transportList[0]
            val totalGuest = transportationModel.totalGuest
            val merchantName = transportationModel.merchant_name ?: ""
            val merchantImg = transportationModel.merchant_picture ?: ""
            val strClass = transportationModel.transportClass ?: ""

            val sdfDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            val dateSdf = SimpleDateFormat("dd MMM")

            val sdf = SimpleDateFormat("HH:mm:ss")
            val sdfs = SimpleDateFormat("HH:mm")

            val departureDate = sdfDate.parse(transportationModel.departure_date ?: "")

            val startDateTime = sdf.parse(transportationModel.departure_time ?: "")
            val endDateTime = sdf.parse(transportationModel.arrival_time ?: "")

            txtOrigin.text = transportationModel.harbor_source_name ?: ""
            txtDestination.text = transportationModel.harbor_destination_name ?: ""

            txtDepartureTime.text = sdfs.format(startDateTime ?: Date())
            txtArrivalTime.text = sdfs.format(endDateTime ?: Date())

            txtDate1.text = dateSdf.format(departureDate ?: Date())
            txtDate2.text = dateSdf.format(departureDate ?: Date())

            txtDuration.text = transportationModel.trip_duration ?: ""

            if (merchantImg.isNotEmpty()){
                Picasso.get().load(merchantImg).into(imgMerchant)
            }

            txtMerchantName.text = merchantName
            txtClass.text = strClass

            if (totalGuest > 1){
                txtGuest.text = "$totalGuest Guest(s)"
            }
            else{
                txtGuest.text = "$totalGuest Guest"
            }
        }
    }

    override fun onDetailBookingError(message: String) {

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.ivDownload -> {
                downloadPDF()
            }

            R.id.imgShareCLose -> {
                storageDialog.dismiss()
            }

            R.id.txtCancel -> {
                storageDialog.dismiss()
            }

            R.id.txtSetting -> {
                intentSetting()
            }

            R.id.linearGuest -> {
                val i = Intent(this,ActivityGuestDetail::class.java)
                i.putExtra("guest_list",guestList)
                startActivity(i)
            }
        }
    }

    override fun onPdfFilePrepare() {
        loadingDialog.show()
    }

    override fun onPdfFileSuccess(fileURL : String) {
        downloadFilePDF(fileURL)
    }

    override fun onPdfFileError(error: String) {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,error,0).show()
    }
}
