package com.example.ticktick

import android.os.Bundle
import android.content.Intent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(this, tasks) { position ->
            tasks.removeAt(position)
            taskAdapter.notifyItemRemoved(position)
        }
        recyclerView.adapter = taskAdapter

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val name = data?.getStringExtra("TASK_NAME") ?: return@registerForActivityResult
                val deadline = data?.getStringExtra("TASK_DEADLINE") ?: return@registerForActivityResult
                val task = Task(name, deadline)
                tasks.add(task)
                taskAdapter.notifyItemInserted(tasks.size - 1)
            }
        }

        val addButton: ImageButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            resultLauncher.launch(intent)
        }
    }
}