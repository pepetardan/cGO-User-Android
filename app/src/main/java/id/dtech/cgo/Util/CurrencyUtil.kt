package id.dtech.cgo.Util

import java.text.DecimalFormat

class CurrencyUtil {
    companion object{
        fun decimal(harga: Long): String {
            val df = DecimalFormat(",###")
            return df.format(harga)
        }
    }
}