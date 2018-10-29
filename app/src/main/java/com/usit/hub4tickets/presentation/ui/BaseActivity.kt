package com.usit.hub4tickets.domain.presentation.screens

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.BaseView
import com.usit.hub4tickets.utils.ConnectivityReceiver
import kotlinx.android.synthetic.main.common_toolbar.*


abstract class BaseActivity : AppCompatActivity(), BaseView {
    private var toolBar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        initToolbar()

        val intentFilter = IntentFilter(ConnectivityReceiver.NETWORK_AVAILABLE_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val isConnected = intent.getBooleanExtra(ConnectivityReceiver.IS_NETWORK_AVAILABLE, false)
                MainApplication.getInstance.connected = isConnected
                if (!isConnected)
                    showToast(getString(R.string.message_no_internet))
            }
        }, intentFilter)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initToolbar() {
        toolBar = this.findViewById(R.id.mainToolbar) as Toolbar

        if (toolBar != null) {
            setSupportActionBar(toolBar)

            toolBar?.setNavigationOnClickListener { onBackPressed() }

            if (supportActionBar != null) {
                setDisplayHomeEnabled(true)
            }
        }
    }

    override fun showProgress(flag: Boolean) {
    }

    override fun showToast(message: String?) {
        val snackbar = message?.let {
            Snackbar.make(
                findViewById(android.R.id.content),
                it, Snackbar.LENGTH_SHORT
            )
        }
        val sbView = snackbar?.view
        val textView = sbView?.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    override fun showDialog(title: String?, message: String?) {
        if (message != null) {
            showToast(message)
        } else {
            showToast(getString(R.string.message_failed_request_general))
        }
    }

    protected abstract fun getLayoutResource(): Int

    private fun setDisplayHomeEnabled(b: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(b)
            supportActionBar!!.setDisplayShowHomeEnabled(b)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun setTitle(title: CharSequence) {
        val toolBarTitle = findViewById<View>(R.id.titleToolBar) as TextView
        toolBarTitle?.setText(title)
    }

    override fun setTitle(titleId: Int) {
        val toolBarTitle = findViewById<View>(R.id.titleToolBar) as TextView
        toolBarTitle?.setText(titleId)
    }

    fun getToolBar(): Toolbar? {
        return toolBar
    }
}
