package com.durante.fabrizio.myaccountant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Fabrizio on 29/12/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="MyAccountant.db";

    public static final String TABLE_PAY="Pagamenti";

    public static final String PAY_PK="Id";
    public static final String PAY_IMPORTO="Importo";
    public static final String PAY_DATA="Data";
    public static final String PAY_LUOGO="Luogo";
    public static final String PAY_CATEGORIA="Categoria";
    public static final String PAY_EK="Cod_Conto";

    public static final String TABLE_CONTO="Conti";

    public static final String CONTO_PK="Id";
    public static final String CONTO_BIL_IN="Bilancio_Iniziale";
    public static final String CONTO_BIL_FIN="Bilancio_Finale";
    public static final String CONTO_NOME="Nome";
    public static final String CONTO_DATA_CREA="Data_Creazione";

    private Context c;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+TABLE_PAY+" (" +
                ""+PAY_PK+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+PAY_IMPORTO+" REAL, " +
                ""+PAY_DATA+" TEXT, " +
                ""+PAY_LUOGO+" TEXT, " +
                ""+PAY_CATEGORIA+" TEXT, " +
                ""+PAY_EK+" INTEGER)";
        db.execSQL(sql);

        sql="CREATE TABLE "+TABLE_CONTO+" (" +
                ""+CONTO_PK+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+CONTO_BIL_IN+" REAL, " +
                ""+CONTO_BIL_FIN+" REAL, "+
                ""+CONTO_NOME+" TEXT, " +
                ""+CONTO_DATA_CREA+" TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST '"+TABLE_PAY+"'");
        db.execSQL("DROP TABLE IF EXIST "+TABLE_CONTO);
        onCreate(db);
    }

    public boolean InsertConto(float bil, String nome){
        SQLiteDatabase db=this.getWritableDatabase();
        Calendar cal=new GregorianCalendar();
        String creazione= cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+1+"/"+cal.get(Calendar.YEAR);
        ContentValues cv=new ContentValues();
        cv.put(CONTO_BIL_IN, bil);
        cv.put(CONTO_BIL_FIN, bil);
        cv.put(CONTO_NOME, nome);
        cv.put(CONTO_DATA_CREA, creazione);
        if (db.insert(TABLE_CONTO, null, cv)==-1)
            return false;
        else
            return true;
    }

    public boolean InsertPay(float importo, String data, String luogo, String cat, int ek){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(PAY_IMPORTO, importo);
        cv.put(PAY_DATA, data);
        cv.put(PAY_LUOGO, luogo);
        cv.put(PAY_CATEGORIA, cat);
        cv.put(PAY_EK, ek);
        if (db.insert(TABLE_PAY, null, cv)==-1)
            return false;
        else
            return true;
    }

    public Cursor get_Conti(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor ris=db.rawQuery("SELECT "+CONTO_NOME+", "+CONTO_BIL_FIN+" FROM "+TABLE_CONTO+" ORDER BY ?", new String[]{CONTO_PK});
        return ris;
    }

    public String get_SaldoTot(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor ris=db.rawQuery("SELECT SUM("+CONTO_BIL_FIN+") FROM "+TABLE_CONTO, null);
        if(ris.getCount()>0) {
            ris.moveToFirst();
            if(ris.getString(0)!=null)
                return ris.getString(0);
            else
                return "0";
        }
        else
            return "0";
    }

    public Cursor get_Pay(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor ris=db.rawQuery("SELECT C."+CONTO_NOME+", P."+PAY_CATEGORIA+", P."+PAY_IMPORTO+", P."+PAY_PK+" " +
                                "FROM "+TABLE_CONTO+" AS C, "+TABLE_PAY+" AS P" +
                                " WHERE P."+PAY_EK+"=C."+CONTO_PK+" LIMIT 10" , null);
        return ris;
    }

    public boolean Aggiorna(int conto){
        SQLiteDatabase db=this.getWritableDatabase();
        float entrate=0, uscite=0, bil;
        Cursor ris=db.rawQuery("SELECT SUM(P."+PAY_IMPORTO+")\n" +
                "FROM "+TABLE_PAY+" as P INNER JOIN "+TABLE_CONTO+" as C ON P."+PAY_EK+"=C."+CONTO_PK+"\n" +
                "WHERE C."+CONTO_PK+"=? AND P."+PAY_CATEGORIA+"=?",
                new String[]{Integer.toString(conto), "Entrata"});
        ris.moveToFirst();
        entrate=ris.getFloat(0);

        ris=db.rawQuery("SELECT SUM(P."+PAY_IMPORTO+")\n" +
                        "FROM "+TABLE_PAY+" as P INNER JOIN "+TABLE_CONTO+" as C ON P."+PAY_EK+"=C."+CONTO_PK+"\n" +
                        "WHERE C."+CONTO_PK+"=? AND P."+PAY_CATEGORIA+"=?",
                new String[]{Integer.toString(conto), "Uscita"});
        ris.moveToFirst();
        uscite=ris.getFloat(0);

        ris=db.rawQuery("SELECT "+CONTO_BIL_IN+"\n" +
                "FROM "+TABLE_CONTO+"\n" +
                "WHERE "+CONTO_PK+"=?", new String[]{Integer.toString(conto)});
        if(!ris.moveToFirst()){
            Toast.makeText(c, "Non trovo il bilancio iniziale del conto", Toast.LENGTH_LONG).show();
            return false;
        }

        bil=ris.getFloat(0)+entrate-uscite;

        ContentValues cv=new ContentValues();
        cv.put(CONTO_BIL_FIN, bil);
        if(db.update(TABLE_CONTO, cv, CONTO_PK+"=?", new String[]{Integer.toString(conto)})==-1)
            return false;
        else
            return true;
    }

    public boolean delete_Conto(int conto){
        SQLiteDatabase db=this.getWritableDatabase();
        if(db.delete(TABLE_CONTO, CONTO_PK+"==?", new String[]{Integer.toString(conto)}) == 0 &&
                db.delete(TABLE_PAY, PAY_EK+"=?", new String[]{Integer.toString(conto)}) == 0)
            return false;
        else
            return true;
    }

    public Cursor get_Conto(int conto){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor ris=db.rawQuery("SELECT "+CONTO_NOME+", "+CONTO_BIL_FIN+", "+CONTO_DATA_CREA+", "+CONTO_BIL_IN+"\n" +
                "FROM "+TABLE_CONTO+" \n" +
                "WHERE "+CONTO_PK+"=?", new String[]{Integer.toString(conto)});
        return ris;
    }

    public Cursor get_Movim(int conto){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor ris=db.rawQuery("SELECT * FROM "+TABLE_PAY+" " +
                "WHERE "+PAY_EK+"=?", new String[]{Integer.toString(conto)});
        return ris;
    }

    public boolean get_Conto(String nome){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor ris=db.rawQuery("SELECT * FROM "+TABLE_CONTO+" WHERE "+CONTO_NOME+"=?", new String[]{nome});
        if(ris.getCount()>0)
            return true;
        else
            return false;
    }

    public boolean update_Conto(int conto, String nome){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(CONTO_NOME, nome);
        if(db.update(TABLE_CONTO, cv, CONTO_PK+"=?", new String[]{Integer.toString(conto)})>0)
            return true;
        else
            return false;
    }

    public Cursor get_singol_Movim(int movim){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor ris=db.rawQuery("SELECT P."+PAY_DATA+", P."+PAY_CATEGORIA+", P."+PAY_IMPORTO+", P."+PAY_LUOGO+", C."+CONTO_NOME+", P."+PAY_PK+" "+
                "FROM "+TABLE_PAY+" AS P, "+TABLE_CONTO+" AS C "+
                "WHERE P."+PAY_EK+"=C."+CONTO_PK+" AND P."+PAY_PK+"=?", new String[]{Integer.toString(movim)});
        return ris;
    }

    public boolean delete_Mov(int mov){
        SQLiteDatabase db=this.getWritableDatabase();
        if(db.delete(TABLE_PAY, PAY_PK+"=?", new String[]{Integer.toString(mov)})==0)
            return false;
        else
            return true;
    }

    public boolean update_Mov(int id, float imp, String data, String tipo, String dove){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(PAY_IMPORTO, imp);
        cv.put(PAY_DATA, data);
        cv.put(PAY_CATEGORIA, tipo);
        cv.put(PAY_LUOGO, dove);
        if(db.update(TABLE_PAY, cv, PAY_PK+"=?", new String[]{Integer.toString(id)})>0)
            return true;
        else
            return false;
    }
}
