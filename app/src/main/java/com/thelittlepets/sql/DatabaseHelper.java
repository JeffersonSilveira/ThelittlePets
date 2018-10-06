package com.thelittlepets.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thelittlepets.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefferson Rosa on 24/04/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

   // Versão do banco de dados
   private static final int DATABASE_VERSION = 1;

    // Nome do Banco de dados
    private static final String DATABASE_NAME = "UserManager.db";

    // Nome da tabela do usuário
    private static final String TABLE_USER = "user";

    // Nomes de colunas da tabela de usuários
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_DESCRIPTION = "user_description";

    // cria uma consulta de tabela sql
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" +")";

    // soltar a consulta sql da tabela
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop User Table se existir
        db.execSQL(DROP_USER_TABLE);

        // Crie tabelas novamente
        onCreate(db);

    }

    /**
     * Este método é criar um registro do usuário
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
       // values.put(COLUMN_USER_DESCRIPTION, user.getDescription());

        // Inserindo linha
       db.insert(TABLE_USER, null, values);
       db.close();
    }

    /**
     * Este método é buscar todos os usuários e retornar a lista de registros do usuário
     * @return list
     */
    public List<User> getAllUser() {

        String[] columns = {   COLUMN_USER_ID,  COLUMN_USER_EMAIL,  COLUMN_USER_NAME,  COLUMN_USER_PASSWORD  };

        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, columns, null,null,null,null, sortOrder);

        // Atravessando todas as linhas e adicionando à lista
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
              //  user.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_USER_DESCRIPTION)));
                // Adicionando o registro do usuário à lista
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // retornando a lista
        return userList;
    }

    /**
     Este método para atualizar o registro do usuário
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
      //  values.put(COLUMN_USER_DESCRIPTION, user.getPassword());


        // atualizando linha
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * Este método é para excluir o registro do usuário
     *
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete o registro do usuário por id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     Este método para verificar o e-mail do usuário existe ou não
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {


        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};



        Cursor cursor = db.query(TABLE_USER,   columns, selection, selectionArgs,null,null,null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * Este método para verificar o usuário existe ou não
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {


        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs,  null,null,null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
