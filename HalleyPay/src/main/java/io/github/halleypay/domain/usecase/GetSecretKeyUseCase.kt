package io.github.halleypay.domain.usecase

import io.github.halleypay.data.model.remote.SecretKeyModel
import io.github.halleypay.data.repository.remote.RemoteRepositoryImpl
import kotlinx.coroutines.flow.Flow

open class GetSecretKeyUseCase(private val remoteRepository: RemoteRepositoryImpl) {

    open fun getSecretKey(params: HashMap<String, Any>): Flow<SecretKeyModel> {
        return remoteRepository.getSecretKey(params)
    }
}