package id.dtech.cgo.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Preferance.ProfileSession
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import id.dtech.cgo.Util.FileUtils
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_editprofile.*
import java.io.File

class ActivityEditprofile : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.EditProfileCallback {

    private lateinit var storageDialog : AlertDialog
    private lateinit var userController: UserController
    private lateinit var userSession: UserSession
    private lateinit var profileSession: ProfileSession
    private lateinit var loadingDialog : AlertDialog

    private lateinit var imgShareCLose : ImageView
    private lateinit var txtCancel : MyTextView
    private lateinit var txtSetting : MyTextView

    private var file : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
        setView()
    }

    private fun setView(){
        userController = UserController()
        loadingDialog = ViewUtil.laodingDialog(this)
        userSession = UserSession((getSharedPreferences("user_session", Context.MODE_PRIVATE)))
        profileSession = ProfileSession((getSharedPreferences("profile_session", Context.MODE_PRIVATE)))

        val imgUrl = profileSession.profile_pict_url ?: ""

        if (imgUrl.isNotEmpty()){
            Picasso.get().load(imgUrl).into(imgProfile)
        }

        edtName.setText(profileSession.full_name ?: "")
        edtEmail.setText(profileSession.user_email ?: "")
        edtPhoneNumber.setText(profileSession.phone_number ?: "")
        edtIdNumber.setText(profileSession.id_number ?: "")

        initiateStorageDialog()

        relativePhoto.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        ivBack.setOnClickListener(this)
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

    private fun showPhoneMenu(view : View, title : String, textView : TextView){
        val popupMenu = PopupMenu(this,view)
        popupMenu.menuInflater.inflate(R.menu.phone_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            textView.text = menuItem.title.toString().trim()
            true
        }
        popupMenu.show()
    }

    private fun showIdMenu(view : View, title : String, textView : TextView){
        val popupMenu = PopupMenu(this,view)
        popupMenu.menuInflater.inflate(R.menu.id_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            textView.text = menuItem.title.toString().trim()
            true
        }
        popupMenu.show()
    }

    private fun intentImage(){
        Dexter.withActivity(this).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "image/*"
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivityForResult(intent, 515)
                    }
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
    private fun editProfile(){

        val user_id = profileSession.id ?: ""
        val token = userSession.access_token ?: ""
        val bookedCountryCode = txtBookedPhone.text.toString().trim()
        val bookedIdCard = txtBookedID.text.toString().trim()
        val bookedFullname = edtName.text.toString().trim()
        val bookedEmail = edtEmail.text.toString().trim()
        val bookedPhoneNumber = edtPhoneNumber.text.toString().trim()
        val bookedIdNumber = edtIdNumber.text.toString().trim()
        val strPhoneNumber = bookedCountryCode.removeSuffix(".")+""+bookedPhoneNumber

        if (bookedFullname.isEmpty()){
            linearFullname.setBackgroundResource(R.drawable.background_border_red)
            txtFullnameError.visibility = View.VISIBLE
            return
        }
        else{
            linearFullname.setBackgroundResource(R.drawable.background_border)
            txtFullnameError.visibility = View.GONE
        }

        if (bookedEmail.isEmpty()){
            linearEmail.setBackgroundResource(R.drawable.background_border_red)
            txtEmailAddressError.visibility = View.VISIBLE
            txtEmailAddressError.text = "Fill in your email address"
            return
        }
        else{
            linearEmail.setBackgroundResource(R.drawable.background_border)
            txtEmailAddressError.visibility = View.GONE
        }

        if (!bookedEmail.isEmailValid()){
            linearEmail.setBackgroundResource(R.drawable.background_border_red)
            txtEmailAddressError.visibility = View.VISIBLE
            txtEmailAddressError.text = "Please enter valid email format"
            return
        }
        else{
            linearEmail.setBackgroundResource(R.drawable.background_border)
            txtEmailAddressError.visibility = View.GONE
        }

        if (bookedPhoneNumber.isEmpty()){
            linearPhoneNumber.setBackgroundResource(R.drawable.background_border_red)
            txtPhoneError.visibility = View.VISIBLE
            return
        }
        else{
            linearPhoneNumber.setBackgroundResource(R.drawable.background_border)
            txtPhoneError.visibility = View.GONE
        }

        if (bookedIdNumber.isEmpty()){
            linearId.setBackgroundResource(R.drawable.background_border_red)
            txtIdError.visibility = View.VISIBLE
            return
        }
        else{
            linearId.setBackgroundResource(R.drawable.background_border)
            txtIdError.visibility = View.GONE
        }

        val body = HashMap<String,Any>()
        body["id"] = user_id
        body["user_email"] = bookedEmail
        body["full_name"] = bookedFullname
        body["phone_number"] = strPhoneNumber
        body["id_number"] = bookedIdNumber

        userController.updateProfile(token,file,body,this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
            R.id.btnSave -> {
                editProfile()
            }
            R.id.relativePhoto-> {
                intentImage()
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
        }
    }

    override fun onEditProfilePrepare() {
        loadingDialog.show()
    }

    override fun onEditProfileSuccess(profile_pict_url : String) {
        loadingDialog.dismiss()
        val bookedCountryCode = txtBookedPhone.text.toString().trim()
        val bookedIdCard = txtBookedID.text.toString().trim()
        val bookedFullname = edtName.text.toString().trim()
        val bookedEmail = edtEmail.text.toString().trim()
        val bookedPhoneNumber = edtPhoneNumber.text.toString().trim()
        val bookedIdNumber = edtIdNumber.text.toString().trim()
        val strPhoneNumber = bookedCountryCode.removeSuffix(".")+""+bookedPhoneNumber

        profileSession.full_name = bookedFullname
        profileSession.user_email = bookedEmail
        profileSession.phone_number = bookedPhoneNumber
        profileSession.id_number = bookedIdNumber
        profileSession.profile_pict_url = profile_pict_url

        ViewUtil.showBlackToast(this,"Edit profile success",
            0).show()
    }

    override fun onEditProfileError(message: String, code: Int) {
        loadingDialog.dismiss()
        if (code == 401){
            ViewUtil.showBlackToast(this,"Session has expired, please login again",
                0).show()
        }
        else{
            ViewUtil.showBlackToast(this,"There was an error, please try again",
                0).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 515 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                val fullPhotoUri = it
                file = FileUtils.getFileFromUri(this,fullPhotoUri)
                val bitmap = BitmapFactory.decodeFile(file?.path ?: "")
                imgProfile.setImageBitmap(bitmap)
            }
        }
    }
}
