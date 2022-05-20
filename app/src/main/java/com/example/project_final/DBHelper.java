package com.example.project_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DataBaseName = "DataBase.db";

    public DBHelper(Context context) {
        super(context, DataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String UserTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "account TEXT not null," +
                "password TEXT not null," +
                "mail TEXT not null" +
                ")";
        String ProblemTable = "CREATE TABLE IF NOT EXISTS Problems (" +
                "number INTEGER not null PRIMARY KEY," +
                "name TEXT not null," +
                "favorite INTEGER DEFAULT 0 NOT NULL," +
                "url TEXT not null" +
                ")";
        String HistoryTable = "CREATE TABLE IF NOT EXISTS History (" +
                "number INTEGER not null PRIMARY KEY AUTOINCREMENT," +
                "name TEXT not null" +
                ")";
        String NotesTable = "CREATE TABLE IF NOT EXISTS Notes (" +
                "name TEXT not null," +
                "note TEXT" +
                ")";



        DB.execSQL(UserTable);
        DB.execSQL(ProblemTable);
        DB.execSQL(HistoryTable);
        DB.execSQL(NotesTable);
        Log.i("DB", "OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        final String UserTable = "DROP TABLE IF EXISTS Users";
        final String ProblemTable = "DROP TABLE IF EXISTS Problems";
        final String HistoryTable = "DROP TABLE IF EXISTS History";

        DB.execSQL(UserTable);
        DB.execSQL(ProblemTable);
        DB.execSQL(HistoryTable);
    }

    private boolean isUserExists(String account, String mail) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor c = DB.rawQuery("SELECT * FROM Users WHERE account = ? OR mail = ?", new String[]{account, mail});
        if (c.moveToFirst()) {
            //Record exist
            c.close();
            return true;
        }
        //Record available
        c.close();
        return false;
    }

    public Boolean insertUserData(String account, String password, String mail) {
        if (isUserExists(account, mail)) {
            return false;
        }

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account", account);
        contentValues.put("password", password);
        contentValues.put("mail", mail);
        long result = DB.insert("Users", null, contentValues);

        return result != -1;
    }

    public Boolean insertProblemData(Integer number, String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("name", name);
        long result = DB.insert("Problems", null, contentValues);

        return result != -1;
    }

    public Boolean insertHistory(String problemName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", problemName);
        Cursor cursor = DB.rawQuery("SELECT * FROM History WHERE name = ?", new String[]{problemName});
        if (cursor.getCount() > 0) {
            DB.delete("History", "name = ?", new String[]{problemName});
        }
        long result = DB.insert("History",null, contentValues);


        return result != -1;
    }

    public Boolean checkLogin(String account, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where account = ? AND password = ?", new String[]{account, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Cursor getUserData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users", null);

        return cursor;
    }

    private Cursor getProblemData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Problems", null);

        return cursor;
    }

    private Cursor getProblemData(String problemName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Problems WHERE name = ?", new String[]{problemName});

        return cursor;
    }

    private Cursor getHistory() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM History ORDER BY number DESC", null);

        return cursor;
    }

    private Cursor getNote(String problemName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Notes WHERE name = ?", new String[]{problemName});

        return cursor;
    }

    public ArrayList<ArrayList<String>> getProblemDataToArrayList()
    {
        ArrayList<ArrayList<String>> problemsArrayList = new ArrayList<>();
        Cursor cursor = getProblemData();

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            do {
                ArrayList<String> problem = new ArrayList<>();

                problem.add("#" + cursor.getInt(0));
                problem.add(cursor.getString(1));
                problemsArrayList.add(problem);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return problemsArrayList;
    }

    public ArrayList<Problem> getProblemToProblemArrayList()
    {
        ArrayList<Problem> problemArrayList = new ArrayList<>();
        Cursor cursor = getProblemData();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Problem problem = new Problem("#" + cursor.getString(0), cursor.getString(1), cursor.getInt(2));
                problemArrayList.add(problem);
            } while (cursor.moveToNext());
        }

        return problemArrayList;
    }

    public Problem getProblemToProblem(String problemName)
    {
        Problem problem;
        Cursor cursor = getProblemData(problemName);
        cursor.moveToFirst();

        problem = new Problem("#" + cursor.getString(0), cursor.getString(1), cursor.getInt(2));

        return  problem;
    }

    public String getProblemURL(String problemName)
    {
        String url;
        Cursor cursor = getProblemData(problemName);
        cursor.moveToFirst();

        url = cursor.getString(3);

        return url;
    }

    public void updateProblemFavorite (String problemName, Boolean problemFavorite)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (problemFavorite) {
            contentValues.put("favorite", 1);
        } else {
            contentValues.put("favorite", 0);
        }
        Cursor cursor = DB.rawQuery("SELECT * FROM Problems WHERE name = ?", new String[]{problemName});
        if (cursor.getCount() > 0) {
            DB.update("Problems", contentValues,"name = ?", new String[]{problemName});
        }
    }

    public ArrayList<Problem> getHistoryToArrayList()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ArrayList<Problem> historyArrayList = new ArrayList<>();
        Cursor historyCursor = getHistory();

        if (historyCursor.getCount() > 0) {
            historyCursor.moveToFirst();

            do {
                Cursor problemCursor = getProblemData(historyCursor.getString(1));
                problemCursor.moveToFirst();
                Problem problem = new Problem("#" + problemCursor.getString(0), problemCursor.getString(1), problemCursor.getInt(2));
                historyArrayList.add(problem);
            } while (historyCursor.moveToNext());
        }

        return historyArrayList;
    }

    public void updateNote (String problemName, String note)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("note", note);
        Cursor cursor = DB.rawQuery("SELECT * FROM Notes WHERE name = ?", new String[]{problemName});
        if (cursor.getCount() > 0) {
            DB.update("Notes", contentValues, "name = ?", new String[]{problemName});
        } else {
            contentValues.put("name", problemName);
            DB.insert("Notes", null, contentValues);
        }
    }

    public String getNoteToString (String problemName)
    {
        String note = "";
        Cursor cursor = getNote(problemName);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            note = cursor.getString(1);
        }

        return note;
    }

    public Boolean updateUserData (String name, String password, String mail)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        contentValues.put("mail", mail);
        Cursor cursor = DB.rawQuery("Select * from Users where name = ?", new String[]{name});
        if (cursor.getCount() > 0)
        {
            long result = DB.update("Users", contentValues, "name=?", new String[]{name});
            if (result == -1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return  false;
        }
    }

    public Boolean deleteUserData (String name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where name = ?", new String[]{name});
        if (cursor.getCount() > 0)
        {
            long result = DB.delete("Users", "name=?", new String[]{name});
            if (result == -1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return  false;
        }
    }

    public Cursor searchUserData(String name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where name = ?", new String[]{name});

        return cursor;
    }
}