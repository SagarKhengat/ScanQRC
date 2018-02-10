package khengat.sagar.scanqrc.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import khengat.sagar.scanqrc.model.User;

/**
 * This class handles all operations regarding the sql database
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ScanQRC.db";
    private static final String TABLE_SCANNED = "scanned";
    private static final String COLUMN_SCANNED_ID = "scanned_id";
    private static final String COLUMN_SCANNED_QRCODE = "code";

    private String CREATE_SCANNED_TABLE = "CREATE TABLE " + TABLE_SCANNED + "(" + COLUMN_SCANNED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SCANNED_QRCODE + " TEXT)";

    private String DROP_SCANNED_TABLE = "DROP TABLE IF EXISTS" + TABLE_SCANNED;


    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_SCANNED_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL(DROP_SCANNED_TABLE);
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    /**
     * This method adds
     * @param item = item that will be added to the database
     * @return a boolean if the code was added to the database or not
     */
    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCANNED_QRCODE, item);
        //Insert Data into Database with a checking if the insert into the database worked
        long result = db.insert(TABLE_SCANNED, null, values);
        if(result == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    /**
     * Returns all the data from the database
     * @return a Cursor pointing on the requested table
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCANNED;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns the ID of the scanned code given as a parameter
     * @param code
     * @return a Cursor pointing on the id
     */
    public Cursor getItemID(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_SCANNED_ID + " FROM " + TABLE_SCANNED + " WHERE " + COLUMN_SCANNED_QRCODE + " = '" + code + "'";
        Cursor data = db.rawQuery(query, null);
        return  data;

    }

    /**
     * Delete a specific Item from database
     * @param id
     * @param code
     */
    public void deleteItem(int id, String code){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_SCANNED + " WHERE " + COLUMN_SCANNED_ID + " = '" + id + "'";
        db.execSQL(query);
    }

    public void resetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_SCANNED;
        db.execSQL(query);
    }


    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

}
