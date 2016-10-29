package thedorkknightrises.notes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Samriddha Basu on 6/20/2016.
 */
public class BackupDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Backup.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NotesDb.Note.TABLE_NAME + " (" +
                    NotesDb.Note._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    NotesDb.Note.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NotesDb.Note.COLUMN_NAME_SUBTITLE + TEXT_TYPE + COMMA_SEP +
                    NotesDb.Note.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    NotesDb.Note.COLUMN_NAME_TIME + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NotesDb.Note.TABLE_NAME;
    private static final String SQL_CREATE_ENTRIES_ARCHIVE =
            "CREATE TABLE " + NotesDb.Archive.TABLE_NAME + " (" +
                    NotesDb.Archive._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    NotesDb.Archive.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NotesDb.Archive.COLUMN_NAME_SUBTITLE + TEXT_TYPE + COMMA_SEP +
                    NotesDb.Archive.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    NotesDb.Archive.COLUMN_NAME_TIME + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES_ARCHIVE =
            "DROP TABLE IF EXISTS " + NotesDb.Archive.TABLE_NAME;

    public BackupDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_ARCHIVE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_ARCHIVE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void merge(Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        NotesDbHelper notesDbHelper = new NotesDbHelper(context);
        String[] projection = {
                NotesDb.Note._ID,
                NotesDb.Note.COLUMN_NAME_TITLE,
                NotesDb.Note.COLUMN_NAME_SUBTITLE,
                NotesDb.Note.COLUMN_NAME_CONTENT,
                NotesDb.Note.COLUMN_NAME_TIME
        };
        Cursor cursor = db.query(NotesDb.Note.TABLE_NAME, projection, null, null, null, null, NotesDb.Note._ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                notesDbHelper.addNote(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

}