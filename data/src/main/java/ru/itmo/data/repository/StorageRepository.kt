package ru.itmo.data.repository

import ru.itmo.chirper.domain.repository.IStorageRepository
import ru.itmo.data.api.StorageApi

class StorageRepository(private val api: StorageApi): IStorageRepository {
    override suspend fun getData(key: String): Map<String, Any> {
        return api.getJsonByKey(key)
    }
}
