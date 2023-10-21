package io.github.halleypay.data.model.remote

import com.google.gson.annotations.SerializedName

data class SecretKeyModel(

    @field:SerializedName("data")
    val secretKeyData: SecretKeyDataModel? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class SecretKeyDataModel(

    @field:SerializedName("secret_key")
    val secretKey: String? = null,

    @field:SerializedName("expired_at")
    val expiredAt: String? = null
)
