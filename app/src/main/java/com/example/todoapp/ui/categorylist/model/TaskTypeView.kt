package com.example.todoapp.ui.categorylist.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.todoapp.R
import com.example.todoapp.data.CategoryID
import com.example.todoapp.databinding.EpoxyTaskTypeViewBinding
import com.example.todoapp.model.TaskType


@ModelView(
    saveViewState = true,
    autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT,
    fullSpan = false
)
class TaskTypeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    styleDef: Int = 0
) : ConstraintLayout(context, attrs, styleDef) {

    private val binding = EpoxyTaskTypeViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        setBackgroundResource(R.drawable.bg_border_16)
    }

    @set:ModelProp
    var taskTypeInfo: TaskType? = null

    @set:CallbackProp
    var clickListener: OnClickListener? = null

    @AfterPropsSet
    fun setData() {
        taskTypeInfo?.let {
            binding.taskTypeTitle.text = context.getString(it.title)
            binding.totalTask.text = it.totalTask.toString()
            when (it.id) {
                CategoryID.TODAY -> {
                    val name = "icon_calendar_${it.day}"
                    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
                    val drawable = context.resources.getDrawable(id)
                    binding.icTaskType.setImageDrawable(drawable)
                }
                else -> {
                    binding.icTaskType.setImageResource(it.icon!!)
                }
            }
        }
        binding.root.setOnClickListener(clickListener)
    }

    companion object {
        const val ID = "task_type_id"
    }

}