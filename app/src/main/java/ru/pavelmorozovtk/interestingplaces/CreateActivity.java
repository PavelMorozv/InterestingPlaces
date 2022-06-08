package ru.pavelmorozovtk.interestingplaces;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler databaseHandler;
    String temp = "";
    Note note;


    EditText title;
    EditText text;
    boolean editFlag=false;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        String accessMessage = intent.getStringExtra("point");
                        temp = accessMessage;
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button btn_back = findViewById(R.id.btn_back);
        Button btn_setPoint = findViewById(R.id.btn_setPoint);
        Button btn_save = findViewById(R.id.btn_save);

        btn_back.setOnClickListener(this);
        btn_setPoint.setOnClickListener(this);
        btn_save.setOnClickListener(this);


        databaseHandler = new DatabaseHandler(this);
        TextView tv_createActivityTitle = findViewById(R.id.tv_createActivityTitle);
        title = findViewById(R.id.tv_title);
        text = findViewById(R.id.tv_text);

        Bundle extra = getIntent().getExtras();
        if (extra != null)
            if (extra.getBoolean("Edit"))
            {
                editFlag = true;
                tv_createActivityTitle.setText(R.string.tv_editActivityTitle);
                note = databaseHandler.getContact(extra.getInt("id"));
                title.setText(note.getTitle());
                text.setText(note.getDescription());
            }
        else tv_createActivityTitle.setText(R.string.tv_createActivityTitle);
    }

    @Override
    public void onClick(View view) {
        Toast t;
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_setPoint:
                Intent intent = new Intent(this, ShowPointActivity.class);

                if (editFlag) intent.putExtra("point", note.getPoint()).putExtra("show", false);
                else intent.putExtra("point", "0 0").putExtra("show", false);
                mStartForResult.launch(intent);

                break;
            case R.id.btn_save:

                databaseHandler = new DatabaseHandler(this);
                if (note != null)
                    databaseHandler.updateContact(new Note(note.getid(), title.getText().toString(), text.getText().toString(), note.getPoint()));
                else
                    databaseHandler.addContact(new Note(title.getText().toString(), text.getText().toString(), temp.length()>1?temp:"0 0"));

                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}