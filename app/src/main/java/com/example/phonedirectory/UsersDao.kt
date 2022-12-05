package com.example.phonedirectory

import android.annotation.SuppressLint
import android.content.ContentValues

class UsersDao {

    fun deleteUser(databaseHelper: DatabaseHelper, userId: Int) {
        val db = databaseHelper.writableDatabase
        db.delete("users", "user_id=?", arrayOf(userId.toString()))
        db.close()
    }

    fun insertUser(databaseHelper: DatabaseHelper, userName: String, userPhone: String) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues()
        values.put("user_name", userName)
        values.put("user_phone", userPhone)
        db.insertOrThrow("users", null, values)
        db.close()
    }

    fun updateUser(
        databaseHelper: DatabaseHelper,
        userId: Int,
        userName: String,
        userPhone: String) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues()
        values.put("user_name", userName)
        values.put("user_phone", userPhone)
        db.update("users", values, "user_id=?", arrayOf(userId.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun getUsers(databaseHelper: DatabaseHelper): ArrayList<Users> {
        val db = databaseHelper.writableDatabase
        val userList = ArrayList<Users>()
        val cursor = db.rawQuery("SELECT * FROM users", null)
        while (cursor.moveToNext()) {
            val user = Users(
                cursor.getInt(cursor.getColumnIndex("user_id")),
                cursor.getString(cursor.getColumnIndex("user_name")),
                cursor.getString(cursor.getColumnIndex("user_phone"))
            )
            userList.add(user)
        }
        return userList
    }

    @SuppressLint("Range")
    fun searchUser(databaseHelper: DatabaseHelper, searchWord: String): ArrayList<Users> {
        val db = databaseHelper.writableDatabase
        val userList = ArrayList<Users>()
        val cursor = db.rawQuery("SELECT * FROM users WHERE user_name like '%$searchWord%'", null)

        while (cursor.moveToNext()) {
            val user = Users(
                cursor.getInt(cursor.getColumnIndex("user_id")),
                cursor.getString(cursor.getColumnIndex("user_name")),
                cursor.getString(cursor.getColumnIndex("user_phone")))
            userList.add(user)
        }
        return userList
    }
}