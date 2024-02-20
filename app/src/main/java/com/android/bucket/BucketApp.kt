package com.android.bucket

import android.app.Application

class BucketApp: Application() {

    val db by lazy{
        ShoppingItemDatabase.getInstanse(this)
    }

}