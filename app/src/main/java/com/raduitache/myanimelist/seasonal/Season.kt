package com.raduitache.myanimelist.seasonal

import java.time.YearMonth
import java.util.TimeZone

enum class Season {
    WINTER,
    SPRING,
    SUMMER,
    FALL;

    override fun toString(): String {
        return when (this) {
            WINTER -> "winter"
            SPRING -> "spring"
            SUMMER -> "summer"
            FALL -> "fall"
        }
    }

    companion object {
        fun current(): Season {
            val currentMonth = YearMonth.now(TimeZone.getDefault().toZoneId()).monthValue

            return when (currentMonth) {
                in 3..5 -> SPRING
                in 6..8 -> SUMMER
                in 9..11 -> FALL
                else -> WINTER
            }
        }
    }
}