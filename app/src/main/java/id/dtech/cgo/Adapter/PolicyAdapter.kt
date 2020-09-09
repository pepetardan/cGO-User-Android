package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.R

class PolicyAdapter (contex : Context) : RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder>() {

    private val contexts = contex

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_policy,parent,false)
        return PolicyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PolicyViewHolder, position: Int) {
        if (position == 0){
            holder.txtName.text = "No refund and cancellation"
        }
        else if (position == 1){
            holder.txtName.text = "If there is any force majeur issue so the trip \n" +
                    "should be cancelled, the trip cannot be \n" +
                    "refunded and we will reschedule your trip"
        }
        else{
            holder.txtName.text = "cGO not resfonsible for the bank charges \n" +
                    "or fee resulting from the transfer of\n" +
                    "the payment"
        }
    }

    class PolicyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
    }
}