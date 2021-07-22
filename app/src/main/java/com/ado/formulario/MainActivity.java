package com.ado.formulario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ado.formulario.database.RoomBD;
import com.ado.formulario.model.Note;
import com.ado.formulario.util.Cypher;
import com.ado.formulario.util.S;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView showNotes;
    private Button btnAdd;
    private EditText editAddNote;
    private RoomBD database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showNotes = (TextView) findViewById(R.id.txtShowNotes);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        editAddNote = (EditText) findViewById(R.id.editNote);

        database = RoomBD.getInstance(this);
    }

    public void agregarNota(View view) {
        String noteRaw = editAddNote.getText().toString().trim();
        if(noteRaw.isEmpty()) {
            showToast(S.NOTE_EMPTY, "");
        } else {
            try {
                saveOnDatabase(Cypher.encrypt(noteRaw));
            } catch (Exception e) {
                showToast(S.ERROR, "Error al tratar de guardar la nota");
            }
        }
    }

    public void readNotes(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

    public void saveOnDatabase(String text) {
        Note nota = new Note();
        nota.setText(text);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.noteDao().insert(nota);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editAddNote.setText("");
                        showToast(S.NOTE_SAVE, "");
                    }
                });
            }
        }).start();
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
}