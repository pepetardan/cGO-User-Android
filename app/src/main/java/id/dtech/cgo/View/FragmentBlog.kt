package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import id.dtech.cgo.Constant.MyConstant

import id.dtech.cgo.R
import io.supercharge.shimmerlayout.ShimmerLayout

class FragmentBlog : Fragment() {

    private lateinit var blogWebView : WebView
    private lateinit var shimmerLayout: ShimmerLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_blog, container, false)
        setView(view)
        return view
    }

    private fun setView(view: View){
        blogWebView = view.findViewById(R.id.blogWebView)
        shimmerLayout = view.findViewById(R.id.shimerLayout)
        setWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView(){

        blogWebView.visibility = View.GONE
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmerAnimation()

        val webSetting = blogWebView.settings
        val webUrl = MyConstant.WEB_BLOG_URL

        webSetting.javaScriptEnabled = true
        blogWebView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY

        blogWebView.settings.useWideViewPort = true
        blogWebView.settings.loadWithOverviewMode = true

        blogWebView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                blogWebView.visibility = View.VISIBLE
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmerAnimation()
            }

            override fun onReceivedError( view: WebView?, request: WebResourceRequest?,
                                          error: WebResourceError? ) {
                blogWebView.visibility = View.VISIBLE
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmerAnimation()
            }
        }

        blogWebView.loadUrl(webUrl)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentBlog()
    }
}
