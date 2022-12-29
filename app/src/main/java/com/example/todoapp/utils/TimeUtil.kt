package com.example.todoapp.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar


object TimeUtil {

    const val miliSecondsOfDay = 86400000


    fun getCurrentDayOfMonth(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentTime(): Long {
        return Calendar.getInstance().timeInMillis
    }

    fun getCurrentStartTimeOfDay(): Long {
        return SimpleDateFormat("yyyy/MM/dd").parse(
            "${Calendar.getInstance().get(Calendar.YEAR)}/${Calendar.getInstance().get(Calendar.MONTH)+1}/${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}"
        ).time
    }

    fun getCurrentEndTimeOfDay(): Long {
        return getCurrentStartTimeOfDay() + miliSecondsOfDay
    }

    fun convertTimestampToDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(timestamp))
    }

    fun dpToPx(context: Context, valueInDp: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
    }

}