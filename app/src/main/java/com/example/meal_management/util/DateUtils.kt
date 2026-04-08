package com.example.meal_management.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Utility functions for date formatting
 */
object DateUtils {
    
    /**
     * Get current date as formatted string
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateString(): String {
        val currentDate = LocalDate.now()
        return "${currentDate.dayOfMonth} ${currentDate.month} ${currentDate.year}"
    }
    
    /**
     * Get short date string (day + month)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getShortDateString(): String {
        val currentDate = LocalDate.now()
        return "${currentDate.dayOfMonth} ${currentDate.month}"
    }
    
    /**
     * Format timestamp to readable date
     */
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * Format timestamp to short date
     */
    fun formatTimestampShort(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
