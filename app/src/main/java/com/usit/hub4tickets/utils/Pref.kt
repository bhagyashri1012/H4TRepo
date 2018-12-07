package com.usit.hub4tickets.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import java.util.*

@SuppressLint("NewApi")
object Pref {

    private var sharedPreferences: SharedPreferences? = null

    fun openPref(context: Context) {

        sharedPreferences = context.getSharedPreferences(
            Constant.Path.PREF_FILE,
            Context.MODE_PRIVATE
        )
    }

    fun openPref_profile(context: Context) {

        sharedPreferences = context.getSharedPreferences(
            Constant.Path.PREF_FILE_PROFILE,
            Context.MODE_PRIVATE
        )

    }

    fun getValue(
        context: Context?, key: String,
        defaultValue: String
    ): String? {
        var result: String? = ""
        if (null != context) {
            Pref.openPref(context)
            if (Pref.sharedPreferences != null) {
                result = Pref.sharedPreferences!!.getString(key, defaultValue)
            }
            Pref.sharedPreferences = null
        }

        return result
    }

    fun getValue_profile(
        context: Context, key: String,
        defaultValue: String
    ): String? {
        Pref.openPref_profile(context)
        val result = Pref.sharedPreferences!!.getString(key, defaultValue)
        Pref.sharedPreferences = null
        return result
    }

    fun setValue(context: Context?, key: String, value: String) {
        Pref.openPref(context!!)
        var prefsPrivateEditor: Editor? = Pref.sharedPreferences!!.edit()
        prefsPrivateEditor!!.putString(key, value)
        prefsPrivateEditor.commit()
        prefsPrivateEditor = null
        Pref.sharedPreferences = null
    }

    fun setValue_profile(context: Context, key: String, value: String) {
        Pref.openPref_profile(context)
        var prefsPrivateEditor: Editor? = Pref.sharedPreferences!!.edit()
        prefsPrivateEditor!!.putString(key, value)
        prefsPrivateEditor.commit()
        prefsPrivateEditor = null
        Pref.sharedPreferences = null
    }

    fun getValue(
        context: Context, key: String,
        defaultValue: Int
    ): Int {
        Pref.openPref(context)
        val result = Pref.sharedPreferences!!.getInt(key, defaultValue)
        Pref.sharedPreferences = null
        return result
    }

    fun setValue(context: Context, key: String, value: Int) {
        Pref.openPref(context)
        var prefsPrivateEditor: Editor? = Pref.sharedPreferences!!.edit()
        prefsPrivateEditor!!.putInt(key, value)
        prefsPrivateEditor.commit()
        prefsPrivateEditor = null
        Pref.sharedPreferences = null
    }

    fun getValue(
        context: Context?, key: String,
        defaultValue: Boolean
    ): Boolean {
        var result = false
        if (null != context) {
            Pref.openPref(context)
            if (Pref.sharedPreferences != null) {
                result = Pref.sharedPreferences!!.getBoolean(key, defaultValue)
            }
            Pref.sharedPreferences = null
        }

        return result
    }

    fun setValue(context: Context?, key: String, value: Boolean) {
        // if (sharedPreferences != null) {
        Pref.openPref(context!!)
        var prefsPrivateEditor: Editor? = Pref.sharedPreferences!!.edit()
        prefsPrivateEditor!!.putBoolean(key, value)
        prefsPrivateEditor.commit()
        prefsPrivateEditor = null
        Pref.sharedPreferences = null
        //    }
    }


    fun setStringSet(
        _Context: Context, key: String,
        mSetArray: Set<String>
    ) {

        Pref.openPref(_Context)

        var preferenceEditor: Editor? = Pref.sharedPreferences!!.edit()

        preferenceEditor!!.putStringSet(key, mSetArray)

        preferenceEditor.commit()
        preferenceEditor = null
        Pref.sharedPreferences = null
    }

    fun getStoredPassHistory(_Context: Context, mKey: String): Set<String> {

        Pref.openPref(_Context)

        val mSetPassHistory = Pref.sharedPreferences!!
            .getStringSet(mKey, HashSet()) as HashSet<String>

        Pref.sharedPreferences = null

        return mSetPassHistory
    }

}