package com.ado.formulario.dao;

import androidx.room.Dao;
import androidx.room.*;
import com.ado.formulario.model.Note;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteDao {
    @Insert(onConflict = REPLACE)
    void insert(Note nota);

    @Delete
    void delete(Note nota);

    @Delete
    void reset(List<Note> mainData);

    @Query("UPDATE tabla_notas SET text =:sText WHERE id =:sID")
    void update (int sID, String sText);

    @Query("SELECT * FROM tabla_notas")
    List<Note> getAll();
}
