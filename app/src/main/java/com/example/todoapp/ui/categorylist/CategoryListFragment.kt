package com.example.todoapp.ui.categorylist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.IntentActivity
import com.example.todoapp.R
import com.example.todoapp.base.BaseFragment
import com.example.todoapp.databinding.FragmentCategoryListBinding
import com.example.todoapp.model.TaskType
import com.example.todoapp.ui.addtask.AddTaskBottomSheetFragment
import com.example.todoapp.ui.taskdetail.TaskDetailFragment
import com.example.todoapp.utils.TimeUtil.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CategoryListFragment : BaseFragment<CategoryListViewModel>(),
    CategoryListController.Callback {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: CategoryListViewModel by activityViewModels()

    @Inject
    lateinit var controller: CategoryListController

    override fun provideViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        binding.rootView.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
            val heightDiff: Int = binding.rootView.rootView.height - binding.rootView.height
            binding.addTaskButton.isVisible = heightDiff <= dpToPx(requireContext(), 200f)
        })

        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setController(controller)
        }

        binding.addTaskButton.setOnClickListener { _ ->
            AddTaskBottomSheetFragment().show(childFragmentManager, "")
        }

        binding.searchBar.setOnTouchListener{ v, event ->
            binding.cancelSearchBtn.isVisible = true
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.cancelSearchBtn.setOnClickListener { view ->
            binding.searchBar.setText("")
            binding.cancelSearchBtn.isGone = true
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    viewModel.doSearch(s.toString())
                } else {
                    controller.searchResult = emptyList()
                }
            }
        })

        controller.callBack = this@CategoryListFragment
    }

    override fun setupViewModel() {
        super.setupViewModel()

        viewModel.state.observe(viewLifecycleOwner) {
            controller.listCategories = it.categoryList
        }

        viewModel.searchResult.observe(viewLifecycleOwner) {
            controller.searchResult = it
        }
    }

    override fun onCategoryClick(category: TaskType) {
        findNavController().navigate(
            R.id.action_categoryListFragment_to_categoryDetailFragment, bundleOf(
                "category" to category
            )
        )
    }

    override fun onItemSearchResultClick(taskId: Int) {
        startActivity(
            IntentActivity.getIntent(
                context,
                TaskDetailFragment::class.java,
                args = bundleOf(
                    "taskId" to taskId.toString()
                )
            )
        )
    }
}