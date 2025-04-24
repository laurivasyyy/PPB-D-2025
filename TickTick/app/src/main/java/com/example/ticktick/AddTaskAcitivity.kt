package com.example.ticktick

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val taskNameInput: EditText = findViewById(R.id.taskNameInput)
        val deadlineInput: EditText = findViewById(R.id.deadlineInput)
        val saveButton: Button = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val name = taskNameInput.text.toString()
            val deadline = deadlineInput.text.toString()

            if (name.isBlank() || deadline.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent()
                intent.putExtra("TASK_NAME", name)
                intent.putExtra("TASK_DEADLINE", deadline)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}