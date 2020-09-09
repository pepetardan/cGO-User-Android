package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import id.dtech.cgo.BuildConfig
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Preferance.BoardingSession
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.VersionCallback {

    private var from = 0

    private lateinit var userController: UserController
    private lateinit var boardingSession: BoardingSession

    private lateinit var updateDialog : AlertDialog
    private lateinit var showCaseDialog : Dialog

    private lateinit var linearDialogExperience : LinearLayout
    private lateinit var linearDialogTransportation : LinearLayout
    private lateinit var linearDialogContent : LinearLayout

    private lateinit var txtDialogTitle : MyTextView
    private lateinit var txtDialogContent : MyTextView
    private lateinit var txtDialogNext : MyTextView

    private lateinit var imgShareCLose : ImageView
    private lateinit var txtLater : MyTextView
    private lateinit var txtUpdate : MyTextView

    private var isNextClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
    }

    private fun setView(){

        userController = UserController()
        boardingSession = BoardingSession(getSharedPreferences("boarding_session", Context.MODE_PRIVATE))

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        
        val b = intent.extras

        if (b != null){
            from = b.getInt("from")
            setBottomNavigation(3)
        }
        else{
            setBottomNavigation(1)

            if (!boardingSession.isMainShowCase){
                initiateDialogFragment()
            }
        }

        initiateUpdateDialog()
        userController.getVersion(1,this)
        frameExplore.setOnClickListener(this)
        frameBlog.setOnClickListener(this)
        frameTrips.setOnClickListener(this)
        frameAccount.setOnClickListener(this)

    }

    private fun initiateDialogFragment(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_home_showcase,null)
        showCaseDialog = Dialog(this,R.style.FullWidth_Dialog)
        showCaseDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        showCaseDialog.setContentView(view)
        showCaseDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        showCaseDialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        showCaseDialog.window?.statusBarColor = Color.TRANSPARENT

        linearDialogExperience  = view.findViewById(R.id.linearDialogExperience)
        linearDialogTransportation = view.findViewById(R.id.linearDialogTransportation)
        linearDialogContent = view.findViewById(R.id.linearDialogContent)

        txtDialogTitle = view.findViewById(R.id.txtDialogTitle)
        txtDialogContent = view.findViewById(R.id.txtDialogContent)
        txtDialogNext = view.findViewById(R.id.txtDialogNext)

        txtDialogNext.setOnClickListener(this)

        showCaseDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setDialogNextButton(){
        if (!isNextClicked){
            linearDialogExperience.visibility = View.INVISIBLE
            linearDialogTransportation.visibility = View.VISIBLE
            txtDialogTitle.text = "Book Sea Transportation"
            txtDialogContent.text = "Booking a ferry has never been this easy. Find your sea " +
                    "transportation here."
            txtDialogNext.text = "OK, GOT IT"
            linearDialogContent.setBackgroundResource(R.drawable.img_tsp_coachmark)
            isNextClicked = true
        }
        else{
            showCaseDialog.dismiss()
            isNextClicked = false
            boardingSession.isMainShowCase = true
        }
    }

    private fun setBottomNavigation(position : Int){

        val blueLayoutParam = FrameLayout.LayoutParams(44.dp,44.dp)
        val grayLayoutParam = FrameLayout.LayoutParams(26.dp,26.dp)

        blueLayoutParam.setMargins(0, 12.dp, 0, 0)
        grayLayoutParam.setMargins(0, 20.dp, 0, 0)

        blueLayoutParam.gravity = Gravity.CENTER_HORIZONTAL
        grayLayoutParam.gravity = Gravity.CENTER_HORIZONTAL

        if (position == 1){

            imgExplore.layoutParams = blueLayoutParam
            imgBlog.layoutParams = grayLayoutParam
            imgTrips.layoutParams = grayLayoutParam
            imgAccount.layoutParams = grayLayoutParam

            imgExplore.setImageResource(R.drawable.ic_explore_blue)
            imgBlog.setImageResource(R.drawable.ic_blog_gray)
            imgTrips.setImageResource(R.drawable.ic_trip_gray)
            imgAccount.setImageResource(R.drawable.ic_profile_gray)

            txtExplore.setTextColor(Color.parseColor("#233E98"))
            txtBlog.setTextColor(Color.parseColor("#6D6D6D"))
            txtTrips.setTextColor(Color.parseColor("#6D6D6D"))
            txtAccount.setTextColor(Color.parseColor("#6D6D6D"))

            goToFragment(FragmentExplore.newInstance())
        }
        else if (position == 2){

            imgExplore.layoutParams = grayLayoutParam
            imgBlog.layoutParams = blueLayoutParam
            imgTrips.layoutParams = grayLayoutParam
            imgAccount.layoutParams = grayLayoutParam

            imgExplore.setImageResource(R.drawable.ic_explore_gray)
            imgBlog.setImageResource(R.drawable.ic_blog_blue)
            imgTrips.setImageResource(R.drawable.ic_trip_gray)
            imgAccount.setImageResource(R.drawable.ic_profile_gray)

            txtExplore.setTextColor(Color.parseColor("#6D6D6D"))
            txtBlog.setTextColor(Color.parseColor("#233E98"))
            txtTrips.setTextColor(Color.parseColor("#6D6D6D"))
            txtAccount.setTextColor(Color.parseColor("#6D6D6D"))

            goToFragment(FragmentBlog.newInstance())
        }
        else if (position == 3){

            imgExplore.layoutParams = grayLayoutParam
            imgBlog.layoutParams = grayLayoutParam
            imgTrips.layoutParams = blueLayoutParam
            imgAccount.layoutParams = grayLayoutParam

            imgExplore.setImageResource(R.drawable.ic_explore_gray)
            imgBlog.setImageResource(R.drawable.ic_blog_gray)
            imgTrips.setImageResource(R.drawable.ic_trip_blue)
            imgAccount.setImageResource(R.drawable.ic_profile_gray)

            txtExplore.setTextColor(Color.parseColor("#6D6D6D"))
            txtBlog.setTextColor(Color.parseColor("#6D6D6D"))
            txtTrips.setTextColor(Color.parseColor("#233E98"))
            txtAccount.setTextColor(Color.parseColor("#6D6D6D"))

            goToFragment(FragmentTrips.newInstance(from))
        }
        else{

            imgExplore.layoutParams = grayLayoutParam
            imgBlog.layoutParams = grayLayoutParam
            imgTrips.layoutParams = grayLayoutParam
            imgAccount.layoutParams = blueLayoutParam

            imgExplore.setImageResource(R.drawable.ic_explore_gray)
            imgBlog.setImageResource(R.drawable.ic_blog_gray)
            imgTrips.setImageResource(R.drawable.ic_trip_gray)
            imgAccount.setImageResource(R.drawable.ic_profile_blue2)

            txtExplore.setTextColor(Color.parseColor("#6D6D6D"))
            txtBlog.setTextColor(Color.parseColor("#6D6D6D"))
            txtTrips.setTextColor(Color.parseColor("#6D6D6D"))
            txtAccount.setTextColor(Color.parseColor("#233E98"))

            goToFragment(FragmentAccount.newInstance())
        }
    }

    private fun goToFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
            .replace(R.id.container,fragment)
        fragmentTransaction.commit()
    }

    private fun initiateUpdateDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_update,null)
        updateDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        updateDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        imgShareCLose = view.findViewById(R.id.imgShareCLose)
        txtLater = view.findViewById(R.id.txtLater)
        txtUpdate = view.findViewById(R.id.txtUpdate)

        imgShareCLose.setOnClickListener(this)
        txtLater.setOnClickListener(this)
        txtUpdate.setOnClickListener(this)
    }

    fun openAppInPlayStore() {
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

    override fun onVersionPrepare() {

    }

    override fun onVersionSuccess(versionCode: Int) {
        val currentVersionCode = BuildConfig.VERSION_CODE

        if (currentVersionCode < versionCode){
            updateDialog.show()
        }
    }

    override fun onVersionError() {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.frameExplore -> {
                setBottomNavigation(1)
            }

            R.id.frameBlog -> {
                setBottomNavigation(2)
            }

            R.id.frameTrips -> {
                setBottomNavigation(3)
            }

            R.id.frameAccount -> {
                setBottomNavigation(4)
            }

            R.id.txtDialogNext -> {
                setDialogNextButton()
            }

            R.id.imgShareCLose -> {
                updateDialog.dismiss()
            }

            R.id.txtLater -> {
                updateDialog.dismiss()
            }

            R.id.txtUpdate -> {
                openAppInPlayStore()
            }
        }
    }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

