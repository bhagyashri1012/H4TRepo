package com.usit.hub4tickets

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.usit.hub4tickets.dashboard.ui.DashboardActivity
import com.usit.hub4tickets.login.ui.LoginActivity
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Pref.setValue(this, PrefConstants.IS_DASHBOARD, false)

        //Initialize the Handler
        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            performOperations()
        }
    }

    private fun performOperations() {
        if (Pref.getValue(this@SplashActivity, PrefConstants.IS_LOGIN, false) || Pref.getValue(this@SplashActivity, PrefConstants.IS_FIRST_TIME, false)) {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
            finish()
        } else {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}
