package com.example.contactsapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contactsapp.data.ContactsData

class ContactsAdapter(var list: List<ContactsData>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTextView: TextView = view.findViewById(R.id.item_view)
        val itemImageView: ImageView = view.findViewById(R.id.contact_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        return ContactViewHolder(layout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = list[position]

        holder.itemTextView.text = currentContact.firstName + " " + currentContact.lastName

        holder.itemImageView.loadRoundedImages(MainFragment(), currentContact.picture)

        holder.itemTextView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(contactId = currentContact.id)
            holder.itemTextView.findNavController().navigate(action) }

        holder.itemImageView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(contactId = currentContact.id)
            holder.itemTextView.findNavController().navigate(action) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun ImageView.loadRoundedImages (fragment: MainFragment, url: String) {
        Glide.with(this)
            .load(url)
            .circleCrop()
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.loading_animation)
            .into(this)
    }
}