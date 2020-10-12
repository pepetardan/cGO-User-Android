package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import id.dtech.cgo.Adapter.DestinationAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.DestinationController
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExpDestinationModel
import id.dtech.cgo.R

import kotlinx.android.synthetic.main.activity_experience_search.*

class ActivityExperienceSearch : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.ExperienceDestinationCallback, ApplicationListener.Companion.ExperienceDestinationListener {

    companion object{
        val searchResult = 119
    }

    private lateinit var expDestinationModel: ExpDestinationModel

    private var from = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience_search)
        setView()
    }

    private fun setView(){
        val b = intent.extras

        b?.let { bundle ->
            from = bundle.getInt("from")
        }

        rvDestination.layoutManager = LinearLayoutManager(this)
        ivBack.setOnClickListener(this)

        edtSearch.addTextChangedListener( object : TextWatcher {
           override fun afterTextChanged(s: Editable?) {

           }

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if (edtSearch.text.isNotEmpty()){
                   DestinationController.loadExperienceDestination(this@ActivityExperienceSearch,
                       edtSearch.text.toString().trim())
               }
           }
       })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun onExperienceDestinationPrepare() {
        shimerLayout.visibility = View.VISIBLE
        rvDestination.visibility = View.GONE
        linearEmpty.visibility = View.GONE

        shimerLayout.startShimmerAnimation()
    }

    @SuppressLint("SetTextI18n")
    override fun onExperienceDestinationLoaded(expDestination: ArrayList<ExpDestinationModel>) {

        shimerLayout.visibility = View.GONE
        shimerLayout.startShimmerAnimation()

       if (expDestination.size > 0){
           rvDestination.visibility = View.VISIBLE
           linearEmpty.visibility = View.GONE
           rvDestination.adapter = DestinationAdapter(this,expDestination,this,0)
       }
        else{
           rvDestination.visibility = View.GONE
           linearEmpty.visibility = View.VISIBLE
           txtError.text = "No location found"
       }
    }

    @SuppressLint("SetTextI18n")
    override fun onExperienceDestinationError() {
        shimerLayout.stopShimmerAnimation()

        shimerLayout.visibility = View.GONE
        rvDestination.visibility = View.GONE
        linearEmpty.visibility = View.VISIBLE

        txtError.text = "There was an error , please try again"
    }

    override fun onExperienceDestinationClicked(model: ExpDestinationModel) {
        expDestinationModel = model
        if (from == 0){
            val i = Intent(this,ActivityExperience::class.java)
            i.putExtra("destination_model",expDestinationModel)
            setResult(searchResult, i)
            finish()
        }
        else{
            val i = Intent(this,ActivityExperience::class.java)
            val type = expDestinationModel.type

            if (type == 1){
                val province_id = expDestinationModel.province_id
                val name = expDestinationModel.province ?: ""
                i.putExtra("location_id",province_id)
                i.putExtra("from_location",3)
                i.putExtra("name",name)
            }
            else{
                val harbor_id = expDestinationModel.id ?: ""
                val name = expDestinationModel.province ?: ""
                i.putExtra("location_id",harbor_id)
                i.putExtra("from_location",1)
                i.putExtra("name",name)
            }

            i.putExtra("from",3)
            i.putExtra("from_intent", 1)

            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
    }
}
