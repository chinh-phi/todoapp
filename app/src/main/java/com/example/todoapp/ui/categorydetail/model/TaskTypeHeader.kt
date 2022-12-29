package com.example.todoapp.ui.categorydetail.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.todoapp.databinding.EpoxyTaskTypeHeaderBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TaskTypeHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    styleDef: Int = 0
) : ConstraintLayout(context, attrs, styleDef) {
    private val binding = EpoxyTaskTypeHeaderBinding.inflate(LayoutInflater.from(context), this)

    @set:ModelProp
    var title: String? = null

    @set:CallbackProp
    var clickListener: OnClickListener? = null

    @AfterPropsSet
    fun build() {
        binding.title.text = title
        binding.icClear.setOnClickListener(clickListener)
    }

    companion object {
        const val ID = "task_type_header"
    }
}