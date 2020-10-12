package id.dtech.cgo.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExperienceDetailModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.View.PackageDetailActivity
import id.dtech.cgo.View.PackageListActivity
import id.dtech.cgo.View.dp
import java.io.Serializable

class PackageAdapter(context : Context, from : Int, packagesList : ArrayList<HashMap<String,Any>>,
                    selectPosition : Int, packageListener : ApplicationListener.Companion.PackageListener) :
    RecyclerView.Adapter<PackageAdapter.PackageAdapterViewHolder>(){

    private val contexts = context
    private val packagesLists = packagesList
    private val listener = packageListener
    private val froms = from

    private var selectedPosition = selectPosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageAdapterViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_package,parent,
            false)
        return PackageAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
       return packagesLists.size
    }

    override fun onBindViewHolder(holder: PackageAdapterViewHolder, position: Int) {
        val packageMap = packagesLists[position]
        val name = packageMap["packageName"] as String
        val price = CurrencyUtil.decimal(packageMap["packagePrice"] as Long).replace(",",".")
        val paymentType = (packageMap["packageTypePayment"] as String).split(" ")[1]
        val packageCurrency = packageMap["packageCurrency"] as String
        val strPrice = "$packageCurrency $price/$paymentType"
        val packageImagesList = packageMap["packageImagesList"] as ArrayList<String>

        val lastIndex = packagesLists.size - 1
        val layoutParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        if (position != lastIndex){
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 0.dp)
        }
        else{
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 24.dp)
        }

        if (packageImagesList.size > 0){
            val strImage = packageImagesList[0]
            if (strImage.isNotEmpty()){
                holder.cardImage.visibility = View.VISIBLE
                Picasso.get().load(strImage).into(holder.imgPackage)
            }
        }
        else{
            holder.cardImage.visibility = View.GONE
        }

        holder.cardParent.layoutParams = layoutParam
        holder.txtPackageName.text = name
        holder.txtPackagePrice.text = strPrice

        if (position == selectedPosition){
            holder.linearPackage.setBackgroundResource(R.drawable.background_add_on)
        }
        else{
            holder.linearPackage.setBackgroundResource(0)
        }

        holder.cardParent.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            listener.onPackageClicked(packageMap)
        }
    }

    class PackageAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val imgPackage = itemView.findViewById<ImageView>(R.id.imgPackage)
        val txtPackageName = itemView.findViewById<MyTextView>(R.id.txtPackageName)
        val txtPackagePrice = itemView.findViewById<MyTextView>(R.id.txtPackagePrice)
        val cardImage = itemView.findViewById<CardView>(R.id.cardImage)
        val linearPackage = itemView.findViewById<LinearLayout>(R.id.linearPackage)
    }
}