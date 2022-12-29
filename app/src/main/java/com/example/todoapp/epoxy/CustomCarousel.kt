package com.example.todoapp.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CustomCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : Carousel(context, attrs) {

    @set:ModelProp
    var itemWidth: Int = 0

    override fun onChildAttachedToWindow(child: View) {
        check(!(itemWidth > 0 && numViewsToShowOnScreen > 0)) {
            "Can't use itemWidth and numViewsToShowOnScreen together"
        }
        if (itemWidth > 0) {
            val childLayoutParams = child.layoutParams
            childLayoutParams.width = itemWidth
        }
        super.onChildAttachedToWindow(child)
    }

    @JvmOverloads
    @ModelProp
    fun createLayoutManager(
        layoutManager: Pair<String, Int> = Pair(
            LINEAR_VERTICAL,
            SPAN_COUNT_DEFAULT
        )
    ) {
        this.layoutManager = when (layoutManager.first) {
            STAGGERED_GRID_LAYOUT -> StaggeredGridLayoutManager(
                layoutManager.second,
                VERTICAL
            )
            GRID_LAYOUT -> GridLayoutManager(
                context,
                layoutManager.second,
                VERTICAL,
                false
            )
            LINEAR_HORIZONTAL -> LinearLayoutManager(context, HORIZONTAL, false)
            else -> LinearLayoutManager(context, VERTICAL, false)
        }
    }

    override fun getSnapHelperFactory(): SnapHelperFactory? = null

    companion object {
        const val ID = "NrCarousel"
        const val LINEAR_VERTICAL = "LINEAR_VERTICAL"
        const val LINEAR_HORIZONTAL = "LINEAR_HORIZONTAL"
        const val GRID_LAYOUT = "GRID_LAYOUT"
        const val STAGGERED_GRID_LAYOUT = "STAGGERED_GRID_LAYOUT"
        const val SPAN_COUNT_DEFAULT = 1
    }
}

inline fun <T> CustomCarouselModelBuilder.withModelsFrom(
    items: List<T>,
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) })
}