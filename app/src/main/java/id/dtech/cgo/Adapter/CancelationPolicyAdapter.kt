package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R

class CancelationPolicyAdapter (context : Context) :
    RecyclerView.Adapter<CancelationPolicyAdapter.CancelationViewHolder>(){

    private val contexts = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancelationViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_cancelation,parent,false)
        return CancelationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CancelationViewHolder, position: Int) {
        if (position == 0){
            holder.txtCancelation.text = "No cancellation"
        }
        else if (position == 1){
            holder.txtCancelation.text = "If there is any force majeur issue so the trip should be cancelled, we will fully refund your payment "
        }
        else{
            holder.txtCancelation.text = "cGO not responsible for the bank charges or fee resulting from the transfer of the payment"
        }
    }

    class CancelationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtCancelation = itemView.findViewById<MyTextView>(R.id.txtCancelation)
    }
}