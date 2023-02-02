package com.example.contactsapp

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadRoundedImageWithGlide (url: String) {
    Glide.with(this)
        .load(url)
        .circleCrop()
        .error(R.drawable.ic_broken_image)
        .placeholder(R.drawable.loading_animation)
        .into(this)
}

fun ImageView.loadSquaredImageWithGlide (url: String) {
    Glide.with(this)
        .load(url)
        .fitCenter()
        .error(R.drawable.ic_broken_image)
        .placeholder(R.drawable.loading_animation)
        .into(this)
}