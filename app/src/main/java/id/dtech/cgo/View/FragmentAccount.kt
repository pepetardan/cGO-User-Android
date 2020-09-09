package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.paypal.android.sdk.payments.LoginActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.dtech.cgo.Preferance.ProfileSession
import id.dtech.cgo.Preferance.UserSession

import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil

class FragmentAccount : Fragment(), View.OnClickListener{

    companion object {
        @JvmStatic
        fun newInstance() = FragmentAccount()
    }

    private lateinit var userSession: UserSession
    private lateinit var profileSession: ProfileSession

    private lateinit var cardLogin : CardView

    private lateinit var linearReferal : LinearLayout
    private lateinit var linearSetting : LinearLayout
    private lateinit var imgEditProfile : ImageView

    private lateinit var linearCredit : LinearLayout
    private lateinit var linearWishlist : LinearLayout

    private lateinit var imgProfile : CircleImageView
    private lateinit var txtName : TextView
    private lateinit var txtEmail : TextView

    private lateinit var txtCreditPoint : TextView
    private lateinit var txtCreditContent : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        setView(view)
        return view
    }

    private fun setView(view: View){

        cardLogin = view.findViewById(R.id.cardLogin)

        imgProfile = view.findViewById(R.id.imgProfile)
        txtName = view.findViewById(R.id.txtName)
        txtEmail = view.findViewById(R.id.txtEmail)

        txtCreditPoint = view.findViewById(R.id.txtCreditPoint)
        txtCreditContent = view.findViewById(R.id.txtCreditContent)

        linearCredit = view.findViewById(R.id.linearCredit)
        linearWishlist = view.findViewById(R.id.linearWishlist)

        linearReferal = view.findViewById(R.id.linearReferal)
        imgEditProfile = view.findViewById(R.id.imgEditProfile)
        linearSetting = view.findViewById(R.id.linearSetting)

        activity?.let {
            userSession = UserSession(it.getSharedPreferences("user_session",Context.MODE_PRIVATE))
            profileSession = ProfileSession(it.getSharedPreferences("profile_session",Context.MODE_PRIVATE))
        }

        cardLogin.setOnClickListener(this)
        linearWishlist.setOnClickListener(this)
        linearReferal.setOnClickListener(this)
        imgEditProfile.setOnClickListener(this)
        linearSetting.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun checkIslogin(){
        if (userSession.access_token != null){

            val profileImage = profileSession.profile_pict_url ?: ""
            txtName.text = profileSession.full_name ?: ""
            txtEmail.text = profileSession.user_email ?: ""

            txtCreditPoint.text = CurrencyUtil.decimal(profileSession.points).replace(",",".")
            txtCreditContent.text = "Collect and redeem your credits"

            if (profileImage.isNotEmpty()){
                Picasso.get().load(profileImage).into(imgProfile)
            }

            imgEditProfile.visibility = View.VISIBLE

            linearCredit.setBackgroundColor(Color.parseColor("#FFFFFF"))
            linearWishlist.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun onResume() {
        super.onResume()
        checkIslogin()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.linearReferal -> {
                activity?.let {
                    startActivity(Intent(it,ActivityShareReferal::class.java))
                }
            }
            R.id.imgEditProfile -> {
                activity?.let {
                    startActivity(Intent(it,ActivityEditprofile::class.java))
                }
            }
            R.id.linearSetting -> {
                activity?.let {
                    startActivity(Intent(it,ActivitySettings::class.java))
                }
            }
            R.id.linearWishlist -> {
                activity?.let {
                    if (userSession.access_token != null){
                        startActivity(Intent(it,ActivityWishlist::class.java))
                    }
                }
            }
            R.id.cardLogin -> {
                activity?.let {
                    if (userSession.access_token == null){
                        startActivity(Intent(it,ActivityLogin::class.java))
                    }
                }
            }
        }
    }
}
