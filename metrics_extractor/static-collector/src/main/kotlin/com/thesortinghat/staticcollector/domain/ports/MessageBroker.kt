package com.thesortinghat.staticcollector.domain.ports

import com.thesortinghat.staticcollector.domain.events.NewDatabase
import com.thesortinghat.staticcollector.domain.events.NewService
import com.thesortinghat.staticcollector.domain.events.NewSystem
import com.thesortinghat.staticcollector.domain.events.NewUsage

interface MessageBroker {
    fun newSystem(s: NewSystem)
    fun newService(s: NewService)
    fun newDatabase(db: NewDatabase)
    fun newUsage(link: NewUsage)
}
