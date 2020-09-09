package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Model.PhotoModel
import id.dtech.cgo.R

class PhotoAdapter (context : Context, datas : ArrayList<HashMap<String,Any>>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val contexs = context
    private val dataList = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(contexs).inflate( R.layout.item_photo,parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val mapData = dataList[position]
        val photos = mapData["exp_photo_image"] as ArrayList<PhotoModel>
        val folderName =     mapData["folder"] as String

        holder.txtName.text = folderName
        holder.txtTotalPhotos.text = ""+photos.size+" Photos"

        holder.rvPhotos.layoutManager = LinearLayoutManager(contexs,LinearLayoutManager.HORIZONTAL,
            false)
        holder.rvPhotos.adapter = PhotoImageAdapter(contexs,photos,folderName)
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvPhotos = itemView.findViewById<RecyclerView>(R.id.rvPhotos)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtTotalPhotos = itemView.findViewById<TextView>(R.id.txtTotalPhotos)
    }
}