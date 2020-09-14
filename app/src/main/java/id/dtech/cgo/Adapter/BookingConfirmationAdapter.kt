package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import java.util.ArrayList

class BookingConfirmationAdapter (context : Context, bookconfirmationList : ArrayList<String>) :
    RecyclerView.Adapter<BookingConfirmationAdapter.BookingConfirmationViewHolder>() {

    private val contexts = context
    private val bookconfirmationLists = bookconfirmationList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingConfirmationViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_filter_type,parent,false)
        return BookingConfirmationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookconfirmationLists.size
    }

    override fun onBindViewHolder(holder: BookingConfirmationViewHolder, position: Int) {
        val name = bookconfirmationLists[position]
        holder.txtName.text = name
    }

    class BookingConfirmationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }
}