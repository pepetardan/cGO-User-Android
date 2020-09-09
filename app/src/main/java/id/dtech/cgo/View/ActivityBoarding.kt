package id.dtech.cgo.View

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import id.dtech.cgo.Adapter.BoardingViewPager
import id.dtech.cgo.Preferance.BoardingSession
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_boarding.*
import java.lang.String
import java.math.BigDecimal


class ActivityBoarding : AppCompatActivity(), View.OnClickListener{

    private lateinit var boardingSession: BoardingSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boarding)
        setView()
    }

    private fun setView(){

        boardingSession = BoardingSession(getSharedPreferences("boarding_session", Context.MODE_PRIVATE))

        boardingPager.adapter = BoardingViewPager(this)
        pagerIndicator.setViewPager(boardingPager)

        boardingPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                when(boardingPager.currentItem){
                    0 -> {
                        btnNext.text = getString(R.string.next)
                    }
                    1 -> {
                        btnNext.text = getString(R.string.next)
                    }
                    2 -> {
                        btnNext.text = getString(R.string.start_exploring)
                    }
                }
            }

            override fun onPageSelected(p0: Int) {
            }
        })

        btnNext.setOnClickListener(this)
    }

    private fun buttonNextAction(){
        val currentPosition = boardingPager.currentItem
        if (currentPosition != 2){
            boardingPager.currentItem++
        }
        else{
            startActivity(Intent(this,MainActivity::class.java))
            boardingSession.isBoardingShowed = true
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnNext -> {
                buttonNextAction()
            }
        }
    }
}
