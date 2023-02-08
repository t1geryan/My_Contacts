package com.example.mycontacts.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ItemContactBinding
import com.example.mycontacts.model.Contact

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    var contacts : List<Contact> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ContactsViewHolder(
        val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)
        return ContactsViewHolder(binding)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            contactName.text = contact.name
            contactNumber.text = contact.number
            if (contact.photo.isNotBlank()) {
                Glide.with(root.context)
                    .load(contact.photo)
                    .centerCrop()
                    .error(R.drawable.base_contact_avatar)
                    .placeholder(R.drawable.base_contact_avatar)
                    .into(avatarImageView)
            } else
                avatarImageView.setImageResource(R.drawable.base_contact_avatar)

            addToFavoriteButton.setImageResource(
                if (contact.isFavorite) R.drawable.ic_favorite_red
                else R.drawable.ic_favorite_daynight)
        }

    }
}