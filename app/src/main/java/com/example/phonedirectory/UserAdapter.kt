package com.example.phonedirectory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class UserAdapter(private val context:Context, private val userList:List<Users>) : RecyclerView.Adapter<UserAdapter.CardViewHolder>() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_card_view, parent, false)
        return CardViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val user = userList.get(position)

        holder.textViewUserInfo.text = "${user.userName} - ${user.userPhone}"

        holder.imageViewMore.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.imageViewMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.actionDelete ->{
                        Snackbar.make(holder.imageViewMore, "Do you want to delete ${user.userName}?", Snackbar.LENGTH_SHORT)
                            .setAction("Yes"){
                                Toast.makeText(context, "${user.userName} is deleted.", Toast.LENGTH_SHORT).show()
                            }.show()
                        true
                    }
                    R.id.actionUpdate ->{
                        alertShow(user)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var textViewUserInfo:TextView
        var imageViewMore:ImageView

        init {
            textViewUserInfo = view.findViewById(R.id.textViewUserInfo)
            imageViewMore = view.findViewById(R.id.more)
        }
    }

    private fun alertShow(user: Users){
        val design = LayoutInflater.from(context).inflate(R.layout.alert_design, null)

        val editTextName = design.findViewById(R.id.editTextName) as EditText
        val editTextPhoneNumber = design.findViewById(R.id.editTextPhone) as EditText

        editTextName.setText(user.userName)
        editTextPhoneNumber.setText(user.userPhone)

        val ad = AlertDialog.Builder(context)

        ad.setTitle("Update User")
        ad.setView(design)
        ad.setPositiveButton("Update"){ dialogInterface, i ->
            val userName = editTextName.text.toString().trim()
            val userPhoneNumber = editTextPhoneNumber.text.toString().trim()
            databaseHelper = DatabaseHelper(context)
            UsersDao().updateUser(databaseHelper, 1, userName, userPhoneNumber)

            Toast.makeText(context, "$userName - $userPhoneNumber", Toast.LENGTH_SHORT).show()
        }

        ad.setNegativeButton("Cancel"){ dialogInterface, i ->

        }

        ad.create().show()
    }
}