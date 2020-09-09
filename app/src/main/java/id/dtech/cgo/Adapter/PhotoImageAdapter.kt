package id.dtech.cgo.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.Model.PhotoModel

import id.dtech.cgo.R
import id.dtech.cgo.View.ActivityPhotoDetail
import id.dtech.cgo.View.dp
import java.util.ArrayList

class PhotoImageAdapter (context : Context, photoList : ArrayList<PhotoModel>, folderName : String)
    : RecyclerView.Adapter<PhotoImageAdapter.PhotoImageViewHolder>() {

    private val contexs = context
    private val items = photoList
    private val name = folderName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoImageViewHolder {
        val view = LayoutInflater.from(contexs).inflate( R.layout.item_photo_image,parent, false)
        return PhotoImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PhotoImageViewHolder, position: Int) {
        val photoModel = items[position]
        val lastIndex = items.size - 1

        Picasso.get().load(photoModel.original ?: "").into(holder.imgImage)

        val layoutParam = FrameLayout.LayoutParams(100.dp,100.dp)

        if (position == 0){
            layoutParam.setMargins(24.dp, 0, 0, 0)
        }
        else if (position != lastIndex && position != 0){
            layoutParam.setMargins(12.dp, 0, 0, 0)
        }
        else{
            layoutParam.setMargins(12.dp, 0, 24.dp, 0)
        }

        holder.cardParent.layoutParams = layoutParam

        holder.cardParent.setOnClickListener {
            val i = Intent(contexs,ActivityPhotoDetail::class.java)
            i.putExtra("folder_name",name)
            i.putParcelableArrayListExtra("photos",items)
            i.putExtra("clicked_position",position)
            contexs.startActivity(i)
        }
    }

    class PhotoImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val imgImage = itemView.findViewById<ImageView>(R.id.imgImage)
    }
}