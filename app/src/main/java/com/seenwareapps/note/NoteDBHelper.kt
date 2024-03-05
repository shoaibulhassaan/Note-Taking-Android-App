package com.seenwareapps.note

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.seenwareapps.note.models.Note

class NoteDBHelper(
    context: Context?,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object {
        private const val DATABASE_NAME = "DB_NOTE.db"
        private const val DATABASE_VER = 1
        private const val TABLE_NAME = "NOTE_TAB"
        private const val COLUMN_ID = "Id"
        private const val COLUMN_TITLE = "Title"
        private const val COLUMN_DESCRIPTION = "Description"
        private const val COLUMN_DATE = "Date"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT" + ")")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
       onCreate(db)
    }


    fun addNote(title : String, description : String, date : String ){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_DESCRIPTION, description)
        values.put(COLUMN_DATE, date)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    @SuppressLint("Range")
    fun getAllNotes(): List<Note> {
        val columns = arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_DATE)
        val notes = mutableListOf<Note>()

        // Get a readable instance of the database
        val db = this.readableDatabase

        // Perform the query on the database
        val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null)

        // Iterate through the cursor and add Notes to the list
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))

            // Split the text into words using whitespace as a delimiter
            val words = description?.split("\\s+".toRegex())
            val shortDescription = words?.take(15)?.joinToString(" ")


            notes.add(Note(id, title, shortDescription.toString(), date))
        }

        // Close the cursor and database
        cursor.close()
        db.close()

        return notes
    }

    fun deleteNote(noteId: Int): Boolean {
        // Get a writable instance of the database
        val db = this.writableDatabase

        // Define the WHERE clause to specify which note to delete
        val whereClause = "$COLUMN_ID = ?"

        // Define the values for the WHERE clause
        val whereArgs = arrayOf(noteId.toString())

        // Attempt to delete the note
        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)

        // Close the database
        db.close()

        // Return true if at least one row was affected (note deleted), otherwise false
        return deletedRows > 0
    }

    fun updateNote(noteId: Int, newTitle: String, newDescription: String, newDate: String): Boolean {
        // Get a writable instance of the database
        val db = this.writableDatabase

        // Create a ContentValues object to store the new values
        val values = ContentValues()
        values.put(COLUMN_TITLE, newTitle)
        values.put(COLUMN_DESCRIPTION, newDescription)
        values.put(COLUMN_DATE, newDate)

        // Define the WHERE clause to specify which note to update
        val whereClause = "$COLUMN_ID = ?"

        // Define the values for the WHERE clause
        val whereArgs = arrayOf(noteId.toString())

        // Attempt to update the note
        val updatedRows = db.update(TABLE_NAME, values, whereClause, whereArgs)

        // Close the database
        db.close()

        // Return true if at least one row was affected (note updated), otherwise false
        return updatedRows > 0
    }

    @SuppressLint("Range")
    fun getNoteById(noteId: Int): Note? {
        // Get a readable instance of the database
        val db = this.readableDatabase

        // Define the columns to retrieve
        val columns = arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_DATE)

        // Define the WHERE clause to specify which note to retrieve
        val selection = "$COLUMN_ID = ?"

        // Define the values for the WHERE clause
        val selectionArgs = arrayOf(noteId.toString())

        // Perform the query on the database
        val cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        // Check if the cursor has any rows
        if (cursor.moveToFirst()) {
            // Retrieve values from the cursor
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))

            // Create a Note object with the retrieved values
            val note = Note(id, title, description, date)

            // Close the cursor and database
            cursor.close()
            db.close()

            // Return the Note object
            return note
        } else {
            // If the cursor is empty, close the cursor and database, and return null
            cursor.close()
            db.close()
            return null
        }
    }

}