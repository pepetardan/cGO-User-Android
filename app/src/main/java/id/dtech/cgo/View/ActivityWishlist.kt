package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.dtech.cgo.Adapter.ServiceExperienceAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExperienceModel
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_wishlist.*

class ActivityWishlist : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.ExperienceSearchCallback, SwipeRefreshLayout.OnRefreshListener,
    ApplicationListener.Companion.ServiceExperienceListener {

    private lateinit var userController: UserController
    private lateinit var userSession: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)
        setView()
    }

    private fun setView(){
        userController = UserController()
        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        userController.getWishList(userSession.access_token ?: "", this)
        rvService.layoutManager = LinearLayoutManager(this)
        swipeRefresh.setOnRefreshListener(this)
        ivBack.setOnClickListener(this)
    }

    override fun onExperienceSearchPrepare() {
        swipeRefresh.isRefreshing = false

        shimerLayout.visibility = View.VISIBLE
        rvService.visibility = View.GONE
        linearEmpty.visibility = View.GONE

        shimerLayout.startShimmerAnimation()
    }

    @SuppressLint("SetTextI18n")
    override fun onExperienceSearchLoaded(experienceList: ArrayList<ExperienceModel>, totalRecords : Int) {
        shimerLayout.visibility = View.GONE
        shimerLayout.stopShimmerAnimation()

        if (experienceList.size > 0){
            linearEmpty.visibility = View.GONE
            rvService.visibility = View.VISIBLE
            rvService.adapter = ServiceExperienceAdapter(this,experienceList,this)

        }
        else{
            rvService.visibility = View.GONE
            linearEmpty.visibility = View.VISIBLE
            txtError1.visibility = View.GONE

            imgEmpty.setImageResource(R.drawable.person_empty_state)
            txtError.text = "Opss, what you're looking for \n isn't available right now :("
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onExperienceSearchError() {
        shimerLayout.stopShimmerAnimation()
        shimerLayout.visibility = View.GONE
        rvService.visibility = View.GONE
        linearEmpty.visibility = View.VISIBLE

        txtError1.visibility = View.VISIBLE

        imgEmpty.setImageResource(R.drawable.example_empty1)
        txtError.text = "There was an error, please try again"
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun onRefresh() {
        userController.getWishList(userSession.access_token ?: "", this)
    }

    override fun onServiceErrorClicked() {

    }
}
