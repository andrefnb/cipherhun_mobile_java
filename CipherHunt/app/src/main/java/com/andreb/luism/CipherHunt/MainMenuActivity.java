package com.andreb.luism.CipherHunt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView bemVindo = (TextView) findViewById(R.id.userName);
        String username = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getString("userName","None");
        bemVindo.setText(username);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
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

    public void goToMain(View view){
        SharedPreferences sp = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE);
        sp.edit().remove("idUser").apply();
        sp.edit().remove("userName").apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
