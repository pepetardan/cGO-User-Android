package id.dtech.cgo.Preferance

import android.content.SharedPreferences

class BoardingSession (sharedPrefs : SharedPreferences){

    private val prefs = sharedPrefs
    private val editor = prefs.edit()

    var isBoardingShowed : Boolean

        set(value) {
            editor.putBoolean("is_boarding",value).commit()
        }
        get() {
            return prefs.getBoolean("is_boarding",false)
        }

    var isMainShowCase : Boolean

        set(value) {
            editor.putBoolean("isMainShowCase",value).commit()
        }
        get() {
            return prefs.getBoolean("isMainShowCase",false)
        }
}