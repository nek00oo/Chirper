package ru.itmo.data.api


import retrofit2.http.GET
import retrofit2.http.Path

interface StorageApi {
    @GET("server/v1/storage/{key}")
    suspend fun getJsonByKey(@Path("key") key: String): Map<String, Any>
}
