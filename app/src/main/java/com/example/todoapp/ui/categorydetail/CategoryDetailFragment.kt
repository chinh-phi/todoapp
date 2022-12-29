package com.example.todoapp.ui.categorydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.IntentActivity
import com.example.todoapp.R
import com.example.todoapp.base.BaseFragment
import com.example.todoapp.data.CategoryType
import com.example.todoapp.databinding.FragmentCategoryDetailBinding
import com.example.todoapp.model.TaskType
import com.example.todoapp.ui.addtask.AddTaskBottomSheetFragment
import com.example.todoapp.ui.categorylist.CategoryListViewModel
import com.example.todoapp.ui.notification.Notification
import com.example.todoapp.ui.taskdetail.TaskDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryDetailFragment : BaseFragment<CategoryDetailViewModel>(), CategoryDetailController.Callback {

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding: FragmentCategoryDetailBinding get() = requireNotNull(_binding)

    private val viewModel: CategoryDetailViewModel by viewModels()

    private val sharedViewModel: CategoryListViewModel by activityViewModels()

    private val category: TaskType?
        get() = arguments?.getParcelable("category")

    @Inject
    lateinit var controller: CategoryDetailController

    override fun provideViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        super.setupUI()
        (activity as? AppCompatActivity)?.apply { setSupportActionBar(binding.toolBar) }

        controller.title = context?.getString(category?.title!!)
        binding.rvHome.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setController(controller)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    updateToolBar(recyclerView)
                }
            })
        }
        binding.toolBar.setNavigationOnClickListener { _ ->
            findNavController().popBackStack()
        }

        binding.addTaskButton.setOnClickListener { _ ->
            AddTaskBottomSheetFragment().show(childFragmentManager, "")
        }

        controller.callback = this@CategoryDetailFragment
    }

    override fun setupViewModel() {
        super.setupViewModel()

        sharedViewModel.state.observe(viewLifecycleOwner) {
            controller.listTask = when (category?.id!!) {
                CategoryType.TODAY.categoryId -> it.listTaskToday.orEmpty()
                CategoryType.ALL.categoryId -> it.listTaskAll.orEmpty()
                CategoryType.FUTURE.categoryId -> it.listTaskFuture.orEmpty()
                CategoryType.COMPLETED.categoryId -> it.listTaskCompleted.orEmpty()
                else -> emptyList()
            }
        }
    }

    private fun updateToolBar(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val isFirstItemVisible = layoutManager.findFirstCompletelyVisibleItemPosition() == 0
        (activity as AppCompatActivity?)!!.supportActionBar?.title = if (isFirstItemVisible) "Danh sách" else context?.getString(category?.title!!)
    }

    override fun clearAllTask() {
        showConfirmDialog()
    }

    override fun clickTask(taskId: Int) {
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

    private fun showConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Bạn có muốn xóa tất cả lời nhắc?")
            .setPositiveButton("Chắc chắn") { _, _ ->
                sharedViewModel.deleteAllTask()
            }
            .setNegativeButton("Hủy") { _, _ ->

            }
            .show()
    }
}