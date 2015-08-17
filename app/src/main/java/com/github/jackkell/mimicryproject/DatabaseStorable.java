package com.github.jackkell.mimicryproject;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface DatabaseStorable {

    void addToDatabase(SQLiteDatabase db);
    void removeFromDatabase(SQLiteDatabase db);
    String getID(SQLiteDatabase db);
}
