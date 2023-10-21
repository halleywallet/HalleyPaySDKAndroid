package io.github.halleypay.data.repository.remote

import io.github.halleypay.data.dataSource.remote.ApiService
import io.github.halleypay.data.model.remote.SecretKeyModel
import io.github.halleypay.domain.repository.remote.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteRepositoryImpl(private val apiService: ApiService) : RemoteRepository {

    override fun getSecretKey(params: HashMap<String, Any>): Flow<SecretKeyModel> = flow {
        val response = apiService.getSecretKey(params)
        emit(response)
    }
}