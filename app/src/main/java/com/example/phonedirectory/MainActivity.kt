package com.example.phonedirectory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var userList:ArrayList<Users>
    private lateinit var adapter: UserAdapter

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Directory Application"
        setSupportActionBar(toolbar)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)

        databaseHelper = DatabaseHelper(this)

        getUsers()

        fab.setOnClickListener{
            alertShow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    private fun alertShow(){
        val design = LayoutInflater.from(this).inflate(R.layout.alert_design, null)

        val editTextName = design.findViewById(R.id.editTextName) as EditText
        val editTextPhoneNumber = design.findViewById(R.id.editTextPhone) as EditText

        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle("Add User")
        alertDialog.setView(design)
        alertDialog.setPositiveButton("Add"){ dialogInterface, i ->
            val userName = editTextName.text.toString().trim()
            val userPhoneNumber = editTextPhoneNumber.text.toString().trim()

            UsersDao().insertUser(databaseHelper, userName, userPhoneNumber)

            getUsers()

            Toast.makeText(applicationContext, "$userName - $userPhoneNumber", Toast.LENGTH_SHORT).show()
        }

        alertDialog.setNegativeButton("Cancel"){ dialogInterface, i ->

        }

        alertDialog.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            Log.e("Sending search", query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            Log.e("Text Change", newText)
        }
        return true
    }

    fun getUsers(){
        userList = UsersDao().getUsers(databaseHelper)

        adapter = UserAdapter(this, userList)
        rv.adapter = adapter
    }

}