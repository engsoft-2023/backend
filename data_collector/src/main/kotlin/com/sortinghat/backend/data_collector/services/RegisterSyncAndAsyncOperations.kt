package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.payloads.ServicesSyncAndAsyncOperationsPayload

interface RegisterSyncAndAsyncOperations {
    fun execute(systemName: String, payload: ServicesSyncAndAsyncOperationsPayload)
}
