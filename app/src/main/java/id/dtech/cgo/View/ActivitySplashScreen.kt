package id.dtech.cgo.View

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import id.dtech.cgo.Preferance.BoardingSession
import id.dtech.cgo.R

class ActivitySplashScreen : AppCompatActivity() {

    private lateinit var boardingSession: BoardingSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setSplashScreen()
    }

    private fun setSplashScreen(){
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        boardingSession = BoardingSession(getSharedPreferences("boarding_session", Context.MODE_PRIVATE))
        Handler().postDelayed({
            if (boardingSession.isBoardingShowed){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivity(Intent(this,ActivityBoarding::class.java))
            }
            finish()
        },3000)
    }
}

