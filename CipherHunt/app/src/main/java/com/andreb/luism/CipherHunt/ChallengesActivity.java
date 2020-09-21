package com.andreb.luism.CipherHunt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChallengesActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.andreb.luism.CipherHunt.MESSAGE";
    private String username = "";
    private boolean concluido = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarChallenges);
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

        addChallenges();
        username = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getString("userName","None");
    }

    //fazer o adicionar dinamico
    private void addChallenges(){

        LinearLayout lista = (LinearLayout) findViewById(R.id.challengesList);
        String query =
                "Select id, nome, numeroEnigmas, num_enigmas_concluidos" +
                        " From Desafio LEFT JOIN Utilizador_Desafio ON id = id_desafio AND id_utilizador=?";
        Cursor c = CipherHuntDbSingleton.getInstance().searchDbRaw(query, new String[] { getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser",-1)+"" });
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++){
            boolean inscrito = (!c.isNull(3));
            String nome = c.getString(1);
            String estado = "";
            int drawable = R.drawable.no_preview;
            int teste = getApplicationContext().getResources().getIdentifier("color_"+c.getString(0),"drawable",getApplicationContext().getPackageName());

            if(inscrito){
                if(teste != 0){
                    drawable = getApplicationContext().getResources().getIdentifier("color_"+c.getString(0),"drawable",getApplicationContext().getPackageName());
                }
                estado = c.getString(3)+"/"+c.getString(2);
                concluido = c.getString(3).equals(c.getString(2));
            }else if(teste != 0){
                drawable = getApplicationContext().getResources().getIdentifier("blocked_"+c.getString(0),"drawable",getApplicationContext().getPackageName());
            }

            createChallenge(lista, nome, inscrito, estado, drawable);
            c.moveToNext();
        }
        addOnClicks(lista);
    }

    private void createChallenge(LinearLayout lista, String titulo, boolean inscrito, String numeros, int image){
        GridLayout gridLayout = grid();
        gridLayout.addView(frame(image));
        gridLayout.addView(texto(titulo,10,10,0, Gravity.TOP));
        if(inscrito){
            gridLayout.addView(texto(numeros,0,10,10,Gravity.BOTTOM | Gravity.END));
        }else{
            gridLayout.addView(image(R.drawable.blocked,true));
        }
        lista.addView(gridLayout);
    }

    private GridLayout grid (){
        GridLayout grid = new GridLayout(this);
        if(!concluido){
            grid.setBackgroundColor(Color.parseColor("#000000"));
        }else{
            grid.setBackgroundColor(Color.parseColor("#006f00"));
        }

        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mp.bottomMargin = 20;
        grid.setLayoutParams(mp);
        return grid;
    }

    private FrameLayout frame(int image){
        FrameLayout frame = new FrameLayout(this);
        if(concluido){
            frame.setForeground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gradient_finished));
        }else{
            frame.setForeground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gradient));
        }
        frame.addView(image(image, false));
        return frame;
    }

    private ImageView image(int image, boolean layout){
        ImageView imagem = new ImageView(this);
        imagem.setImageResource(image);
        if (layout){
            imagem.setLayoutParams(layoutParams(0,0,10,10,Gravity.BOTTOM | Gravity.END));
        }
        return imagem;
    }

    private TextView texto (String txt, int marginTop, int marginRight, int marginBottom, int gravity){
        TextView texto = new TextView(this);
        texto.setText(txt);
        texto.setTextColor(Color.parseColor("#FFFFFF"));
        texto.setLayoutParams(layoutParams(0, marginTop, marginRight, marginBottom, gravity));
        return texto;
    }

    private GridLayout.LayoutParams layoutParams(int marginLeft, int marginTop, int marginRight, int marginBottom, int gravity){
        GridLayout.LayoutParams gr = new GridLayout.LayoutParams();
        gr.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        gr.height = GridLayout.LayoutParams.WRAP_CONTENT;
        gr.width = GridLayout.LayoutParams.WRAP_CONTENT;
        gr.setGravity(gravity);

        return gr;
    }

    private void addOnClicks(LinearLayout lista){
        final LinearLayout listaF = lista;
        for (int i = 0; i < lista.getChildCount(); i++) {
            final int valor = i;
            View v = lista.getChildAt(i);
            v.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    String nomeDesafio = ((TextView)((GridLayout)listaF.getChildAt(valor)).getChildAt(1)).getText().toString();
                    Intent intent = new Intent(getApplicationContext(), ChallengesProfileActivity.class);
                    String[] array = {nomeDesafio, username};
                    intent.putExtra(EXTRA_MESSAGE, array);
                    startActivity(intent);
                }
            });
        }
    }

    public void goToMainMenu(View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
