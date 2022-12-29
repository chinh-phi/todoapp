package com.example.todoapp.ui.addtask

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.todoapp.R
import com.example.todoapp.base.BaseBottomSheetDialogFragment
import com.example.todoapp.data.database.Task
import com.example.todoapp.databinding.FragmentAddTaskBottomSheetBinding
import com.example.todoapp.ui.categorylist.CategoryListViewModel
import com.example.todoapp.ui.notification.Notification
import com.example.todoapp.utils.TimeUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.util.Calendar


@AndroidEntryPoint
class AddTaskBottomSheetFragment : BaseBottomSheetDialogFragment<AddTaskViewModel>() {

    private var _binding: FragmentAddTaskBottomSheetBinding? = null
    private val binding: FragmentAddTaskBottomSheetBinding get() = requireNotNull(_binding)

    private val viewModel: AddTaskViewModel by viewModels()
    private val shareViewModel: CategoryListViewModel by activityViewModels()

    override fun provideViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            parentLayout?.let { bottomSheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun setupUI() {
        super.setupUI()
        createNotificationChannel()
        binding.imvAddRight.isEnabled = false
        binding.calendarView.isGone = true
        binding.timeView.isGone = true
        binding.calendarView.minDate = TimeUtil.getCurrentTime()
        binding.imvCloseLeft.setOnClickListener { _ ->
            dismiss()
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
                if (s.isNotEmpty()) {
                    binding.imvAddRight.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    binding.imvAddRight.isEnabled = true
                } else {
                    binding.imvAddRight.setTextColor(ContextCompat.getColor(requireContext(), R.color.black300s))
                    binding.imvAddRight.isEnabled = false
                }
            }
        })
        binding.imvAddRight.setOnClickListener { _ ->
            shareViewModel.insert(Task(
                id = 0,
                title = binding.taskTitle.text.toString(),
                description = binding.taskDescription.text.toString(),
                time = viewModel.convertTimeToTimestamp(viewModel.date!!, viewModel.time!!)
            ))
        }
        binding.switchDate.setOnCheckedChangeListener { button, isChecked ->
            binding.calendarView.isGone = !isChecked
        }
        binding.switchTime.setOnCheckedChangeListener { button, isChecked ->
            binding.timeView.isGone = !isChecked
        }
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            viewModel.date = "$year.${month+1}.$dayOfMonth AD at"
        }
        binding.timeView.setOnTimeChangedListener { picker, hour, minute ->
            viewModel.time = "$hour:$minute:00 +0700"
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.date = "${Calendar.getInstance().get(Calendar.YEAR)}.${Calendar.getInstance().get(Calendar.MONTH)+1}.${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)} AD at"
        viewModel.time = "${Calendar.getInstance().get(Calendar.HOUR_OF_DAY)}:${Calendar.getInstance().get(Calendar.MINUTE)}:00 +0700"
        shareViewModel.taskIdInserted.observe(viewLifecycleOwner) {
            if (it != shareViewModel.currentTaskId.value) {
                shareViewModel.currentTaskId.value = it
                scheduleNotification(it)
                dismiss()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun createNotificationChannel() {
        val name = "Noti channel"
        val desc = "Noti desc"
        val important = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Notification.channelId, name, important)
        channel.description = desc
        val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("NewApi")
    private fun scheduleNotification(taskId: Long) {
        val intent = Intent(activity?.applicationContext, Notification::class.java)
        val title = binding.taskTitle.text.toString()
        val message = binding.taskDescription.text.toString()
        intent.putExtra(Notification.titleExtra, title)
        intent.putExtra(Notification.messageExtra, message)
        intent.putExtra(Notification.taskId, taskId.toString())

        val pendingIntent = PendingIntent.getBroadcast(
            activity?.applicationContext,
            Notification.notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = viewModel.convertTimeToTimestamp(viewModel.date!!, viewModel.time!!)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }
}