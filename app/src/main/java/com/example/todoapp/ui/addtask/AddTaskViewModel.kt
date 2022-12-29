package com.example.todoapp.ui.addtask

import com.example.todoapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor() : BaseViewModel() {
    var date: String? = null
    var time: String? = null

    fun convertTimeToTimestamp(date: String, time: String): Long {
        val info = "$date $time"
        val timeInfo = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").parse(info)
        return timeInfo.time
    }
}