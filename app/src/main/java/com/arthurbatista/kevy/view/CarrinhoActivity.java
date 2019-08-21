package com.arthurbatista.kevy.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthurbatista.kevy.R;
import com.arthurbatista.kevy.model.Produto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//TODO: MANEIRA DE COMPARTILHAR/SALVAR O CARRINHO

public class CarrinhoActivity extends AppCompatActivity {

    public static List<Produto> carrinho = new ArrayList<>();

    public static final String CARRINHO = "com.arthurbatista.kevy.view.CARRINHO";

    public static final String TAG = "CARRINHO";

    public static float precoCarrinho = 0;

    private TextView txtPrecoTotal;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00#");
    private String precoFormatado = decimalFormat.format(precoCarrinho);

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
            carrinhoAdapter.setProdutos(carrinho);
            Log.i(TAG, "Preco init " + precoCarrinho);
            precoFormatado = "R$ " + decimalFormat.format(precoCarrinho);
            txtPrecoTotal.setText(precoFormatado);
        }

        carrinhoAdapter.setOnItemClickListener(new CarrinhoAdapter.OnItemClickListener() {

            @Override
            public void onMoreButtonClick(Produto produto) {
                int index = carrinho.indexOf(produto);
                int quantidadeAtual = carrinho.get(index).getQuantidadeProduto() + 1;

                carrinho.get(index).setQuantidadeProduto(quantidadeAtual);

                precoCarrinho = precoCarrinho + Float.valueOf(carrinho.get(index).getPrecoProduto().substring(2).replace(',', '.'));
                precoFormatado = "R$ " + decimalFormat.format(precoCarrinho);

                txtPrecoTotal.setText(precoFormatado);

                Log.i(TAG, "Preco depois de adicionar " + precoCarrinho);
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

                        precoCarrinho = precoCarrinho - Float.valueOf(pdtNoCarrinho.getPrecoProduto().substring(2).replace(',', '.'));
                        precoFormatado = "R$ " + decimalFormat.format(precoCarrinho);

                        txtPrecoTotal.setText(precoFormatado);

                        Log.i(TAG, "Preco depois de remover " + precoCarrinho);

                        if (carrinho.size() == 0) {
                            precoCarrinho = 0;
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
                    precoCarrinho = precoCarrinho - Float.valueOf(pdtNoCarrinho.getPrecoProduto().substring(2).replace(',', '.'));

                    precoFormatado = "R$ " + decimalFormat.format(precoCarrinho);

                    txtPrecoTotal.setText(precoFormatado);

                    Log.i(TAG, "Preco depois de remover " + precoCarrinho);
                }
            }
        });
    }

}
