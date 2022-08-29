package com.sortinghat.backend.data_collector.application.utils

data class ResponseHttp<T>(val status: Int, val data: T)
