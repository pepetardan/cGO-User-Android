package id.dtech.cgo.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment

import id.dtech.cgo.R

class FragmentInputGuest : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         val view = inflater.inflate(R.layout.fragment_input_guest, container, false)
        return view
    }

}
