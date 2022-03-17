package com.erickrodrigues.staticcollector.domain.ports

import com.erickrodrigues.staticcollector.domain.vo.ResponseHttp

interface HttpPort {
    fun get(url: String): ResponseHttp
}