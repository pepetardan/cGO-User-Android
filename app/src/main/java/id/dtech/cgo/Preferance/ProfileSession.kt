package id.dtech.cgo.Preferance

import android.content.SharedPreferences

class ProfileSession (sharedPrefs : SharedPreferences){
    private val prefs = sharedPrefs
    private val editor = prefs.edit()

    var id : String?

        set(value) {
            editor.putString("id",value).commit()
        }
        get() {
            return prefs.getString("id",null)
        }

    var user_email : String?

        set(value) {
            editor.putString("user_email",value).commit()
        }
        get() {
            return prefs.getString("user_email",null)
        }

    var password : String?

        set(value) {
            editor.putString("password",value).commit()
        }
        get() {
            return prefs.getString("password",null)
        }

    var full_name : String?

        set(value) {
            editor.putString("full_name",value).commit()
        }
        get() {
            return prefs.getString("full_name",null)
        }

    var phone_number : String?

        set(value) {
            editor.putString("phone_number",value).commit()
        }
        get() {
            return prefs.getString("phone_number",null)
        }

    var verification_send_date : String?

        set(value) {
            editor.putString("verification_send_date",value).commit()
        }
        get() {
            return prefs.getString("verification_send_date",null)
        }

    var verification_code : Int

        set(value) {
            editor.putInt("verification_code",value).commit()
        }
        get() {
            return prefs.getInt("verification_code",0)
        }

    var profile_pict_url : String?

        set(value) {
            editor.putString("profile_pict_url",value).commit()
        }
        get() {
            return prefs.getString("profile_pict_url",null)
        }

    var address : String?

        set(value) {
            editor.putString("address",value).commit()
        }
        get() {
            return prefs.getString("address",null)
        }

    var dob : String?

        set(value) {
            editor.putString("dob",value).commit()
        }
        get() {
            return prefs.getString("dob",null)
        }

    var gender : Int

        set(value) {
            editor.putInt("gender",value).commit()
        }
        get() {
            return prefs.getInt("gender",0)
        }

    var id_type : Int

        set(value) {
            editor.putInt("id_type",value).commit()
        }
        get() {
            return prefs.getInt("id_type",0)
        }

    var id_number : String?

        set(value) {
            editor.putString("id_number",value).commit()
        }
        get() {
            return prefs.getString("id_number",null)
        }

    var referral_code : String?

        set(value) {
            editor.putString("referral_code",value).commit()
        }
        get() {
            return prefs.getString("referral_code", null)
        }

    var points : Long

        set(value) {
            editor.putLong("points",value).commit()
        }
        get() {
            return prefs.getLong("points",0)
        }

    var token : String?

        set(value) {
            editor.putString("token",value).commit()
        }
        get() {
            return prefs.getString("token",null)
        }

    fun removeSharedPrefsData(){
        editor.remove("id").commit()
        editor.remove("user_email").commit()
        editor.remove("password").commit()
        editor.remove("full_name").commit()
        editor.remove("phone_number").commit()
        editor.remove("verification_send_date").commit()
        editor.remove("verification_code").commit()
        editor.remove("profile_pict_url").commit()
        editor.remove("address").commit()
        editor.remove("dob").commit()
        editor.remove("gender").commit()
        editor.remove("id_type").commit()
        editor.remove("id_number").commit()
        editor.remove("referral_code").commit()
        editor.remove("points").commit()
        editor.remove("token").commit()
    }

}