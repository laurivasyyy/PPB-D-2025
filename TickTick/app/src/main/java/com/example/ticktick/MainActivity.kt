package com.example.ticktick

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktick.adapter.TaskAdapter
import com.example.ticktick.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var emptyView: TextView

    private var sortByStatus: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvTasks)
        taskAdapter = TaskAdapter { task ->
            taskViewModel.toggleTaskStatus(task)
        }

        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up ViewModel
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        // Set up empty view
        emptyView = findViewById(R.id.tvEmptyState)

        // Observe LiveData with sorting
        updateTaskList()

        // Set up FAB
        val fab = findViewById<FloatingActionButton>(R.id.fabAddTask)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateTaskList() {
        if (sortByStatus) {
            taskViewModel.allTasksByStatus.observe(this) { tasks ->
                updateUI(tasks)
            }
        } else {
            taskViewModel.allTasksByDeadline.observe(this) { tasks ->
                updateUI(tasks)
            }
        }
    }

    private fun updateUI(tasks: List<com.example.ticktick.data.TaskEntity>) {
        if (tasks.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
        taskAdapter.submitList(tasks)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort -> {
                sortByStatus = !sortByStatus
                updateTaskList()
                val sortText = if (sortByStatus) R.string.diurutkan_berdasarkan_status else R.string.diurutkan_berdasarkan_tenggat
                item.title = getString(sortText)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}