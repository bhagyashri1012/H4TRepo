package com.usit.hub4tickets.utils

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


/**
 * Created by Bhagyashri Burade
 * Date: 31/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

class utility{
    fun customAlert(context:Context)
    {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(context)

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to close this application ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                    dialog, id -> //context.finish()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("AlertDialogExample")
        // show alert dialog
        alert.show()
    }
}