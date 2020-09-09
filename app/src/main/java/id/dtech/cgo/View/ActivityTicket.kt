package id.dtech.cgo.View
import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
import id.dtech.cgo.Adapter.ServiceTypeAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.BookingController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import id.dtech.cgo.Util.DownloadFileFromURL
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_ticket.*
import kotlinx.android.synthetic.main.activity_ticket.txtOrderID
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*

class ActivityTicket : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.PdfFileCallback, MyCallback.Companion.DetailBookingCallback{

    private lateinit var loadingDialog : AlertDialog
    private lateinit var storageDialog : AlertDialog

    private lateinit var imgShareCLose : ImageView
    private lateinit var txtCancel : MyTextView
    private lateinit var txtSetting : MyTextView

    private lateinit var guestList : ArrayList<HashMap<String,Any>>
    private lateinit var bookingController: BookingController
    private var order_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)
        setView()
    }

    private fun setView(){

        loadingDialog = ViewUtil.laodingDialog(this)

        rvType.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,
            false)
        bookingController = BookingController()

        intent.extras?.let { bundle ->
            order_id = bundle.getString("order_id") ?: ""
            bookingController.getDetailBooking(order_id,this)
        }

        initiateStorageDialog()

        imgCopy.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        ivDownload.setOnClickListener(this)
        linearGuest.setOnClickListener(this)
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
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    bookingController.getPDF(order_id,"experience",this@ActivityTicket)
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

    private fun copyToClipBoard(){
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text",  txtMerchantPhone.text ?: "")
        myClipboard.setPrimaryClip(myClip)
        ViewUtil.showBlackToast(this,"Account number copied",0).show()
    }

    override fun onClick(v: View?) {
        when(v?.id){
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

            R.id.imgCopy -> {
                copyToClipBoard()
            }
        }
    }

    override fun onDetailBookingPrepare() {

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onDetailBookingSuccess(data: HashMap<String, Any>) {
        val experienceMap = data["experience"] as HashMap<String,Any>
        val bookedByArray = data["booked_by"] as ArrayList<HashMap<String,Any>>
        val bookedByMap = bookedByArray[0]
        val total_price = data["total_price"] as Long
        val currency = data["currency"] as String
        val typeList = experienceMap["exp_type"] as ArrayList<String>
        val expDuration = experienceMap["exp_duration"] as Int
        val bookingDate = data["booking_date"] as String
        val harborName = experienceMap["harbors_name"] as String
        val provinceName = experienceMap["province_name"] as String

        guestList = data["guest_desc"] as ArrayList<HashMap<String,Any>>
        val total_guest = guestList.size
        val merchant_picture = experienceMap["merchant_picture"] as String
        val merchant_name = experienceMap["merchant_name"] as String
        val merchant_phone = experienceMap["merchant_phone"] as String
        val barcode_picture = data["ticket_qr_code"] as String

        rvType.adapter = ServiceTypeAdapter(this,typeList)
        applyDate(bookingDate,expDuration)

        if (barcode_picture.isNotEmpty()){
            Picasso.get().load(barcode_picture).into(imgQrCode)
        }

        if (merchant_picture.isNotEmpty()){
            Picasso.get().load(merchant_picture).into(imgMerchantPick)
        }

        if (total_guest > 1){
            txtGuest.text = "$total_guest Guest(s)"
        }
        else{
            txtGuest.text = "$total_guest Guest"
        }

        val sdfTime = SimpleDateFormat("HH:mm:ss")
        val sdfsTime = SimpleDateFormat("HH:mm")
        val myDate = sdfTime.parse(experienceMap["exp_pickup_time"] as String) ?: Date()
        val strTime = sdfsTime.format(myDate)

        if (experienceMap["exp_pickup_time"] as String =="00:00:00"){
            linearTime.visibility = View.GONE
        }

        txtOrderID.text = order_id
        txtLocation.text = "$harborName, $provinceName"
        txtTitle.text = experienceMap["exp_title"] as String
        txtMeetPlace.text = experienceMap["exp_pickup_place"] as String
        txtPickupTime.text = strTime
        txtMerchantName.text =  merchant_name
        txtMerchantPhone.text = merchant_phone

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

    override fun onDetailBookingError(message: String) {

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
