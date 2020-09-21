package com.andreb.luism.CipherHunt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by luism on 02/12/2016.
 */

public class CipherHuntDbSingleton {
    private static CipherHuntDbSingleton instance = new CipherHuntDbSingleton();

    private CipherHuntDbHelper dbHelper = null;
    private boolean isPovoado = false;

    public CipherHuntDbSingleton() {
    }

    public static CipherHuntDbSingleton getInstance() {
        return instance;
    }

    public CipherHuntDbHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(Context context) {
        this.dbHelper = new CipherHuntDbHelper(context);
        this.isPovoado = context.getSharedPreferences("com.andreb.luism", Context.MODE_PRIVATE).getBoolean("povoado",false);
    }

    public SQLiteDatabase getReadableDb() {
        return this.dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDb() {
        return this.dbHelper.getWritableDatabase();
    }

    public void povoarDB() {

        if(isPovoado){
            return;
        }

        addUtilizador("admin", "admin", "André", "andrebastos2601@gmail.com", "Setúbal, Portugal");
        addUtilizador("notadmin", "notadmin", "Luís", "luismestre@outlook.com", "Setúbal, Portugal");
        addUtilizador("teste", "teste", "teste dos testes", "teste-iculo@gmail.com", "Somewhere");
        addUtilizador("Winner", "123", "teste dos testes", "teste-culo@gmail.com", "Somewhere");

        addDesafio("Teste","Este é o desafio teste, o desafio usado para testar a aplicação",2);
        addDesafio("Gravity Falls", "Desafio sobre a série Gravity Falls", 3);
        addDesafio("IPS Hunt", "Um desafio que te obrigará a interagir com" +
                " outros cursos e fazer-te percorrer e conhecer o resto do Campus IPS.",3);

        addEnigmaPista("thisone", "Quanto é 1+1?", "2", "Lê o outro código QR que tens ao lado", "38.721805:-8.839251", 1);
        addEnigmaPista("number2", "Esta é a resposta.", "Esta", "Parabéns, resolveste o Desafio Teste", "38.721805:-8.839251", 1);

        addEnigmaPista("gf1","Swcn g q crgnkfq fcu rgtuqpcigpu rtkpekrcku fq ugtkg Itcxkva Hcnnu?",
                "Pines","Proxima pista nao se encontra muito longe daqui. Olha à tua volta e descobrirás",
                "37.721805:-8.839251",2);
        addEnigmaPista("gf2","Dhny r n nyphaun dhr Fgnayrl Cvarf r Ovyy Pvcure punznz nb Sbeq?","Sixer",
                "A última pista descobrirás debaixo de uma árvore. Por onde te encontras é só seguires as setas.",
                "38.721805:-7.839251",2);
        addEnigmaPista("gf3","Cgmx q a bduyquda zayq pq Pubbqd Buzqe?","Mason",
                "Parabéns, resolveste o desafio de Gravity Falls. És um perito a resolver enigmas.",
                "38.821805:-8.039251",2);

        addEnigmaPista("estRules","Qual é a melhor Escola (sigla)?","EST","Saúde todos precisamos\n" +
                "De pessoas para nos tratar,\n" +
                "Que ajudam quando temos dores,\n" +
                "Ou quando estamos a espirrar.\n\n" +
                "Podes precisar de ajuda\n" +
                "Para encontrar o próximo enigma,\n" +
                "Pergunta no corredor\n" +
                "Terás a resposta do paradigma.","38.521802:-8.838534",3);
        addEnigmaPista("essRules","Complete a frase: Haja _____!","Saúde","Já soubeste chegar aqui,\n" +
                "E resolveste já 2 enigmas!!! Mas ainda ainda não acabou ;)\n" +
                "No próximo local encontrarás a chave para o último nível.\n" +
                "No final quando o solucionares, acabarás o desafio.\n\n" +
                "Este encontra-se num local\n" +
                "Onde se estuda para ensinar.\n" +
                "Será necessário mais pistas?\n" +
                "Tu ao sítio irás chegar!","38.521802:-8.838534",3);
        addEnigmaPista("eseRules","Qual o nome do curso mais 'social' desta escola?","Comunicação Social",
                "Parabéns, chegaste ao fim do Desafio. Espero que te tenhas divertido," +
                        " e continua curioso pois poderá vir mais surpresas no futuro.\nFica Atento!","38.520033:-8.838221",3);


        associarUtilizadorDesafio(1,1);
        associarUtilizadorDesafio(3,1,1,0);
        associarUtilizadorDesafio(2,2);
        associarUtilizadorDesafio(4,1,2,1);
        associarUtilizadorDesafio(4,2,3,2);
        associarUtilizadorDesafio(4,3,3,2);
    }

    public Cursor searchDbRaw(String query, String [] argumentos){
        Cursor c = getReadableDb().rawQuery(query, argumentos);
        return c;
    }

    public Cursor searchDb(String tableName, String[] projection, String selection, String[] selectionArgs){
        Cursor c = getReadableDb().query(tableName,projection,selection,selectionArgs,null,null,null);
        return c;
    }

    public void addUtilizador(String username, String password, String nome, String email, String localidade){
        ContentValues values = new ContentValues();
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_USERNAME, username);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_PASSWORD, password);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_NOME, nome);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_EMAIL, email);
        values.put(CipherHuntDbContract.TabelaUtilizadores.COLUMN_NAME_LOCALIDADE, localidade);

        getWritableDb().insert(CipherHuntDbContract.TabelaUtilizadores.TABLE_NAME, null, values);
    }

    public void addDesafio(String nome, String descricao, int numeroDeEnigmas){
        ContentValues values = new ContentValues();
        values.put(CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_NAME, nome);
        values.put(CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_DESCRICAO, descricao);
        values.put(CipherHuntDbContract.TabelaDesafio.COLUMN_NAME_NUM_ENIGMAS, numeroDeEnigmas);

        getWritableDb().insert(CipherHuntDbContract.TabelaDesafio.TABLE_NAME, null, values);
    }

    public void associarUtilizadorDesafio(int idUtilizador, int idDesafio){
        associarUtilizadorDesafio(idUtilizador, idDesafio, 0, 0);
    }

    public void associarUtilizadorDesafio(int idUtilizador, int idDesafio, int enigmaDesbloqueados, int enigmasConcluidos){
        ContentValues values = new ContentValues();
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR, idUtilizador);
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO, idDesafio);
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS, enigmaDesbloqueados);
        values.put(CipherHuntDbContract.TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS, enigmasConcluidos);

        getWritableDb().insert(CipherHuntDbContract.TabelaUtilizadorDesafio.TABLE_NAME, null, values);
    }

    private void addEnigmaPista(String password, String pergunta, String resposta, String pista, String localizacao, int idDesafio){
        ContentValues values = new ContentValues();
        values.put(CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PASSWORD, password);
        values.put(CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PERGUNTA, pergunta);
        values.put(CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_RESPOSTA, resposta);
        values.put(CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_PISTA, pista);
        values.put(CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_LOCALIZACAO, localizacao);
        values.put(CipherHuntDbContract.TabelaEnigma.COLUMN_NAME_ID_DESAFIO, idDesafio);

        getWritableDb().insert(CipherHuntDbContract.TabelaEnigma.TABLE_NAME, null, values);
    }
}
