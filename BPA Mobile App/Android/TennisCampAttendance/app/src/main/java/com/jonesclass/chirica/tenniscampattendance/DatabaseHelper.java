package com.jonesclass.chirica.tenniscampattendance;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows user to manipulate the SQLite database stored on the device for this app.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
//   public static final String ROSTER_NAME = "ROSTER_NAME";

    public static final String MEMBER_TABLE = "MEMBER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MEMBER_FIRST_NAME = "MEMBER_FIRST_NAME";
    public static final String COLUMN_MEMBER_LAST_NAME = "MEMBER_LAST_NAME";
    public static final String COLUMN_ACTIVE_MEMBER = "ACTIVE_MEMBER";
    public static final String COLUMN_MEMBER_ROSTER = "MEMBER_ROSTER";

    /**
     * Constructor.
     * @param context = Application context.
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, "members.db", null, 1);
    }

    @Override
    /**
     * Creates database table.
     */
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createMemberTableStatement = "CREATE TABLE " + MEMBER_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_MEMBER_FIRST_NAME + " TEXT, " +
                COLUMN_MEMBER_LAST_NAME + " TEXT, " +
                COLUMN_ACTIVE_MEMBER + " BOOL, " +
                COLUMN_MEMBER_ROSTER + " TEXT)";
        sqLiteDatabase.execSQL(createMemberTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    /**
     * Clears table in database.
     * @return boolean if operation was successful.
     */
    public boolean removeAllMembers() {
        SQLiteDatabase db = this.getWritableDatabase();
        String memberRemoveQuery = "DELETE FROM " + MEMBER_TABLE;
        Cursor cursor1 = db.rawQuery(memberRemoveQuery, null);

        boolean isSuccessful1 = cursor1.moveToFirst();
        db.close();
        cursor1.close();
        return (isSuccessful1);
    }


    //MEMBER METHODS

    /**
     * Adding row in database table.
     * @param member = member to add.
     * @return boolean if operation was successful.
     */
    public boolean addMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(COLUMN_MEMBER_FIRST_NAME, member.getFirstName());
        contentValues.put(COLUMN_MEMBER_LAST_NAME, member.getLastName());
        contentValues.put(COLUMN_ACTIVE_MEMBER, member.isActive());
        contentValues.put(COLUMN_MEMBER_ROSTER, member.getCamp());
        long insertValue = db.insert(MEMBER_TABLE, null, contentValues);
        db.close();

        if (insertValue == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Updating columns in a row in the database table.
     * @param member = member whose information to update.
     * @return boolean if operation was successful.
     */
    public boolean updateMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_MEMBER_FIRST_NAME, member.getFirstName());
        contentValues.put(COLUMN_MEMBER_LAST_NAME, member.getLastName());
        contentValues.put(COLUMN_ACTIVE_MEMBER, member.isActive());
        contentValues.put(COLUMN_MEMBER_ROSTER, member.getCamp());

        long rows = db.update(MEMBER_TABLE, contentValues,
                COLUMN_ID + " = ?", new String[] {
                        Integer.toString(member.getId())
                });
        db.close();

        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removing row from database table.
     * @param member = member whose row to remove.
     * @return boolean if operation was successful.
     */
    public boolean removeMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        String removeQuery = "DELETE FROM " + MEMBER_TABLE + " WHERE " +
                COLUMN_ID + " = " + member.getId();
        Cursor cursor = db.rawQuery(removeQuery, null);

        boolean itemExists = cursor.moveToFirst();
        db.close();
        cursor.close();
        return itemExists;
    }

    /**
     * Method that returns all members in the database.
     * @return List of members.
     */
    public List<Member> getMembers() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Member> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + MEMBER_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int memberID = cursor.getInt(0);
                String memberFirstName = cursor.getString(3);
                String memberLastName = cursor.getString(4);

                //Ternary operator: first thing is condition, then question mark,
                //then return the thing left of colon if true and thing right of colon if false
                boolean memberActive = cursor.getInt(5) == 1 ? true : false;
                String campName = cursor.getString(6);

                if (campName != null) {
                    returnList.add(new Member(memberFirstName, memberLastName, memberID, memberActive, campName));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }

    //Account Authentication Methods

    /**
     * Inserting a new user into the database. Only fills the username and password columns.
     * Everything else is null because there is no need to access it.
     *
     * @param username = String user input.
     * @param password = String user input.
     * @return boolean if operation was successful.
     */
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);

        long insertValue = db.insert(MEMBER_TABLE, null, contentValues);
        db.close();

        if (insertValue == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns list of all usernames in this database.
     * @return List of username Strings.
     */
    public List<String> getUsernames() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + MEMBER_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(1);
                returnList.add(username);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }

    /**
     * Returns list of all passwords in this database.
     * @return List of password Strings.
     */
    public List<String> getPasswords() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + MEMBER_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String password = cursor.getString(2);
                returnList.add(password);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
}
