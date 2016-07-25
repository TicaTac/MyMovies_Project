package clm.mymovies;

import android.content.ContentValues;
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
    int dbID;
    myDbHelper helper;

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
        dbID=intent.getIntExtra(myConstants.DB_ID,-2);
        helper=new myDbHelper(this);
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
                url=null;

                myMovie movie=new myMovie(name,desc,url);
                if (dbID>=0) {
                    ContentValues cv = new ContentValues();
                    cv.put(myConstants.DB_MOVIE_NAME,name);
                    cv.put(myConstants.DB_MOVIE_DESC,desc);
                    cv.put(myConstants.DB_MOVIE_NAME,name);

                    helper.getWritableDatabase().update(myConstants.DB_TABLE,cv,myConstants.DB_ID+"=?",new String[]{""+dbID});
                }
                else commands.addDb(movie);
                finish();

            }
        });
    }
}
