package cs2340.teamnasamovierecommender.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    public static final String TABLE_INFO = "info";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MAJOR = "major";
    public static final String COLUMN_BANNED = "banned";

    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_MOVIE_ID = "_id";
    public static final String COLUMN_MOVIE_NAME = "movieName";
    public static final String COLUMN_MOVIE_YEAR = "movieYear";
    public static final String COLUMN_USER_RATING = "rating";
    private static final int DATABASE_VERSION = 1;

    /**
     * @param context
     * @param DATABASE_NAME
     */
    public DBHandler(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String infoQuery = "CREATE TABLE " + TABLE_INFO + "(" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_BANNED + " TEXT, " +
                COLUMN_MAJOR + " TEXT ); ";
        db.execSQL(infoQuery);

        String movieQuery = "CREATE TABLE " + TABLE_MOVIES + "(" +
                COLUMN_MOVIE_ID + " TEXT UNIQUE, " +
                COLUMN_MOVIE_NAME + " TEXT, " +
                COLUMN_MOVIE_YEAR + " TEXT, " +
                COLUMN_USER_RATING + " TEXT );";
        db.execSQL(movieQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);
        onCreate(db);
    }

    //Add a new row to the database
    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues infoValues = new ContentValues();
        infoValues.put(COLUMN_USERNAME, user.getUsername());
        infoValues.put(COLUMN_PASSWORD, user.getPassword());
        infoValues.put(COLUMN_EMAIL, user.getEmail());
        infoValues.put(COLUMN_NAME, user.getUsername());
        infoValues.put(COLUMN_MAJOR, user.getMajor());
        infoValues.put(COLUMN_BANNED, Boolean.toString(user.isBanned()));
        
        db.insert(TABLE_INFO, null, infoValues);
    }

    public void addRating(int id, String name, double rating, String year) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues movieValues = new ContentValues();
        movieValues.put(COLUMN_MOVIE_ID, id);
        movieValues.put(COLUMN_MOVIE_NAME, name);
        movieValues.put(COLUMN_USER_RATING, rating);
        movieValues.put(COLUMN_MOVIE_YEAR, year);

        db.insertWithOnConflict(TABLE_MOVIES, null, movieValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeRating(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_MOVIES, COLUMN_MOVIE_ID + " = " + id, null);
    }

    public Double getRating(int id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_USER_RATING + " FROM " + TABLE_MOVIES + " WHERE " + COLUMN_MOVIE_ID + " = '" + id + "'", null);

        Double rating = null;
        if (c.moveToFirst())
            rating = Double.parseDouble(c.getString(c.getColumnIndex(COLUMN_USER_RATING)));

        return rating;
    }

    public String login(String username) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_INFO + " WHERE " + COLUMN_USERNAME + " = '" + username + "'", null);

        String pass = null;
        if (c.moveToFirst()) pass = c.getString(c.getColumnIndex(COLUMN_PASSWORD));

        return pass;
    }

    public String getName() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_INFO, null);

        String name = null;
        if (c.moveToFirst()) name = c.getString(c.getColumnIndex(COLUMN_NAME));

        return name;
    }

    public String getEmail() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_EMAIL + " FROM " + TABLE_INFO, null);

        String email = null;
        if (c.moveToFirst()) email = c.getString(c.getColumnIndex(COLUMN_EMAIL));

        return email;
    }

    public String getMajor() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_MAJOR + " FROM " + TABLE_INFO, null);

        String major = null;
        if (c.moveToFirst()) major = c.getString(c.getColumnIndex(COLUMN_MAJOR));

        return major;
    }

    private String getPassword() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_INFO, null);

        String pass = null;
        if (c.moveToFirst()) pass = c.getString(c.getColumnIndex(COLUMN_PASSWORD));

        return pass;
    }

    private String getUsername() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_USERNAME + " FROM " + TABLE_INFO, null);

        String user = null;
        if (c.moveToFirst()) user = c.getString(c.getColumnIndex(COLUMN_USERNAME));

        return user;
    }

    public void updateInfo(String name, String major, String email) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues infoValues = new ContentValues();
        infoValues.put(COLUMN_NAME, name);
        infoValues.put(COLUMN_MAJOR, major);
        infoValues.put(COLUMN_EMAIL, email);
        infoValues.put(COLUMN_USERNAME, getUsername());
        infoValues.put(COLUMN_PASSWORD, getPassword());
        infoValues.put(COLUMN_BANNED, getBanned());

        db.insertWithOnConflict(TABLE_INFO, null, infoValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getBanned() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_BANNED + " FROM " + TABLE_INFO, null);

        String banned = null;
        if (c.moveToFirst()) banned = c.getString(c.getColumnIndex(COLUMN_BANNED));

        return banned;
    }

    public void setBanned(String banned) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues infoValues = new ContentValues();
        infoValues.put(COLUMN_NAME, getName());
        infoValues.put(COLUMN_MAJOR, getMajor());
        infoValues.put(COLUMN_EMAIL, getEmail());
        infoValues.put(COLUMN_USERNAME, getUsername());
        infoValues.put(COLUMN_PASSWORD, getPassword());
        infoValues.put(COLUMN_BANNED, banned);

        db.insertWithOnConflict(TABLE_INFO, null, infoValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updatePassword(String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues infoValues = new ContentValues();
        infoValues.put(COLUMN_NAME, getName());
        infoValues.put(COLUMN_MAJOR, getMajor());
        infoValues.put(COLUMN_EMAIL, getEmail());
        infoValues.put(COLUMN_USERNAME, getUsername());
        infoValues.put(COLUMN_PASSWORD, password);

        db.insertWithOnConflict(TABLE_INFO, null, infoValues, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public Cursor rawQuery(String query) {
        SQLiteDatabase db = getWritableDatabase();

        return db.rawQuery(query, null);
    }

}
