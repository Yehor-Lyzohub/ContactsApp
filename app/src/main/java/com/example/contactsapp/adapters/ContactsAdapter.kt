package com.example.contactsapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.R
import com.example.contactsapp.data.ContactsData
import com.example.contactsapp.loadRoundedImageWithGlide
import com.example.contactsapp.ui.MainFragmentDirections

class ContactsAdapter(val onClick: (String) -> Unit) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    var oldList: List<ContactsData> = listOf()

    inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTextView: TextView = view.findViewById(R.id.item_view)
        val itemImageView: ImageView = view.findViewById(R.id.contact_img)
        val itemCheckBox: CheckBox = view.findViewById(R.id.favoriteCheckBox)
    }

    class ContactDiffCallback(
        var oldContactList: List<ContactsData>,
        var newContactList: List<ContactsData>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldContactList.size
        }

        override fun getNewListSize(): Int {
            return newContactList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldContactList[oldItemPosition].id == (newContactList[newItemPosition].id))
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldContactList[oldItemPosition] == newContactList[newItemPosition]
        }
    }

    fun setData(newList: List<ContactsData>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(ContactDiffCallback(oldList, newList))
        oldList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        return ContactViewHolder(layout)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = oldList[position]
        holder.itemTextView.text = currentContact.firstName + " " + currentContact.lastName
        holder.itemImageView.loadRoundedImageWithGlide(currentContact.picture)
        holder.itemCheckBox.isChecked = currentContact.isFavorite

        holder.itemTextView.setOnClickListener {
            nextFragmentAction(holder, currentContact)
        }

        holder.itemImageView.setOnClickListener {
            nextFragmentAction(holder, currentContact)
        }

        holder.itemCheckBox.setOnClickListener {
            onClick(currentContact.id)
        }
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    private fun nextFragmentAction(holder: ContactViewHolder, currentContact: ContactsData) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
            contactId = currentContact.id,
            contactIsFavorite = currentContact.isFavorite
        )
        holder.itemTextView.findNavController().navigate(action)
    }
}