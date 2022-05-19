package com.example.edor

import android.content.Context
import com.example.edor.sqlDatabases.TimetableDatabase

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

    fun getGradeArray(): List<String>{
        return gradePoint.keys.toList()
    }
}

object UserInfo {

    private var name: String? = null
    private var university:String? = null
    private var program:String? = null
    private var email: String? = null

    fun getName(): String? {
        return name
    }

    fun getUniversity(): String? {
        return university
    }

    fun getProgram(): String? {
        return program
    }

    fun getEmail(): String? {
        return email
    }

    fun setName(value: String?){
         name = value
    }

    fun setUniversity(value: String?) {
         university = value
    }

    fun setProgram(value: String?) {
         program = value
    }

    fun setEmail(value: String?) {
         email = value
    }


}