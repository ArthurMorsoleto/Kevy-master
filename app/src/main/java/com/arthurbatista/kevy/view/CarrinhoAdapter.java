package com.arthurbatista.kevy.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arthurbatista.kevy.R;
import com.arthurbatista.kevy.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ProdutoCarrinhoHolder> {

    private List<Produto> produtosCarrinho = new ArrayList<>();

    private CarrinhoAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public ProdutoCarrinhoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrinho, parent, false);
        return new ProdutoCarrinhoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoCarrinhoHolder holder, int position) {
        Produto produtoAtual = produtosCarrinho.get(position);

        byte[] produtoImage = produtoAtual.getImagemProduto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(produtoImage, 0, produtoImage.length);
        holder.imageViewImagemProduto.setImageBitmap(bitmap);

        holder.textViewNome.setText(produtoAtual.getNomeProduto());
        holder.textViewPreco.setText(String.valueOf(produtoAtual.getPrecoProduto()));

        holder.textQuantidade.setText(String.valueOf(produtoAtual.getQuantidadeProduto()));
    }

    @Override
    public int getItemCount() {
        return produtosCarrinho.size();
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtosCarrinho = produtos;
        notifyDataSetChanged();
    }

    class ProdutoCarrinhoHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewImagemProduto;
        private TextView textViewNome;
        private TextView textViewPreco;
        private TextView textQuantidade;
        private Button btnMore;
        private Button btnLess;

        ProdutoCarrinhoHolder(@NonNull View itemView) {
            super(itemView);
            imageViewImagemProduto = itemView.findViewById(R.id.imgProduto);
            textViewNome = itemView.findViewById(R.id.txtNomeProduto);
            textViewPreco = itemView.findViewById(R.id.txtPrecoProduto);
            btnLess = itemView.findViewById(R.id.btn_less);
            btnMore = itemView.findViewById(R.id.btn_more);
            textQuantidade = itemView.findViewById(R.id.txtQuantidadeToCarrinho);

            btnMore.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onMoreButtonClick(produtosCarrinho.get(position));
                    textQuantidade.setText(String.valueOf(produtosCarrinho.get(position).getQuantidadeProduto()));
                }
            });

            btnLess.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onLessButtonClick(produtosCarrinho.get(position));
                    textQuantidade.setText(String.valueOf(produtosCarrinho.get(position).getQuantidadeProduto()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onMoreButtonClick(Produto produto);

        void onLessButtonClick(Produto produto);
    }

    public void setOnItemClickListener(CarrinhoAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
