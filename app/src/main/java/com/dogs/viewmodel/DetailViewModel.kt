package com.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Database
import com.dogs.db.DogDataBase
import com.dogs.model.DogBreed
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogDetail = MutableLiveData<DogBreed>()

    fun fetchDodById(id: Int){
        launch {
            val dog = DogDataBase(getApplication()).dogDao()
            dogDetail.value = dog.dogBreed(id)
        }
    }

}