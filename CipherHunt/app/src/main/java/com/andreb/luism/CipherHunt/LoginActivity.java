package com.andreb.luism.CipherHunt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarLogin);
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

    public void login(View view){

        String inputUsername = ((AutoCompleteTextView)findViewById(R.id.username)).getText().toString();
        String inputPassword = ((EditText)findViewById(R.id.password)).getText().toString();

        boolean isRegistered = verificarLogin(inputUsername, inputPassword);

        if(!isRegistered){
            TextView t = (TextView)findViewById(R.id.textViewError);
            t.setTextColor(Color.RED);
            t.setAlpha(1);
        }else{
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    private boolean verificarLogin (String username, String password){

        String[] projection = { CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_ID };
        String tableName = CipherHuntDbContract.TabelaUtilizadores.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_USERNAME + " = ? AND " +
                CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };

        Cursor c = CipherHuntDbSingleton.getInstance().searchDb(tableName, projection, selection, selectionArgs);

        c.moveToFirst();

        int count = c.getCount();

        if(count == 1){
            SharedPreferences sp = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE);
            sp.edit().putInt("idUser",c.getInt(0)).apply();
            sp.edit().putString("userName",username).apply();
            c.close();
            return true;
        }
        return false;
    }
}
