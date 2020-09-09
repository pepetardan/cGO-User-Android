package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R

class GuestDetailAdapter (context : Context, guestList : ArrayList<HashMap<String,Any>>) :
    RecyclerView.Adapter<GuestDetailAdapter.GuestDetailViewHolder>() {

    private val contexs = context
    private val guests = guestList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestDetailViewHolder {
        val view = LayoutInflater.from(contexs).inflate(R.layout.item_guest_detail,parent,
            false)
        return GuestDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return guests.size
    }

    override fun onBindViewHolder(holder: GuestDetailViewHolder, position: Int) {

        val guestMap = guests[position]
        val guestType = guestMap["type"] as String

        holder.txtName.text = guestMap["fullname"] as String
        holder.txtType.text = guestType

        if (guestType != "Adult"){
            holder.txtIdCard.text = "-"
        }
        else{
            holder.txtIdCard.text = guestMap["idnumber"] as String
        }

        if (position == 0){
            holder.txtGuest.visibility = View.VISIBLE
        }
        else{
            holder.txtGuest.visibility = View.GONE
        }

        if (position == 2){
            holder.viewGuest.visibility = View.GONE
        }
        else{
            holder.viewGuest.visibility = View.VISIBLE
        }
    }

    class GuestDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtGuest = itemView.findViewById<MyTextView>(R.id.txtGuest)
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
        val txtType = itemView.findViewById<MyTextView>(R.id.txtType)
        val txtIdCard = itemView.findViewById<MyTextView>(R.id.txtIdCard)
        val viewGuest = itemView.findViewById<View>(R.id.viewGuest)
    }
}