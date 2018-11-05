package com.usit.hub4tickets.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.usit.hub4tickets.R
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import com.usit.hub4tickets.utils.webview.LinkSpanToOpenTextLinks
import se.simbio.encryption.Encryption
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
object Utility {

    private val TAG = Utility::class.java.name
    private var sProgressDialog: ProgressDialog? = null

    private val encryptionInstance: Encryption
        get() {
            val iv = byteArrayOf(-89, -19, 17, -83, 86, 106, -31, 30, -5, -111, 61, -75, -84, 95, 120, -53)
            return Encryption.getDefault("AZSXDCFVGBHNJMKL", "QAWSEDRFTGYHUJIKOLP", iv)
        }

    //"yyyy-MM-dd"
    val currentDate: String
        get() {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd")
            val now = Date()
            return sdfDate.format(now)
        }

    //"yyyy-MM-dd"
    val currentDateTime: String
        get() {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
            val now = Date()
            return sdfDate.format(now)
        }

    fun showProgressDialog(context: Context) {
        if (null != sProgressDialog && sProgressDialog!!.isShowing) {
            return
        }
        sProgressDialog = ProgressDialog(context)

        sProgressDialog!!.setMessage(context.resources.getString(R.string.loading))
        sProgressDialog!!.setCancelable(false)

        if (context is Activity) {
            if (!context.isFinishing) {
                sProgressDialog!!.show()
            }
        } else {
            sProgressDialog!!.show()
        }
    }

