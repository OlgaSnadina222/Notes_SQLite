package com.notes.notes.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.notes.notes.utils.DbConstance

class DbHelper (context: Context): SQLiteOpenHelper(context,DbConstance.DATABASE_NAME,null,DbConstance.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbConstance.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DbConstance.SQL_DELETE_TABLE)
        onCreate(db)
    }
}