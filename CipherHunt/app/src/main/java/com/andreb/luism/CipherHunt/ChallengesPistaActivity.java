package com.andreb.luism.CipherHunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChallengesPistaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_pista);
        String pista = getIntent().getStringExtra(ChallengesEnigmaActivity.EXTRA_MESSAGE);
        if (pista == null){
            pista = getIntent().getStringExtra(ChallengesProfileActivity.EXTRA_MESSAGE);
        }
        ((TextView)findViewById(R.id.textPista)).setText(pista);
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