    fun RotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun hideProgressBar() {
        try {
            if (null != sProgressDialog && sProgressDialog!!.isShowing) {

                val context = sProgressDialog!!.context

                if (context is Activity) {

                    if (!context.isFinishing) {
                        sProgressDialog!!.dismiss()
                        sProgressDialog = null
                    }
                } else {
                    sProgressDialog!!.dismiss()
                    sProgressDialog = null
                }
            }
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "Simple ignore the exceprion", e)
        }

    }


    //checks if device connected to internet
    fun isConnectedToInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networks = connectivityManager.allNetworks
            var networkInfo: NetworkInfo?
            for (mNetwork in networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork)

                if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        } else {
            if (connectivityManager != null) {

                val info = connectivityManager.allNetworkInfo
                if (info != null) {
                    for (networkInfo in info) {
                        if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
                            Log.d(
                                TAG,
                                "NETWORKNAME: " + networkInfo.typeName
                            )
                            return true
                        }
                    }
                }
            }
        }
        Log.v(TAG, "not connected to internet")
        return false
    }

    //show soft keyboard
    fun showSoftKeyboard(activity: Activity, view: View) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(
            view.applicationWindowToken,
            InputMethodManager.SHOW_FORCED, 0
        )
    }


    fun showToastMessage(mContext: Context, msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    //Check if email is valid or not
    fun validateEmail(strEmail: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return if (strEmail.matches(emailPattern.toRegex()))
            true
        else
            false
    }

    //Check if email is valid or not
    fun validatePassword(strPwd: String): Boolean {
        val pdwPattern = "^(?=.{6,})(?=.*[@#$%^&+=]).*$"

        return if (strPwd.matches(pdwPattern.toRegex()))
            true
        else
            false
    }


    private fun getEncryptedData(data: String): String {
        return encryptionInstance.encryptOrNull(data)
    }

    private fun getDecryptedData(data: String): String {
        return encryptionInstance.decryptOrNull(data)
    }

    fun storeToSharedPref(context: Context, prefKey: String, data: String) {

        Pref.setValue(context, prefKey, getEncryptedData(data))

        Log.d(TAG, "Encrypt:" + getEncryptedData(data))
    }

    fun getFromSharedPref(context: Context, prefKey: String): String {
        val customerId = Pref.getValue(context, prefKey, "")
        return if (customerId == "") {
            customerId
        } else getDecryptedData(customerId!!)
    }

    fun setStatusBarColor(mContext: Context, colorCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = (mContext as Activity).window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#$colorCode")
        }
    }

    fun isValidEmailAddress(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    fun applyFont(button: RadioButton, context: Activity) {
        button.typeface = Typeface.createFromAsset(context.assets, "fonts/Montserrat-Regular.ttf")
    }


    fun getAppVersionName(context: Context): String {
        var pInfo: PackageInfo? = null
        var version = ""
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo!!.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }

    fun getDateFormatAsLastSeen(date: Calendar): String {

        //   String dayNumberSuffix = getDayNumberSuffix(date.get(Calendar.DAY_OF_MONTH));
        val dateFormat = SimpleDateFormat(" d MMM yyyy")
        return dateFormat.format(date.time)
    }

    fun getDateFormatYYYY_MM_DD(dateValue: String): String {
        if (dateValue == "" || null == dateValue) {
            return ""
        }
        val calendar = convertStingToDateYYYY_MM_DD(dateValue)
        //  String dayNumberSuffix = getDayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH));
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun convertStingToDateYYYY_MM_DD(dateValue: String): Calendar {
        val updatedDateValue =
            dateValue.replace("st".toRegex(), "").replace("nd".toRegex(), "").replace("rd".toRegex(), "")
                .replace("th".toRegex(), "")
        val calendar = GregorianCalendar()
        var date: Date? = null
        try {
            date = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(updatedDateValue)
            calendar.time = date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return calendar
    }

    fun getDateDifferenceInHours(currentDate: Date, lastCachedDate: Date): Long {
        val diff = currentDate.time - lastCachedDate.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        return minutes / 60
    }

    fun getDateFormatAsLastSeen(context: Context, smsTimeInMilis: String): String {
        val smsTime = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH)
        try {
            smsTime.time = sdf.parse(smsTimeInMilis)// all done
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val now = Calendar.getInstance()

        return if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            "Today "
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            "Yesterday "
        } else {
            Utility.getDateFormat(smsTimeInMilis)
        }
    }

    /*
          Convert date string(1991-11-23) to  format like 2nd Sept 2016
          param : String
           */
    fun getDateFormat(dateValue: String?): String {
        if (null == dateValue) {
            return ""
        } else if (dateValue == "") {
            return ""
        }

        val calendar = convertStingToDate(dateValue)
        // String dayNumberSuffix = getDayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH));
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        return dateFormat.format(calendar.time)
    }

    private fun convertStingToDate(dateValue: String): Calendar {

        val calendar = GregorianCalendar()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateValue)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        calendar.time = date
        return calendar
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun reduceSizeOfImage(file: File): File? {
        try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image

            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 25

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)

            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file

            file.createNewFile()
            val outputStream = FileOutputStream(file)

            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)


            return file
        } catch (e: Exception) {
            return null
        }

    }

    fun getDeviceInfo(context: Context): String {
        var pInfo: PackageInfo? = null
        var version = 1
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo!!.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        /*
        Get Device Info
         */
        var deviceInformation = ""
        deviceInformation += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
        deviceInformation += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT
        deviceInformation += "\n Device: " + android.os.Build.DEVICE
        deviceInformation += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")"
        deviceInformation += "\n AppVersion: $version"
        deviceInformation += "\n FCM Token " + Pref.getValue(context, PrefConstants.DEVICE_TOKEN, "")

        Log.d("Device_info", "" + version)
        return deviceInformation

    }

    fun getDateFormatTransaction(dateValue: String?): String {
        if (null == dateValue) {
            return ""
        } else if (dateValue == "") {
            return ""
        }
        val calendar = convertStingToDate(dateValue)
        // String dayNumberSuffix = getDayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH));
        val dateFormat = SimpleDateFormat("dd MMM yy")
        return dateFormat.format(calendar.time)
    }

    fun getStringTypeFromServerDate(dateValue: Date?): String {
        if (null == dateValue || dateValue.equals("")) {
            return ""
        }
        var date = ""
        try {
            date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(dateValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return date
    }

    fun setTextColor(textView: TextView, colorCode: String) {
        textView.setTextColor(Color.parseColor("#$colorCode"))
    }

    fun formatCustomerRange(customerPoints: String): String {
        return if (customerPoints != "") {
            if (java.lang.Double.parseDouble(customerPoints) > 9999999) {
                "\u221E"
            } else DecimalFormat("##.##").format(java.lang.Double.parseDouble(customerPoints))
        } else ""
    }

    fun getMobileNumberFormat(mobileNumber: String): String {
        if (mobileNumber == "") {
            return ""
        }
        val splitStr = mobileNumber.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return splitStr[1]
    }

    fun getDateFormatYYYY_MM_DDFromServerDate(dateValue: String): Date? {
        if (dateValue == "" || null == dateValue) {
            return null
        }
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(dateValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var resultDate: Date? = null
        try {
            resultDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(SimpleDateFormat("yyyy-MM-dd").format(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return resultDate
    }

    fun dateBetween(startDate: Date?, endDate: Date?): Boolean {
        // assume these are set to something
        if (null == startDate || endDate == null) {
            return false
        }
        val date = convertInToDate_yyyyMMdd(currentDate)
        // the date in question
        // DateUtils.isToday(startDate);
        if (date == startDate) {
            return true
        } else if (date == endDate) {
            return true
        }
        return date!!.after(startDate) && date.before(endDate)
    }

    fun convertInToDate_yyyyMMdd(dateValue: String?): Date? {
        if (null == dateValue) {
            return null
        }
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*Date resultDate = null;
        try {
            resultDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        return date
    }

    fun rateUsDialog(context: Context, activity: Activity, merchantId: String) {

        CustomDialogPresenter.showDialog(
            context,
            "Show us some love!",
            "If you enjoy using smat loyal, please take a moment to rate it and leave  us your valuable feedback.",
            "Rate now",
            "I'll do it later",
            object : CustomDialogPresenter.CustomDialogView {
                override fun onPositiveButtonClicked() {

                    Pref.setValue(context, Constant.Path.IS_RATE_US, true)
                    val appPackageName = context.packageName
                    try {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri
                                    .parse("market://details?id=$appPackageName")
                            )
                        )
                    } catch (anfe: android.content.ActivityNotFoundException) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                            )
                        )
                    }

                }

                override fun onNegativeButtonClicked() {
                    Pref.setValue(context, Constant.Path.IS_RATE_US, false)
                }
            })
    }

    fun getDateDeference(smsTimeInMilis: String): Boolean {

        var createdTimeDate: Date? = null
        try {
            createdTimeDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(smsTimeInMilis)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val now = Calendar.getInstance()
        now.add(Calendar.DAY_OF_YEAR, -16)
        val daysAgo = now.time

        return if (createdTimeDate == daysAgo) {
            false
        } else daysAgo.after(createdTimeDate)
    }

    fun dateAfterCurrentDate(expiryDate: Date?): Boolean {
        if (null == expiryDate) {
            return false
        }
        // assume these are set to something
        val date = convertInToDate_yyyyMMdd(currentDate)
        // the date in question 2017-03-31
        return if (date == expiryDate) {
            true
        } else date!!.before(expiryDate)
    }

    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun openUrlInWebView(mContext: Context, spannable: Spannable, textView: TextView, header: String) {
        val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
        for (urlSpan in spans) {
            val linkSpan = LinkSpanToOpenTextLinks(mContext, urlSpan.url, header)
            val spanStart = spannable.getSpanStart(urlSpan)
            val spanEnd = spannable.getSpanEnd(urlSpan)
            spannable.setSpan(linkSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.removeSpan(urlSpan)
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    fun getDeviceId(context: Context): String {
        val deviceId: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return deviceId
    }
}
