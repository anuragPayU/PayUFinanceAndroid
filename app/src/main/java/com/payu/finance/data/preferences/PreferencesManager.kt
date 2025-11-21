package com.payu.finance.data.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Preferences manager for storing app data
 * Uses SharedPreferences to store cookies and user data
 */
class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREF_NAME = "PayUFinancePrefs"
        private const val KEY_COOKIES = "cookies"
        private const val KEY_MUID = "muid"
    }
    
    /**
     * Save cookies from Set-Cookie headers
     * Cookies are stored as a semicolon-separated string
     */
    fun saveCookies(cookies: String) {
        prefs.edit().putString(KEY_COOKIES, cookies).apply()
    }
    
    /**
     * Get saved cookies
     * Returns empty string if no cookies are saved
     */
    fun getCookies(): String {
        return prefs.getString(KEY_COOKIES, "") ?: ""
    }
    
    /**
     * Save muid (user ID)
     */
    fun saveMuid(muid: Long) {
        prefs.edit().putLong(KEY_MUID, muid).apply()
    }
    
    /**
     * Get saved muid
     * Returns -1 if no muid is saved
     */
    fun getMuid(): Long {
        return prefs.getLong(KEY_MUID, -1L)
    }
    
    /**
     * Clear all saved preferences (useful for logout)
     */
    fun clear() {
        prefs.edit().clear().apply()
    }
}

