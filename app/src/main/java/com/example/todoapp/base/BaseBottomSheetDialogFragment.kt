package com.example.todoapp.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<T : BaseViewModel> :
    BottomSheetDialogFragment(),
    BaseView {

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

}