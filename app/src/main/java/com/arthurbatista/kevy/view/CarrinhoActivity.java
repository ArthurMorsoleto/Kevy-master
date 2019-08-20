package com.arthurbatista.kevy.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.arthurbatista.kevy.R;
import com.arthurbatista.kevy.model.Produto;
import com.arthurbatista.kevy.viewmodel.ProdutoViewModel;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    public static List<Produto> carrinho = new ArrayList<>();

    public static final String CARRINHO = "com.arthurbatista.kevy.view.CARRINHO";

    public static final String TAG = "CARRINHO";

    public static int precoTotal = 0;

    public static int precoCarrinho = 0;

    private TextView txtPrecoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        txtPrecoTotal = findViewById(R.id.txtPrecoCarrinho);

        RecyclerView recyCarrinho = findViewById(R.id.recycleViewCarrinho);
        recyCarrinho.setLayoutManager(new LinearLayoutManager(this));
        recyCarrinho.setHasFixedSize(true);

        CarrinhoAdapter carrinhoAdapter = new CarrinhoAdapter();

        recyCarrinho.setAdapter(carrinhoAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra(CARRINHO)) {

            //TODO: Arrumar o preço do carrinho

            carrinhoAdapter.setProdutos(carrinho);

            for (Produto pdt : carrinho
                 ) {
                precoTotal = precoTotal + Integer.valueOf(pdt.getPrecoProduto());
            }
            txtPrecoTotal.setText(String.valueOf(precoTotal));
        }

        carrinhoAdapter.setOnItemClickListener(new CarrinhoAdapter.OnItemClickListener() {

            @Override
            public void onMoreButtonClick(Produto produto) {
                int index = carrinho.indexOf(produto);
                int quantidadeAtual = carrinho.get(index).getQuantidadeProduto() + 1;

                carrinho.get(index).setQuantidadeProduto(quantidadeAtual);

                precoTotal = precoTotal + Integer.valueOf(carrinho.get(index).getPrecoProduto());
                txtPrecoTotal.setText(String.valueOf(precoTotal));
            }

            @Override
            public void onLessButtonClick(Produto produto) {
                int index = carrinho.indexOf(produto);
                Produto pdtNoCarrinho = carrinho.get(index);

                if ((pdtNoCarrinho.getQuantidadeProduto() - 1) <= 0) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(CarrinhoActivity.this);
                    dialog.setTitle("Atenção");
                    dialog.setMessage("Deseja remover do carrinho?");
                    dialog.setPositiveButton("Sim", (dialog1, which) -> {
                        carrinho.remove(index);
                        carrinhoAdapter.setProdutos(carrinho);

                        precoTotal = precoTotal - Integer.valueOf(pdtNoCarrinho.getPrecoProduto());
                        txtPrecoTotal.setText(String.valueOf(precoTotal));

                        if (carrinho.size() == 0) {
                            precoTotal = 0;
                            finish();
                        }
                    });
                    dialog.setNegativeButton("Não", (dialog12, which) -> {
                    });
                    dialog.create();
                    dialog.show();

                } else {
                    int quantidadeAtual = pdtNoCarrinho.getQuantidadeProduto() - 1;
                    pdtNoCarrinho.setQuantidadeProduto(quantidadeAtual);
                    precoTotal = precoTotal - Integer.valueOf(pdtNoCarrinho.getPrecoProduto());
                    txtPrecoTotal.setText(String.valueOf(precoTotal));
                }
            }
        });
    }

}
