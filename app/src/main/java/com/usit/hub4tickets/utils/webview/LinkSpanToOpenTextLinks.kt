package com.usit.hub4tickets.utils.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.style.URLSpan
import android.view.View
import com.usit.hub4tickets.utils.Constant

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

@SuppressLint("ParcelCreator")
class LinkSpanToOpenTextLinks(internal var mContext: Context, url: String, internal var header: String) : URLSpan(url) {

    override fun onClick(view: View) {
        val url = url
        val intent = Intent(mContext, WebViewActivity::class.java)
        intent.putExtra(Constant.Path.URL, url)
        intent.putExtra(Constant.Path.HEADING, header)
        mContext.startActivity(intent)
    }
}