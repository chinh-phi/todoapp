package com.example.todoapp.ui.categorydetail.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.todoapp.R
import com.example.todoapp.data.database.Task
import com.example.todoapp.databinding.EpoxyTaskItemViewBinding
import com.example.todoapp.utils.TimeUtil
import java.sql.Date
import java.text.SimpleDateFormat

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TaskItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    styleDef: Int = 0
) : ConstraintLayout(context, attrs, styleDef) {
    private val binding = EpoxyTaskItemViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        setBackgroundResource(R.drawable.bg_border_16)
    }

    @set:ModelProp
    var task: Task? = null

    @set:CallbackProp
    var clickListener: OnClickListener? = null

    @SuppressLint("NewApi")
    @AfterPropsSet
    fun build() {
        binding.taskTitle.text = task?.title
        binding.taskDescription.text = task?.description
        binding.taskTime.text = TimeUtil.convertTimestampToDate(task?.time!!)
        binding.root.setOnClickListener(clickListener)
    }

    companion object {
        const val ID = "task_item_view"
    }
}