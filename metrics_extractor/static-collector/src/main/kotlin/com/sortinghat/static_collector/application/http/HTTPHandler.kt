package com.sortinghat.static_collector.application.http

import com.sortinghat.static_collector.domain.ports.HTTPPort
import com.sortinghat.static_collector.domain.responses.HTTPResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HTTPHandler : HTTPPort {
    override fun get(url: String): HTTPResponse {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(URI.create(url))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return HTTPResponse(response.statusCode(), response.body())
    }
}