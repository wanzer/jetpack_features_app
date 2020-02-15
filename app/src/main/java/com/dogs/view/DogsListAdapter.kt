package com.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dogs.R
import com.dogs.databinding.ItemDogBinding
import com.dogs.model.DogBreed
import com.dogs.utils.getProgressDrawable
import com.dogs.utils.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter (val dogsList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogsViewHolder>(),
    DogClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent,false)
        return DogsViewHolder(view)
    }

    override fun getItemCount(): Int = dogsList.size

    override fun onBindViewHolder(holder: DogsViewHolder, position: Int) {
        holder.view.dog = dogsList[position]
        holder.view.listener = this

//        holder.itemView.dog_name.text = dogsList.get(position).dogBreed
//        holder.itemView.dog_life_span.text = dogsList.get(position).liveSpan
//        holder.itemView.dog_image.loadImage(dogsList.get(position).imageUrl, getProgressDrawable(holder.itemView.context))
//
//        holder.itemView.setOnClickListener {
//            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
//            action.dogId = dogsList[position].uuid
//            Navigation.findNavController(it).navigate(action)
//        }
    }

    override fun onDogClick(view: View) {
        val dogId = view.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.dogId = dogId
        Navigation.findNavController(view).navigate(action)
    }

    fun updateDogsList(dogsList: List<DogBreed>){
        this.dogsList.clear()
        this.dogsList.addAll(dogsList)
        notifyDataSetChanged()
    }

    class DogsViewHolder(var view : ItemDogBinding) : RecyclerView.ViewHolder(view.root)
}