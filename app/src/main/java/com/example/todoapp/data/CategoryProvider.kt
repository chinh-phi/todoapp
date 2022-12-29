package com.example.todoapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.example.todoapp.R

enum class CategoryType(
    @CategoryID val categoryId: Int,
    @StringRes val categoryName: Int,
    @DrawableRes val icon: Int? = null
) {
    TODAY(
        CategoryID.TODAY, R.string.type_today, null
    ),
    FUTURE(
        CategoryID.FUTURE, R.string.type_future, R.drawable.icon_calendar
    ),
    ALL(
        CategoryID.ALL, R.string.type_all, R.drawable.icon_all
    ),
    COMPLETED(
        CategoryID.COMPLETED, R.string.type_completed, R.drawable.icon_completed
    )
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    CategoryID.TODAY,
    CategoryID.FUTURE,
    CategoryID.ALL,
    CategoryID.COMPLETED,
)
annotation class CategoryID {
    companion object {
        const val TODAY = 1
        const val FUTURE = 2
        const val ALL = 3
        const val COMPLETED = 4
    }
}