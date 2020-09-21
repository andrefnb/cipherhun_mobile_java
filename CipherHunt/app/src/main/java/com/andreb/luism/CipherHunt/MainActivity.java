package com.andreb.luism.CipherHunt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoActionBarStyle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.activity_main).setBackgroundResource(R.drawable.background);
        if (CipherHuntDbSingleton.getInstance().getDbHelper() == null){
            CipherHuntDbSingleton.getInstance().setDbHelper(getApplicationContext());
            CipherHuntDbSingleton.getInstance().povoarDB();
            this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).edit().putBoolean("povoado", true).apply();
        }
        if(!this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getString("userName","None").equals("None")){
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToRegistar(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }
}
