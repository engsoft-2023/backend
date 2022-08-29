package com.sortinghat.backend.domain.behaviors

interface Visitable {
    fun accept(v: Visitor)

    fun children(): Iterable<Visitable>
}
