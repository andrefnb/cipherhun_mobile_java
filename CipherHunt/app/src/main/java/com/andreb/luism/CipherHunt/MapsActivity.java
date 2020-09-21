package com.andreb.luism.CipherHunt;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<Integer> listaDesafiosIds;
    private List<String> listaDesafios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarMaps);
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

        ImageButton camera = (ImageButton) findViewById(R.id.buttonCamera);
        final Intent intent2 = new Intent(this, QrCodeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        if(!this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getString("userName","None").equals("None")) {
            getDesafioPeloUser();
            List<String> listaLocalizacoes = new ArrayList<>();
            if (listaDesafiosIds != null) {
                for (int i = 0; i < listaDesafiosIds.size(); i++) {
                    int idDesafio = listaDesafiosIds.get(i);
                    String localizacao = getLocalizacaoPorIdDesafioEIdUser(idDesafio);
                    if (localizacao != null){
                        listaLocalizacoes.add(localizacao);
                    }
                }

                List<LatLng> listaLocalizacoesFinal = new ArrayList<>();

                for (int i = 0; i < listaLocalizacoes.size(); i++) {

                    StringTokenizer tokenizer = new StringTokenizer(listaLocalizacoes.get(i), ":");

                    String lat = tokenizer.nextToken();
                    String lng = tokenizer.nextToken();

                    LatLng wtv = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    listaLocalizacoesFinal.add(wtv);
                }

                for (int k = 0; k < listaLocalizacoesFinal.size(); k++) {
                    mMap.addMarker(new MarkerOptions()
                            .position(listaLocalizacoesFinal.get(k))
                            .title(listaDesafios.get(k)));
                }

            }

            // Set a listener for marker click.
            mMap.setOnMarkerClickListener(this);
        }
    }


    public void getDesafioPeloUser(){
        /*String idUser = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser", -1)+"";
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();
        String[] projection = { CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO };
        String tableName = CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR + " = ?";
        String[] selectionArgs = { idUser };
        Cursor c = db.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );*/
        String query =
                "Select id, nome, num_enigmas_concluidos" +
                        " From Desafio INNER JOIN Utilizador_Desafio ON id = id_desafio"+
                        " AND id_utilizador=?";

        Cursor c = CipherHuntDbSingleton.getInstance().searchDbRaw(query, new String[] { getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser",-1)+"" });

        c.moveToFirst();

        listaDesafiosIds = new ArrayList<>();
        listaDesafios = new ArrayList<>();

        try {
            do {
                listaDesafiosIds.add(Integer.parseInt(c.getString(0)));
                listaDesafios.add(getString(R.string.challenge)+": "+c.getString(1)+" - "+getString(R.string.enigma)+(c.getInt(2)+1));
            } while (c.moveToNext());

        }catch(CursorIndexOutOfBoundsException e){
            listaDesafiosIds = null;
        }
        c.close();
    }

    private String getLastEnigmaLocPerDesafio (int idDesafio, int nrEnigmasConcluidos){
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();
        String[] projection = { CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_NUM_ENIGMAS};
        String tableName = CipherHuntDbContract.TabelaDesafio.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_ID + " = ? ";
        String[] selectionArgs = { idDesafio+"" };
        Cursor c = db.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );
        String nrEnigmasTotalDesafio = "0";
        try {
            c.moveToFirst();
            nrEnigmasTotalDesafio = c.getString(0);
        }catch(NullPointerException e){
            return null;
        }

        if(nrEnigmasConcluidos == Integer.parseInt(nrEnigmasTotalDesafio)){
            return null;
        }

        String[] projection1 = { CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_ID,
                CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_LOCALIZACAO};
        String tableName1 = CipherHuntDbContract.TabelaEnigma.TABLE_NAME;
        String selection1 = CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_ID_DESAFIO + " = ? ";
        String[] selectionArgs1 = { idDesafio+"" };
        Cursor c1 = db.query(
                tableName1,
                projection1,
                selection1,
                selectionArgs1,
                null, null, null
        );
        String locEnigmaLastDesafio = "0";
        try {
            c1.move(nrEnigmasConcluidos+1);
            locEnigmaLastDesafio = c1.getString(1);
        }catch(NullPointerException e){
            return null;
        }
        c1.close();
        return locEnigmaLastDesafio;
    }

    private String getLocalizacaoPorIdDesafioEIdUser (int idDesafio){
        int idUser = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser", -1);
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();
        String[] projection = { CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS };
        String tableName = CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR + " = ? AND " +
                CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO + " = ?";
        String[] selectionArgs = { idUser+"", idDesafio+""};
        Cursor c = db.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );
        c.moveToFirst();
        String nrEnigmasConcluidos = c.getString(0);
        c.close();
        return getLastEnigmaLocPerDesafio(idDesafio, Integer.parseInt(nrEnigmasConcluidos));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}