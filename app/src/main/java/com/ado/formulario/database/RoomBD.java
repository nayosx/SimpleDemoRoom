package com.ado.formulario.database;

import androidx.room.*;
import android.content.Context;

import com.ado.formulario.dao.NoteDao;
import com.ado.formulario.model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class RoomBD extends RoomDatabase {
    private static RoomBD baseDeDatos;
    private static String DATABASE_NAME = "baseDeDatosParaNotas";

    public synchronized static RoomBD getInstance(Context context) {
        if (baseDeDatos == null) {
            baseDeDatos = Room.databaseBuilder(context.getApplicationContext(), RoomBD.class,
                    DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return baseDeDatos;
    }

    public abstract NoteDao noteDao();
}
