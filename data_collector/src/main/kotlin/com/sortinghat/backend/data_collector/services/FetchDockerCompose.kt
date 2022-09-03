package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.exceptions.UnableToFetchDataException
import com.sortinghat.backend.data_collector.utils.HttpAbstraction
import com.sortinghat.backend.data_collector.utils.FetchResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FetchDockerCompose(@Autowired private val httpAbstraction: HttpAbstraction<String>) : DataFetcher {

    override fun execute(url: String, filename: String): FetchResponse {
        val info = getRemoteRepoInfo(url, filename)
        val response = httpAbstraction.get(info.rawContentUrl)

        if (response.status != 200) {
            throw UnableToFetchDataException("status ${response.status}: error while fetching")
        }

        return FetchResponse(info.repoName, response.data)
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
            repoName,
            rawContentUrl
        )
    }

    private class RemoteRepoInfo(val repoName: String, val rawContentUrl: String)
}
