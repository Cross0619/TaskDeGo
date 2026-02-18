package com.example.taskdego.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskdego.data.entity.mPokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface mPokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: mPokemonEntity)

    @Query("SELECT * FROM m_pokemons WHERE m_pokemon_id = :pokemonId")
    suspend fun getPokemonById(pokemonId: Int): mPokemonEntity?

    @Query("SELECT * FROM m_pokemons ORDER BY croix_no")
    fun getAllPokemons(): Flow<List<mPokemonEntity>>
}