package com.example.taskdego.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "m_pokemons")
data class mPokemonEntity(
    @PrimaryKey val m_pokemon_id: Int,
    val croix_no: Int,
    val pokemon_name: String,
    val form_name: String?,
    val species: String,
    val type1: String,
    val type2: String?,
    val catch_rate: Int,
    val tall: Float,
    val weight: Float,
    val description: String
)