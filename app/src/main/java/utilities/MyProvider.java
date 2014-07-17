package utilities;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyProvider extends ContentProvider {

    public static final Uri SHIFTS_URI = Uri
            .parse("content://com.shifts.provider/shiftsTEST");

    private MySQLiteHelper helper;
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.shifts.provider", "shiftsTEST", ALLROWS);
        uriMatcher.addURI("com.shifts.provider", "shiftsTEST/#", SINGLE_ROW);
    }

    public static final String KEY_ID = "_id";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TIMESTAMP_IN = "TimeStampIn";
    public static final String KEY_TIMESTAMP_OUT = "TimeStampOut";
    public static final String KEY_STARTING_HOUR_STRING = "StartingHourString";
    public static final String KEY_FINISHING_HOUR_STRING = "FinishingHourString";
    public static final String KEY_STARTING_MINUTES_STRING = "StartingMinutesString";
    public static final String KEY_FINISHIN_MINUTES_STRING = "FinishingMinutesString";
    public static final String KEY_STARTING_HOUR_INT = "StartingHourINT";
    public static final String KEY_FINISHING_HOUR_INT = "FinishingHourINT";
    public static final String KEY_STARTING_MINUTES_INT = "StartingMinutesINT";
    public static final String KEY_FINISHIN_MINUTES_INT = "FinishingMinutesINT";
    public static final String KEY_TOTAL_TIME_OF_SHIFT_STRING = "TotalTimeOfShiftString";
    public static final String KEY_MONTH_STRING = "MonthString";
    public static final String KEY_DATE = "Date";
    public static final String KEY_DAY = "Day";
    public static final String KEY_INSERT_MODE = "InsertMode";
    public static final String KEY_MONEY = "money";
    public static final String KEY_START_TEXT_VIEW = "startTEXTView";
    public static final String KEY_END_TEXT_VIEW = "endTEXTView";
    public static final String KEY_LENGTH_IN_SECONDS = "time";

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID
                        + "="
                        + rowID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : "");
            default:
                break;
        }

        if (selection == null)
            selection = "1";

        // Execute the deletion.
        int deleteCount = db.delete(helper.DATABASE_TABLE, selection,
                selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        switch (uriMatcher.match(uri)) {
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.paad.elemental";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.paad.elemental";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + SHIFTS_URI);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String nullColumnHack = null;
        long id = db.insert(helper.DATABASE_TABLE, nullColumnHack, values);

        if (id > -1) {
            Uri insertedID = ContentUris.withAppendedId(SHIFTS_URI, id);
            getContext().getContentResolver().notifyChange(insertedID, null);
            return insertedID;

        } else
            return null;

    }

    @Override
    public boolean onCreate() {
        helper = new MySQLiteHelper(getContext(), MySQLiteHelper.DATABASE_NAME,
                null, MySQLiteHelper.DATABASE_VERSION);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db;
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(helper.DATABASE_TABLE);

        switch (uriMatcher.match(SHIFTS_URI)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
            default:
                break;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID
                        + "="
                        + rowID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : "");

            default:
                break;
        }

        int updateCount = db.update(helper.DATABASE_TABLE, values, selection,
                selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    private static class MySQLiteHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "shiftsDB";
        private static final String DATABASE_TABLE = "shiftsTEST";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "create table "
                + DATABASE_TABLE
                + " ("
                + KEY_ID
                + " integer primary key autoincrement, "
                + "TimeStampIn not null, TimeStampOut not null, time not null, StartingHourString not null, FinishingHourString not null, StartingMinutesString not null, "
                + "FinishingMinutesString not null, StartingHourINT not null, FinishingHourINT not null, StartingMinutesINT not null, FinishingMinutesINT not null, TotalTimeOfShiftString not null,"
                + "MonthString not null, Date not null, Day not null, InsertMode not null, money not null, startTEXTView not null, endTEXTView not null )";

        public MySQLiteHelper(Context ctx, String name, CursorFactory factory,
                              int version) {
            super(ctx, MySQLiteHelper.DATABASE_NAME, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS shifts");
            onCreate(db);

        }
    }

}
