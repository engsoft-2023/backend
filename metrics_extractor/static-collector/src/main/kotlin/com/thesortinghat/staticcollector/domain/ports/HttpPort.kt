package com.thesortinghat.staticcollector.domain.ports

import com.thesortinghat.staticcollector.domain.vo.ResponseHttp

interface HttpPort {
    fun get(url: String): ResponseHttp
}