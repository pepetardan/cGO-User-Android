package id.dtech.cgo.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import id.dtech.cgo.View.PackageListActivity
import id.dtech.cgo.View.dp

class PackageAdapter(context : Context, packagesList : ArrayList<String>) : RecyclerView.Adapter<PackageAdapter.PackageAdapterViewHolder>(){

    private val contexts = context
    private val packagesLists = packagesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageAdapterViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_package,parent,
            false)
        return PackageAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
       return packagesLists.size
    }

    override fun onBindViewHolder(holder: PackageAdapterViewHolder, position: Int) {
        val name = packagesLists[position]

        val lastIndex = packagesLists.size - 1
        val layoutParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        if (position != lastIndex){
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 0.dp)
        }
        else{
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 24.dp)
        }

        holder.cardParent.layoutParams = layoutParam
        holder.txtPackageName.text = name

        holder.cardParent.setOnClickListener {
            val i = Intent(contexts,PackageListActivity::class.java)
            contexts.startActivity(i)
        }
    }

    class PackageAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val txtPackageName = itemView.findViewById<MyTextView>(R.id.txtPackageName)
        val txtPackagePrice = itemView.findViewById<MyTextView>(R.id.txtPackagePrice)
    }
}