package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import id.dtech.cgo.Preferance.ProfileSession
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_share_referal.*

class ActivityShareReferal : AppCompatActivity(), View.OnClickListener{

    private lateinit var shareDialog : Dialog
    private lateinit var profileSession : ProfileSession
    private lateinit var userSession : UserSession

    private lateinit var imgShareCLose : ImageView
    private lateinit var linearFacebook : LinearLayout
    private lateinit var linearTwitter : LinearLayout

    private lateinit var facebookShareDialog : ShareDialog

    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_referal)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        profileSession = ProfileSession(getSharedPreferences("profile_session", Context.MODE_PRIVATE))
        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))

        if (userSession.access_token != null){
            cardView.visibility = View.VISIBLE
            btnNext.text = "SHARE NOW"
            txtReferal.text = profileSession.referral_code ?: ""
            isLogin = true
        }
        else{
            cardView.visibility = View.GONE
            btnNext.text = "LOG IN AND START SHARING"
            isLogin = false
        }

        setShareDialog()

        btnNext.setOnClickListener(this)
        imgCopy.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    private fun setShareDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_sharewith,null)
        shareDialog = Dialog(this,R.style.FullWidth_Dialog)
        shareDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        shareDialog.setContentView(view)
        shareDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        imgShareCLose = view.findViewById(R.id.imgShareCLose)
        linearFacebook = view.findViewById(R.id.linearFacebook)
        linearTwitter = view.findViewById(R.id.linearTwitter)

        imgShareCLose.setOnClickListener(this)
        linearFacebook.setOnClickListener(this)
        linearTwitter.setOnClickListener(this)
    }

    private fun copyToClipBoard(){
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text",  profileSession.referral_code ?: "")
        myClipboard.setPrimaryClip(myClip)
        ViewUtil.showBlackToast(this,"Referal code copied",0).show()
    }

    private fun setsShareFacebook(){
        val experienceLink = "http://cgo.co.id/"
        facebookShareDialog = ShareDialog(this)
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val linkContent =  ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(experienceLink))
                .setQuote("Facebook : Have you tried cGO? Use my code and you will get up to IDR 300.000 for your first booking.")
                .setShareHashtag(
                    ShareHashtag.Builder()
                    .setHashtag("#cGOExplore")
                    .build()
                )
                .build()

            facebookShareDialog.show(linkContent)
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.imgShareCLose -> {
                shareDialog.dismiss()
            }

            R.id.linearFacebook -> {
                setsShareFacebook()
            }

            R.id.linearTwitter -> {

            }

            R.id.imgCopy -> {
                copyToClipBoard()
            }

            R.id.btnNext -> {
                 if (isLogin){
                    shareDialog.show()
                 }
                else{
                     startActivity(Intent(this, ActivityLogin::class.java))
                 }
            }
        }
    }
}
