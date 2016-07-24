package clm.mymovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
    myCommands commands;
    EditText nameTV;
    EditText descTV;
    String url,name,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Log.d("DB ","Loading AddActivity");
        commands=new myCommands(AddActivity.this);

        nameTV=(EditText) findViewById(R.id.nameET);
        descTV=(EditText) findViewById(R.id.descriptionET);


        Button saveBTN=(Button) findViewById(R.id.saveBTN);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddActivity"," Add new Record to DB");
                // TODO: 7/24/2016 add update Record in DB

                name=nameTV.getText().toString();
                desc=descTV.getText().toString();
                url="";

                myMovie movie=new myMovie(name,desc,url);
                commands.addDb(movie);
                finish();

            }
        });
    }
}
