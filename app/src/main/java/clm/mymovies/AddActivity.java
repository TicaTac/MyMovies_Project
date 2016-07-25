package clm.mymovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {
    myCommands commands;
    EditText nameTV;
    EditText descTV;
    String url,name,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        commands=new myCommands(AddActivity.this);
        nameTV=(EditText) findViewById(R.id.nameET);
        descTV=(EditText) findViewById(R.id.descriptionET);

        // get Source intent -
        // if dbID -1/-2 => new Item
        //  dbID >=0 then edit SQL record
        Intent intent=getIntent();
        int dbID=intent.getIntExtra(myConstants.DB_ID,-2);

        Log.d("DB ","Loading AddActivity dbID"+dbID);


        if (dbID>=0) {
            // load SQ: record
            Cursor c= commands.getDbQuery(dbID);
            if (c.moveToNext()) {
                nameTV.setText(c.getString(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_NAME)));
                descTV.setText(c.getString(c.getColumnIndexOrThrow(myConstants.DB_MOVIE_DESC)));
            }
        }



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
