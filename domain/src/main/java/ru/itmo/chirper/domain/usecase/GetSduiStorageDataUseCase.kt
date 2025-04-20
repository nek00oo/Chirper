package ru.itmo.chirper.domain.usecase

import ru.itmo.chirper.domain.repository.IStorageRepository

class GetSduiStorageDataUseCase(private val repository: IStorageRepository) {
    suspend fun execute(key: String): Map<String, Any> {
        return repository.getData(key)
    }
}