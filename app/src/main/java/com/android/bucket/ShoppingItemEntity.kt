package com.android.bucket

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName= "shopping-items-tabel")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)