package com.erickrodrigues.staticcollector.domain.fetchers

import com.erickrodrigues.staticcollector.domain.exceptions.UnableToFetchDataException
import com.erickrodrigues.staticcollector.domain.http.HttpPort

class DockerComposeFetcher(private val httpPort: HttpPort) : DataFetcher {

    override fun run(url: String): FetchResponse {
        val pattern = "(?:https?://)?(?:www\\.)?github\\.com/(.+)/(.+)/?".toRegex()
        val matchResult = pattern.matchEntire(url)

        if (matchResult == null || matchResult.groupValues.size < 3) {
            throw UnableToFetchDataException("Given url is invalid")
        }

        val userOrOrgName = matchResult.groupValues[1]
        val repoName = matchResult.groupValues[2]

        val rawUrl = "https://raw.githubusercontent.com/${userOrOrgName}/${repoName}/main/docker-compose.yaml"
        val response = httpPort.get(rawUrl)

        if (response.status != 200) {
            throw UnableToFetchDataException("[Status ${response.status}]: error while fetching")
        }

        return FetchResponse(repoName, response.data)
    }
}