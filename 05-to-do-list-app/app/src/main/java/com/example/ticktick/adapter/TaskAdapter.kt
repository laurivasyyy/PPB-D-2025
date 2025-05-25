package com.example.ticktick.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktick.R
import com.example.ticktick.data.TaskEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskAdapter(private val onTaskCheckedChangeListener: (TaskEntity) -> Unit) :
    ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask, onTaskCheckedChangeListener)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.cbCompleted)
        private val tvTaskName: TextView = itemView.findViewById(R.id.tvTaskName)
        private val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

        fun bind(task: TaskEntity, onTaskCheckedChangeListener: (TaskEntity) -> Unit) {
            checkBox.isChecked = task.isCompleted
            tvTaskName.text = task.name
            tvDeadline.text = task.deadline.format(dateTimeFormatter)

            // Apply strikethrough and gray color for completed tasks
            if (task.isCompleted) {
                tvTaskName.setTextColor(Color.GRAY)
                tvDeadline.setTextColor(Color.GRAY)
            } else {
                tvTaskName.setTextColor(Color.BLACK)

                // Set text color based on deadline
                val now = LocalDateTime.now()
                when {
                    task.deadline.isBefore(now) -> {
                        // Overdue - red
                        tvDeadline.setTextColor(Color.RED)
                    }
                    task.deadline.isBefore(now.plusDays(1)) -> {
                        // Due today - orange
                        tvDeadline.setTextColor(Color.rgb(255, 165, 0)) // Orange
                    }
                    else -> {
                        // Future - green
                        tvDeadline.setTextColor(Color.rgb(0, 128, 0)) // Green
                    }
                }
            }

            // Set click listener for the checkbox
            checkBox.setOnClickListener {
                onTaskCheckedChangeListener(task)
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem == newItem
        }
    }
}