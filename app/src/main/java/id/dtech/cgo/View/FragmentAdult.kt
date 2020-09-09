package id.dtech.cgo.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.dtech.cgo.R

class FragmentAdult : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_adult, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentAdult()
    }
}
