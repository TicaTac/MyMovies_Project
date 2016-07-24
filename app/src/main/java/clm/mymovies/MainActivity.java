package clm.mymovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    myCommands commands;
    ListView lv;
    Cursor c;
    FloatingActionButton addfabBTN;

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                // finish();
                Toast.makeText(MainActivity.this,"Exit",Toast.LENGTH_SHORT).show();
                refreshList();
                break;

            case R.id.erase_all:
                commands=new myCommands(this);
                Toast.makeText(this,"Erasing all DB",Toast.LENGTH_SHORT);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle("Are you sure you want to erase?");
                dialog.setMessage("This will erase all the database");
                //dialog.setIcon(R.drawable.icon);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE,"ok",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        commands.clearDB();
                        refreshList();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"cancel",new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        refreshList();
                    }
                });

                dialog.show();

                break;
        }


            return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(MainActivity.this);
        inflater.inflate(R.menu.main_options_menu,menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Main","Loading...");
        // todo link list to simple cursor adapter
        lv = (ListView) findViewById(R.id.moviesLV);
        commands = new myCommands(this);

        // TODO: 7/24/2016 add movie records to DB

        myMovie movie1= new myMovie("Movie1","Description1",null);
        myMovie movie2= new myMovie("Movie2","Description2",null);
        myMovie movie3= new myMovie("Movie3","Description3",null);

        commands.addDb(movie1);
        commands.addDb(movie2);
        commands.addDb(movie3);

        Log.d("Main"," getDB");

        refreshList();
        Log.d("Main"," add Options menu");

        addfabBTN= (FloatingActionButton ) findViewById(R.id.addFABTN);
        addfabBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.d("DB ","Main-Clicked Add Button");
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,1);*/
                PopupMenu popup=new PopupMenu(MainActivity.this, addfabBTN, Gravity.NO_GRAVITY);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_manual:
                                Log.d("DB ","Main-Clicked Manual Add Button");
                                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                                startActivityForResult(intent,1);
                                break;

                            case R.id.add_web:
                                Toast.makeText(MainActivity.this,"add from web",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                popup.inflate(R.menu.main_popup_add_menu);
                popup.show();

            }
        });

        lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Main"," Edit/Update Movie");
                Intent intent
            }
        });

        lv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return true;
            }
        });


    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    public void refreshList(){
        Log.d("Main"," Start Refresh ListView");

        c = commands.getDbQuery();
        String[] from ={myConstants.DB_MOVIE_NAME,myConstants.DB_MOVIE_DESC};
        int[] to ={R.id.nameTV,R.id.descTV};

        Log.d("Main"," Link To Adapter");
        SimpleCursorAdapter adapter =new SimpleCursorAdapter(this,R.layout.single_movie_item,c,from,to);
        lv.setAdapter(adapter);

        Log.d("Main","Ended Refresh ListView");

    }
}
