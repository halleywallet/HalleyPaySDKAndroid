package io.github.halleypay.core.ui

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import io.github.halleypay.core.utils.AuxiliaryFunctionsManager
import io.github.halleypay.data.dataSource.local.sharedPrefrences.SharedPreferencesManager
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    lateinit var auxiliaryFunctionsManager: AuxiliaryFunctionsManager
    override fun attachBaseContext(newBase: Context) {
        auxiliaryFunctionsManager = AuxiliaryFunctionsManager(SharedPreferencesManager(newBase))
        val newConfiguration = Configuration(newBase.resources?.configuration)
        val locale = Locale(SharedPreferencesManager(newBase).getLanguageApp())
        newConfiguration.fontScale = 1.0f
        Locale.setDefault(locale)
        newConfiguration.setLocale(locale)
        newConfiguration.setLayoutDirection(locale)
        applyOverrideConfiguration(newConfiguration)
        super.attachBaseContext(newBase)
    }
}
