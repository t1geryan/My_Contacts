package com.example.mycontacts.ui.contact_list_utils

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ItemContactBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.OnContactChangeListener

class ContactsAdapter(private val listener: OnContactChangeListener) :
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(), View.OnClickListener {

    var contacts: List<Contact> = emptyList()
        set(value) {
            val diffCallback = ContactsDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(view: View) {
        val contact = view.tag as Contact
        when (view.id) {
            R.id.isFavoriteImageView -> listener.onChangeFavoriteStatus(contact)
            R.id.contactNumber -> listener.onCall(contact)
            else -> showPopupMenu(view)
        }
    }

    // single viewType
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
        holder.bind(contact)
    }

    private fun showPopupMenu(view: View) {
        val context = view.context
        val popupMenu = PopupMenu(view.context, view)
        val contact = view.tag as Contact

        popupMenu.menu.add(
            0, POPUP_ID_CHANGE_DATA, Menu.NONE, context.getString(R.string.change_data)
        )
        popupMenu.menu.add(0, POPUP_ID_CALL, Menu.NONE, context.getString(R.string.call_contact))
        popupMenu.menu.add(
            0, POPUP_ID_DELETE, Menu.NONE, context.getString(R.string.delete_contact)
        )

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                POPUP_ID_DELETE -> listener.onDelete(contact)
                POPUP_ID_CHANGE_DATA -> listener.onChangeData(contact)
                POPUP_ID_CALL -> listener.onCall(contact)
                else -> throw Exception("IllegalMenuItemException")
            }
            true
        }

        popupMenu.show()
    }

    class ContactsViewHolder(
        private val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun setTags(data: Any) {
            with(binding) {
                root.tag = data
                isFavoriteImageView.tag = data
                contactNumber.tag = data
                contactName.tag = data
            }
        }

        fun bind(contact: Contact) {
            setTags(contact)
            with(binding) {
                contactName.text = contact.name
                contactNumber.text = contact.number
                if (contact.photo.isNotBlank()) {
                    Glide.with(root.context).load(contact.photo).centerCrop()
                        .error(R.drawable.base_avatar_daynight)
                        .placeholder(R.drawable.base_avatar_daynight).into(avatarImageView)
                } else avatarImageView.setImageResource(R.drawable.base_avatar_daynight)

                if (contact.isFavorite) isFavoriteImageView.setImageResource(R.drawable.ic_favorite_daynight)
                else isFavoriteImageView.setImageResource(R.drawable.ic_favorite_border_daynight)
            }
        }
    }

    companion object {
        private const val POPUP_ID_DELETE = 0
        private const val POPUP_ID_CHANGE_DATA = 1
        private const val POPUP_ID_CALL = 2
    }
}