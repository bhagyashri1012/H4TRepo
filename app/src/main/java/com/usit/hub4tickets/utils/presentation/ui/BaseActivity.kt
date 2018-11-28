package com.usit.hub4tickets.domain.presentation.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.BaseView
import com.usit.hub4tickets.utils.ConnectivityReceiver
import com.usit.hub4tickets.utils.Utility


abstract class BaseActivity : AppCompatActivity(), BaseView, View.OnClickListener {
    private var toolBar: Toolbar? = null
    private var titleToolBar: TextView? = null
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

    private fun initToolbar() {
        toolBar = this.findViewById(R.id.mainToolbar) as Toolbar
        setSupportActionBar(toolBar)
        if (supportActionBar != null) {
            setDisplayHomeEnabled(true)
        }
        toolBar?.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolBar?.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {

            finish()
        }
        return super.onOptionsItemSelected(item)
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
        return true
    }

    override fun setTitle(title: CharSequence) {
        titleToolBar = this.findViewById(R.id.titleToolBar) as TextView
        titleToolBar?.text = title
    }

    override fun setTitle(titleId: Int) {
        titleToolBar = this.findViewById(R.id.titleToolBar) as TextView
        titleToolBar?.setText(titleId)
    }

    fun getToolBar(): Toolbar? {
        return toolBar
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.mainToolbar -> toolBar!!.setNavigationOnClickListener { onBackPressed() }

        }
    }
}
