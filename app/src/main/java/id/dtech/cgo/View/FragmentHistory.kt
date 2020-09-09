package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.dtech.cgo.Adapter.TripsAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Preferance.UserSession

import id.dtech.cgo.R

class FragmentHistory : Fragment(), MyCallback.Companion.MyBookingHistoryCallback,
    SwipeRefreshLayout.OnRefreshListener, MyCallback.Companion.RefreshTokenCallback{

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHistory()
    }

    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    private lateinit var userController : UserController
    private lateinit var userSession: UserSession

    private lateinit var linearNotLogin : LinearLayout
    private lateinit var linearErrorEmpty : LinearLayout
    private lateinit var myRecyclerView : RecyclerView

    private lateinit var imgState : ImageView
    private lateinit var txtState : MyTextView

    private lateinit var fragmentActivity : FragmentActivity

    private var accessToken = ""
    private var expiresIn = 0L
    private var tokenType  = ""
    private var refreshToken = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        setView(view)
        return view
    }

    private fun setView(view : View){

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        linearNotLogin = view.findViewById(R.id.linearNotLogin)
        linearErrorEmpty = view.findViewById(R.id.linearErrorEmpty)
        myRecyclerView = view.findViewById(R.id.myRecyclerView)

        imgState = view.findViewById(R.id.imgState)
        txtState = view.findViewById(R.id.txtState)

        activity?.let {
            fragmentActivity = it

            userController = UserController()
            userSession = UserSession(it.getSharedPreferences("user_session",
                Context.MODE_PRIVATE))

            accessToken = userSession.access_token ?: ""
            refreshToken = userSession.refresh_token ?: ""

            myRecyclerView.layoutManager = LinearLayoutManager(it)
            swipeRefreshLayout.setOnRefreshListener(this)

            if (userSession.access_token != null){
                userController.getMyBookingHistory("past-30-days",
                    userSession.access_token ?: "",this)
            }
            else{
                imgState.setImageResource(R.drawable.ic_empty_ticket)
                linearNotLogin.visibility = View.VISIBLE
                txtState.visibility = View.GONE
            }
        }
    }

    private fun refreshToken(){
        val refreshToken = userSession.refresh_token ?: ""
        val dataMap = HashMap<String,Any>()
        dataMap["refresh_token"] = refreshToken
        userController.createRefreshToken(dataMap,this)
    }

    override fun onMyBookingHistoryPrepare() {
        swipeRefreshLayout.isRefreshing = true
    }

    @SuppressLint("SetTextI18n")
    override fun onMyBookingHistorySuccess(data: ArrayList<HashMap<String, Any>>) {
        swipeRefreshLayout.isRefreshing = false

        if (data.size > 0){
            linearErrorEmpty.visibility = View.GONE
            myRecyclerView.visibility = View.VISIBLE

            myRecyclerView.adapter = TripsAdapter(fragmentActivity,data,4)
        }
        else{
            linearErrorEmpty.visibility = View.VISIBLE
            myRecyclerView.visibility = View.GONE

            imgState.setImageResource(R.drawable.person_empty_state)
            txtState.text = "Opss, what you're looking for \n isn't available right now :("
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMyBookingHistoryError(message: String) {
        swipeRefreshLayout.isRefreshing = false

        linearErrorEmpty.visibility = View.VISIBLE
        myRecyclerView.visibility = View.GONE

        imgState.setImageResource(R.drawable.example_empty1)
        txtState.text = "There was an error, please try again"
    }

    override fun onRefreshTokenPrepare() {

    }

    override fun onRefreshTokenSuccess(data: HashMap<String, Any>) {
        accessToken = data["access_token"]  as String
        expiresIn = data["expires_in"] as Long
        tokenType = data["token_type"] as String
        refreshToken = data["refresh_token"] as String

        userSession.access_token = accessToken
        userSession.expires_in = expiresIn
        userSession.token_type = tokenType
        userSession.refresh_token = refreshToken
    }

    override fun onRefreshTokenError(message: String, code: Int) {

    }

    override fun onRefresh() {
        userController.getMyBookingHistory("past-30-days",userSession.access_token ?: "",this)
    }
}
