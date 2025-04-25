package com.example.ticktick.data

import androidx.lifecycle.LiveData
import java.time.LocalDateTime

class TaskRepository(private val taskDao: TaskDao) {

    // Get all tasks
    val allTasksByDeadline: LiveData<List<TaskEntity>> = taskDao.getAllTasksByDeadline()
    val allTasksByStatus: LiveData<List<TaskEntity>> = taskDao.getAllTasksByStatus()

    // Get overdue tasks
    fun getOverdueTasks(): LiveData<List<TaskEntity>> {
        return taskDao.getOverdueTasks(LocalDateTime.now())
    }

    // Insert task
    suspend fun insert(task: TaskEntity) {
        taskDao.insert(task)
    }

    // Update task
    suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }

    // Delete task
    suspend fun delete(task: TaskEntity) {
        taskDao.delete(task)
    }

    // Toggle task status
    suspend fun toggleTaskStatus(task: TaskEntity) {
        taskDao.updateTaskStatus(task.id, !task.isCompleted)
    }
}