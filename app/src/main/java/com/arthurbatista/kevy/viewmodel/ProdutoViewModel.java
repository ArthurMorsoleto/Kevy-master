package com.arthurbatista.kevy.viewmodel;

import android.app.Application;

import com.arthurbatista.kevy.model.Produto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ProdutoViewModel extends AndroidViewModel {

    private ProdutoRepository repository;
    private LiveData<List<Produto>> allProdutos;
    private LiveData<List<Produto>> listProdutos;

    public ProdutoViewModel(@NonNull Application application) {
        super(application);
        repository = new ProdutoRepository(application);
        allProdutos = repository.getAllProdutos();
    }

    public void insert(Produto produto) {
        repository.insert(produto);
    }

    public void delete(Produto produto) {
        repository.delete(produto);
    }

    public void update(Produto produto) {
        repository.update(produto);
    }

    public LiveData<List<Produto>> getAllProdutos() {
        return allProdutos;
    }
}
