package com.example.lista_telefonica.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lista_telefonica.R
import com.example.lista_telefonica.databinding.ActivityContactImageSelectionBinding

class ContactImageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactImageSelectionBinding
    private lateinit var i: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactImageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        i=intent
        binding.imageOne.setOnClickListener {
            sendID(R.drawable.one)
        }
        binding.imageTwo.setOnClickListener {
            sendID(R.drawable.two)
        }
        binding.imageThree.setOnClickListener {
            sendID(R.drawable.three)
        }
        binding.imageFour.setOnClickListener {
            sendID(R.drawable.four)
        }
        binding.imageFive.setOnClickListener {
            sendID(R.drawable.five)
        }
        binding.imageSix.setOnClickListener {
            sendID(R.drawable.six)
        }

    }

    private fun sendID(id: Int) {
        i.putExtra("id",id)
        setResult(1,i)
        finish()
    }
}