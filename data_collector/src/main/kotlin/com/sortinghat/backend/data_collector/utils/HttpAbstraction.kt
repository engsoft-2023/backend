package com.sortinghat.backend.data_collector.utils

interface HttpAbstraction<T> {
    fun get(url: String): ResponseHttp<T>
}
