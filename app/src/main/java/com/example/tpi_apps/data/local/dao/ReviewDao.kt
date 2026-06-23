package com.example.tpi_apps.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tpi_apps.data.local.entities.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews ORDER BY date DESC, time DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE userId = :userId ORDER BY date DESC, time DESC")
    fun getReviewsByUser(userId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE id = :id LIMIT 1")
    suspend fun getReviewById(id: String): ReviewEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Query("DELETE FROM reviews")
    suspend fun clearAll()
}
