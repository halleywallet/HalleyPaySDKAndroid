package ir.millennium.halleywallet

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import ir.github.halleypay.HalleyPay
import ir.github.halleypay.callback.CallbackGetSecretKey

class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        setupTheme()
        setupHalleyWallet()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun setupTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setupHalleyWallet() {
        HalleyPay.configure(
            applicationContext,
            "+98",
            "9159176670",
            "ksndwnkjncoeiwiowqncocneiocn"
        )
        HalleyPay.callback = object : CallbackGetSecretKey {
            override fun onSuccess(secretKey: String?) {
                Log.d("secretKey:", secretKey!!)
            }

            override fun onFailure(message: String) {
                Log.d("onFailure:", message)
            }
        }
    }
}


