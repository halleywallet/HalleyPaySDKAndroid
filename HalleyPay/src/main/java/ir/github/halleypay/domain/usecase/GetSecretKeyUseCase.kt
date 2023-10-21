package ir.github.halleypay.domain.usecase

import ir.github.halleypay.data.model.remote.SecretKeyModel
import ir.github.halleypay.data.repository.remote.RemoteRepositoryImpl
import kotlinx.coroutines.flow.Flow

open class GetSecretKeyUseCase(private val remoteRepository: RemoteRepositoryImpl) {

    open fun getSecretKey(params: HashMap<String, Any>): Flow<SecretKeyModel> {
        return remoteRepository.getSecretKey(params)
    }
}