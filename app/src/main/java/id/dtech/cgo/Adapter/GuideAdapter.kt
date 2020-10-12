package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.R
import id.dtech.cgo.View.dp
import kotlinx.android.synthetic.main.activity_detail_experience.*

class GuideAdapter(context : Context, from : Int, guideList : ArrayList<HashMap<String,Any>>,selectePosition : Int
                   ,guideListener : ApplicationListener.Companion.GuideListener?) :
    RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private val contexts = context
    private val froms = from
    private val guides = guideList
    private val listener = guideListener

    private var selectedPosition = selectePosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
       val view = LayoutInflater.from(contexts).inflate(R.layout.item_guide,parent,false)
        return GuideViewHolder(view)
    }

    override fun getItemCount(): Int {
       return guides.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guides[position]
        val guideName = guide["guide_name"] as String
        val guidePic = guide["guide_photo"] as String
        val isCertified = guide["guide_certified"] as Int
        val isAvailable = guide["guide_available"] as Boolean
        val rating = guide["guide_rating"] as Double
        val genderName = guide["gender_value"] as String
        val guideLicence = guide["guide_licence"] as String
        val guideDesc = guide["guide_desc"] as String
        val languages = guide["genderLanguages"] as ArrayList<HashMap<String,Any>>
        val lastIndex = guides.size - 1

        val layoutParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        if (position != lastIndex){
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 0.dp)
        }
        else{
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 24.dp)
        }

            if (froms == 1){

            if (isAvailable){
                if (selectedPosition == position){
                    holder.imgCheckTypeBlue.visibility = View.VISIBLE
                    holder.btnNext.visibility = View.VISIBLE
                    holder.linearParent.setBackgroundResource(R.drawable.background_add_on)
                }
                else{
                    holder.imgCheckTypeBlue.visibility = View.GONE
                    holder.btnNext.visibility = View.GONE
                    holder.linearParent.setBackgroundResource(0)
                }
            }
            else{
                holder.linearParent.setBackgroundColor(Color.parseColor("#F2F2F2"))
            }

            holder.linearParent.setOnClickListener {
                if (isAvailable){
                    selectedPosition = position
                    notifyDataSetChanged()
                }
            }
        }

        if (guidePic.isNotEmpty()){
            Picasso.get().load(guidePic).into(holder.imgPic)
        }
        else{

        }

        Log.d("gambarcuy", guidePic)

        if (isCertified == 1){
            holder.imgCertified.visibility = View.VISIBLE
            holder.txtCertified.visibility = View.VISIBLE
        }
        else{
            holder.imgCertified.visibility = View.GONE
            holder.txtCertified.visibility = View.GONE
        }

        var strName = ""

        if (languages.size > 1){
            for (i in 0 until languages.size){
                val language = languages[i]
                val name =  language["genderLanguageName"] as? String ?: ""

                if (i != languages.size - 1){
                    strName += "$name, "
                }
                else{
                    strName += name
                }
            }
        }
        else{
            strName = languages[0]["genderLanguageName"] as? String ?: ""
        }

        holder.txtLanguage.text = strName

        holder.txtName.text = guideName
        holder.txtGender.text = genderName
        holder.txtDescription.text = guideDesc
        holder.txtLicence.text = "Licence: $guideLicence"
        holder.cardParent.layoutParams = layoutParam

        setStar(rating,holder.imgStar1,holder.imgStar2,holder.imgStar3,holder.imgStar4,
            holder.imgStar5,holder.txtStar)

        holder.btnNext.setOnClickListener {
            listener?.onGuideClicked(guide)
        }
    }

    private fun setStar(count : Double, star1 : ImageView, star2 : ImageView, star3 : ImageView,
                        star4 : ImageView, star5 : ImageView,txtCount : TextView
    ){

        if (count == 0.0){
            txtCount.text = "0 /"
            star1.setImageResource(R.drawable.ic_star_empty)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 0.5){
            txtCount.text = "0.5 /"
            star1.setImageResource(R.drawable.ic_star_half)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.0){
            txtCount.text = "1 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.5){
            txtCount.text = "1.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_half)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.0){
            txtCount.text = "2 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.5){
            txtCount.text = "2.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_half)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.0){
            txtCount.text = "3 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.5){
            txtCount.text = "3.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_half)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.0){
            txtCount.text = "4 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.5){
            txtCount.text = "4.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_half)
        }
        else if (count == 5.0){
            txtCount.text = "5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }
    }

    class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val imgCheckTypeBlue = itemView.findViewById<ImageView>(R.id.imgCheckTypeBlue)
        val btnNext = itemView.findViewById<Button>(R.id.btnNext)
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
        val imgPic = itemView.findViewById<CircleImageView>(R.id.imgPic)
        val imgCertified  = itemView.findViewById<ImageView>(R.id.imgCertified)
        val txtGender = itemView.findViewById<MyTextView>(R.id.txtGender)
        val txtCertified = itemView.findViewById<MyTextView>(R.id.txtCertified)
        val txtLicence = itemView.findViewById<MyTextView>(R.id.txtLicence)
        val txtDescription = itemView.findViewById<MyTextView>(R.id.txtDescription)
        val txtLanguage = itemView.findViewById<MyTextView>(R.id.txtLanguage)
        val txtStar = itemView.findViewById<MyTextView>(R.id.txtStar)

        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)

        val imgStar1 = itemView.findViewById<ImageView>(R.id.imgStar1)
        val imgStar2 = itemView.findViewById<ImageView>(R.id.imgStar2)
        val imgStar3 = itemView.findViewById<ImageView>(R.id.imgStar3)
        val imgStar4 = itemView.findViewById<ImageView>(R.id.imgStar4)
        val imgStar5 = itemView.findViewById<ImageView>(R.id.imgStar5)
    }
}