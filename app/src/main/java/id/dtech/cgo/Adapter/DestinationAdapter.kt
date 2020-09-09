package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExpDestinationModel
import id.dtech.cgo.R
import id.dtech.cgo.View.dp

class DestinationAdapter(context : Context, destinationList : ArrayList<ExpDestinationModel>, listener
: ApplicationListener.Companion.ExperienceDestinationListener, from : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val contexs = context
    private val destinations = destinationList
    private val listeners = listener
    private val froms = from

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val view = LayoutInflater.from(contexs).inflate(R.layout.item_destination,parent,
                false)
            return DestinationViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(contexs).inflate(R.layout.item_destination_city,parent,
                false)
            return DestinationCityViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val model = destinations[position]
        return model.type
    }

    override fun getItemCount(): Int {
        return destinations.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val destinationModel = destinations[position]

        if (holder is DestinationViewHolder){
            holder.txtName.text = destinationModel.province

            val layoutParam = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            if (position != destinations.size - 1){
                layoutParam.setMargins(24.dp, 24.dp, 24, 0.dp)
            }
            else{
                layoutParam.setMargins(24.dp, 24.dp, 24, 24.dp)
            }

            holder.linearParent.layoutParams = layoutParam

            if (froms == 0){
                holder.linearParent.setOnClickListener {
                    listeners.onExperienceDestinationClicked(destinationModel)
                }
            }
        }
        else{
            val myHolder = holder as DestinationCityViewHolder
            myHolder.txtName.text = destinationModel.harbors_name

            val layoutParam = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            if (position != destinations.size - 1){
                layoutParam.setMargins(42.dp, 12.dp, 24, 0.dp)
            }
            else{
                layoutParam.setMargins(42.dp, 12.dp, 24, 24.dp)
            }

            myHolder.linearParent.layoutParams = layoutParam

            myHolder.linearParent.setOnClickListener {
                listeners.onExperienceDestinationClicked(destinationModel)
            }
        }
    }

    class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
    }

    class DestinationCityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
    }
}
