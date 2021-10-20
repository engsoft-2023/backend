package domain.adapters

interface HTTPAdapter {
    fun get(url: String): HTTPResponse
}