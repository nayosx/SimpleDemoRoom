package com.ado.formulario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ado.formulario.database.RoomBD;
import com.ado.formulario.model.Note;
import com.ado.formulario.util.Cypher;

import java.util.List;
import com.ado.formulario.util.S;

public class MainActivity2 extends AppCompatActivity {

    private TextView showNotes;
    private RoomBD database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        showNotes = (TextView) findViewById(R.id.showNotes);

        database = RoomBD.getInstance(this);
        readNotesFromDataBase();
    }

    private void readNotesFromDataBase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Note> lista = database.noteDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notesToShowInTextView(lista);
                    }
                });
            }
        }).start();
    }

    private void notesToShowInTextView(List<Note> listaNoteas) {
        String datos = "";
        showNotes.setText("");
        try {
            for(Note note: listaNoteas) {
                datos += "Id: "+note.getId()+"Nota: "+ Cypher.decrypt(note.getText())+" \n";
                // datos += "Id: "+note.getId()+"Nota: "+note.getText()+" \n";
            }
            datos += " Este texto lo agrega luego del loop";
            showNotes.setText(datos);
        } catch (Exception e) {
            showToast(S.ERROR, "No se a podido leer la nota desde la base de datos");
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
}