package id.dtech.cgo.Util

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import id.dtech.cgo.R

class ViewUtil {
    companion object{
        fun laodingDialog(context: Context) : AlertDialog {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_loading,null)
            val loadingDialog = AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create()

            return loadingDialog
        }

        fun showBlackToast(context : Context, message : String, duration : Int) : Toast{
            val toastView = Toast.makeText(context ,message, duration)
            toastView.view.setBackgroundResource(R.drawable.background_black_toast)
            toastView.view.findViewById<TextView>(android.R.id.message).setTextColor(Color.
            parseColor("#FFFFFF"))
            return toastView
        }
    }
}