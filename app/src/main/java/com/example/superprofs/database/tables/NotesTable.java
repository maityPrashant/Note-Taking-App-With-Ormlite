package com.example.superprofs.database.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Superprofs on 6/14/2016.
 */
@DatabaseTable(tableName = "NotesTable")
public class NotesTable {

    @DatabaseField(generatedId = true, columnName = "_id")
    private Long _id;

    @DatabaseField
    private String noteText;

    @DatabaseField
    private String description;


    public NotesTable(String s, String s1) {
    }

    public NotesTable() {
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return noteText;
    }

    public Long get_id() {
        return _id;
    }

}