package ir.github.halleypay.domain.repository.remote

import ir.github.halleypay.data.model.remote.SecretKeyModel
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    fun getSecretKey(params: HashMap<String, Any>): Flow<SecretKeyModel>
}