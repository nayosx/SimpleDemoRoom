package com.ado.formulario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ado.formulario.database.RoomBD;
import com.ado.formulario.model.Note;
import com.ado.formulario.util.Cypher;

import java.util.ArrayList;
import java.util.List;
import com.ado.formulario.util.S;

public class MainActivity2 extends AppCompatActivity {

    private RoomBD database;
    private ListView listView;

    private String [] listNotes;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = (ListView) findViewById(R.id.listOfNotes);

        database = RoomBD.getInstance(this);
        readNotesFromDataBase();
    }

    public void readNotes(View view) {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }

    private void readNotesFromDataBase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Note> lista = database.noteDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setNotes(lista);
                    }
                });
            }
        }).start();
    }

    private void setNotes(List<Note> notes) {
        if(notes.size() > 0) {
            listNotes = new String[notes.size()];
            int i = 0;
            try {
                for(Note note: notes) {
                    listNotes[i] = Cypher.decrypt(note.getText());
                    i++;
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listNotes);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                showToast(S.ERROR, "No se a podido leer la nota desde la base de datos");
            }
        }

    }

    private void showToast(int type, String message) {
        switch (type) {
            case S.NOTE_EMPTY:
                Toast.makeText(this, "No se puede guardar una nota vacia", Toast.LENGTH_SHORT).show();
                break;
            case S.NOTE_SAVE:
                Toast.makeText(this, "La nota a sido guardada con exito", Toast.LENGTH_SHORT).show();
                break;
            case S.ERROR:
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.readNotesFromDataBase();
    }
}