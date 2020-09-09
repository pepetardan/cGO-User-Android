package id.dtech.cgo.Preferance

import android.content.SharedPreferences

class UserSession (sharedPrefs : SharedPreferences) {

    private val prefs = sharedPrefs
    private val editor = prefs.edit()

    var access_token : String?

        set(value) {
            editor.putString("access_token",value).commit()
        }
        get() {
            return prefs.getString("access_token",null)
        }

    var expires_in : Long

        set(value) {
            editor.putLong("expires_in",value).commit()
        }
        get() {
            return prefs.getLong("expires_in",0)
        }

    var token_type : String?

        set(value) {
            editor.putString("token_type",value).commit()
        }
        get() {
            return prefs.getString("token_type",null)
        }

    var refresh_token : String?

        set(value) {
            editor.putString("refresh_token",value).commit()
        }
        get() {
            return prefs.getString("refresh_token",null)
        }

    fun removeSharedPrefsData(){
        editor.remove("access_token").commit()
        editor.remove("expires_in").commit()
        editor.remove("token_type").commit()
        editor.remove("refresh_token").commit()
    }
}
