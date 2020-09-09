package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener

import id.dtech.cgo.Model.TransportationModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.View.ActivityTransportationResultDetail
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransportationResultAdapter (context : Context, transportationList : ArrayList<TransportationModel>,
                                   transportationResultListener : ApplicationListener.Companion.TransportationResultListener )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val contexs = context
    private var transportLists = transportationList
    private val listener  = transportationResultListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0){
            val view = LayoutInflater.from(contexs).inflate(
                R.layout.item_transportation_result,parent,
                false)
            return TransportationResultViewHolder(view)
        }
        else if (viewType == 1){
            val view = LayoutInflater.from(contexs).inflate(R.layout.item_loading,parent,
                false)
            return LoadingViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(contexs).inflate(R.layout.item_error,parent,
                false)
            return ErrorViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return transportLists.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (holder is TransportationResultViewHolder){
           val transportationModel = transportLists[position]
           val merchantPic = transportationModel.merchant_picture ?: ""

           val sdf = SimpleDateFormat("HH:mm:ss")
           val sdfs = SimpleDateFormat("HH:mm")

           val startDateTime = sdf.parse(transportationModel.departure_time ?: "")
           val endDateTime = sdf.parse(transportationModel.arrival_time ?: "")

           transportationModel.price?.let {
               val adultPrice = CurrencyUtil.decimal(it["adult_price"] as Long).replace(",",".")
               val childrenPrice = CurrencyUtil.decimal(it["children_price"] as Long).replace(",",".")
               val priceType = " / "+it["price_type"] as String
               val currencyLabel = it["currency_label"] as String

               val strAdultPrice = "$currencyLabel $adultPrice"
               val strChildrenPrice = "$currencyLabel $childrenPrice"

               holder.txtAdultPrice.text = strAdultPrice
               holder.txtAdultPax.text = priceType

               holder.txtChildrenPrice.text = strChildrenPrice
               holder.txtChildrenPax.text = priceType
           }

           if (merchantPic.isNotEmpty()){
               Picasso.get().load(merchantPic).into(holder.imgMerchant)
           }

           holder.txtMerchantName.text = transportationModel.merchant_name ?: ""
           holder.txtClass.text = transportationModel.transportClass ?: ""

           holder.txtDeparture.text = transportationModel.harbor_source_name ?: ""
           holder.txtArrival.text = transportationModel.harbor_destination_name ?: ""

           holder.txtDuration.text = transportationModel.trip_duration ?: ""

           holder.txtDepartureTime.text = sdfs.format(startDateTime ?: Date())
           holder.txtArrivalTime.text = sdfs.format(endDateTime ?: Date())

           holder.cardParent.setOnClickListener {
               Log.d("my_image",transportationModel.transportation_id ?: "")
               listener.onTransportationClassClicked(transportationModel,0)
           }

           holder.linearDetail.setOnClickListener {
               listener.onTransportationClassClicked(transportationModel,1)
           }
       }
        else if (holder is LoadingViewHolder){

       }
        else{
           (holder as ErrorViewHolder).txtTryAgain.setOnClickListener {

           }
       }
    }

    override fun getItemViewType(position: Int): Int {
        val transportModel = transportLists[position]
        return transportModel.viewType
    }

    fun updateTransportList(transportationList : ArrayList<TransportationModel>){
        this.transportLists = transportationList
        notifyDataSetChanged()
    }

    class TransportationResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val imgMerchant = itemView.findViewById<ImageView>(R.id.imgMerchant)
        val txtMerchantName = itemView.findViewById<TextView>(R.id.txtMerchantName)
        val txtClass = itemView.findViewById<TextView>(R.id.txtClass)
        val txtDeparture = itemView.findViewById<TextView>(R.id.txtDeparture)
        val txtArrival = itemView.findViewById<TextView>(R.id.txtArrival)
        val txtDuration = itemView.findViewById<TextView>(R.id.txtDuration)

        val txtDepartureTime = itemView.findViewById<TextView>(R.id.txtDepartureTime)
        val txtArrivalTime = itemView.findViewById<TextView>(R.id.txtArrivalTime)

        val txtAdultPrice = itemView.findViewById<TextView>(R.id.txtAdultPrice)
        val txtAdultPax = itemView.findViewById<TextView>(R.id.txtAdultPax)
        val txtChildrenPrice = itemView.findViewById<TextView>(R.id.txtChildrenPrice)
        val txtChildrenPax = itemView.findViewById<TextView>(R.id.txtChildrenPax)

        val linearDetail = itemView.findViewById<LinearLayout>(R.id.linearDetail)
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtTryAgain = itemView.findViewById<MyTextView>(R.id.txtTryAgain)
    }
}