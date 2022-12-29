package com.example.todoapp.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : BaseViewModel> : Fragment(), BaseView {

    abstract fun provideViewModel(): T

    override fun provideContext(): Context? = context

    override fun provideRootView(): View? = view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupViewModel()
    }

    override fun setupViewModel() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (!isAdded) setTargetFragment(null, -1)
        super.onSaveInstanceState(outState)
    }

}