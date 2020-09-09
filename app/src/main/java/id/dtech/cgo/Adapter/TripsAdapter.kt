package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import id.dtech.cgo.View.*
import kotlinx.android.synthetic.main.layout_checkout.*
import kotlinx.android.synthetic.main.layout_payment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TripsAdapter(contex : Context, data : ArrayList<HashMap<String,Any>>, from : Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val contexts = contex
    private val dataList = data
    private val froms = from

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val view = LayoutInflater.from(contexts).inflate(R.layout.item_experience_trips,
                parent,false)
            return TripsExpViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(contexts).inflate(R.layout.item_transportation_trips,
                parent,false)
            return TripsTransViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val dataMap = dataList[position]
            val order_id = dataMap["order_id"] as String

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            val daySdf = SimpleDateFormat("dd")
            val dateSdf = SimpleDateFormat("dd MMM yy")
            val sdfDayMonth = SimpleDateFormat("dd MMMM yyyy")

            if (holder is TripsExpViewHolder){
                var expDate : Date

                if (froms == 4){
                   val is_review = dataMap["is_review"] as Boolean

                    if (!is_review){
                        holder.txtReview.visibility = View.VISIBLE
                    }
                    else{
                        holder.txtReview.visibility = View.GONE
                    }
                }

                if (dataMap["booking_date"] != null){
                    val strDate = dataMap["booking_date"] as String
                    expDate = sdf.parse(strDate) ?: Date()
                }
                else{
                    val strDate = dataMap["exp_booking_date"] as String
                    expDate = sdf.parse(strDate) ?: Date()
                }

                val duration = dataMap["exp_duration"] as Int

                if (duration > 1){
                    val calendar = Calendar.getInstance()
                    calendar.time = expDate
                    calendar.add(Calendar.DAY_OF_YEAR,duration)

                    val strDay = daySdf.format(expDate)
                    val strDate = dateSdf.format(calendar.time)
                    val strDayDate = "$strDay - $strDate"

                    holder.txtOrderID.text = "ID : $order_id  •  $strDayDate"
                }
                else{
                    val strDay = sdfDayMonth.format(expDate)
                    holder.txtOrderID.text = "ID : $order_id  •  $strDay"
                }

                holder.txtTitle.text = dataMap["exp_title"] as String
                holder.txtLocation.text = dataMap["province"] as String+", "+dataMap["country"]
                        as String

                holder.cardParent.setOnClickListener {
                    if (froms == 1){
                        val i = Intent(contexts,ActivityPaymentDetail::class.java)
                        i.putExtra("order_id",order_id)
                        i.putExtra("from",1)
                        contexts.startActivity(i)
                    }
                    else if (froms == 2){
                        val i = Intent(contexts,ActivityWaitingConfirmation::class.java)
                        i.putExtra("order_id",order_id)
                        contexts.startActivity(i)
                    }
                    else if (froms == 3){
                        val i = Intent(contexts,ActivityCheckout::class.java)
                        i.putExtra("order_id",order_id)
                        contexts.startActivity(i)
                    }
                    else{
                        val i = Intent(contexts,ActivityHistoryDetail::class.java)
                        i.putExtra("order_id",order_id)
                        contexts.startActivity(i)
                    }
                }

                holder.txtReview.setOnClickListener {
                    val i = Intent(contexts,ActivityWriteReview::class.java)
                    i.putExtra("order_id",order_id)
                    contexts.startActivity(i)
                }
            }
            else {
                val myHolder = holder as TripsTransViewHolder
                myHolder.txtOrderID.text = "ID : $order_id"
                myHolder.txtFrom.text = dataMap["trans_from"] as String
                myHolder.txtTo.text = dataMap["trans_to"] as String

                val transGuestMap = dataMap["trans_guest"] as HashMap<String,Any>
                val adultCount = transGuestMap["adult"] as Int
                val childrenCount = transGuestMap["children"] as Int
                val infantCount = 0

                val strDate = dataMap["booking_date"] as String
                val expDate = sdf.parse(strDate) ?: Date()
                val strDay = sdfDayMonth.format(expDate)

                if (adultCount > 0 && childrenCount > 0 && infantCount > 0){
                    if (adultCount > 1){
                        myHolder.txtGuest.text = "$adultCount Adult(s), $childrenCount Children, $infantCount Infant"
                    }
                    else{
                        myHolder.txtGuest.text = "$adultCount Adult, $childrenCount Children, $infantCount Infant"
                    }
                }
                else if (adultCount > 0){
                    if (adultCount > 1){
                        myHolder.txtGuest.text = "$adultCount Adult(s)"
                    }
                    else{
                        myHolder.txtGuest.text = "$adultCount Adult"
                    }
                }
                else if (childrenCount > 0){
                    myHolder.txtGuest.text = "$childrenCount Children"
                }
                else{
                    myHolder.txtGuest.text = "$infantCount Infant"
                }

                myHolder.txtDate.text = strDay
                myHolder.cardParent.setOnClickListener {
                    if (froms == 1){
                        val i = Intent(contexts,ActivityTransportationPaymentDetail::class.java)
                        i.putExtra("order_id",order_id)
                        contexts.startActivity(i)
                    }
                    else if (froms == 2){

                    }
                    else if (froms == 3){
                        val i = Intent(contexts,ActivityCheckout::class.java)
                        i.putExtra("order_id",order_id)
                        contexts.startActivity(i)
                    }
                    else{
                        val i = Intent(contexts,ActivityHistoryDetail::class.java)
                        i.putExtra("order_id",order_id)
                        contexts.startActivity(i)
                    }
                }
            }
    }

    override fun getItemViewType(position: Int): Int {
        val type = dataList[position]["booking_type"]

        if (type != null){
            type as Int

            if (type == 0){
                return 1
            }
            else{
                return 2
            }
        }
        else{
            return 0
        }
    }

    class TripsExpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtOrderID = itemView.findViewById<MyTextView>(R.id.txtOrderID)
        val txtTitle = itemView.findViewById<MyTextView>(R.id.txtTitle)
        val txtLocation = itemView.findViewById<MyTextView>(R.id.txtLocation)
        val txtGuest = itemView.findViewById<MyTextView>(R.id.txtGuest)
        val txtReview = itemView.findViewById<MyTextView>(R.id.txtReview)
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
    }

    class TripsTransViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtOrderID = itemView.findViewById<MyTextView>(R.id.txtOrderID)
        val txtDate = itemView.findViewById<MyTextView>(R.id.txtDate)
        val txtFrom = itemView.findViewById<MyTextView>(R.id.txtFrom)
        val txtTo = itemView.findViewById<MyTextView>(R.id.txtTo)
        val txtGuest = itemView.findViewById<MyTextView>(R.id.txtGuest)
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
    }
}