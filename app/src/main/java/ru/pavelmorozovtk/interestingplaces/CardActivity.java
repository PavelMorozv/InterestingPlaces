package ru.pavelmorozovtk.interestingplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler databaseHandler;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Button btn_back = findViewById(R.id.btn_back);
        Button btn_showMaps = findViewById(R.id.btn_showMaps);
        Button btn_edit = findViewById(R.id.btn_edit);
        Button btn_delete = findViewById(R.id.btn_delete);

        TextView title = findViewById(R.id.tv_Title);
        TextView text = findViewById(R.id.tv_text);

        btn_back.setOnClickListener(this);
        btn_showMaps.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        databaseHandler = new DatabaseHandler(this);
        note = databaseHandler.getContact(getIntent().getExtras().getInt("note"));

        if (note != null) {
            title.setText(note.getTitle());
            text.setText(note.getDescription());
        }

    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_showMaps:
                startActivity(new Intent(this, ShowPointActivity.class)
                        .putExtra("point", note.getPoint()).putExtra("show", true));
                break;
            case R.id.btn_edit:
                startActivity(new Intent(this, CreateActivity.class)
                        .putExtra("Edit", true)
                        .putExtra("id", note.getid()));
                break;
            case R.id.btn_delete:
                databaseHandler = new DatabaseHandler(this);
                databaseHandler.deleteContact(note);
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}