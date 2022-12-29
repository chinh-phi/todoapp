package com.example.todoapp.ui.categorylist

import com.airbnb.epoxy.Carousel
import com.example.todoapp.R
import com.example.todoapp.data.database.Task
import com.example.todoapp.epoxy.CustomCarousel
import com.example.todoapp.epoxy.CustomEpoxyController
import com.example.todoapp.epoxy.customCarousel
import com.example.todoapp.epoxy.withModelsFrom
import com.example.todoapp.model.TaskType
import com.example.todoapp.ui.categorydetail.model.TaskItemView
import com.example.todoapp.ui.categorydetail.model.TaskItemViewModel_
import com.example.todoapp.ui.categorylist.model.TaskTypeView
import com.example.todoapp.ui.categorylist.model.TaskTypeViewModel_
import com.example.todoapp.utils.observable
import javax.inject.Inject

class CategoryListController @Inject constructor() : CustomEpoxyController() {

    var callBack: Callback? = null

    var listCategories: List<TaskType> by observable(emptyList(), this::requestModelBuild)

    var searchResult: List<Task> by observable(emptyList(), this::requestModelBuild)

    override fun buildModels() {

        if (searchResult.isNotEmpty()) {
            customCarousel {
                id(CustomCarousel.ID)
                createLayoutManager(Pair(CustomCarousel.LINEAR_VERTICAL, 1))
                withModelsFrom(this@CategoryListController.searchResult) { task ->
                    TaskItemViewModel_().apply {
                        id(TaskItemView.ID, task.id.toString())
                        task(task)
                        clickListener { _ ->
                            this@CategoryListController.callBack?.onItemSearchResultClick(task.id)
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
            return
        } else {
            if (listCategories.isNotEmpty()) {
                customCarousel {
                    id(CustomCarousel.ID)
                    createLayoutManager(Pair(CustomCarousel.GRID_LAYOUT, 2))
                    withModelsFrom(this@CategoryListController.listCategories) { category ->
                        TaskTypeViewModel_().apply {
                            id(TaskTypeView.ID, category.id.toString())
                            taskTypeInfo(category)
                            clickListener { _ ->
                                this@CategoryListController.callBack?.onCategoryClick(category)
                            }
                        }
                    }
                    padding(
                        Carousel.Padding.resource(
                            R.dimen.dp16,
                            R.dimen.dp16,
                            R.dimen.dp16,
                            R.dimen.dp8,
                            R.dimen.dp16
                        )
                    )
                }
            }
        }

    }

    interface Callback {
        fun onCategoryClick(category: TaskType)
        fun onItemSearchResultClick(taskId: Int)
    }
}