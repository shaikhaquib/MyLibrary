package com.chanakya.testview.shadow

import java.io.Serializable

class ShadowProperty : Serializable {
    var shadowColor = 0
        private set
    var shadowRadius = 0
        private set
    var shadowDx = 0
        private set
    var shadowDy = 0
        private set
    var shadowSide = ALL
        private set

    fun setShadowSide(shadowSide: Int): ShadowProperty {
        this.shadowSide = shadowSide
        return this
    }

    val shadowOffset: Int
        get() = shadowOffsetHalf * 2
    val shadowOffsetHalf: Int
        get() = if (0 >= shadowRadius) 0 else Math.max(shadowDx, shadowDy) + shadowRadius

    fun setShadowColor(shadowColor: Int): ShadowProperty {
        this.shadowColor = shadowColor
        return this
    }

    fun setShadowRadius(shadowRadius: Int): ShadowProperty {
        this.shadowRadius = shadowRadius
        return this
    }

    fun setShadowDx(shadowDx: Int): ShadowProperty {
        this.shadowDx = shadowDx
        return this
    }

    fun setShadowDy(shadowDy: Int): ShadowProperty {
        this.shadowDy = shadowDy
        return this
    }

    companion object {
        const val ALL = 0x1111
        const val LEFT = 0x0001
        const val TOP = 0x0010
        const val RIGHT = 0x0100
        const val BOTTOM = 0x1000
    }
}