package voc.cn.cnvoccoin.util

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import voc.cn.cnvoccoin.VocApplication
import java.lang.reflect.Type

/**
 * 全局存储SharePreference
 */
class PreferenceUtil {
    var preferences: SharedPreferences? = null

    companion object {
        val instance: PreferenceUtil? = PreferenceUtil()
    }

    init {
        preferences = VocApplication.getInstance()!!.getSharedPreferences("voccoin", Context.MODE_PRIVATE)
    }

    fun getString(key: String): String? {
        val value = preferences?.getString(key, null)
        return value ?: null
    }

    fun getInt(key: String, defaultValue: Int = 0): Int? {
        val value = preferences?.getInt(key, defaultValue)
        return value ?: null
    }

  /*  fun getBoolean(key: String): Boolean? {
        val value = preferences?.getBoolean(key, false)
        return value ?: null
    }*/

    fun getBoolean(key: String,defaultValue: Boolean = false): Boolean? {
        val value = preferences?.getBoolean(key, defaultValue)
        return value ?: null
    }

    fun getLong(key: String): Long? {
        val value = preferences?.getLong(key, -1)
        return value ?: null
    }

    fun getFloat(key: String): Float? {
        val value = preferences?.getFloat(key, -1.0f)
        return value ?: null
    }

    fun <T>get(key: String, type: Type): T? {
        var gson = Gson()
        var value: String? = preferences?.getString(key , null)
        var deserializableValue: T = gson.fromJson(value , type)
        return deserializableValue
    }

    fun set(key: String, value: String?) {
        if (TextUtils.isEmpty(value)) return
        preferences?.edit()?.putString(key, value)?.commit()
    }

    fun set(key: String, value: Boolean) {
        preferences?.edit()?.putBoolean(key, value)?.commit()
    }

    fun set(key: String, value: Int) {
        preferences?.edit()?.putInt(key, value)?.commit()
    }

    fun set(key: String, value: Float) {
        preferences?.edit()?.putFloat(key, value)?.commit()
    }

    fun set(key: String, value: Long) {
        preferences?.edit()?.putLong(key, value)?.commit()
    }

    fun <T>set(key: String, value: T) {
        var gson = Gson()
        var serializableValue = gson.toJson(value)
        preferences?.edit()?.putString(key, serializableValue)?.commit()
    }

    fun setStringSet(key: String, set: Set<String>) {
        preferences?.edit()?.putStringSet(key, set)?.commit()
    }

    fun getStringSet(key: String): Set<String>? {
        return preferences?.getStringSet(key, null)
    }

    fun remove(key: String) {
        preferences?.edit()?.remove(key)?.commit()
    }

}
