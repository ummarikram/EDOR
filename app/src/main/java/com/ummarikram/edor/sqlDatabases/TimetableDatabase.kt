package com.ummarikram.edor.sqlDatabases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.ummarikram.edor.dataClasses.Schedule


class TimetableDatabase(context:Context):SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {

    companion object {
        const val DATABASE_VERSION = 1;
        const val DATABASE_NAME = "timetable.db";
        const val TABLE_NAME = "my_timetable_table";
        const val COLUMN_DAY = "day";
        const val COLUMN_COURSE = "course";
        const val COLUMN_START_TIME = "start";
        const val COLUMN_END_TIME = "end";
        const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_DAY + " TEXT, " + COLUMN_COURSE + " TEXT, " + COLUMN_START_TIME + " TEXT, " + COLUMN_END_TIME + " TEXT, " + "PRIMARY KEY (" + COLUMN_DAY + ", " + COLUMN_COURSE +")"+ ");";
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun deleteSchedule(schedule: Schedule, context: Context)
    {
        val db = this.writableDatabase

        try {

            val day = schedule.day
            val course = schedule.course

            val selectQuery = "DELETE FROM $TABLE_NAME WHERE $COLUMN_DAY = '$day' AND $COLUMN_COURSE = '$course'"

            db.execSQL(selectQuery)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "ERROR : ${e.message}",Toast.LENGTH_LONG).show()
        }

        db.close()
    }

    fun insertSchedule(schedule: Schedule, context: Context):Long{
        val db=this.writableDatabase

        val contentValues=ContentValues()

        contentValues.put(COLUMN_DAY,schedule.day)
        contentValues.put(COLUMN_COURSE,schedule.course)
        contentValues.put(COLUMN_START_TIME,schedule.startTime)
        contentValues.put(COLUMN_END_TIME,schedule.endTime)

        val success = db.insert(TABLE_NAME,null,contentValues)

        db.close()

        return success

    }

    fun getScheduleByDay(day: String, context: Context): ArrayList<Schedule> {

        val scheduleList: ArrayList<Schedule> = ArrayList()

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DAY = '$day'"

            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "ERROR : ${e.message}",Toast.LENGTH_LONG).show()
            return scheduleList
        }

        var course:String
        var startTime:String
        var endTime:String

        if(cursor.moveToFirst())
        {
            do{

                course=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE))
                startTime=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME))
                endTime=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_TIME))

                val schedule = Schedule(day,course,startTime,endTime)

                scheduleList.add(schedule)

            }while(cursor.moveToNext())
        }

        db.close()
        cursor.close()

        return scheduleList

    }

}