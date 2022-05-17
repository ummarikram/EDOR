package com.example.edor

import android.content.Context

object DatabaseFactory {

    private var timetableDatabase: TimetableDatabase? = null

    fun getTimetableDatabaseInstance(context: Context): TimetableDatabase? {
        if (timetableDatabase == null) {
            timetableDatabase = TimetableDatabase(context)
        }
        return timetableDatabase
    }
}

object GradePoints {

    private var gradePoint : HashMap<String, Double> = hashMapOf("A+" to 4.0, "A" to 4.0, "A-" to 3.67, "B+" to 3.33, "B" to 3.00, "B-" to 2.67, "C+" to 2.33, "C" to 2.00)

    fun getGradePoint(grade: String): Double? {
        return gradePoint[grade]
    }
}