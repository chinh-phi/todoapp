package com.example.todoapp.base

import android.content.Context
import android.view.View

interface BaseView {

    fun provideRootView(): View?

    fun provideContext(): Context?

    fun setupUI() {
        provideRootView()?.setOnClickListener(null)
    }

    fun setupViewModel()

}