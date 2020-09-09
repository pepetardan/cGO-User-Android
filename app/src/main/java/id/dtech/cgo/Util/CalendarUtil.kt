package id.dtech.cgo.Util

import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.util.*

class CalendarUtil {
    companion object{
        fun daysOfWeekFromLocale() : Array<DayOfWeek> {
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            var daysOfWeek = DayOfWeek.values()

            if (firstDayOfWeek != DayOfWeek.MONDAY) {
                val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
                val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
                daysOfWeek = rhs + lhs
            }
            return daysOfWeek
        }
    }
}