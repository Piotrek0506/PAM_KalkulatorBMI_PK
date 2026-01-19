package com.example.pam_projekt_pk

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "bmi_table")
data class BmiRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weight: String,
    val height: String,
    val bmiResult: String,
    val date: String
)

@Dao
interface BmiDao {
    @Query("SELECT * FROM bmi_table ORDER BY id DESC")
    fun getAllHistory(): Flow<List<BmiRecord>>

    @Insert
    suspend fun insert(record: BmiRecord)
}


@Database(entities = [BmiRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bmiDao(): BmiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bmi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}