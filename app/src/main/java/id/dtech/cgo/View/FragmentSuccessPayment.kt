package id.dtech.cgo.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import carbon.widget.RelativeLayout
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment

import id.dtech.cgo.R

class FragmentSuccessPayment : RoundedBottomSheetDialogFragment(){

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSuccessPayment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_success_payment, container, false)
        return view
    }
}
