package com.raduitache.myanimelist.seasonal

import com.raduitache.myanimelist.responses.Sorting
import kotlinx.coroutines.flow.first
import java.time.Year
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

    fun previous(year: Int): Pair<Season, Int> {
        var returnableYear = year
        return Pair(
            when (this) {
                WINTER -> {
                    returnableYear -= 1
                    FALL
                }

                SPRING -> WINTER
                SUMMER -> SPRING
                FALL -> SUMMER
            }, returnableYear
        )
    }

    companion object {
        fun currentYear() = Year.now(TimeZone.getDefault().toZoneId()).value

        fun current(): Season {
            val currentMonth = YearMonth.now(TimeZone.getDefault().toZoneId()).monthValue

            return when (currentMonth) {
                in 3..5 -> SPRING
                in 6..8 -> SUMMER
                in 9..11 -> FALL
                else -> WINTER
            }
        }

        fun generateLastSeasons(
            howMany: Int,
            currentSeason: Season = current(),
            currentYear: Int = currentYear()
        ): List<Pair<Season, Int>> {
            val last = mutableListOf<Pair<Season, Int>>()
            last += currentSeason.previous(currentYear)
            for (i in (1..<howMany)) {
                last.add(last.last().first.previous(last.last().second))
            }
            return last
        }
    }
}


enum class WatchingState {
    watching,
    completed,
    on_hold,
    dropped,
    plan_to_watch;

    fun next() = try {
        val toTypedArray = WatchingState.entries.toTypedArray()
        val currentIndex = toTypedArray.indexOf(this)
        toTypedArray[currentIndex + 1]
    } catch (_: Exception) {
        watching
    }

}