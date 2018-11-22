package com.usit.hub4tickets.utils.view.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.usit.hub4tickets.R

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
object CustomDialogPresenter {


    fun showDialog(
        mContext: Context?,
        dialogTitle: String?,
        message: String?,
        positiveButtonName: String,
        negativeButtonName: String?,
        listener: CustomDialogView?
    ) {

        val builder = AlertDialog.Builder(mContext!!)
        val dialog = builder.create()
        builder.setTitle(dialogTitle)
        builder.setCancelable(false)
        builder.setMessage(message)

        builder.setPositiveButton(positiveButtonName) { dialog, id ->
            listener?.onPositiveButtonClicked()
        }

        if (negativeButtonName != null) {
            builder.setNegativeButton(negativeButtonName) { dialog, id ->
                listener?.onNegativeButtonClicked()
            }
        }

        val neutral_button = dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        val positive_button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)

        neutral_button?.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        positive_button?.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        if (!(mContext as Activity).isFinishing) {
            builder.show()
        }

    }

    interface CustomDialogView {
        fun onPositiveButtonClicked()

        fun onNegativeButtonClicked()
    }
}
