package ir.github.halleypay.data.repository.remote

import ir.github.halleypay.data.dataSource.remote.ApiService
import ir.github.halleypay.data.model.remote.SecretKeyModel
import ir.github.halleypay.domain.repository.remote.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteRepositoryImpl(private val apiService: ApiService) : RemoteRepository {

    override fun getSecretKey(params: HashMap<String, Any>): Flow<SecretKeyModel> = flow {
        val response = apiService.getSecretKey(params)
        emit(response)
    }
}