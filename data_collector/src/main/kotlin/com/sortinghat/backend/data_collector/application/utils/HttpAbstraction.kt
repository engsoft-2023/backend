package com.sortinghat.backend.data_collector.application.utils

interface HttpAbstraction<T> {
    fun get(url: String): ResponseHttp<T>
}
