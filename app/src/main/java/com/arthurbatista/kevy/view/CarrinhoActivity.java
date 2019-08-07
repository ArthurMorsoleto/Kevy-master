package com.arthurbatista.kevy.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.arthurbatista.kevy.R;
import com.arthurbatista.kevy.model.Produto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    public static final String CARRINHO = "com.arthurbatista.kevy.view.CARRINHO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);



        //TODO: Náo estou conseguindo pegar a list<produto>
        //TODO: vou tentar pegar do sqlite direto relacioando os ids

        Intent intent = getIntent();
        if (intent.hasExtra(CARRINHO)) {

            List<Produto> listaCarrinho = (List<Produto>) intent.getSerializableExtra(CARRINHO);
            for (Produto pdt : listaCarrinho
            ) {
                Log.i("Carrinho", "onCreate: " + pdt.getNomeProduto());
            }

        }
    }
}
