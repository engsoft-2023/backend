package com.sortinghat.backend.domain.behaviors

import com.sortinghat.backend.domain.model.*

interface Visitor {
    fun visit(s: Service)
    fun visit(db: Database)
    fun visit(op: Operation)
    fun visit(ch: MessageChannel)
    fun visit(module: Module)
    fun visit(system: ServiceBasedSystem)
}
