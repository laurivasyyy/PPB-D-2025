package com.example.ticktick.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDateTime

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAllTasksByDeadline(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, deadline ASC")
    fun getAllTasksByStatus(): LiveData<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean)

    @Query("SELECT * FROM tasks WHERE deadline < :currentTime AND isCompleted = 0 ORDER BY deadline ASC")
    fun getOverdueTasks(currentTime: LocalDateTime): LiveData<List<TaskEntity>>
}