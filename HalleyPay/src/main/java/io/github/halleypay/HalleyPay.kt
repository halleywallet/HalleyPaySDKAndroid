package io.github.halleypay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.halleypay.callback.CallbackGetSecretKey
import io.github.halleypay.data.repository.remote.RemoteRepositoryImpl
import io.github.halleypay.data.repository.remote.RetrofitClientImpl
import io.github.halleypay.domain.usecase.GetSecretKeyUseCase
import io.github.halleypay.presentation.activity.HalleyPayActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

object HalleyPay {

    private val coroutinesScope = CoroutineScope(Dispatchers.IO)

    private var secretKey: String? = null

    private var codeCountry: String? = null
    private var userPhone: String? = null
    private var appKey: String? = null

    lateinit var getSecretKeyUseCase: GetSecretKeyUseCase

    var callback: CallbackGetSecretKey? = null

    fun configure(context: Context, codeCountry: String, userPhone: String, appKey: String) {
        this.codeCountry = codeCountry
        this.userPhone = userPhone
        this.appKey = appKey
        getSecretKeyUseCase =
            RetrofitClientImpl.buildApiNonCache(context)
                ?.let { RemoteRepositoryImpl(it) }?.let { GetSecretKeyUseCase(it) }!!
        val params = HashMap<String, Any>()
        params["mobile_prefix"] = codeCountry
        params["mobile"] = userPhone
        params["app_key"] = appKey
        getSecretKey(params)
    }

    private fun getSecretKey(params: HashMap<String, Any>) {
        getSecretKeyUseCase.getSecretKey(params)
            .flowOn(Dispatchers.IO)
            .map { secretKeyModel ->
                secretKey = secretKeyModel.secretKeyData?.secretKey
                callback?.onSuccess(secretKey)
            }
            .onStart {
            }
            .catch { throwable ->
                callback!!.onFailure("you got Error ${throwable.message}")
            }.launchIn(coroutinesScope)
    }

    fun start(activity: Activity, secretKey: String) {

        if (codeCountry?.isEmpty() == true) {
            Log.d("HalleyWallet:", "Code country is wrong")
        } else if (userPhone?.isEmpty() == true || userPhone?.length != 10) {
            Log.d("HalleyWallet:", "user phone is wrong")
        } else if (secretKey.isEmpty()) {
            Log.d("HalleyWallet:", "secretKey is empty")
        } else {
            activity.startActivity(Intent(activity, HalleyPayActivity::class.java))
            //callback after activity
        }
    }
}