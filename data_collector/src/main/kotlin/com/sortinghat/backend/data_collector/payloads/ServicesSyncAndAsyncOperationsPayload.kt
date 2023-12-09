package com.sortinghat.backend.data_collector.payloads

data class ServicesSyncAndAsyncOperationsPayload(
    val operations: Map<String, SyncAndAsyncOperations>
)

data class SyncAndAsyncOperations(
    val synchronous:  Map<String, String>,
    val asynchronous: Map<String, String>
)
