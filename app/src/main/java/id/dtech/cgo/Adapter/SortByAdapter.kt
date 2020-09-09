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

class SortByAdapter (context : Context, sortList : ArrayList<HashMap<String,Any>>, sortByListener :
ApplicationListener.Companion.SortByListener) : RecyclerView.Adapter<SortByAdapter.SortViewHolder>() {

    private val contexts = context
    private val sortLists = sortList
    private val listener = sortByListener

    private var clickedPosition = 0
    private var isViewClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
        val view = LayoutInflater.from(contexts).inflate(
            R.layout.item_class,parent,
            false)
        return SortViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sortLists.size
    }

    override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
        val sortMap = sortLists[position]
        val className = sortMap["name"] as String
        holder.txtName.text = className

        val layoutParam = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        if (position != sortLists.size - 1){
            layoutParam.setMargins(0.dp, 24.dp, 0.dp, 0.dp)
            holder.dividerView.visibility = View.VISIBLE
        }
        else{
            layoutParam.setMargins(0.dp, 24.dp, 0.dp, 24.dp)
            holder.dividerView.visibility = View.GONE
        }

        holder.linearParent.layoutParams = layoutParam

        if (isViewClicked){
            if (clickedPosition == position){
                holder.imgCheck.visibility = View.VISIBLE
            }
            else{
                holder.imgCheck.visibility = View.GONE
            }
        }

        holder.linearParent.setOnClickListener {
            listener.onSortByClicked(sortMap)
            clickedPosition = position
            isViewClicked = true
            notifyDataSetChanged()
        }
    }

    class SortViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val imgCheck = itemView.findViewById<ImageView>(R.id.imgCheck)
        val dividerView = itemView.findViewById<View>(R.id.dividerView)
    }
}