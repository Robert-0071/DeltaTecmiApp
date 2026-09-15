package com.example.notemanager.data.local

import androidx.room.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 1. Entidad Room (Paso 2.1 del Manual)
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false,
    val imageUri: String? = null
)

// 2. Convertidor para LocalDateTime (Paso 2.2 del Manual)
class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}
