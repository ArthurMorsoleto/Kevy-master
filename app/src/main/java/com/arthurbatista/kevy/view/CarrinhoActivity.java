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

    private List<Produto> carrinhoFinal = new ArrayList<>();

    private List<Integer> listaCarrinhoId = new ArrayList<>();

    private int precoTotal = 0;

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

            listaCarrinhoId = (List<Integer>) intent.getSerializableExtra(CARRINHO);


            //TODO: REFAZER ESSA PORRA, PQ O LUAN MANDOU
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

                                carrinhoFinal.get(carrinhoFinal.indexOf(pdt)).setQuantidadeProduto(1);

                                precoTotal = precoTotal + Integer.valueOf(pdt.getPrecoProduto());

                                txtPrecoTotal.setText(String.valueOf(precoTotal));
                            }
                        }
                    }
                    carrinhoAdapter.setProdutos(carrinhoFinal);
                }
            });
        }


        carrinhoAdapter.setOnItemClickListener(new CarrinhoAdapter.OnItemClickListener() {

            @Override
            public void onMoreButtonClick(Produto produto) {
                int index = carrinhoFinal.indexOf(produto);
                int quantidadeAtual = carrinhoFinal.get(index).getQuantidadeProduto() + 1;
                carrinhoFinal.get(index).setQuantidadeProduto(quantidadeAtual);
                precoTotal = precoTotal + Integer.valueOf(carrinhoFinal.get(index).getPrecoProduto());
                txtPrecoTotal.setText(String.valueOf(precoTotal));
            }

            @Override
            public void onLessButtonClick(Produto produto) {
                int index = carrinhoFinal.indexOf(produto);
                if ((carrinhoFinal.get(index).getQuantidadeProduto() - 1) <= 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CarrinhoActivity.this);
                    dialog.setTitle("Atenção");
                    dialog.setMessage("Deseja remover do carrinho?");
                    dialog.setPositiveButton("Sim", (dialog1, which) -> {

                        carrinhoFinal.remove(index); //TODO: Refazer o carrinhoFinal, que está initul

                        carrinho.remove(index);
                        Log.i(TAG, TAG + carrinho);

                        carrinhoAdapter.setProdutos(carrinhoFinal);
                        if (carrinhoFinal.size() == 0) {
                            finish();
                        }

                    });
                    dialog.setNegativeButton("Não", (dialog12, which) -> {
                    });
                    dialog.create();
                    dialog.show();
                } else {
                    int quantidadeAtual = carrinhoFinal.get(index).getQuantidadeProduto() - 1;
                    carrinhoFinal.get(index).setQuantidadeProduto(quantidadeAtual);
                    precoTotal = precoTotal - Integer.valueOf(carrinhoFinal.get(index).getPrecoProduto());
                    txtPrecoTotal.setText(String.valueOf(precoTotal));
                }
            }
        });
    }

}
