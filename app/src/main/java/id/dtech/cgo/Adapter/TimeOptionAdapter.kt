package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.TimeOptionModel
import id.dtech.cgo.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeOptionAdapter(context : Context, timeList : ArrayList<TimeOptionModel>, from : Int,
                        timeOptionListener : ApplicationListener.Companion.TimeOptionListener) :
    RecyclerView.Adapter<TimeOptionAdapter.TimeOptionViewHolder>() {

    private val contexts = context
    private val timeOpList = timeList

    private val froms = from
    private val listener = timeOptionListener

    private var currentPositiom = 0
    private var isViewClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeOptionViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_time_option,parent,false)
        return TimeOptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return timeOpList.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: TimeOptionViewHolder, position: Int) {
        val timeOpModel = timeOpList[position]

        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("HH:mm")
        val startDateTime = sdf.parse(timeOpModel.start_time ?: "")
        val endDateTime = sdf.parse(timeOpModel.end_time ?: "")

        val startTime = sdfs.format(startDateTime ?: Date())
        val endTime = sdfs.format(endDateTime ?: Date())
        val strTime = "$startTime - $endTime"

        holder.txtTime.text = strTime

        if (isViewClicked){
            if (currentPositiom == position){
                holder.linearParent.setBackgroundResource(R.drawable.backgrounf_timeops)
            }
            else{
                holder.linearParent.setBackgroundResource(R.drawable.background_more)
            }
        }

        holder.linearParent.setOnClickListener {
            currentPositiom = position
            isViewClicked = true
            listener.onTimeOptionClicked(timeOpModel,froms)
            notifyDataSetChanged()
        }
    }

    class TimeOptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtTime = itemView.findViewById<TextView>(R.id.txtTime)
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
    }
}