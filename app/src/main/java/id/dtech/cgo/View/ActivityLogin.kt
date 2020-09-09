package id.dtech.cgo.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_login.*


class ActivityLogin : AppCompatActivity(), View.OnClickListener, MyCallback.Companion.OTPCallback {

    private lateinit var loadingDialog : AlertDialog
    private lateinit var userController: UserController

    private var strPhoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setView()
    }

    private fun setView() {
        userController = UserController()
        loadingDialog = ViewUtil.laodingDialog(this)
        ivBack.setOnClickListener(this)
        linearPhone.setOnClickListener(this)
        btnNext.setOnClickListener(this)
    }

    private fun sendOtp(){
        val countryCode = txtCountryCode.text.toString().trim().removeSuffix(".")
        val phoneNumber = edtPhoneNumber.text.toString().trim()

        if (phoneNumber.isEmpty()){
            ViewUtil.showBlackToast(this,"Please input phone number",0).show()
            return
        }

        strPhoneNumber = "$countryCode$phoneNumber"

        val body = HashMap<String,Any>()
        body["phone_number"] = strPhoneNumber

        userController.requestOTP(body,this)
    }

    private fun intentActivityVerification(code : String, expiredDate : String, expiredMilis : Long){
        val i = Intent(this,ActivityVerification::class.java)
        i.putExtra("phone_number",strPhoneNumber)
        i.putExtra("code",code)
        i.putExtra("expired",expiredDate)
        i.putExtra("expired_milis",expiredMilis)
        startActivity(i)
    }

    private fun showPhoneMenu(view : View, title : String, textView : MyTextView){
        val popupMenu = PopupMenu(this,view)
        popupMenu.menuInflater.inflate(R.menu.phone_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            textView.text = menuItem.title.toString().trim()
            true
        }
        popupMenu.show()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.btnNext -> {
                sendOtp()
            }

            R.id.linearPhone -> {
                showPhoneMenu(linearPhone,"",txtCountryCode)
            }
        }
    }

    override fun onOTPPrepare() {
        loadingDialog.show()
    }

    override fun onOTPSuccess(code : String, expiredDate : String, expiredMilis : Long) {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,"Code sent",0).show()
        intentActivityVerification(code,expiredDate,expiredMilis)
    }

    override fun onOTPError() {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,"Oppss something looks like error, " +
                "please try again",0).show()
    }
}

