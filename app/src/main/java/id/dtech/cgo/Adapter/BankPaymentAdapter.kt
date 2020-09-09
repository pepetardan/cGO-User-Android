package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.PaymentMethodModel
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.item_bank_payment.view.*

class BankPaymentAdapter(context : Context, bankList : ArrayList<PaymentMethodModel>, bankListener :
ApplicationListener.Companion.BankMethodListener) : RecyclerView.Adapter<BankPaymentAdapter.BankViewHolder>() {

    private val contexts = context
    private val lists = bankList
    private val listener = bankListener

    private var isSelected = false
    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
       val view = LayoutInflater.from(contexts).inflate(R.layout.item_bank_payment,parent,
           false)
        return BankViewHolder(view)
    }

    override fun getItemCount(): Int {
       return lists.size
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
       val paymentModel = lists[position]
       Picasso.get().load(paymentModel.icon ?: "").into(holder.imgBank)
       holder.txtBank.text = paymentModel.name ?: ""

       if (isSelected){
           if (currentPosition == position){
               holder.checkList.setImageResource(R.drawable.ic_checktype_blue)
           }
           else{
               holder.checkList.setImageResource(R.drawable.ic_circle_graybgk)
           }
       }

        holder.linearParent.setOnClickListener {
            isSelected = true
            currentPosition = position
            listener.onBankMethodClicked(paymentModel,position,isSelected)
            notifyDataSetChanged()
        }

    }

    class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgBank = itemView.findViewById<ImageView>(R.id.imgBank)
        val txtBank = itemView.findViewById<TextView>(R.id.txtBank)
        val checkList = itemView.findViewById<ImageView>(R.id.checkList)
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
    }

}