package com.example.lista_telefonica.adapter.listener

import com.example.lista_telefonica.model.ContactModel

class ContactOnClickListener(val clickListener: (contact:ContactModel) -> Unit)
{
    fun onClick(contact: ContactModel) = clickListener
}