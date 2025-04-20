package ru.itmo.chirper.domain.repository

interface IStorageRepository {
    suspend fun getData(key: String): Map<String, Any>
}