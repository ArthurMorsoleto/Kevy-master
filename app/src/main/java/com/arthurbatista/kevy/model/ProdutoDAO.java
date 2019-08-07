package com.arthurbatista.kevy.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProdutoDAO {

    @Insert
    void insert(Produto produto);

    @Update
    void update(Produto produto);

    @Delete
    void delete(Produto produto);

    @Query("SELECT * FROM produto_table")
    LiveData<List<Produto>> getAllProdutos();

}
