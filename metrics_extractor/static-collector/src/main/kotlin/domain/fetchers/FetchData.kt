package domain.fetchers

interface FetchData {
    fun run(url: String): Response
}