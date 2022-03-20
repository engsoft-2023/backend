package com.thesortinghat.staticcollector.application.adapters

import com.thesortinghat.staticcollector.domain.ports.HttpPort
import com.thesortinghat.staticcollector.domain.vo.ResponseHttp
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpAdapter : HttpPort {
    override fun get(url: String): ResponseHttp {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(URI.create(url))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return ResponseHttp(response.statusCode(), response.body())
    }
}