package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.R

class RedeemAdapter (contex : Context) : RecyclerView.Adapter<RedeemAdapter.RedeemViewHolder>() {

    private val contexts = contex

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedeemViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_redeem,parent,false)
        return RedeemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 4
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RedeemViewHolder,
        position: Int
    ) {
        if (position == 0){
            holder.txtName.text = "Make sure your e-ticket is ready"
        }
        else if (position == 1){
            holder.txtName.text = "Show your printed e-ticket or e-ticket on \n" +
                    "syour mobile at the counter. If you are \n" +
                    "using your e-ticket  on your mobile, please \n" +
                    "adjust the brightness of your mobile \n" +
                    "screen before scanning the QR code on \n" +
                    "the e-ticket."
        }
        else if (position == 2){
            holder.txtName.text = "Only your e-ticket purchased on cGO is \n" +
                    "valid. Receipts or proof of payment cannot\n" +
                    "be used to enter."
        }
        else {
            holder.txtName.text = "Exchange your e-ticket no later than 30\n" +
                    "minutes before activities is starting"
        }
    }

    class RedeemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
    }
}