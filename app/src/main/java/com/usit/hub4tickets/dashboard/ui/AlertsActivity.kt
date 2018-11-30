package com.usit.hub4tickets.dashboard.ui

import android.os.Bundle
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.common_toolbar.*

class AlertsActivity : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)
        title = resources.getString(R.string.action_alerts)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }

    }
}
