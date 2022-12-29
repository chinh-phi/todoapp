package com.example.todoapp.ui.taskdetail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.todoapp.base.BaseFragment
import com.example.todoapp.data.database.Task
import com.example.todoapp.databinding.FragmentTaskDetailBinding
import com.example.todoapp.ui.notification.Notification
import com.example.todoapp.utils.TimeUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class TaskDetailFragment : BaseFragment<TaskDetailViewModel>() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding: FragmentTaskDetailBinding get() = requireNotNull(_binding)

    private val viewModel: TaskDetailViewModel by viewModels()

    private val taskId: String?
        get() = arguments?.getString(Notification.taskId)

    private var isChangedTitle: Boolean = false
    private var isChangedDescription: Boolean = false
    private var isChangedTime: Boolean = false

    private var time: Long? = null

    override fun provideViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        binding.calendarView.minDate = TimeUtil.getCurrentTime()

        (activity as? AppCompatActivity)?.apply { setSupportActionBar(binding.toolBar) }

        binding.toolBar.setNavigationOnClickListener { _ ->
            activity?.onBackPressed()
        }

        binding.taskTitle.addTextChangedListener(object : TextWatcher {
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
                if (viewModel.taskTitle != null) {
                    isChangedTitle = s.toString() != viewModel.taskTitle
                    binding.saveButton.isVisible = isChangedTitle || isChangedDescription || isChangedTime
                }
            }
        })

        binding.taskDescription.addTextChangedListener(object : TextWatcher {
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
                if (viewModel.taskDescription != null) {
                    isChangedDescription = s.toString() != viewModel.taskDescription
                    binding.saveButton.isVisible = isChangedTitle || isChangedDescription || isChangedTime
                }
            }
        })

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val hour = binding.timeView.currentHour
            val minute = binding.timeView.currentMinute

            val currentTime = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").parse(
                "$year.${month+1}.$dayOfMonth AD at $hour:$minute:00 +0700"
            ).time

            time = currentTime

            if (viewModel.taskTime != null) {
                isChangedTime = viewModel.taskTime != currentTime
                binding.saveButton.isVisible = isChangedTitle || isChangedDescription || isChangedTime
            }

        }
        binding.timeView.setOnTimeChangedListener { picker, hour, minute ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = binding.calendarView.date
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val currentTime = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").parse(
                "$year.${month+1}.$day AD at $hour:$minute:00 +0700"
            ).time

            time = currentTime

            if (viewModel.taskTime != null) {
                isChangedTime = viewModel.taskTime != currentTime
                binding.saveButton.isVisible = isChangedTitle || isChangedDescription || isChangedTime
            }
        }

        binding.saveButton.setOnClickListener { _ ->
            viewModel.updateTask(
                Task(
                    id = taskId?.toInt()!!,
                    title = binding.taskTitle.text.toString(),
                    description = binding.taskDescription.text.toString(),
                    time = time
                )
            )
        }

        binding.icDelete.setOnClickListener { _ ->
            showConfirmDialog()
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.getTaskInfo(taskId!!.toInt())

        viewModel.taskInfo.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.taskTitle.setText(it.title)
                binding.taskDescription.setText(it.description)
                binding.calendarView.date = it.time!!
                binding.timeView.currentHour = TimeUtil.convertTimestampToDate(it.time).substring(11, 13).toInt()
                binding.timeView.currentMinute = TimeUtil.convertTimestampToDate(it.time).substring(14).toInt()

                viewModel.taskTitle = it.title
                viewModel.taskDescription = it.description
                viewModel.taskTime = it.time

                time = it.time
            }
        }

        viewModel.updateStatus.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        }

        viewModel.deleteStatus.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        }
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Bạn có muốn xóa lời nhắc này?")
            .setPositiveButton("Chắc chắn") { _, _ ->
                viewModel.deleteTask(taskId?.toInt()!!)
            }
            .setNegativeButton("Hủy") { _, _ ->

            }
            .show()
    }

}