package com.example.superprofs.test;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.superprofs.database.DatabaseHelper;
import com.example.superprofs.database.tables.NotesTable;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

public class NotesProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.superprofs.test.notesProvider";
    private static final String BASE_PATH = "NotesTable";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    //Constants for identifying table and columns
    public static final String TABLE_NOTES = "NotesTable";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "noteText";
    public static final String NOTE_CREATED = "noteCreated";

    public static final String[] ALL_COLUMNS =
            {NOTE_ID, NOTE_TEXT, NOTE_CREATED};

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Note";

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor mCursor = null;
        QueryBuilder<NotesTable, Long> builder = null;
        try {
            builder = databaseHelper.getNotesTablesDao().queryBuilder();
            if (selection != null) {
                builder.where().eq(NotesProvider.NOTE_ID, selection);
            }
            CloseableIterator<NotesTable> iterator = databaseHelper.getNotesTablesDao().iterator(builder.prepare());
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            mCursor = results.getRawCursor();
        } catch (Exception e) {
            Log.d("DatabaseHelper", "caught exception in getNotesTables " + e);
        }
        return mCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        NotesTable notesTable = new NotesTable();
        notesTable.setNoteText(values.get(NotesProvider.NOTE_TEXT).toString());
        try {
            databaseHelper.getNotesTablesDao().createOrUpdate(notesTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Long id = notesTable.get_id();
        Uri uri1 = Uri.parse(BASE_PATH + "/" + id);
        return uri1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delete = 0;
        try {
            DeleteBuilder<NotesTable, Long> deleteBuilder = databaseHelper.getNotesTablesDao().deleteBuilder();
            if (selection != null) {
                deleteBuilder.where().eq(NotesProvider.NOTE_ID, selection);
            }
            delete = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int update = 0;
        UpdateBuilder<NotesTable, Long> updateBuilder = null;
        try {
            updateBuilder = databaseHelper.getNotesTablesDao().updateBuilder();
            updateBuilder.updateColumnValue(NotesProvider.NOTE_TEXT, values.get(NotesProvider.NOTE_TEXT).toString());
            updateBuilder.where().eq(NotesProvider.NOTE_ID, selection);
            update = updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return update;
    }


}
