package domain.adapters

import domain.fetchers.Response

interface HTTPAdapter {
    fun get(url: String): Response
}