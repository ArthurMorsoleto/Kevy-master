package com.arthurbatista.kevy.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.arthurbatista.kevy.model.Produto;
import com.arthurbatista.kevy.model.ProdutoDAO;
import com.arthurbatista.kevy.model.ProdutoDataBase;

import java.util.List;

import androidx.lifecycle.LiveData;

import static android.content.ContentValues.TAG;

class ProdutoRepository {

    private ProdutoDAO produtoDAO;
    private LiveData<List<Produto>> allProdutos;

    ProdutoRepository(Application application) {
        ProdutoDataBase dataBase = ProdutoDataBase.getInstance(application);
        produtoDAO = dataBase.produtoDAO();

        LiveData<List<Produto>> listProdutos = produtoDAO.getAllProdutos();
        Log.i(TAG, "ProdutoRepository: " + listProdutos);
        allProdutos = produtoDAO.getAllProdutos();
    }

    void insert(Produto produto) {
        new InsertProdutoAsyncTask(produtoDAO).execute(produto);
    }

    void update(Produto produto) {
        new UpdateProdutoAsyncTask(produtoDAO).execute(produto);
    }

    void delete(Produto produto) {
        new DeleteProdutoAsyncTask(produtoDAO).execute(produto);
    }

    LiveData<List<Produto>> getAllProdutos() {
        return allProdutos;
    }

    private static class InsertProdutoAsyncTask extends AsyncTask<Produto, Void, Void> {
        private ProdutoDAO produtoDAO;

        private InsertProdutoAsyncTask(ProdutoDAO produtoDAO) {
            this.produtoDAO = produtoDAO;
        }

        @Override
        protected Void doInBackground(Produto... produtos) {
            produtoDAO.insert(produtos[0]);
            return null;
        }
    }

    private static class DeleteProdutoAsyncTask extends AsyncTask<Produto, Void, Void> {
        private ProdutoDAO produtoDAO;

        private DeleteProdutoAsyncTask(ProdutoDAO produtoDAO) {
            this.produtoDAO = produtoDAO;
        }

        @Override
        protected Void doInBackground(Produto... produtos) {
            produtoDAO.delete(produtos[0]);
            return null;
        }
    }

    private static class UpdateProdutoAsyncTask extends AsyncTask<Produto, Void, Void> {
        private ProdutoDAO produtoDAO;

        private UpdateProdutoAsyncTask(ProdutoDAO produtoDAO) {
            this.produtoDAO = produtoDAO;
        }

        @Override
        protected Void doInBackground(Produto... produtos) {
            produtoDAO.update(produtos[0]);
            return null;
        }
    }


}
