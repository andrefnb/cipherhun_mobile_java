package com.andreb.luism.CipherHunt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class ChallengesEnigmaActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.andreb.luism.CipherHunt.MESSAGE";
    private String [] enigma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_enigma);

        enigma = getIntent().getStringArrayExtra(QrCodeActivity.EXTRA_MESSAGE);
        if(enigma == null){
            enigma = getIntent().getStringArrayExtra(ChallengesProfileActivity.EXTRA_MESSAGE);
        }
        ((TextView)findViewById(R.id.textEnigma)).setText(enigma[0]);
    }

    public void verificarResposta(View view){
        String resposta = ((TextView)findViewById(R.id.enigmaResposta)).getText().toString();
        if(resposta.equals(enigma[1]) ||
                resposta.equals(enigma[1].toLowerCase())){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            atualizarDb();
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle(R.string.titulo_enigma);
            builder.setMessage(R.string.mensagem_enigma);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();
            Intent intent = new Intent(this, ChallengesPistaActivity.class);
            intent.putExtra(EXTRA_MESSAGE, enigma[3]);
            finish();
            startActivity(intent);
        }
    }

    private void atualizarDb(){
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();

        String coluna1 = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS;
        String coluna2 = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS;
        String coluna3 = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR;
        String coluna4 = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO;

        int idUSer = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser", -1);
        String idDesafio =  enigma[2];


        db.execSQL("UPDATE "+CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME+" SET "+
                coluna1+"="+coluna2+" where "+coluna3+"="+idUSer+" and "+coluna4+"="+idDesafio);

    }

    public void goToMainMenu(View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goToChallenges(View view){
        Intent intent = new Intent(this, ChallengesActivity.class);
        startActivity(intent);
    }

    public void goToMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
