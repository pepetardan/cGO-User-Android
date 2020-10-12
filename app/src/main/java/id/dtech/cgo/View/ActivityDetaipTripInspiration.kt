package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_detaip_trip_inspiration.*

class ActivityDetaipTripInspiration : AppCompatActivity(), View.OnClickListener{

    private var webUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaip_trip_inspiration)
        setView()
    }

    private fun setView(){
        val b = intent.extras

        b?.let { bundle ->
            webUrl = bundle.getString("trip_url") ?: ""
            setWebView()
        }

        ivBack.setOnClickListener(this)
        icRefresh.setOnClickListener(this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView(){

        mProgressBar.max = 100

        val webSetting = myWebView.settings
        webSetting.domStorageEnabled = true
        webSetting.javaScriptEnabled = true
        myWebView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY

        myWebView.settings.useWideViewPort = true
        myWebView.settings.loadWithOverviewMode = true

        myWebView.webChromeClient = object  : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                mProgressBar.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                super.onReceivedIcon(view, icon)
            }
        }

        myWebView.webViewClient = object : WebViewClient(){

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                url?.let { strUrl ->

                }

                mProgressBar.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                webUrl = request?.url.toString()
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                mProgressBar.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }

        myWebView.loadUrl(webUrl)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.icRefresh -> {
                setWebView()
            }
        }
    }
}