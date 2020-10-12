package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.AddOnModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil

class AdapterAddonList (context : Context, addOnList : ArrayList<AddOnModel>, currency : String) :
    RecyclerView.Adapter<AdapterAddonList.AddonListViewHolder>() {

    private val contexts = context
    private val addonLists = addOnList
    private val currencys = currency

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddonListViewHolder {
        val view = LayoutInflater.from(contexts).inflate(
            R.layout.item_addon_list, parent, false)
        return AddonListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddonListViewHolder, position: Int) {
        val addOnModel = addonLists[position]
        val name = addOnModel.name ?: ""
        val addOnTicketPrice = CurrencyUtil.decimal(addOnModel.amount).replace(",",
            ".")

        holder.txtName.text = "Add On ($name)"
        holder.txtPrice.text = "$currencys $addOnTicketPrice"
    }

    override fun getItemCount(): Int {
        return addonLists.size
    }

    class AddonListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
        val txtPrice = itemView.findViewById<MyTextView>(R.id.txtPrice)
    }
}