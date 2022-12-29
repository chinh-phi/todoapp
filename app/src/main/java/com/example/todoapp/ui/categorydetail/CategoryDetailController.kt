package com.example.todoapp.ui.categorydetail

import com.airbnb.epoxy.Carousel
import com.example.todoapp.R
import com.example.todoapp.data.database.Task
import com.example.todoapp.epoxy.CustomCarousel
import com.example.todoapp.epoxy.CustomEpoxyController
import com.example.todoapp.epoxy.customCarousel
import com.example.todoapp.epoxy.withModelsFrom
import com.example.todoapp.ui.categorydetail.model.TaskItemView
import com.example.todoapp.ui.categorydetail.model.TaskItemViewModel_
import com.example.todoapp.ui.categorydetail.model.TaskTypeHeader
import com.example.todoapp.ui.categorydetail.model.taskItemView
import com.example.todoapp.ui.categorydetail.model.taskTypeHeader
import com.example.todoapp.ui.categorylist.model.TaskTypeView
import com.example.todoapp.ui.categorylist.model.TaskTypeViewModel_
import com.example.todoapp.utils.observable
import javax.inject.Inject

class CategoryDetailController @Inject constructor() : CustomEpoxyController() {

    var callback: Callback? = null
    var title: String? by observable(null, ::requestModelBuild)
    var listTask: List<Task> by observable(emptyList(), ::requestModelBuild)
    override fun buildModels() {

        taskTypeHeader {
            id(TaskTypeHeader.ID)
            title(this@CategoryDetailController.title)
            clickListener { _ ->
                this@CategoryDetailController.callback?.clearAllTask()
            }
        }

        if (listTask.isNotEmpty()) {
            customCarousel {
                id(CustomCarousel.ID)
                createLayoutManager(Pair(CustomCarousel.LINEAR_VERTICAL, 1))
                withModelsFrom(this@CategoryDetailController.listTask) { task ->
                    TaskItemViewModel_().apply {
                        id(TaskItemView.ID, task.id.toString())
                        task(task)
                        clickListener { _ ->
                            this@CategoryDetailController.callback?.clickTask(task.id)
                        }
                    }
                }
                padding(
                    Carousel.Padding.resource(
                        R.dimen.dp16,
                        R.dimen.dp16,
                        R.dimen.dp16,
                        R.dimen.dp16,
                        R.dimen.dp16
                    )
                )
            }
        }
    }

    interface Callback {
        fun clearAllTask()
        fun clickTask(taskId: Int)
    }
}