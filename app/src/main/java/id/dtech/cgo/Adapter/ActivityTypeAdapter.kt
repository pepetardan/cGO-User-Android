package id.dtech.cgo.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ActivityTypeModel
import id.dtech.cgo.R

class ActivityTypeAdapter(context : Context, selectedBool : ArrayList<Boolean> ,list : ArrayList<ActivityTypeModel>,
    listener : ApplicationListener.Companion.ActivityTypeListener ) : RecyclerView.Adapter<ActivityTypeAdapter.ActivityTypeViewHolder>() {

    private val contexts = context
    private val lists = list
    private val selectedList = selectedBool
    private val listeners = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTypeViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_type,parent,false)
        return ActivityTypeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ActivityTypeViewHolder, position: Int) {
          val activityTypeModel = lists[position]
          val isSelected = selectedList[position]

          holder.txtName.text = activityTypeModel.name ?: ""

        if (position != lists.size - 1){
            holder.viewDivider.visibility = View.VISIBLE
        }
        else{
            holder.viewDivider.visibility = View.GONE
        }

        if (isSelected){
            holder.imgCheck.setImageResource(R.drawable.ic_checktype_blue)
        }
        else{
            holder.imgCheck.setImageResource(R.drawable.ic_typecheck_gray)
        }

        holder.linearParent.setOnClickListener {
            selectedList[position] = !isSelected
            listeners.onActivityTypeClicked(activityTypeModel,!isSelected)
            notifyDataSetChanged()
        }
    }

    class ActivityTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val imgCheck = itemView.findViewById<ImageView>(R.id.imgCheck)
        val viewDivider = itemView.findViewById<View>(R.id.viewDivider)
    }
}