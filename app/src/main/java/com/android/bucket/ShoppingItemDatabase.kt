package com.android.bucket

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItemEntity::class], version = 1)
abstract class ShoppingItemDatabase: RoomDatabase() {

    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object{

        @Volatile
        private var INSTANSE: ShoppingItemDatabase? = null

        fun getInstanse(context: Context): ShoppingItemDatabase{

            synchronized(this){
                var instanse = INSTANSE

                if (instanse == null){
                    instanse = Room.databaseBuilder(
                        context.applicationContext, ShoppingItemDatabase::class.java, "employee-database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANSE = instanse
                }
                return instanse
            }

        }

    }

}