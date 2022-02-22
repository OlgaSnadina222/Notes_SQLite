package com.notes.notes.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.notes.notes.ListItem
import com.notes.notes.utils.DbConstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DbManager (context: Context) {

    private val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = dbHelper.writableDatabase
    }

    suspend fun insetToDb(title: String, content: String, check: String, time: String) = withContext(Dispatchers.IO){
        val values = ContentValues().apply {
            put(DbConstance.COLUMN_TITLE, title)
            put(DbConstance.COLUMN_CONTENT, content)
            put(DbConstance.COLUMN_CHECK_BOX, check)
            put(DbConstance.COLUMN_TIME, time)
        }
        db?.insert(DbConstance.TABLE_NAME,null,values)
    }

    suspend fun updateItem(title: String, content: String, check: String, id: Int, time: String) = withContext(Dispatchers.IO){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(DbConstance.COLUMN_TITLE, title)
            put(DbConstance.COLUMN_CONTENT, content)
            put(DbConstance.COLUMN_CHECK_BOX, check)
            put(DbConstance.COLUMN_TIME, time)
        }
        db?.update(DbConstance.TABLE_NAME,values,selection,null)
    }

    @SuppressLint("Recycle", "Range")
    suspend fun readFromDb(searcher: String) = withContext(Dispatchers.IO){
        val dataList = ArrayList<ListItem>()
        val selection = "${DbConstance.COLUMN_TITLE} like?"
        val cursor = db?.query(
            DbConstance.TABLE_NAME,
            null,
            selection,
            arrayOf("%$searcher%"),
            null,
            null,
            null
        )
        while (cursor?.moveToNext() == true) {
            val dataTitle = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_TITLE))
            val dataDesc = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_CONTENT))
            val dataChBox = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_CHECK_BOX))
            val dataTime = cursor.getString(cursor.getColumnIndex(DbConstance.COLUMN_TIME))
            val dataID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val item = ListItem()
            item.title = dataTitle
            item.desc = dataDesc
            item.check = dataChBox.toBoolean()
            item.time = dataTime
            item.id = dataID
            dataList.add(item)
        }
        cursor?.close()
        return@withContext dataList
    }

    fun closeDb(){
        dbHelper.close()
    }

    fun deleteFromDb(id: String){
        val selection = BaseColumns._ID + "=$id"
        db?.delete(DbConstance.TABLE_NAME,selection,null)
    }
}