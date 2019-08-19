package com.arthurbatista.kevy.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.arthurbatista.kevy.R;
import com.arthurbatista.kevy.model.Produto;
import com.arthurbatista.kevy.model.ProdutoDAO;
import com.arthurbatista.kevy.viewmodel.ProdutoViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    public static final String CARRINHO = "com.arthurbatista.kevy.view.CARRINHO";
    private static final String TAG = "CARRINHO";
    private List<Produto> carrinhoFinal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        Intent intent = getIntent();
        if (intent.hasExtra(CARRINHO)) {

            List<Integer> listaCarrinhoId = (List<Integer>) intent.getSerializableExtra(CARRINHO);


            ProdutoViewModel produtoViewModel = ViewModelProviders.of(this).get(ProdutoViewModel.class);

            LiveData<List<Produto>> list = produtoViewModel.getAllProdutos();

            list.observe(this, produtos -> {
                if (!(produtos == null || produtos.size() == 0)) {

                    for (Integer id : listaCarrinhoId
                    ) {
                        for (Produto pdt : produtos
                        ) {
                            if (id == pdt.getId()) {
                               carrinhoFinal.add(pdt);
                            }
                        }
                    }
                    Log.i(TAG, "carrinhoFinal: " + carrinhoFinal);

                    //TODO: FINALIZAR O LAYOUT DO CARRINHO +  FOOTER DE PREÇO
                }
            });
        }
    }


}
