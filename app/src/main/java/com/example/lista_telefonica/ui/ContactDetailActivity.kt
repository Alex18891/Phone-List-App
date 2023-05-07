package com.example.lista_telefonica.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.lista_telefonica.R
import com.example.lista_telefonica.database.DBHelper
import com.example.lista_telefonica.databinding.ActivityContactDetailBinding
import com.example.lista_telefonica.model.ContactModel

class ContactDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageid: Int = -1
    private var contactModel = ContactModel()
    private val REQUEST_PHONE_CALL = 1

    @SuppressLint("SuspiciousIndentation", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val i = intent
        val id = i.extras?.getInt("id")
        dbHelper = DBHelper(applicationContext)
        if(id != null) {
            contactModel = dbHelper.getContact(id)
            populate()
        }
        else{
            finish()
        }
        binding.buttonEdit.setOnClickListener {
            binding.layoutEditDelete.visibility = View.VISIBLE
            binding.layoutEdit.visibility = View.GONE
            changeEditText(true)
        }
        binding.buttonDelete.setOnClickListener {
            val res = dbHelper.deleteContact(contactModel.id)
            if(res>0){
                Toast.makeText(applicationContext, "Delete Ok", Toast.LENGTH_SHORT).show()
                setResult(1,i)
                finish()
            }
            else{
                Toast.makeText(applicationContext, "Delete Error", Toast.LENGTH_SHORT).show()
                setResult(0,i)
                finish()
            }
        }
        binding.buttonCancel.setOnClickListener {
            binding.layoutEditDelete.visibility = View.GONE
            binding.layoutEdit.visibility = View.VISIBLE
            populate()
            changeEditText(false)
        }

        binding.buttonBack.setOnClickListener {
            setResult(0,i)
            finish()
        }

        binding.imageEmail.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactModel.email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Contact")
            emailIntent.putExtra(Intent.EXTRA_TEXT,"Sent by ListaTelefonica APP")

            try {
                startActivity(Intent.createChooser(emailIntent,"Choose Email Client..."))
            }catch (e:Exception){
                Toast.makeText(applicationContext,e.message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.imagePhone.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            }
            else{
                val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" +contactModel.phone))
                startActivity(dialIntent)
            }
        }

        binding.buttonSave.setOnClickListener {
          val res =  dbHelper.updateContact(
                id = contactModel.id,
                name = binding.editName.text.toString(),
                address = binding.editAddress.text.toString(),
                email= binding.editEmail.text.toString(),
                phone = binding.editPhone.text.toString().toInt(),
                imageid = imageid
                )
            if(res>0 && binding.editPhone.toString().isNotEmpty()){
                Toast.makeText(applicationContext, "Update Ok", Toast.LENGTH_SHORT).show()
                setResult(1,i)
                finish()
            }
            else{
                Toast.makeText(applicationContext, "Update Error", Toast.LENGTH_SHORT).show()
                setResult(0,i)
                finish()
            }
        }
        binding.imageContact.setOnClickListener {
            if(binding.editName.isEnabled){
                launcher.launch(Intent(applicationContext,ContactImageSelectionActivity::class.java))
            }
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.data != null && it.resultCode == 1)
            {
                if(it.data?.extras!=null){
                    imageid = it.data?.getIntExtra("id",0)!!
                    binding.imageContact.setImageResource(imageid)
                }

            }
            else{
                imageid = -1
                binding.imageContact.setImageResource(R.drawable.user)
            }
        }

    }

    private fun changeEditText(status: Boolean) {
        binding.editName.isEnabled = status
        binding.editAddress.isEnabled = status
        binding.editEmail.isEnabled = status
        binding.editPhone.isEnabled = status
    }

    private fun populate() {
            binding.editName.setText(contactModel.name)
            binding.editAddress.setText(contactModel.address)
            binding.editEmail.setText(contactModel.email)
            binding.editPhone.setText(contactModel.phone.toString())
            if(contactModel.imageid > 0){
                binding.imageContact.setImageResource(contactModel.imageid)
            }else{
                binding.imageContact.setImageResource(R.drawable.user)
            }
        }
}