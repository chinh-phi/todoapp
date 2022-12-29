package com.example.todoapp.epoxy

import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController

abstract class CustomEpoxyController :
    EpoxyController(defaultModelBuildingHandler, EpoxyAsyncUtil.getAsyncBackgroundHandler())