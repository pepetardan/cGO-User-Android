package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.activity_payment_redirect.*
import java.lang.Exception

class ActivityPaymentRedirect : AppCompatActivity(), View.OnClickListener{

    companion object{
        val PAYMENT_REDIRECT_RESULT = 515
    }

    private var webUrl = ""
    private var isSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_redirect)
        setView()
    }

    private fun setView(){

        intent.extras?.let { bundle ->
            webUrl = bundle.getString("web_url") ?: ""
            setWebView()
        }

        icClose.setOnClickListener(this)
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

                       val uri =  Uri.parse(strUrl)
                       if (uri.getQueryParameter("order_id") != null && uri.getQueryParameter("status_code") != null
                           && uri.getQueryParameter("transaction_status") != null){
                           isSuccess = true
                           setActivityResult()
                       }
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

    private fun setActivityResult(){
        val i = Intent(this,ActivityCheckout::class.java)
        i.putExtra("success",isSuccess)
        setResult(PAYMENT_REDIRECT_RESULT,i)
        finish()
    }

    override fun onBackPressed() {
        setActivityResult()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.icClose -> {
                setActivityResult()
            }

            R.id.icRefresh -> {
                myWebView.reload()
            }
        }
    }
}

