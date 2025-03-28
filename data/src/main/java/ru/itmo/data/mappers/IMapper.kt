package ru.itmo.data.mappers

interface IMapper<in T, out R> {
    fun map(dto: T): R
}