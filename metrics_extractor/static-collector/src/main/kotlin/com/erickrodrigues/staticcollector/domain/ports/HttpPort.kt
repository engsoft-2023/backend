package com.erickrodrigues.staticcollector.domain.http

interface HttpPort {
    fun get(url: String): ResponseHttp
}