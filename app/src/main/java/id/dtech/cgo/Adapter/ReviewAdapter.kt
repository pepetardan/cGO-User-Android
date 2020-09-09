package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Model.ReviewModel
import id.dtech.cgo.R

class ReviewAdapter (context : Context, reviews : ArrayList<ReviewModel>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private val contexs = context
    private val reviewList = reviews

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(contexs).inflate(
            R.layout.item_review,parent,
            false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewModel = reviewList[position]
        holder.txtName.text = reviewModel.name
        holder.txtDescription.text = reviewModel.desc
        setStar(reviewModel.values,holder.imgStar1,holder.imgStar2,holder.imgStar3,
            holder.imgStar4,holder.imgStar5)
    }

    private fun setStar(count : Double, star1 : ImageView, star2 : ImageView, star3 : ImageView,
                        star4 : ImageView, star5 : ImageView ){

        if (count == 0.0){
            star1.setImageResource(R.drawable.ic_star_empty)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 0.5){
            star1.setImageResource(R.drawable.ic_star_half)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_half)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_half)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_half)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_half)
        }
        else if (count == 5.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtDescription = itemView.findViewById<TextView>(R.id.txtDescription)

        val imgStar1 = itemView.findViewById<ImageView>(R.id.imgStar1)
        val imgStar2 = itemView.findViewById<ImageView>(R.id.imgStar2)
        val imgStar3 = itemView.findViewById<ImageView>(R.id.imgStar3)
        val imgStar4 = itemView.findViewById<ImageView>(R.id.imgStar4)
        val imgStar5 = itemView.findViewById<ImageView>(R.id.imgStar5)
    }
}