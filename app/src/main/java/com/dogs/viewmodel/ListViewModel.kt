package com.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dogs.api.ApiService
import com.dogs.db.DogDataBase
import com.dogs.model.DogBreed
import com.dogs.utils.NotificationsHelper
import com.dogs.utils.SharedPreferencesHelper
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application) {

    var pref = SharedPreferencesHelper(getApplication())
    var refreshTime = 5 * 60 * 1000 * 1000 * 1000L // 5 min in nano-seconds (5 min, 60sec, 1000milisec, 1000microsec, 1000nanosec)

    var disposable = CompositeDisposable()

    var dogs = MutableLiveData<List<DogBreed>>()
    var error = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()

    fun updateDogsData() {
        checkCashDuration()
        val updateTime = pref.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDB()
        }else{
            fetchFromRemote()
        }
    }

    fun refreshByPassCash(){
        fetchFromRemote()
    }

    private fun checkCashDuration(){
        val cashPreference = pref.getCashDuration()
        try {
            val cashPreferenceInt = cashPreference?.toInt() ?: 5 * 60
            refreshTime = cashPreferenceInt.times(1000 * 1000 * 1000L )
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }
    }

    private fun fetchFromDB(){
        loading.value = true
        launch {
            val dogs = DogDataBase(getApplication()).dogDao().getAllDogs()
            dogRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from data base", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote() {
        disposable.add(
            ApiService.getDogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogs: List<DogBreed>) {
                       storeDogLocally(dogs)
                        Toast.makeText(getApplication(), "Dogs retrieved from endpoint", Toast.LENGTH_SHORT).show()
                        NotificationsHelper(getApplication()).createNofitication()
                    }

                    override fun onError(e: Throwable) {
                        error.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )

    }

    private fun dogRetrieved(list: List<DogBreed>){
        dogs.value = list
        error.value = false
        loading.value = false
    }

    private fun storeDogLocally(dogs: List<DogBreed>){
        launch {
            val dao = DogDataBase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*dogs.toTypedArray())
            var i = 0
            while (i < dogs.size){
                dogs[i].uuid = result[i].toInt()
                ++i
            }
            dogRetrieved(dogs)
        }
        pref.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}