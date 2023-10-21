package io.github.halleypay.data.dataSource.remote

import io.github.halleypay.data.model.remote.SecretKeyModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/sdk/get-secret")
    suspend fun getSecretKey(@Body params: HashMap<String, Any>): SecretKeyModel
}