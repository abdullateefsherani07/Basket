package com.android.bucket

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

    private val shoppingItemDao = (application as BucketApp).db.shoppingItemDao()
    private var _shoppingItems = MutableStateFlow<List<ShoppingItemEntity>>(emptyList())
    var shoppingItems: StateFlow<List<ShoppingItemEntity>> = _shoppingItems

    init{
        fetchShoppingItems()
    }

    private fun fetchShoppingItems(){
        viewModelScope.launch{
            shoppingItemDao.fetchAllShoppingItems().collect{
                _shoppingItems.value = it
            }
        }
    }

    fun insertShoppingItem(name: String, quantity: Int){
        viewModelScope.launch{
            shoppingItemDao.insert(ShoppingItemEntity(name = name, quantity = quantity))
        }
    }

    fun updateShoppingItem(item: ShoppingItemEntity){
        viewModelScope.launch{
            shoppingItemDao.update(item)
        }
    }

    fun deleteShoppingItem(item: ShoppingItemEntity){
        viewModelScope.launch {
            shoppingItemDao.delete(item)
        }
    }

    fun setEditingItem(item: ShoppingItemEntity){
        _shoppingItems.value = _shoppingItems.value.map{
            it.copy(isEditing = it.id == item.id)
        }
    }

}