package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import java.util.ArrayList

class PaymentAvaibilityAdapter (context : Context, paymentList : ArrayList<String>) :
    RecyclerView.Adapter<PaymentAvaibilityAdapter.PaymentAvaibilityViewHolder>() {

    private val contexts = context
    private val paymentLists = paymentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAvaibilityViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_filter_type,parent,false)
        return PaymentAvaibilityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paymentLists.size
    }

    override fun onBindViewHolder(holder: PaymentAvaibilityViewHolder, position: Int) {
        val name = paymentLists[position]
        holder.txtName.text = name
    }

    class PaymentAvaibilityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }
}