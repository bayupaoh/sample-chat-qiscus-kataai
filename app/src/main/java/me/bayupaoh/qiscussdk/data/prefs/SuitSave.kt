package me.bayupaoh.qiscussdk.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log

import com.google.common.reflect.TypeParameter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class SuitSave {

    companion object {
        private var instance: SuitSave? = null
        private var mSharedPreferences: SharedPreferences? = null

        fun init(context: Context) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        }

        fun instance(): SuitSave {
            if (instance == null) {
                validateInitialization()
                synchronized(SuitSave::class.java) {
                    if (instance == null) {
                        instance = SuitSave()
                    }
                }
            }
            return instance!!
        }

        private fun validateInitialization() {
            if (mSharedPreferences == null)
                throw SuitException("SuitSave Library must be initialized inside your application class by calling SuitSave.init(getApplicationContext)")
        }
    }

    fun saveInt(key: String, value: Int) {
        val editor = mSharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        return if (isValidKey(key)) {
            mSharedPreferences!!.getInt(key, 0)
        } else 0
    }

    fun saveBoolean(key: String, value: Boolean) {
        val editor = mSharedPreferences!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return if (isValidKey(key)) {
            mSharedPreferences!!.getBoolean(key, false)
        } else {
            false
        }
    }


    fun saveFloat(key: String, value: Float) {
        val editor = mSharedPreferences!!.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String): Float {
        return if (isValidKey(key)) {
            mSharedPreferences!!.getFloat(key, 0.0f)
        } else 0.0f
    }


    fun saveLong(key: String, value: Long) {
        val editor = mSharedPreferences!!.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String): Long {
        return if (isValidKey(key)) {
            mSharedPreferences!!.getLong(key, 0)
        } else 0
    }


    fun saveString(key: String, value: String) {
        val editor = mSharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return if (isValidKey(key)) {
            mSharedPreferences!!.getString(key, null)
        } else null
    }

    fun <T> saveObject(key: String, `object`: T) {
        val objectString = Gson().toJson(`object`)
        val editor = mSharedPreferences!!.edit()
        editor.putString(key, objectString)
        editor.apply()
    }

    fun <T> getObject(key: String, classType: Class<T>): T? {
        if (isValidKey(key)) {
            val objectString = mSharedPreferences!!.getString(key, null)
            if (objectString != null) {
                return Gson().fromJson(objectString, classType)
            }
        }
        return null
    }


    fun <T> saveObjectsList(key: String, objectList: List<T>) {
        val objectString = Gson().toJson(objectList)
        val editor = mSharedPreferences!!.edit()
        editor.putString(key, objectString)
        editor.apply()
    }

    fun <T> getObjectsList(key: String, classType: Class<T>): List<T>? {
        if (isValidKey(key)) {
            val objectString = mSharedPreferences!!.getString(key, null)
            if (objectString != null) {
                return Gson().fromJson<List<T>>(objectString, object : TypeToken<List<T>>() {

                }
                        .where(object : TypeParameter<T>() {

                        }, classType)
                        .type)
            }
        }

        return null
    }

    fun clearSession() {
        val editor = mSharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }

    fun deleteValue(key: String): Boolean {
        if (isValidKey(key)) {
            val editor = mSharedPreferences!!.edit()
            editor.remove(key)
            editor.apply()
            return true
        }

        return false
    }

    private fun isValidKey(key: String): Boolean {
        val map = mSharedPreferences!!.all
        return if (map.containsKey(key)) {
            true
        } else {
            Log.e("SuitSave", "No element founded in sharedPrefs with the key $key")
            false
        }
    }


}