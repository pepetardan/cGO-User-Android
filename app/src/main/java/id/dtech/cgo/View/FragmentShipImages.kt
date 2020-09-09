package id.dtech.cgo.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

import id.dtech.cgo.R

class FragmentShipImages : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(transportImages : ArrayList<HashMap<String,Any>>) = FragmentShipImages().apply {
            val b = Bundle()
            b.putSerializable("images", transportImages)
            arguments = b
        }
    }

    private lateinit var imgOne : ImageView
    private lateinit var imgTwo : ImageView
    private lateinit var imgThree : ImageView

    private lateinit var txtMore : TextView

    private lateinit var transportImages : ArrayList<HashMap<String,Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = arguments

        b?.let { it ->
            transportImages = it.getSerializable("images") as ArrayList<HashMap<String,Any>>
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_ship_images, container, false)
        setView(view)
        return view
    }

    private fun setView(view : View){
        imgOne = view.findViewById(R.id.imgOne)
        imgTwo = view.findViewById(R.id.imgTwo)
        imgThree = view.findViewById(R.id.imgThree)
        txtMore = view.findViewById(R.id.txtMore)

        if (transportImages.size > 0){
            Log.d("jumlah_gambar",""+transportImages.size)
            if (transportImages.size == 1){

                imgOne.visibility = View.VISIBLE
                imgTwo.visibility = View.GONE
                imgThree.visibility = View.GONE

                val img = transportImages[0]["original"] as String

                if (img.isNotEmpty()){
                    activity?.let {
                        Picasso.get().load(img).into(imgOne)
                    }
                }
            }
            else if (transportImages.size == 2){
                imgOne.visibility = View.VISIBLE
                imgTwo.visibility = View.VISIBLE
                imgThree.visibility = View.GONE

                val img = transportImages[0]["original"] as String
                val img2 = transportImages[1]["original"] as String

                if (img.isNotEmpty()){
                    activity?.let {
                        Picasso.get().load(img).into(imgOne)
                        Picasso.get().load(img2).into(imgThree)
                    }
                }
            }
            else{
                imgOne.visibility = View.VISIBLE
                imgTwo.visibility = View.VISIBLE
                imgThree.visibility = View.VISIBLE

                val img = transportImages[0]["original"] as String
                val img2 = transportImages[1]["original"] as String
                val img3 = transportImages[2]["original"] as String

                if (img.isNotEmpty()){
                    activity?.let {
                        Picasso.get().load(img).into(imgOne)
                        Picasso.get().load(img2).into(imgTwo)
                        Picasso.get().load(img3).into(imgThree)
                    }
                }

                txtMore.visibility = View.VISIBLE
            }
        }
    }
}
