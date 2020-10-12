package id.dtech.cgo.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ActivityTypeModel
import id.dtech.cgo.R
import id.dtech.cgo.View.dp

class ActivityTypeHomeAdapter(context : Context, activityTypeList : ArrayList<ActivityTypeModel>,
                              activityTypeListener : ApplicationListener.Companion.ActivityTypeListener
) :
    RecyclerView.Adapter<ActivityTypeHomeAdapter.ActivityTypeHomeViewHolder>() {

    private val contexts = context
    private val activities = activityTypeList
    private val listener = activityTypeListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTypeHomeViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_activity_type_home,
            parent, false)
        return ActivityTypeHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityTypeHomeViewHolder, position: Int) {
        val activityTypeModel = activities[position]
        holder.txtName.text = activityTypeModel.name ?: ""

        val lastIndex = activities.size - 1
        val layoutParam = LinearLayout.LayoutParams(100.dp,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        if (activities.size > 1){
            if (position == 0){
                layoutParam.setMargins(24.dp, 0.dp, 0.dp, 0.dp)
            }
            if (position != lastIndex){
                layoutParam.setMargins(0.dp, 0.dp, 0.dp, 0.dp)
            }
            else{
                layoutParam.setMargins(0.dp, 0.dp, 24.dp, 0.dp)
            }
        }
        else{
            layoutParam.setMargins(24.dp, 0.dp, 24.dp, 0.dp)
        }

        Picasso.get().load(activityTypeModel.icon_mobile).into(holder.imgIcon)

        holder.linearParent.layoutParams = layoutParam
        holder.linearParent.setOnClickListener {
            listener.onActivityTypeClicked(activityTypeModel,false)
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    class ActivityTypeHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val imgIcon = itemView.findViewById<ImageView>(R.id.imgIcon)
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }
}