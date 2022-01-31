package com.erickrodrigues.staticcollector.domain.ports

import com.erickrodrigues.staticcollector.domain.events.NewDatabase
import com.erickrodrigues.staticcollector.domain.events.NewService
import com.erickrodrigues.staticcollector.domain.events.NewSystem
import com.erickrodrigues.staticcollector.domain.events.NewUsage

interface MessageBroker {
    fun newSystem(s: NewSystem)
    fun newService(s: NewService)
    fun newDatabase(db: NewDatabase)
    fun newUsage(link: NewUsage)
}
