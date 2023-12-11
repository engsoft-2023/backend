package com.sortinghat.backend.data_collector.payloads

data class ServicesSyncAndAsyncOperationsPayload(
    val operations: Map<String, SyncAndAsyncOperations>
)

data class SyncAndAsyncOperations(
    val synchronous:  Map<String, Set<String>>,
    val asynchronous: Map<String, Set<String>>
)
