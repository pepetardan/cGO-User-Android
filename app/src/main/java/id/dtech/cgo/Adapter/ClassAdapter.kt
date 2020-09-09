package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.R
import id.dtech.cgo.View.dp
import java.util.ArrayList

class ClassAdapter(context : Context, classList : ArrayList<String>, transportationClassListener :
    ApplicationListener.Companion.TransportationClassListener) : RecyclerView.Adapter<
        ClassAdapter.ClassViewHolder>() {

    private val contexts = context
    private val classLists = classList
    private val listener = transportationClassListener

    private var clickedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_class,parent,
            false)
        return ClassViewHolder(view)
    }

    override fun getItemCount(): Int {
        return classLists.size
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val className = classLists[position]
        holder.txtName.text = className

        val layoutParam = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        if (position != classLists.size - 1){
            layoutParam.setMargins(24.dp, 24.dp, 24, 0.dp)
            holder.dividerView.visibility = View.VISIBLE
        }
        else{
            layoutParam.setMargins(24.dp, 24.dp, 24, 24.dp)
            holder.dividerView.visibility = View.GONE
        }

        holder.linearParent.layoutParams = layoutParam

        if (clickedPosition == position){
            holder.imgCheck.visibility = View.VISIBLE
        }
        else{
            holder.imgCheck.visibility = View.GONE
        }

        holder.linearParent.setOnClickListener {
            listener.onTransportationClassClicked(className)
            clickedPosition = position
            notifyDataSetChanged()
        }
    }

    class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val imgCheck = itemView.findViewById<ImageView>(R.id.imgCheck)
        val dividerView = itemView.findViewById<View>(R.id.dividerView)
    }

}