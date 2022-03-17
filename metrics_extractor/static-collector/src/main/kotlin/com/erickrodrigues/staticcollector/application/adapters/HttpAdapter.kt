package com.erickrodrigues.staticcollector.application.adapters

import com.erickrodrigues.staticcollector.domain.http.HttpPort
import com.erickrodrigues.staticcollector.domain.vo.ResponseHttp
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