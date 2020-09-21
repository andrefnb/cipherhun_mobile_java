package com.andreb.luism.CipherHunt;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Intent intent = new Intent(this, MainActivity.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void registar(View view){
        String username = ((EditText) findViewById(R.id.usernameRegisto)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordRegisto)).getText().toString();
        String nome = ((EditText) findViewById(R.id.nomeRegisto)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailRegisto)).getText().toString();
        String localidade = ((EditText) findViewById(R.id.localidadeRegisto)).getText().toString();

        if(areInformationsValid(username,password,nome,email,localidade)){
            atualizarBaseDeDados(username,password,nome,email,localidade);

            Cursor c = getUserId(CipherHuntDbSingleton.getInstance().getReadableDb(), username);

            SharedPreferences sp = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE);
            sp.edit().putInt("idUser",c.getInt(0)).apply();
            sp.edit().putString("userName",username).apply();
            c.close();

            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle(R.string.sucesso);
            builder.setMessage(R.string.mensagemSucesso);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();

            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    private boolean areInformationsValid(String username, String password, String nome, String email, String localidade){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(R.string.erro);

        String erro = "";
        boolean valid = true;
        if(username.length() == 0 || password.length() == 0){
            erro += getString(R.string.mensagemErro1);
            valid = false;
        }
        if(!(!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            erro += getString(R.string.mensagemErro2);
            valid = false;
        }
        String query = "Select id From Utilizador Where username=?";
        Cursor c = CipherHuntDbSingleton.getInstance().searchDbRaw(query, new String[] { username });
        if(c.getCount() != 0){
            erro += getString(R.string.mensagemErro3);
            valid = false;
            c.close();
        }
        String query1 = "Select id From Utilizador Where email=?";
        Cursor c1 = CipherHuntDbSingleton.getInstance().searchDbRaw(query1, new String[] { email });
        if(c1.getCount() != 0){
            erro += getString(R.string.mensagemErro4);
            valid = false;
            c1.close();
        }
        if(!valid){
            builder.setMessage(erro);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();
        }
        return valid;
    }

    private void atualizarBaseDeDados(String username, String password, String nome, String email, String localidade){
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getWritableDb();

        ContentValues values = new ContentValues();

        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_USERNAME, username);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_PASSWORD, password);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_NOME, nome);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_EMAIL, email);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_LOCALIDADE, localidade);

        db.insert(CipherHuntDbContract.TabelaUtilizadores.TABLE_NAME, null, values);
    }

    private Cursor getUserId(SQLiteDatabase db, String username){
        String[] projection1 = { CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_ID};
        String tableName1 = CipherHuntDbContract.TabelaUtilizadores.TABLE_NAME;
        String selection1 = CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs1 = { username };

        Cursor c1 = db.query(
                tableName1,
                projection1,
                selection1,
                selectionArgs1,
                null, null, null
        );
        c1.moveToFirst();
        return c1;
    }
}
