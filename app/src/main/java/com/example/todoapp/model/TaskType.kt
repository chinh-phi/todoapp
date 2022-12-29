package com.example.todoapp.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.example.todoapp.R
import com.example.todoapp.utils.TimeUtil
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TaskType(
    val id: Int,
    @StringRes val title: Int,
    @DrawableRes val icon: Int? = null,
    val day: Int? = null,
    val totalTask: Int? = null
) : Parcelable