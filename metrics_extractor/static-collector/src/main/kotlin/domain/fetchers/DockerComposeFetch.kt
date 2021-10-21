package domain.fetchers

import domain.adapters.HTTPAdapter
import domain.exceptions.UnableToFetchDataException

class DockerComposeFetch(private val httpAdapter: HTTPAdapter) : FetchData {

    override fun run(url: String): String {
        val pattern = "(?:https?://)?(?:www\\.)?github\\.com/(.+)/(.+)/?".toRegex()
        val matchResult = pattern.matchEntire(url)

        if (matchResult == null || matchResult.groupValues.size < 3) {
            throw UnableToFetchDataException("Given url is invalid")
        }

        val userOrOrgName = matchResult.groupValues[1]
        val repoName = matchResult.groupValues[2]

        val rawUrl = "https://raw.githubusercontent.com/${userOrOrgName}/${repoName}/main/docker-compose.yaml"
        val response = httpAdapter.get(rawUrl)

        if (response.status != 200) {
            throw UnableToFetchDataException("[Status ${response.status}]: error while fetching")
        }

        return response.data
    }
}