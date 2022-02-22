package com.notes.notes.utils

import android.provider.BaseColumns

object DbConstance {
    const val TABLE_NAME = "my_table"
    const val COLUMN_TITLE = "title"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_CHECK_BOX = "check_box"
    const val COLUMN_TIME = "time"
    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "MyLessonDb.db"
    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ("+
            "${BaseColumns._ID} INTEGER PRIMARY KEY,$COLUMN_TITLE TEXT,$COLUMN_CONTENT TEXT,$COLUMN_CHECK_BOX TEXT,$COLUMN_TIME TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}