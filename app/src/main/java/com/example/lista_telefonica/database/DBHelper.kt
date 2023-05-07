package com.example.lista_telefonica.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lista_telefonica.model.ContactModel
import com.example.lista_telefonica.model.UserModel

class DBHelper(context: Context): SQLiteOpenHelper(context,"database.db",null,1) {

    val sql = arrayOf(
        "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)",
        "INSERT INTO users(username, password) VALUES ('admin','password')",
        "CREATE TABLE contact(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,address TEXT, email TEXT UNIQUE, phone INT, imageid INT)",
        "INSERT INTO contact(name,address, email,phone,imageid) values('Maria','Address Maria','maria@mail.pt',911222333,-1)",
        "INSERT INTO contact(name,address, email,phone,imageid) values('João','Address João','joao@mail.pt',912345678,-1)",
    )

    override fun onCreate(db: SQLiteDatabase) {
       sql.forEach {
           db.execSQL(it)
       }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    //USERS
    fun insertUser(username: String, password: String): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username",username)
        contentValues.put("password",password)
        val res = db.insert("users",null,contentValues)
        db.close()
        return res
    }

    fun updateUser(id: Int, username: String, password: String):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username",username)
        contentValues.put("password",password)
        val res = db.update("users",contentValues,"id=?", arrayOf(id.toString()))
        db.close()
        return res
    }
    fun deleteUser(id: Int): Int{
        val db = this.writableDatabase
        val res = db.delete("users","id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun getuser(username: String,password: String) : UserModel{
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", arrayOf(username,password))
        var userModel = UserModel()
        if(c.count == 1)
        {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val UsernameIndex = c.getColumnIndex("username")
            val PasswordIndex = c.getColumnIndex("password")

            userModel = UserModel(id=c.getInt(idIndex),username = c.getString(UsernameIndex),password= c.getString(PasswordIndex))
        }
        db.close()
        return userModel
    }

    fun Login(username: String,password: String) : Boolean{
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", arrayOf(username,password))
        var userModel = UserModel()
        if(c.count == 1)
        {
            db.close()
            return true
        }
        else{
            db.close()
            return false
        }
    }

    // CONTACTS

    fun insertContact(name: String,address: String, email: String,phone: Int, imageid: Int): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name",name)
        contentValues.put("email",email)
        contentValues.put("address",address)
        contentValues.put("phone",phone)
        contentValues.put("imageid",imageid)
        val res = db.insert("contact",null,contentValues)
        db.close()
        return res
    }

    fun updateContact(id: Int, name: String, address: String, email: String,phone: Int, imageid: Int):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name",name)
        contentValues.put("email",email)
        contentValues.put("address",address)
        contentValues.put("phone",phone)
        contentValues.put("imageid",imageid)
        val res = db.update("contact",contentValues,"id=?", arrayOf(id.toString()))
        db.close()
        return res
    }
    fun deleteContact(id: Int): Int{
        val db = this.writableDatabase
        val res = db.delete("contact","id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun getContact(id:Int) : ContactModel{
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM contact WHERE id=?", arrayOf(id.toString()))
        var contactModel = ContactModel()
        if(c.count == 1)
        {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val addressIndex = c.getColumnIndex("address")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imageIdIndex = c.getColumnIndex("imageid")

            contactModel = ContactModel(id=c.getInt(idIndex),name = c.getString(nameIndex),address = c.getString(addressIndex),email= c.getString(emailIndex),phone= c.getInt(phoneIndex),
                imageid= c.getInt(imageIdIndex))
        }
        db.close()
        return contactModel
    }

    fun getAllContact() : ArrayList<ContactModel>{
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM contact",null)
        val listcontactModel = ArrayList<ContactModel>()
        if(c.count > 0)
        {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val addressIndex = c.getColumnIndex("address")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imageIdIndex = c.getColumnIndex("imageid")
            do {
                val contactModel = ContactModel(id=c.getInt(idIndex),name = c.getString(nameIndex),address = c.getString(addressIndex),email= c.getString(emailIndex),phone= c.getInt(phoneIndex),
                    imageid= c.getInt(imageIdIndex))
                listcontactModel.add(contactModel)
            }while(c.moveToNext())
        }
        db.close()
        return listcontactModel
    }
}