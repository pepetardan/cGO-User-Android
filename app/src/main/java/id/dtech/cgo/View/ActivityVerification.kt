package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.poovam.pinedittextfield.PinField
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.Preferance.ProfileSession
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_verification.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

class ActivityVerification : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.LoginCallback, MyCallback.Companion.UserInfoCallback {

    private lateinit var loadingDialog : AlertDialog

    private lateinit var userSession: UserSession
    private lateinit var profileSession : ProfileSession
    private lateinit var userController: UserController

    private var strPhoneNumber = ""
    private var strCode = ""
    private var strExpired = ""
    private var expiredMilis = 0L

    private var inputedCode = ""

    private var accessToken = ""
    private var expiresIn = 0L
    private var tokenType  = ""
    private var refreshToken = ""

    private var isLoginSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){

        loadingDialog = ViewUtil.laodingDialog(this)
        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        profileSession = ProfileSession(getSharedPreferences("profile_session", Context.MODE_PRIVATE))
        userController = UserController()

        val b = intent.extras

        b?.let { bundle ->

            strPhoneNumber = bundle.getString("phone_number") ?: ""
            strCode = bundle.getString("code") ?: ""
            strExpired = bundle.getString("expired") ?: ""
            expiredMilis = bundle.getLong("expired_milis")

            txtPhone.text = "Please type the verification code sent \n to $strPhoneNumber"
            etBlock.setText(strCode)
            countDown()
        }

        etBlock.onTextCompleteListener = object : PinField.OnTextCompleteListener{
            override fun onTextComplete(enteredText: String): Boolean {
                inputedCode = enteredText
                val textLength = enteredText.length
                val isLength = textLength >= 6

                btnConfirm.isEnabled = isLength

                return isLength
            }
        }

        ivBack.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
    }

    private fun countDown() {

        val countDownTimer = object : CountDownTimer(expiredMilis, 1000) {
            override fun onTick(p0: Long) {
                val millis: Long = p0

                val time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

                txTime.text = time
            }

            override fun onFinish() {
                println("Time up")
            }
        }
        
        countDownTimer.start()
    }

    private fun login(){
        val body = HashMap<String,Any>()
        body["email"] = strPhoneNumber
        body["password"] = inputedCode
        body["type"] = "user"
        body["scope"] = "phone_number"

        userController.requestLogin(body,this)
    }

    private fun intentRegister(){
        val i = Intent(this,ActivitySignUp::class.java)
        i.putExtra("phone_number",strPhoneNumber)
        i.putExtra("access_token",accessToken)
        i.putExtra("expires_in",expiresIn)
        i.putExtra("token_type",tokenType)
        i.putExtra("refresh_token",refreshToken)
        startActivity(i)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.btnConfirm -> {
                login()
            }
        }
    }

    override fun onLoginPrepare() {
        loadingDialog.show()
    }

    override fun onLoginSuccess(data : HashMap<String,Any>) {

        accessToken = data["access_token"]  as String
        expiresIn = data["expires_in"] as Long
        tokenType = data["token_type"] as String
        refreshToken = data["refresh_token"] as String

        userSession.access_token = accessToken
        userSession.expires_in = expiresIn
        userSession.token_type = tokenType
        userSession.refresh_token = refreshToken

        userController.getUserInfo(accessToken,this)

    }

    override fun onLoginError(message: String, code: Int) {

        loadingDialog.dismiss()

        if (code == 401){
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
        else if (code == 404){
            intentRegister()
        }
        else if (code == 115){
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,"Looks like something error, please try again",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onUserInfoPrepare() {
        Log.d("profile_loading","Loading...")
    }

    override fun onUserInfoSuccess(data: HashMap<String, Any>) {

        isLoginSuccess = true

        profileSession.id = data["id"] as String
        profileSession.user_email = data["user_email"]  as String
        profileSession.full_name = data["full_name"] as String
        profileSession.phone_number = data["phone_number"] as String
        profileSession.profile_pict_url = data["profile_pict_url"] as String
        profileSession.address = data["address"] as String
        profileSession.dob = data["dob"] as String
        profileSession.gender = data["gender"] as Int
        profileSession.id_type = data["id_type"] as Int
        profileSession.id_number = data["id_number"] as String
        profileSession.referral_code = data["referral_code"] as String
        profileSession.points = data["points"] as Long

        val i = Intent(this,MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun onUserInfoError(error: String) {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,error,0).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isLoginSuccess){
            userSession.removeSharedPrefsData()
        }
    }
}
