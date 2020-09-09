package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.Preferance.ProfileSession
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_sign_up.*

class ActivitySignUp : AppCompatActivity(), View.OnClickListener, MyCallback.Companion.RegisterCallback{

    private lateinit var loadingDialog : AlertDialog
    private lateinit var userController: UserController
    private lateinit var profileSession: ProfileSession
    private lateinit var userSession: UserSession

    private var phoneNumber = ""
    private var accessToken = ""
    private var expiresIn = 0L
    private var tokenType  = ""
    private var refreshToken = ""

    private var isPasswordShowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setView()
    }

    private fun setView(){

        loadingDialog = ViewUtil.laodingDialog(this)
        profileSession = ProfileSession(getSharedPreferences("profile_session", Context.MODE_PRIVATE))
        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        userController = UserController()

        val b = intent.extras

        b?.let {
            phoneNumber = it.getString("phone_number") ?: ""
            accessToken = it.getString("access_token") ?: ""
            expiresIn = it.getLong("expires_in")
            tokenType = it.getString("token_type") ?: ""
            refreshToken = it.getString("refresh_token") ?: ""

            txtNumber.text = phoneNumber
        }

        imgHide.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        btnDone.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun register(){
        val fullname = edtFullname.text.toString().trim()
        val email = edtAddress.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (fullname.isEmpty()){
            linearFullname.setBackgroundResource(R.drawable.background_border_red)
            txtErrorFullname.visibility = View.VISIBLE
            return
        }
        else{
            linearFullname.setBackgroundResource(R.drawable.background_border)
            txtErrorFullname.visibility = View.GONE
        }

        if (email.isEmpty()){
            txtErrorAddress.visibility = View.VISIBLE
            txtErrorAddress.text = "Please fill your email"
            linearAddress.setBackgroundResource(R.drawable.background_border_red)
            return
        }
        else{
            linearAddress.setBackgroundResource(R.drawable.background_border)
            txtErrorAddress.visibility = View.GONE
        }

        if (!email.isEmailValid()){
            txtErrorAddress.visibility = View.VISIBLE
            linearAddress.setBackgroundResource(R.drawable.background_border_red)
            txtErrorAddress.text = "Please fill with correct email format"
            return
        }
        else{
            linearAddress.setBackgroundResource(R.drawable.background_border)
            txtErrorAddress.visibility = View.GONE
        }

        if (password.isEmpty() || password.length < 7){
            txtErrorPassword.visibility = View.VISIBLE
            linearPassword.setBackgroundResource(R.drawable.background_border_red)
            return
        }
        else{
            linearPassword.setBackgroundResource(R.drawable.background_border)
            txtErrorPassword.visibility = View.GONE
        }

        val dataMap = HashMap<String,Any>()
        dataMap["user_email"] = email
        dataMap["password"] = password
        dataMap["full_name"] = fullname
        dataMap["phone_number"] = phoneNumber

        userController.requestRegister(dataMap,this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.imgHide -> {
                if (!isPasswordShowed){
                    edtPassword.transformationMethod = null
                    imgHide.setImageResource(R.drawable.ic_show_pass)
                    isPasswordShowed = true
                }
                else{
                    edtPassword.transformationMethod = PasswordTransformationMethod()
                    imgHide.setImageResource(R.drawable.ic_hide_pass)
                    isPasswordShowed = false
                }
            }

            R.id.btnDone -> {
                register()
            }
        }
    }

    override fun onRegisterPrepare() {
        loadingDialog.show()
    }

    override fun onRegisterSuccess(data: HashMap<String, Any>) {

        loadingDialog.dismiss()

       userSession.access_token = accessToken
       userSession.expires_in = expiresIn
       userSession.token_type = tokenType
       userSession.refresh_token = refreshToken

       profileSession.id = data["id"] as String
       profileSession.user_email = data["user_email"]  as String
       profileSession.full_name = data["full_name"] as String
       profileSession.phone_number = data["phone_number"] as String
       profileSession.id = data["verification_send_date"] as String
       profileSession.verification_code = data["verification_code"] as Int
       profileSession.profile_pict_url = data["profile_pict_url"] as String
       profileSession.address = data["address"] as String
       profileSession.dob = data["dob"] as String
       profileSession.gender = data["gender"] as Int
       profileSession.id_type = data["id_type"] as Int
       profileSession.id_number = data["id_number"] as String
       profileSession.referral_code = data["referral_code"] as String
       profileSession.points = data["points"] as Long
       profileSession.token = data["token"] as String

        val i = Intent(this,MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun onRegisterError(message: String, code: Int) {
        loadingDialog.dismiss()
        Toast.makeText(this,"Failed to process request",Toast.LENGTH_SHORT).show()
    }
}
