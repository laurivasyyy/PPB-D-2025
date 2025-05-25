package com.example.ticktick

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ticktick.data.TaskEntity
import com.example.ticktick.viewmodel.TaskViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var editTextTask: EditText
    private lateinit var buttonDate: Button
    private lateinit var buttonTime: Button
    private lateinit var buttonSave: Button

    private var selectedDateTime: LocalDateTime = LocalDateTime.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        supportActionBar?.apply {
            title = getString(R.string.tambah_tugas)
            setDisplayHomeAsUpEnabled(true)
        }

        editTextTask = findViewById(R.id.etTaskName)
        buttonDate = findViewById(R.id.btnPickDate)
        buttonTime = findViewById(R.id.btnPickTime)
        buttonSave = findViewById(R.id.btnSave)

        updateDateButtonText()
        updateTimeButtonText()

        buttonDate.setOnClickListener {
            showDatePicker()
        }

        buttonTime.setOnClickListener {
            showTimePicker()
        }

        buttonSave.setOnClickListener {
            saveTask()
        }
    }

    private fun showDatePicker() {
        val year = selectedDateTime.year
        val month = selectedDateTime.monthValue - 1
        val day = selectedDateTime.dayOfMonth

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDateTime = selectedDateTime.withYear(selectedYear)
                .withMonth(selectedMonth + 1)
                .withDayOfMonth(selectedDay)
            updateDateButtonText()
        }, year, month, day).show()
    }

    private fun showTimePicker() {
        val hour = selectedDateTime.hour
        val minute = selectedDateTime.minute

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedDateTime = selectedDateTime.withHour(selectedHour)
                .withMinute(selectedMinute)
            updateTimeButtonText()
        }, hour, minute, true).show()
    }

    private fun updateDateButtonText() {
        buttonDate.text = selectedDateTime.format(dateFormatter)
    }

    private fun updateTimeButtonText() {
        buttonTime.text = selectedDateTime.format(timeFormatter)
    }

    private fun saveTask() {
        val taskName = editTextTask.text.toString().trim()

        if (taskName.isEmpty()) {
            Toast.makeText(this, getString(R.string.harap_isi_nama_tugas), Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDateTime.isBefore(LocalDateTime.now())) {
            Toast.makeText(this, getString(R.string.tenggat_waktu_tidak_valid), Toast.LENGTH_SHORT).show()
            return
        }

        val task = TaskEntity(
            name = taskName,
            deadline = selectedDateTime
        )

        taskViewModel.insert(task)

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}