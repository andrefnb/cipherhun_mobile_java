package com.andreb.luism.CipherHunt;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.zxing.Result;

import java.util.StringTokenizer;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public final static String EXTRA_MESSAGE = "com.andreb.luism.CipherHunt.MESSAGE";
    private ZXingScannerView mScanerView;
    private int idUser = 0;
    private Intent mapa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScanerView = new ZXingScannerView(this);
        mScanerView.setResultHandler(this);
        mScanerView.startCamera();
        // Inflate the layout for this
        mapa = getIntent();
        idUser = this.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getInt("idUser", -1);
        setContentView(mScanerView);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mScanerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        //Do anything with result here :D

        mScanerView.stopCamera();
        Log.w("handleResult", result.getText());
        String idDesafio = "";
        String numEnigma = "";
        String password = "";
        boolean isValid = false;
        try{
            String resultado = result.getText().toString();
            StringTokenizer st = new StringTokenizer(resultado,"|");

            idDesafio = st.nextToken();
            numEnigma = st.nextToken();
            password = st.nextToken();
            isValid = isQrCodeValid(idDesafio, numEnigma);
        }catch (Exception e){

        }

        if(isValid){
            SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();

            String[] projection = { CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PASSWORD,
                    CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PERGUNTA,
                    CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_RESPOSTA,
                    CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PISTA };
            String tableName = CipherHuntDbContract.TabelaEnigma.TABLE_NAME;
            String selection = CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_ID_DESAFIO + " = ? ";
            String[] selectionArgs = { idDesafio };

            Cursor c = db.query(
                    tableName,
                    projection,
                    selection,
                    selectionArgs,
                    null, null, null
            );
            c.moveToPosition(Integer.parseInt(numEnigma)-1);

            if(c.getString(0).equals(password)){

                atualizarDesafio(idDesafio, Integer.parseInt(numEnigma));

                String [] enigma = { c.getString(1), c.getString(2), idDesafio, c.getString(3) };

                Intent intent = new Intent(this, ChallengesEnigmaActivity.class);
                intent.putExtra(EXTRA_MESSAGE, enigma);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("QR Válido");
                builder.setMessage("Desbloqueou o nivel "+numEnigma);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
                finish();
                startActivity(intent);
                c.close();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("QR Inválido");
            builder.setMessage("O qr que está a tentar ler não é válido");
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();
            Intent intent = new Intent(this, MapsActivity.class);
            finish();
            startActivity(intent);
        }

        //Resume scanning
        mScanerView.resumeCameraPreview(this);
    }

    private void atualizarDesafio(String idDesafio, int numEnigma){
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();

        ContentValues values = new ContentValues();
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS, numEnigma);
        String selection = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR + " =? AND " +
                CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO + " =?";
        String [] selectionArgs = { idUser+"", idDesafio};

        db.update(
                CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private boolean isQrCodeValid(String idDesafio, String idEnigma){
        SQLiteDatabase db = CipherHuntDbSingleton.getInstance().getReadableDb();

        String[] projection = { CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS,
                CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS };
        String tableName = CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME;
        String selection = CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR + " = ? AND " +
                CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO + " = ? ";
        String[] selectionArgs = { idUser+"", idDesafio };

        Cursor c = db.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );
        c.moveToFirst();

        if(c.getCount() == 0){
            //despoletar uma mensagem em que o utilizador não se encontra inscrito neste desafio
            c.close();
            return false;
        }else if(Integer.parseInt(idEnigma) <= c.getInt(0)){
            //despoletar uma mensagem sobre já ter debloqueado este nível
            c.close();
            return false;
        } else if(Integer.parseInt(idEnigma) > c.getInt(0)+1){
            //despoletar uma mensagem sobre ainda nao ter chegado àquele nível ainda
            c.close();
            return false;
        } else if(Integer.parseInt(idEnigma) == c.getInt(0)+1 && c.getInt(0) > c.getInt(1)){
            //despoletar uma mensagem sobre ainda nao ter concluido o nivel anterior
            c.close();
            return false;
        }else{
            c.close();
            return true;
        }
    }
}
