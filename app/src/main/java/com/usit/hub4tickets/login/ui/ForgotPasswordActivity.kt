package com.usit.hub4tickets.login.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.verify_otp_dialog.*


class ForgotPasswordActivity : BaseActivity() {

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        title = resources.getString(R.string.action_forgot_password)
        send_otp_button.setOnClickListener {
            verifyOtp()
            }
    }

    private fun verifyOtp()
    {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.verify_otp_dialog, null)
        builder.setView(dialogView);
        builder.setTitle(R.string.two_step_verification)
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.dismiss() }

        builder.setPositiveButton("Submit") { _, _ ->
            val otp = editTextEnterOtp.text.toString()
        }
        builder.show()
    }
}
