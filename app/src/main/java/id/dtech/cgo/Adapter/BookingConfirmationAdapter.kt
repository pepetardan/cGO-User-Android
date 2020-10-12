package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.R
import java.util.ArrayList

class BookingConfirmationAdapter (context : Context, bookconfirmationList : ArrayList<HashMap<String,Any>>,
                                  bookingConfirmationListener: ApplicationListener.Companion.BookingConfirmationListener) :
    RecyclerView.Adapter<BookingConfirmationAdapter.BookingConfirmationViewHolder>() {

    private val contexts = context
    private val bookconfirmationLists = bookconfirmationList
    private val listener = bookingConfirmationListener

    private var currentPosition = 0
    private var isClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingConfirmationViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_filter_type,parent,false)
        return BookingConfirmationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookconfirmationLists.size
    }

    override fun onBindViewHolder(holder: BookingConfirmationViewHolder, position: Int) {
        val paymentMap = bookconfirmationLists[position]
        val name = paymentMap["name"] as String
        holder.txtName.text = name

        if (isClicked){
            if (currentPosition == position){
                holder.imgCheck.setImageResource(R.drawable.ic_checktype_blue)
            }
            else{
                holder.imgCheck.setImageResource(R.drawable.ic_checklist_inactive)
            }
        }

        holder.linearParent.setOnClickListener {
            currentPosition = position
            isClicked = true
            listener.onBookingConfirmationClicked(paymentMap)
            notifyDataSetChanged()
        }
    }

    class BookingConfirmationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
        val imgCheck = itemView.findViewById<ImageView>(R.id.imgCheck)
    }
}