package com.thesortinghat.staticcollector.application.utils

data class ResponseHttp<T>(val status: Int, val data: T)
