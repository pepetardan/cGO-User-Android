package id.dtech.cgo.View

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import id.dtech.cgo.BuildConfig
import id.dtech.cgo.Preferance.ProfileSession
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_settings.*

class ActivitySettings : AppCompatActivity(), View.OnClickListener {

    private lateinit var userSession: UserSession
    private lateinit var profileSession: ProfileSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setView()
    }

    private fun setView(){
        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        profileSession = ProfileSession(getSharedPreferences("profile_session", Context.MODE_PRIVATE))
        txtVersion.text = BuildConfig.VERSION_NAME

        cardLogout.setOnClickListener(this)
        linearRateApp.setOnClickListener(this)
        linearPrivacy.setOnClickListener(this)
        linearTnc.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    private fun intentMainActivity(){

        userSession.removeSharedPrefsData()
        profileSession.removeSharedPrefsData()

        val i = Intent(this,MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }


    private fun openAppInPlayStore() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri)

        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        }
        else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        goToMarketIntent.addFlags(flags)

        try {

            startActivity(goToMarketIntent)

        } catch (e: ActivityNotFoundException) {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName"))

            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.ivBack -> {
                finish()
            }

            R.id.cardLogout -> {
                intentMainActivity()
            }

            R.id.linearPrivacy -> {
                startActivity(Intent(this,ActivityPrivacyPolicy::class.java))
            }

            R.id.linearTnc -> {
                startActivity(Intent(this,ActivityTNC::class.java))
            }

            R.id.linearRateApp -> {
                openAppInPlayStore()
            }
        }
    }
}
