package com.example.sayurbox.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.sayurbox.data.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY firstName ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET firstName = :firstName, lastName = :lastName WHERE email = :email")
    suspend fun updateUserNames(email: String, firstName: String, lastName: String)

    @Query("UPDATE users SET phoneNumber = :phoneNumber WHERE email = :email")
    suspend fun updatePhoneNumber(email: String, phoneNumber: String)

    @Query("UPDATE users SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUserByEmail(email: String)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}