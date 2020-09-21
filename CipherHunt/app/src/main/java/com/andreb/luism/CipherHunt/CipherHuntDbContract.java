package com.andreb.luism.CipherHunt;

import android.provider.BaseColumns;

/**
 * Created by luism on 02/12/2016.
 */

public class CipherHuntDbContract {
    private CipherHuntDbContract() {}

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String VARCHAR_TYPE = " VARCHAR(30)";
    private static final String UNIQUE = " UNIQUE";
    private static final String COMMA_SEP = ",";

    //CRIACAO DE TABELAS
    public static final String SQL_CREATE_UTILIZADORES =
            "CREATE TABLE " + TabelaUtilizadores.TABLE_NAME + " (" +
                    TabelaUtilizadores.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TabelaUtilizadores.COLUMN_NAME_USERNAME + VARCHAR_TYPE + UNIQUE + COMMA_SEP +
                    TabelaUtilizadores.COLUMN_NAME_EMAIL + VARCHAR_TYPE + UNIQUE + COMMA_SEP +
                    TabelaUtilizadores.COLUMN_NAME_LOCALIDADE + VARCHAR_TYPE + COMMA_SEP +
                    TabelaUtilizadores.COLUMN_NAME_NOME + VARCHAR_TYPE + COMMA_SEP +
                    TabelaUtilizadores.COLUMN_NAME_PASSWORD + VARCHAR_TYPE + " ); ";

    public static final String SQL_CREATE_DESAFIOS =
            "CREATE TABLE " + TabelaDesafio.TABLE_NAME + " (" +
                    TabelaDesafio.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP+
                    TabelaDesafio.COLUMN_NAME_NAME + VARCHAR_TYPE + UNIQUE + COMMA_SEP+
                    TabelaDesafio.COLUMN_NAME_DESCRICAO + VARCHAR_TYPE + COMMA_SEP+
                    TabelaDesafio.COLUMN_NAME_NUM_ENIGMAS + INTEGER_TYPE + " );";

    public static final String SQL_CREATE_UTILIZADOR_DESAFIO =
            "CREATE TABLE " + TabelaUtilizadorDesafio.TABLE_NAME + " (" +
                    TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR + INTEGER_TYPE + COMMA_SEP +
                    TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO + INTEGER_TYPE + COMMA_SEP +
                    TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS + INTEGER_TYPE + COMMA_SEP +
                    TabelaUtilizadorDesafio.COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY ("+ TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR+
                    ") REFERENCES "+ TabelaUtilizadores.TABLE_NAME+"("+ TabelaUtilizadores.COLUMN_NAME_ID + ")" + COMMA_SEP+
                    "FOREIGN KEY ("+ TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO+
                    ") REFERENCES "+ TabelaDesafio.TABLE_NAME+"("+ TabelaDesafio.COLUMN_NAME_ID+")" + COMMA_SEP+
                    "PRIMARY KEY (" + TabelaUtilizadorDesafio.COLUMN_NAME_ID_UTILIZADOR + COMMA_SEP +
                    TabelaUtilizadorDesafio.COLUMN_NAME_ID_DESAFIO + ") "+ " );";

    public static final String SQL_CREATE_ENIGMAS =
            "CREATE TABLE " + TabelaEnigma.TABLE_NAME + " (" +
                    TabelaEnigma.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TabelaEnigma.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    TabelaEnigma.COLUMN_NAME_PERGUNTA + TEXT_TYPE + COMMA_SEP +
                    TabelaEnigma.COLUMN_NAME_RESPOSTA + TEXT_TYPE + COMMA_SEP +
                    TabelaEnigma.COLUMN_NAME_PISTA + TEXT_TYPE + COMMA_SEP +
                    TabelaEnigma.COLUMN_NAME_LOCALIZACAO + VARCHAR_TYPE + COMMA_SEP +
                    TabelaEnigma.COLUMN_NAME_ID_DESAFIO + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY ("+ TabelaEnigma.COLUMN_NAME_ID_DESAFIO +
                    ") REFERENCES "+ TabelaDesafio.TABLE_NAME+"("+ TabelaDesafio.COLUMN_NAME_ID+")" +");";


    //ELIMINAR TABELAS
    public static final String SQL_DELETE_UTILIZADORES =
            "DROP TABLE IF EXISTS " + TabelaUtilizadores.TABLE_NAME+"; ";
    public static final String SQL_DELETE_DESAFIOS =
            "DROP TABLE IF EXISTS " + TabelaDesafio.TABLE_NAME+"; ";
    public static final String SQL_DELETE_UTILIZADOR_DESAFIO =
            "DROP TABLE IF EXISTS " + TabelaUtilizadorDesafio.TABLE_NAME+"; ";
    public static final String SQL_DELETE_ENIGMAS =
            "DROP TABLE IF EXISTS " + TabelaEnigma.TABLE_NAME+"; ";


    public static class TabelaUtilizadores implements BaseColumns {
        public static final String TABLE_NAME = "Utilizador";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_NOME = "nome";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_LOCALIDADE = "localidade";

    }

    public static class TabelaDesafio implements BaseColumns {
        public static final String TABLE_NAME = "Desafio";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "nome";
        public static final String COLUMN_NAME_DESCRICAO = "descricao";
        public static final String COLUMN_NAME_NUM_ENIGMAS = "numeroEnigmas";
    }

    public static class TabelaUtilizadorDesafio implements BaseColumns {
        public static final String TABLE_NAME = "Utilizador_Desafio";
        public static final String COLUMN_NAME_ID_UTILIZADOR = "id_utilizador";
        public static final String COLUMN_NAME_ID_DESAFIO = "id_desafio";
        public static final String COLUMN_NAME_NUM_ENIGMAS_DESBLOQUEADOS = "num_enigmas_desbloqueados";
        public static final String COLUMN_NAME_NUM_ENIGMAS_CONCLUIDOS = "num_enigmas_concluidos";
    }

    public static class TabelaEnigma implements BaseColumns {
        public static final String TABLE_NAME = "Enigma";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_PERGUNTA = "pergunta";
        public static final String COLUMN_NAME_RESPOSTA = "resposta";
        public static final String COLUMN_NAME_PISTA = "pista";
        public static final String COLUMN_NAME_LOCALIZACAO = "localizacao";
        public static final String COLUMN_NAME_ID_DESAFIO = "id_desafio";
    }
}
