package com.sortinghat.backend.data_collector.utils

import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class HttpHandler: HttpAbstraction<String> {

    override fun get(url: String): ResponseHttp<String> {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return ResponseHttp(response.statusCode(), response.body())
    }
}
