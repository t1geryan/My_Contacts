package com.example.mycontacts.view.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ItemContactBinding
import com.example.mycontacts.model.Contact

class ContactsAdapter(private val listener: OnContactChangeListener) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(), View.OnClickListener {

    class ContactsViewHolder(
        val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var contacts : List<Contact> = emptyList()
        set(value) {
            val diffCallback = ContactsDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(view: View) {
        val contact = view.tag as Contact
        when(view.id) {
            R.id.isFavoriteImageView -> listener.onChangeFavoriteStatus(contact)
            else -> listener.onDeleteContact(contact)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.isFavoriteImageView.setOnClickListener(this)

        return ContactsViewHolder(binding)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            root.tag = contact
            isFavoriteImageView.tag = contact

            contactName.text = contact.name
            contactNumber.text = contact.number
            if (contact.photo.isNotBlank()) {
                Glide.with(root.context)
                    .load(contact.photo)
                    .centerCrop()
                    .error(R.drawable.base_avatar_daynight)
                    .placeholder(R.drawable.base_avatar_daynight)
                    .into(avatarImageView)
            } else
                avatarImageView.setImageResource(R.drawable.base_avatar_daynight)

            if (contact.isFavorite)
                isFavoriteImageView.setImageResource(R.drawable.ic_favorite_daynight)
            else
                isFavoriteImageView.setImageResource(R.drawable.ic_favorite_border_daynight)
        }

    }
}