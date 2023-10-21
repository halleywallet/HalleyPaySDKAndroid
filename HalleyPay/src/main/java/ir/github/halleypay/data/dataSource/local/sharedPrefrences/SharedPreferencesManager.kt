package ir.github.halleypay.data.dataSource.local.sharedPrefrences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ir.github.halleypay.domain.entity.TypeLanguage
import ir.github.halleypay.domain.entity.TypeTheme

class SharedPreferencesManager(val context: Context) {
    private val SHARED_PREF_NAME = "HalleySdkSharedPref"
    private val TYPE_THEME = "typeTheme"
    private val LANGUAGE_APP = "languageApp"

    fun setStatusTheme(isLightThemeActive: Int) {
        val editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit()
        editor.putInt(TYPE_THEME, isLightThemeActive)
        editor.apply()
    }

    fun getStatusTheme(): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        return sharedPreferences.getInt(TYPE_THEME, TypeTheme.DARK.typeTheme)
    }

    fun setLanguageApp(language: String) {
        val editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit()
        editor.putString(LANGUAGE_APP, language)
        editor.apply()
    }

    fun getLanguageApp(): String {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        return sharedPreferences.getString(LANGUAGE_APP, TypeLanguage.PERSIAN.typeLanguage)!!
    }
}