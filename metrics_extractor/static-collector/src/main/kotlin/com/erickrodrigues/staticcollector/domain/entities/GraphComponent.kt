package com.erickrodrigues.staticcollector.domain.entities

import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

abstract class GraphComponent {
    override fun toString(): String {
        val fields = this::class.memberProperties
            .filter { member -> member.visibility == KVisibility.PUBLIC }
            .joinToString(", ") { member -> "${member.name}=${member.getter.call(this)}" }
        return "${this::class.simpleName}($fields)"
    }
}