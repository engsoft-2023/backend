package domain.ports

import domain.responses.HTTPResponse

interface HTTPPort {
    fun get(url: String): HTTPResponse
}