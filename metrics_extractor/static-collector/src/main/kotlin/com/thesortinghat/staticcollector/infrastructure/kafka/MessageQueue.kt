package com.thesortinghat.staticcollector.infrastructure.kafka

interface MessageQueue {
    fun newSystem(s: NewSystem)
    fun newService(s: NewService)
    fun newDatabase(db: NewDatabase)
    fun newUsage(link: NewUsage)
}
