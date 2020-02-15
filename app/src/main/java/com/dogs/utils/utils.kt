package com.dogs.utils

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dogs.R

const val PERMISSION_SEND_SMS = 234

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun AppCompatImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable?){
    val requestOptions = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(imageView: AppCompatImageView, url: String?){
    imageView.loadImage(url, getProgressDrawable(imageView.context))
}