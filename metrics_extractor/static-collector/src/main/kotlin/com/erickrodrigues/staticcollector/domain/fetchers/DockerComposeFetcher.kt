package com.erickrodrigues.staticcollector.domain.fetchers

import com.erickrodrigues.staticcollector.domain.exceptions.UnableToFetchDataException
import com.erickrodrigues.staticcollector.domain.ports.HttpPort
import com.erickrodrigues.staticcollector.domain.vo.FetchResponse

class DockerComposeFetcher(private val httpPort: HttpPort) : DataFetcher {

    override fun run(url: String, filename: String): FetchResponse {
        val (userOrOrgName, repoName, rawContentUrl) = getRemoteRepoData(url, filename)
        val response = httpPort.get(rawContentUrl)

        if (response.status != 200) {
            throw UnableToFetchDataException("[Status ${response.status}]: error while fetching")
        }

        return FetchResponse("$userOrOrgName/$repoName", response.data)
    }

    private fun getRemoteRepoData(url: String, filename: String): RemoteRepoData {
        var matchResult: MatchResult? = null

        listOf("github", "gitlab")
            .forEach {
                val pattern = "(?:https?://)?(?:www\\.)?($it)\\.com/(.+)/(.+)/?".toRegex()
                val match = pattern.matchEntire(url)
                if (match != null && match.groupValues.size >= 4) matchResult = match
            }

        if (matchResult == null) {
            throw UnableToFetchDataException("Given url is invalid")
        }

        val repoType = matchResult!!.groupValues[1]
        val userOrOrgName = matchResult!!.groupValues[2]
        val repoName = matchResult!!.groupValues[3]
        val rawContentUrl =
            if (repoType == "github")
                "https://raw.githubusercontent.com/$userOrOrgName/$repoName/main/$filename"
            else
                "https://gitlab.com/$userOrOrgName/$repoName/raw/master/$filename"

        return RemoteRepoData(
            userOrOrgName = userOrOrgName,
            repoName = repoName,
            rawContentUrl = rawContentUrl
        )
    }

    data class RemoteRepoData(val userOrOrgName: String, val repoName: String, val rawContentUrl: String)
}
