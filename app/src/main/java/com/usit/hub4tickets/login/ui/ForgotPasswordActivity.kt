package com.usit.hub4tickets.login.ui

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*


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

    private fun verifyOtp() {
        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.verify_otp_dialog, null)
        val editText = dialogView.findViewById(R.id.editTextEnterOtp) as EditText
        val buttonCancel = dialogView.findViewById(R.id.button_cancel) as Button
        val buttonVerify = dialogView.findViewById(R.id.button_verify) as Button
        buttonVerify.setOnClickListener {
            val otp = editText.toString()
        }
        buttonCancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }
}
