package com.dogs.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dogs.model.DogBreed


@Database (entities = arrayOf(DogBreed::class), version = 2)
abstract class DogDataBase : RoomDatabase() {

    abstract fun dogDao(): DogDao

    companion object {
        @Volatile private var instance : DogDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDataBBase(context).also {
                instance = it
            }
        }

        private fun buildDataBBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DogDataBase::class.java,
            "dogdatabbase"
        ).fallbackToDestructiveMigration().build()
    }
}