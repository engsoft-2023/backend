package com.thesortinghat.staticcollector.domain.fetchers

import com.thesortinghat.staticcollector.domain.exceptions.UnableToFetchDataException
import com.thesortinghat.staticcollector.domain.ports.HttpPort
import com.thesortinghat.staticcollector.domain.vo.FetchResponse

class DockerComposeFetcher(private val httpPort: HttpPort) : DataFetcher {

    override fun run(url: String, filename: String): FetchResponse {
        val (userOrOrgName, repoName, rawContentUrl) = getRemoteRepoInfo(url, filename)
        val response = httpPort.get(rawContentUrl)

        if (response.status != 200) {
            throw UnableToFetchDataException("status ${response.status}: error while fetching")
        }

        return FetchResponse("$userOrOrgName/$repoName", response.data)
    }

    private fun getRemoteRepoInfo(url: String, filename: String): RemoteRepoInfo {
        val matchResult = listOf("github", "gitlab")
                .map { "(?:https?://)?(?:www\\.)?($it)\\.com/(.+)/(.+)/?".toRegex().matchEntire(url) }
                .find { it != null && it.groupValues.size >= 4 }
            ?: throw UnableToFetchDataException("given url is invalid")

        val repoType = matchResult.groupValues[1]
        val userOrOrgName = matchResult.groupValues[2]
        val repoName = matchResult.groupValues[3]
        val rawContentUrl = hashMapOf(
                "github" to "https://raw.githubusercontent.com/$userOrOrgName/$repoName/main/$filename",
                "gitlab" to "https://gitlab.com/$userOrOrgName/$repoName/raw/master/$filename"
        )[repoType]!!

        return RemoteRepoInfo(
            userOrOrgName = userOrOrgName,
            repoName = repoName,
            rawContentUrl = rawContentUrl
        )
    }

    data class RemoteRepoInfo(val userOrOrgName: String, val repoName: String, val rawContentUrl: String)
}
