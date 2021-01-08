package com.asmatullah.spaceapp.common.core.locale

import java.io.Serializable

data class Language(val name: String = "", val code: String = "") : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other.javaClass != javaClass) return false

        other as Language
        return this.name == other.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + code.hashCode()
        return result
    }
}