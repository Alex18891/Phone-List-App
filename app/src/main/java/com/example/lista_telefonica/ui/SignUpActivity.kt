package com.example.lista_telefonica.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lista_telefonica.database.DBHelper
import com.example.lista_telefonica.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = DBHelper(this)
        binding.buttonRegister.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val pass = binding.editPassword.text.toString()
            val confirmpass = binding.editConfirmpassword.text.toString()
            if (username.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()) {
                if (pass.equals(confirmpass)) {
                    val res = db.insertUser(username, pass)
                    if (res > 0) {
                        Toast.makeText(applicationContext, "Signup OK", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Signup Error", Toast.LENGTH_SHORT)
                            .show()
                        binding.editPassword.setText("")
                        binding.editConfirmpassword.setText("")
                        binding.editUsername.setText("")
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Passwords are not the same",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please insert all required fields",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }

    }
}