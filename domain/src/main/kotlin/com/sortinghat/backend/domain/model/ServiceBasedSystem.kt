package com.sortinghat.backend.domain.model

import com.sortinghat.backend.domain.behaviors.Visitable
import com.sortinghat.backend.domain.behaviors.Visitor

data class ServiceBasedSystem(val name: String, val description: String): Visitable {

    override fun accept(v: Visitor) {
        v.visit(this)
    }

    override fun children() = emptySet<Visitable>()
}
