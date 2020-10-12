package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.AddOnModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.View.dp

class AddOnAdapter(context : Context, addOnList : ArrayList<AddOnModel>, paymentType : String,
                   listener : ApplicationListener.Companion.AddOnListener, checkoutListener :
                   ApplicationListener.Companion.AddOnCheckoutListener)
    : RecyclerView.Adapter<AddOnAdapter.AddOnViewHolder>() {

    private val contexts = context
    private var itemList = addOnList
    private val paymentTYpes = paymentType
    private val addOnListener = listener
    private val checkoutListeners = checkoutListener

   private var currentPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_add_on,parent,
            false)
        return AddOnViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddOnViewHolder, position: Int) {
         val addOnModel = itemList[position]

         val lastIndex = itemList.size - 1
         val selected = addOnModel.selected
         val price = CurrencyUtil.decimal(addOnModel.amount).replace(",",".")
         val total_price = CurrencyUtil.decimal(addOnModel.total_price).replace(",",".")
         val currency = addOnModel.currency ?: ""

         holder.txtServiceName.text = addOnModel.name
         holder.txtDesc.text = addOnModel.desc
         holder.txtPrice.text = "$currency $total_price"

        if (position != 0){
            holder.txtTitle.text = addOnModel.name
            holder.txtAmount.text = "$currency $price"
            holder.linearAmount.visibility = View.VISIBLE
            holder.linearTotalPrice.visibility = View.GONE
            holder.priceDivider.visibility = View.GONE
        }
        else{
            holder.txtTitle.text = addOnModel.name
            holder.linearAmount.visibility = View.GONE
            holder.linearTotalPrice.visibility = View.VISIBLE
            holder.priceDivider.visibility = View.VISIBLE
        }

         if (selected){
            holder.imgCheck.setImageResource(R.drawable.ic_checkmark_circle)
            holder.linearbackground.setBackgroundResource(R.drawable.background_add_on)
         }
         else{
            holder.imgCheck.setImageResource(R.drawable.ic_blank_lightblue)
            holder.linearbackground.setBackgroundResource(0)
        }

        val layoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)

        if (itemList.size > 1){
            if (position != lastIndex){
                layoutParam.setMargins(24.dp, 24.dp, 24.dp, 0.dp)
            }
            else{
                layoutParam.setMargins(24.dp, 24.dp, 24.dp, 24.dp)
            }
        }
        else{
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 24.dp)
        }

        holder.cardParent.layoutParams = layoutParam

        holder.cardParent.setOnClickListener {
            if(position != 0){
                addOnListener.onAddOnClicked(addOnModel,selected,position)
                notifyDataSetChanged()
            }
        }
    }

    fun updateList(addOnList : ArrayList<AddOnModel>){
        this.itemList = addOnList
        notifyDataSetChanged()
    }

    class AddOnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearbackground = itemView.findViewById<LinearLayout>(R.id.linearBackground)
        val linearAmount = itemView.findViewById<LinearLayout>(R.id.linearAmount)
        val linearTotalPrice = itemView.findViewById<LinearLayout>(R.id.linearTotalPrice)
        val txtAmount = itemView.findViewById<TextView>(R.id.txtAmount)

        val imgCheck = itemView.findViewById<ImageView>(R.id.imgCheck)
        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtDesc = itemView.findViewById<TextView>(R.id.txtDesc)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val txtServiceName = itemView.findViewById<TextView>(R.id.txtServiceName)

        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val priceDivider = itemView.findViewById<View>(R.id.priceDivider)
    }
}