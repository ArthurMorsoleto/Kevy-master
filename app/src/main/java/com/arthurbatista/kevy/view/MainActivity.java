package com.arthurbatista.kevy.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthurbatista.kevy.R;
import com.arthurbatista.kevy.model.Produto;
import com.arthurbatista.kevy.viewmodel.ProdutoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_PRODUTO_REQUEST = 1;
    private static final String TAG = "MAIN";

    public List<Produto> carrinho = new ArrayList<Produto>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ProdutoAdapter produtoAdapter = new ProdutoAdapter();

        recyclerView.setAdapter(produtoAdapter);

        ProdutoViewModel produtoViewModel = ViewModelProviders.of(this).get(ProdutoViewModel.class);
        produtoViewModel.getAllProdutos().observe(this, produtoAdapter::setProdutos);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddProdutoActivity.class);
            startActivityForResult(intent, ADD_PRODUTO_REQUEST);
        });

        produtoAdapter.setOnItemClickListener(new ProdutoAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Produto produto) {
                Intent intent = new Intent(MainActivity.this, AddProdutoActivity.class);
                intent.putExtra(AddProdutoActivity.EXTRA_PRODUTO, produto);
                startActivity(intent);
            }

            @Override
            public void onCartClick(Produto produto) {
                int isInCarrinho = carrinho.indexOf(produto);
                if (isInCarrinho == -1) {
                    carrinho.add(produto);
                }

                Snackbar.make(
                        findViewById(R.id.fab),
                        "Carrinho",
                        Snackbar.LENGTH_LONG
                ).setAction(
                        "Ver Carrinho",
                        view -> {
                            List<Integer> produtosId = new ArrayList<>();
                            for (Produto pdt : carrinho) {
                                produtosId.add(pdt.getId());
                            }
                            Intent intent = new Intent(MainActivity.this, CarrinhoActivity.class);
                            intent.putExtra(CarrinhoActivity.CARRINHO, (Serializable) produtosId);
                            startActivity(intent);
                        }
                ).show();
            }
        });
    }


}
