package com.andreb.luism.CipherHunt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String nome = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getString("userName","None");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Intent intent = new Intent(this, MainMenuActivity.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        updateTextViews(nome);
    }

    private void updateTextViews(String username){
        TextView usernameTitle = (TextView) findViewById(R.id.usernameProfile);
        TextView nome = (TextView) findViewById(R.id.nomeProfile);
        TextView email = (TextView) findViewById(R.id.emailProfile);
        TextView localidade = (TextView) findViewById(R.id.localidadeProfile);
        TextView nrDesafiosComecados = (TextView) findViewById(R.id.nrDesafiosComecadosProfile);
        TextView nrDesafiosConcluidos = (TextView) findViewById(R.id.nrDesafiosConcluidosProfile);

        usernameTitle.setText(username);

        Cursor c = getUserInformation();
        nome.setText(nome.getText().toString() + ": "+c.getString(0));
        email.setText(email.getText().toString() + ": "+c.getString(1));
        localidade.setText(localidade.getText().toString() + ": "+c.getString(2));
        c.close();

        updateTextView(nrDesafiosComecados, getUserChallengesRegistered());
        updateTextView(nrDesafiosConcluidos, getUserChallengesFinished());
    }

    private void updateTextView (TextView text, int value){
        text.setText(text.getText().toString()+": " +value);
    }

    private Cursor getUserInformation(){
        String userId = getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser",0)+"";

        String[] projection = { CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_NOME,
                CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_EMAIL,
                CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_LOCALIDADE};
        String tableName = CipherHuntDbContract.TabelaUtilizadores.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_ID + " = ? ";
        String[] selectionArgs = { userId };

        Cursor c = CipherHuntDbSingleton.getInstance().searchDb( tableName, projection, selection, selectionArgs);
        c.moveToFirst();

        return c;
    }

    private int getUserChallengesRegistered(){
        String query =
                "Select id_utilizador" +
                        " From Utilizador_Desafio Where "+
                        " id_utilizador=?";

        Cursor c = CipherHuntDbSingleton.getInstance().searchDbRaw(query, new String[] { getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser",-1)+"" });
        int count = c.getCount();
        c.close();
        return count;
    }

    private int getUserChallengesFinished(){
        String query =
                "Select id, nome, numeroEnigmas, num_enigmas_concluidos" +
                        " From Desafio INNER JOIN Utilizador_Desafio ON id = id_desafio"+
                        " AND id_utilizador=? AND num_enigmas_concluidos = numeroEnigmas";

        Cursor c = CipherHuntDbSingleton.getInstance().searchDbRaw(query, new String[] { getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser",-1)+"" });
        int count = c.getCount();
        c.close();
        return count;
    }
}
