package com.example.taskdego.data.entity

data class ItemWithQuantity(
    val m_item_id: Int,
    val item_name: String,
    val price: Int?,
    val is_pokemon_candy: Boolean,
    val quantity: Int?  // NULLの場合は0個として扱う
)