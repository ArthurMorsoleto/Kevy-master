package com.arthurbatista.kevy.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.arthurbatista.kevy.R;
import com.arthurbatista.kevy.model.Produto;
import com.arthurbatista.kevy.viewmodel.ProdutoViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class AddProdutoActivity extends AppCompatActivity {

    private ProdutoViewModel produtoViewModel;

    private EditText edtNome;
    private EditText edtPreco;
    private NumberPicker nbmQuantidade;
    private ImageView imgViewProduto;
    private EditText edtDescricao;

    private Boolean isUpdateOrDelete = false;
    private int produtoToUpdateOrDelete = 0;

    final int REQUEST_CODE_GALLERY = 999;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public static final String EXTRA_PRODUTO = "com.arthurbatista.kevy.EXTRA_PRODUTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        edtNome = findViewById(R.id.edtNomeProduto);
        edtPreco = findViewById(R.id.edtPrecoProduto);
        nbmQuantidade = findViewById(R.id.nbmQuantidade);
        imgViewProduto = findViewById(R.id.imgViewProduto);
        edtDescricao = findViewById(R.id.edtDescricaoProduto);

        produtoViewModel = new ProdutoViewModel(getApplication());

        nbmQuantidade.setMinValue(1);
        nbmQuantidade.setMaxValue(100);
        nbmQuantidade.setValue(1);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        setTitle("Adicionar Produto");

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PRODUTO)) {
            setTitle("Atualizar produto");
            Produto produtoAtual = (Produto) intent.getSerializableExtra(EXTRA_PRODUTO);

            edtNome.setText(produtoAtual.getNomeProduto());

            edtPreco.setText(produtoAtual.getPrecoProduto());

            nbmQuantidade.setValue(produtoAtual.getQuantidadeProduto());

            edtDescricao.setText(produtoAtual.getDescricaoProduto());

            byte[] imgProduto = produtoAtual.getImagemProduto();

            imgViewProduto.setImageBitmap(
                    BitmapFactory.decodeByteArray(imgProduto, 0, imgProduto.length)
            );

            isUpdateOrDelete = true;

            produtoToUpdateOrDelete = produtoAtual.getId();
        }

        imgViewProduto.setOnClickListener(v -> {
            try {
                //Caixa de diálog - Escolha Camera ou Galeria
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProdutoActivity.this);
                dialog.setTitle("Por Favor");
                dialog.setMessage("Escolha uma opção para prosseguir:");
                dialog.setPositiveButton("Camera", (dialog1, which) -> {
                    ActivityCompat.requestPermissions(
                            AddProdutoActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE
                    );
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                });
                dialog.setNegativeButton("Galeria", (qdialog, which) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.requestPermissions(
                                AddProdutoActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY
                        );
                    }
                });
                dialog.create();
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_produto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_produto:
                salvarProduto();
                return true;
            case R.id.delete_produto:
                deletarProduto();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void deletarProduto() {
        if (isUpdateOrDelete) {
            Produto produto = (new Produto(
                    "",
                    0,
                    "",
                    "",
                    imageViewToByte(imgViewProduto)
            ));
            produto.setId(produtoToUpdateOrDelete);

            AlertDialog.Builder dialog = new AlertDialog.Builder(AddProdutoActivity.this);
            dialog.setTitle("Atenção");
            dialog.setMessage("Deseja excluir esse produto?");
            dialog.setPositiveButton("Sim", (dialog1, which) -> {
                produtoViewModel.delete(produto);
                Toast.makeText(AddProdutoActivity.this, "Produto deletado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            });
            dialog.setNegativeButton("Não", (dialog12, which) -> {

            });
            dialog.create();
            dialog.show();
        }
    }

    private void salvarProduto() {
        String precoProduto = edtPreco.getText().toString();
        String nomeProduto = edtNome.getText().toString();
        String descProduto = edtDescricao.getText().toString();
        int quantidadeProduto = nbmQuantidade.getValue();
        byte[] photoProduto = imageViewToByte(imgViewProduto);

        if (nomeProduto.trim().isEmpty()) {
            edtNome.requestFocus();
            Toast.makeText(this, "Insira o nome do produto", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (precoProduto.isEmpty()) {
            edtPreco.requestFocus();
            Toast.makeText(this, "Insira o preço do produto", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (photoProduto.length < 0) {
            imgViewProduto.requestFocus();
            Toast.makeText(this, "É necessário inserir uma imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        Produto produto = (new Produto(
                nomeProduto,
                quantidadeProduto,
                precoProduto,
                descProduto,
                photoProduto
        ));

        if (isUpdateOrDelete) {
            produto.setId(produtoToUpdateOrDelete);
            produtoViewModel.update(produto);
            Toast.makeText(this, "Produto Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
        }
        else {
            produtoViewModel.insert(produto);
            Toast.makeText(this, "Produto Salvo com Sucesso", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            return;
        }
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                assert uri != null;
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgViewProduto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgViewProduto.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /* METODOS DE SUPORTE */

    //metodo para transformar imagem em byteArray
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        bitmap = getResizedBitmap(bitmap, 480, 640);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    //método para alterar o tamanho do bitmap
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }


}
