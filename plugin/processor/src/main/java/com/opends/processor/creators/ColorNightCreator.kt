package com.opends.processor.creators

class ColorNightCreator {

    fun colorRefToLight(ref: String) = OPEN_COLOR_LOCATION + ref + COLOR_INSTANCE_MODIFIER_LIGHT

    fun colorRefToDark(ref: String) = OPEN_COLOR_LOCATION + ref + COLOR_INSTANCE_MODIFIER_DARK

    fun getLightColorModifierName() = COLOR_INSTANCE_MODIFIER_LIGHT

    fun getDarkColorModifierName() = COLOR_INSTANCE_MODIFIER_DARK

    private companion object {
        private const val OPEN_COLOR_LOCATION = "com.opends.color."
        private const val COLOR_INSTANCE_MODIFIER_LIGHT = "Light"
        private const val COLOR_INSTANCE_MODIFIER_DARK = "Dark"
    }
}
