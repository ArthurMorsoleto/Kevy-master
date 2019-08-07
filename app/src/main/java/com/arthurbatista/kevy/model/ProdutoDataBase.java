package com.arthurbatista.kevy.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = { Produto.class }, version = 1 , exportSchema = false)
public abstract class ProdutoDataBase extends RoomDatabase  {

    private static ProdutoDataBase instance;

    public abstract ProdutoDAO produtoDAO();

    public static synchronized ProdutoDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProdutoDataBase.class,
                    "produto_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private ProdutoDAO produtoDAO;

        private PopulateDbAsyncTask(ProdutoDataBase db){
            produtoDAO = db.produtoDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            /*produtoDAO.insert(new Produto(
                    "Produto 1",
                    5,
                    70.00,
                    "Produto muito legal",
                    null

            ));
            produtoDAO.insert(new Produto(
                    "Produto 2",
                    3,
                    20.00,
                    "Produto muito barato",
                    null
            ));
            produtoDAO.insert(new Produto(
                    "Produto 3",
                    10,
                    70.00,
                    "Produto muito feio",
                    null
            ));*/
            return null;
        }
    }
}
