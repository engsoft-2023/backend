package com.thesortinghat.staticcollector.application.utils

interface HttpAbstraction<T> {
    fun get(url: String): ResponseHttp<T>
}
