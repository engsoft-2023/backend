package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.payloads.ServicesEndpointsRegistrationPayload

interface RegisterServicesEndpoints {
    fun execute(systemName: String, payload: ServicesEndpointsRegistrationPayload)
}
