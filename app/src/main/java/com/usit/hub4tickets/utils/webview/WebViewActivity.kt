package com.usit.hub4tickets.utils.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_web_view.*
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

    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        if (intent.extras != null) {
            url = intent.extras.getString(Constant.Path.URL)
            title = intent.extras.getString(Constant.Path.HEADING)
        }
        mainToolbar.setNavigationOnClickListener { onBackPressed() }

        loadURL(url)
    }

    private fun loadURL(url: String?) {
        web_view!!.loadUrl(url)
        val settings = web_view!!.settings
        settings.javaScriptEnabled = true
        web_view!!.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        Utility.showProgressDialog(this)
        web_view!!.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                Utility.hideProgressBar()
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Utility.hideProgressBar()
            }
        }
    }
}
