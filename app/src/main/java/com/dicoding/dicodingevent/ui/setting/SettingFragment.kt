package com.dicoding.dicodingevent.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dicoding.dicodingevent.DailyReminderWorker
import com.dicoding.dicodingevent.R
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment(R.layout.fragment_setting) {

    private lateinit var viewModel: SettingViewModel
    private lateinit var switchDailyReminder: SwitchMaterial

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SettingViewModel::class.java]

        val layoutAppearance = view.findViewById<LinearLayout>(R.id.layout_appearance)
        val subTitle = view.findViewById<TextView>(R.id.tv_appearance_subtitle)
        switchDailyReminder = view.findViewById(R.id.switch_daily_reminder)

        viewModel.themeChoice.observe(viewLifecycleOwner) { theme ->
            subTitle.text = when (theme) {
                0 -> "System device theme"
                1 -> "Light Theme"
                2 -> "Dark Theme"
                else -> "System device theme"
            }
        }

        viewModel.isDailyReminderEnabled.observe(viewLifecycleOwner) { isEnabled ->
            switchDailyReminder.isChecked = isEnabled
        }

        layoutAppearance.setOnClickListener {
            showAppearanceDialog()
        }

        switchDailyReminder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDailyReminderState(isChecked)
            if (isChecked) {
                startDailyReminderWorker()
            } else {
                cancelDailyReminderWorker()
            }
        }
    }

    private fun showAppearanceDialog() {
        val options = arrayOf("Use device Theme", "Light theme", "Dark Theme")
        val checkedItem = viewModel.themeChoice.value ?: 0

        AlertDialog.Builder(requireContext())
            .setTitle("Choose Appearance")
            .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                viewModel.setThemeChoice(which)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startDailyReminderWorker() {
        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS,
            15, TimeUnit.MINUTES )
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "daily_reminder_worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun cancelDailyReminderWorker() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork("daily_reminder_worker")
    }
}
