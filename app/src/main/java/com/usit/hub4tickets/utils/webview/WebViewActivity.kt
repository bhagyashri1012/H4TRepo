package com.usit.hub4tickets.utils.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class WebViewActivity : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    private var webView: WebView? = null
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        if (intent.extras != null) {
            val heading = intent.extras.getString(Constant.Path.HEADING)
            this.url = intent.extras.getString(Constant.Path.URL)
            titleToolBar.text = heading
        }
        loadURL()
    }

    internal fun loadURL() {
        webView!!.loadUrl(url)
        val settings = webView!!.settings
        settings.javaScriptEnabled = true
        webView!!.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        Utility.showProgressDialog(this)
        webView!!.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {

                Utility.hideProgressBar()
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Utility.hideProgressBar()
            }
        }
    }
}
