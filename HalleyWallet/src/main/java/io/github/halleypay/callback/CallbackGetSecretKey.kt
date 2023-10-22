package io.github.halleypay.callback

interface CallbackGetSecretKey {
    fun onSuccess(secretKey: String?)
    fun onFailure(s: String)
}