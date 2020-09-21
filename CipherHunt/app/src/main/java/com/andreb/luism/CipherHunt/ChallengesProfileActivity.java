package com.andreb.luism.CipherHunt;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChallengesProfileActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.andreb.luism.CipherHunt.MESSAGE";
    private String numEnigma = "";
    private int userId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_profile);
        userId = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser",0);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarChallengesProfile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Intent intent = new Intent(this, ChallengesActivity.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        String[] array = getIntent().getStringArrayExtra(ChallengesActivity.EXTRA_MESSAGE);
        meterInfos(array[0]);
    }

    private void meterInfos(String nomeD) {
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();

        String[] projection = { CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_NAME,
                CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_DESCRICAO,
                CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_NUM_ENIGMAS};
        String tableName = CipherHuntDbContract.TabelaDesafio.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_NAME + " = ? ";
        String[] selectionArgs = { nomeD };

        Cursor c = db.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );
        c.moveToFirst();

        TextView nome = (TextView) findViewById(R.id.nomeDesafio);
        TextView descricao = (TextView) findViewById(R.id.descricaoDesafio);
        TextView nrEnigmas = (TextView) findViewById(R.id.nrEnigmasDesafio);

        nome.setText(nome.getText().toString() + ": "+c.getString(0));
        descricao.setText(descricao.getText().toString() + ": "+c.getString(1));
        nrEnigmas.setText(nrEnigmas.getText().toString() + ": "+c.getString(2));

        String[] projection1 = { CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS,
                CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS};
        String tableName1 = CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME;
        String selection1 = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO + " = ? AND "+
                CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR +" = ? ";
        String[] selectionArgs1 = { getDesafioIdByName(nomeD)+"" , userId+""};

        Cursor c1 = db.query(
                tableName1,
                projection1,
                selection1,
                selectionArgs1,
                null, null, null
        );

        try {
            c1.moveToFirst();
            numEnigma = c1.getString(1);
            //codigo caso ja tenha acabado o desafio
            boolean jaConcluiu = false;

            if(Integer.parseInt(c1.getString(1)) == Integer.parseInt(c.getString(2))){
                ((TextView)findViewById(R.id.estadoDesafioPerfil)).setText(R.string.terminou_challenges_profile);
                findViewById(R.id.inscricaoDesafioPerfil).setVisibility(View.GONE);
                jaConcluiu = true;
            }
            final boolean jaConcluiuF = jaConcluiu;


            //codigo caso ja tenha iniciado o desafio

            final int idDesafio = getDesafioIdByName(nomeD);
            boolean serPista = false;
            final int numEnigmasDesbloqueados = Integer.parseInt(c1.getString(0));
            if(Integer.parseInt(c1.getString(0)) == Integer.parseInt(c1.getString(1))){
                serPista = true;
            }
            final boolean serPistaF = serPista;

            if(!jaConcluiuF) {
                ((TextView) findViewById(R.id.estadoDesafioPerfil)).setText(getString(R.string.DesafioInfo) + c1.getString(1) + getString(R.string.DesafioInfo1));
                ((Button) findViewById(R.id.inscricaoDesafioPerfil)).setText(R.string.continuarDesafio);

                c.close();

                findViewById(R.id.inscricaoDesafioPerfil).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        //ir buscar o id do enigma pela ordem do cursor do enigma com o id do desafio
                        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();
                        String[] projection = {CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_ID,
                                CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PERGUNTA,
                                CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_RESPOSTA,
                                CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PISTA
                        };
                        String tableName = CipherHuntDbContract.TabelaEnigma.TABLE_NAME;
                        String selection = CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_ID_DESAFIO + " = ? ";
                        String[] selectionArgs = {idDesafio + ""};
                        Cursor c = db.query(
                                tableName,
                                projection,
                                selection,
                                selectionArgs,
                                null, null, null
                        );
                        if(numEnigmasDesbloqueados>0){
                            c.move(numEnigmasDesbloqueados);
                        }else{
                            c.moveToFirst();
                        }

                        String [] arrayEnigma = {c.getString(1), c.getString(2), idDesafio+"" ,c.getString(3)};
                        String arrayPista = c.getString(3);
                        if(numEnigmasDesbloqueados == 0){
                            arrayPista = getString(R.string.primeiraPista);
                        }
                        //a tal condicao

                        if (serPistaF) {
                            Intent intent = new Intent(getApplicationContext(), ChallengesPistaActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, arrayPista);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ChallengesEnigmaActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, arrayEnigma);
                            startActivity(intent);
                        }
                        c.close();

                    }
                });
            }

        }catch(java.lang.RuntimeException e){

        }
        c1.close();
    }

    private int getDesafioIdByName (String nome){
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();
        String[] projection = { CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_ID};
        String tableName = CipherHuntDbContract.TabelaDesafio.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_NAME + " = ? ";
        String[] selectionArgs = { nome };
        Cursor c = db.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );
        c.moveToFirst();
        return Integer.parseInt(c.getString(0));
    }

    public void inscrever(View view){

        int idDesafio = getDesafioIdByName(getIntent().getStringArrayExtra(ChallengesActivity.EXTRA_MESSAGE)[0]);

        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getWritableDb();

        ContentValues values = new ContentValues();

        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR, userId);
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO, idDesafio);
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS, 0);
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS, 0);

        db.insert(CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME, null, values);

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void goToMainMenu(View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
