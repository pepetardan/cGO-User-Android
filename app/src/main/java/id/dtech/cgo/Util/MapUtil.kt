package id.dtech.cgo.Util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapUtil {
    companion object{
        fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int, activity: Activity): BitmapDescriptor {
            val vectorDrawable = ResourcesCompat.getDrawable(activity.resources, id, null)!!
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}