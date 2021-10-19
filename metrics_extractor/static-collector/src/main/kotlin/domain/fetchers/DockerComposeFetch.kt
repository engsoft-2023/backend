package domain.fetchers

import domain.adapters.HTTPAdapter

class DockerComposeFetch(private val httpAdapter: HTTPAdapter) : FetchData {

    override fun run(url: String): Response {
        // do additional logic for fetching data
        return httpAdapter.get(url)
    }
}