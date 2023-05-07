package com.example.lista_telefonica.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lista_telefonica.R
import com.example.lista_telefonica.adapter.ContactListAdapter
import com.example.lista_telefonica.adapter.listener.ContactOnClickListener
import com.example.lista_telefonica.database.DBHelper
import com.example.lista_telefonica.databinding.ActivityMainBinding
import com.example.lista_telefonica.model.ContactModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactList: List<ContactModel>
   // private lateinit var adapter: ArrayAdapter<ContactModel>
    private lateinit var adapter: ContactListAdapter
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var dbHelper: DBHelper
    private var ascDesc: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DBHelper(this)
        val sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)

        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(applicationContext)
        loadList()

        binding.buttonLogout.setOnClickListener {
            val editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("username","")
            editor.apply()
            finish()
        }
      //  contactList = dbHelper.getAllContact()
       // adapter = ArrayAdapter(
        //   applicationContext,android.R.layout.simple_list_item_1,contactList)
     //   binding.listViewContacts.adapter = adapter
  /*      binding.listViewContacts.setOnItemClickListener { _, _, i, _ ->
           // Toast.makeText(applicationContext,contactList[i].name,Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext,ContactDetailActivity::class.java)
            intent.putExtra("id",contactList[i].id)
            //startActivity(intent)
            result.launch(intent)
        }*/
        binding.buttonAdd.setOnClickListener {
            result.launch(Intent(applicationContext,NewContactActivity::class.java))
        }

        binding.buttonOrder.setOnClickListener{
            if(ascDesc)
            {
                binding.buttonOrder.setImageResource(R.drawable.baseline_arrow_upward_24)
            }
            else
            {
                binding.buttonOrder.setImageResource(R.drawable.baseline_arrow_downward_24)
            }
            ascDesc = !ascDesc
            contactList = contactList.reversed()
            placeadapter()
        }

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.data != null && it.resultCode == 1){
                loadList()
            }
            else if(it.data != null && it.resultCode == 0)
            {
                Toast.makeText(applicationContext,"Operation Canceled",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun placeadapter(){
        adapter = ContactListAdapter(contactList, ContactOnClickListener { contact->
            val intent = Intent(applicationContext,ContactDetailActivity::class.java)
            intent.putExtra("id",contact.id)
            result.launch(intent)
        })
        binding.recyclerViewContacts.adapter = adapter
    }

    private fun loadList(){
        contactList = dbHelper.getAllContact().sortedWith(compareBy{it.name})
        placeadapter()
    }
}