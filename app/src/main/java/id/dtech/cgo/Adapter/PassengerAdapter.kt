package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.R

class PassengerAdapter(context : Context, passengerList : ArrayList<HashMap<String,Any>>,
                       passengerListener : ApplicationListener.Companion.GuestListener)
    : RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>() {

    private val contexts = context
    private val passengers = passengerList
    private val listener = passengerListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_passenger,parent,false)
        return PassengerViewHolder(view)
    }

    override fun getItemCount(): Int {
       return passengers.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        val passengerMap = passengers[position]
        val pos = position + 1

        val is_filled = passengerMap["is_filled"] as Boolean
        val fullname = passengerMap["fullname"] as String
        val name = passengerMap["name"] as String

        if (is_filled){
            val nameTitle = fullname.split(" ")[0]
            holder.txtPassenger.setTextColor(Color.parseColor("#202020"))
            holder.txtPassenger.text = fullname.removePrefix(nameTitle)
        }
        else{
            holder.txtPassenger.setTextColor(Color.parseColor("#143ABE"))
            holder.txtPassenger.text = "Passenger $pos: $name"
        }

        holder.linearContainer.setOnClickListener {
            listener.onGuestClicked(passengerMap,position)
        }
    }

    class PassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtPassenger = itemView.findViewById<MyTextView>(R.id.txtPassenger)
        val linearContainer = itemView.findViewById<LinearLayout>(R.id.linearContainer)
    }
}