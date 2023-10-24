package io.homangostar.halleywallet

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import io.github.halleypay.HalleyPay
import io.github.halleypay.callback.CallbackGetSecretKey

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTheme()
        setupHalleyWallet()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
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


