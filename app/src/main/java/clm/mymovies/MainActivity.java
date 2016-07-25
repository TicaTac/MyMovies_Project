package clm.mymovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    myCommands commands;
    ListView lv;
    Cursor c;
    FloatingActionButton addfabBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Main","Loading...");
        // todo link list to simple cursor adapter
        lv = (ListView) findViewById(R.id.moviesLV);
        commands = new myCommands(this);

        // TODO: 7/24/2016 add movie records to DB

      /*  myMovie movie1= new myMovie("Movie1","Description1",null, null);
        myMovie movie2= new myMovie("Movie2","Description2",null, null);
        myMovie movie3= new myMovie("Movie3","Description3",null, null);
        commands.addDb(movie1);
        commands.addDb(movie2);
        commands.addDb(movie3);*/

        Log.d("Main"," getDB");

        refreshList();
        Log.d("Main"," add Options menu");

        addfabBTN= (FloatingActionButton ) findViewById(R.id.addFABTN);
        addfabBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.d("DB ","Main-Clicked Add Button");
                Intent intent=new Intent(MainActivity.this,AddEditActivity.class);
                startActivityForResult(intent,1);*/
                PopupMenu popup=new PopupMenu(MainActivity.this, addfabBTN, Gravity.NO_GRAVITY);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_manual:
                                Log.d("DB ","Main-Clicked Manual Add Button");
                                Intent intent=new Intent(MainActivity.this,AddEditActivity.class);
                                startActivityForResult(intent,1);
                                break;

                            case R.id.add_web:
                                commands.toaster(MainActivity.this,"add from web");
                                break;
                        }
                        return true;
                    }
                });
                popup.inflate(R.menu.main_popup_add_menu);
                popup.show();

            }
        });

        registerForContextMenu(lv);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Main"," Edit/Update Movie");
                int dbID=-1;

                commands.toaster(MainActivity.this,"Position "+position );

                dbID=getDbID(position);

                Intent intent=new Intent(MainActivity.this,AddEditActivity.class);
                intent.putExtra(myConstants.DB_ID,dbID);
                startActivityForResult(intent,2);
            }
        });


    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }
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
                commands.toaster(MainActivity.this,"Exit");
                refreshList();
                break;

            case R.id.erase_all:
                commands=new myCommands(this);
                commands.toaster(this,"Erasing all DB");
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater=new MenuInflater(MainActivity.this);
        inflater.inflate(R.menu.main_listview_context_menu,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("Main"," ListView - LongClick started Context Menu");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        commands.toaster(MainActivity.this,"ContextItemSelected "+info.position);
        int dbID=getDbID(info.position);

        switch (item.getItemId()){
            case R.id.edit_listview_item_MI:
                Log.d("Main"," Edit ListView Item "+info.position);


                Intent intent=new Intent(MainActivity.this,AddEditActivity.class);
                intent.putExtra(myConstants.DB_ID,dbID);
                startActivityForResult(intent,myConstants.RESULT_EDIT);

                break;
            case R.id.erase_listview_item_MI:

                myCommands commands=new myCommands(MainActivity.this);
                commands.deleteDb(dbID);
                break;


        }

        return true;
    }

    public void refreshList(){
        Log.d("Main"," Start Refresh ListView");

        c = commands.getDbQuery();



        String[] from ={myConstants.DB_MOVIE_NAME,myConstants.DB_MOVIE_DESC,myConstants.DB_MOVIE_IMAGE};
        int[] to ={R.id.nameTV,R.id.descTV,R.id.thumbIV,};

        Log.d("Main"," Link To Adapter");
        //SimpleCursorAdapter adapter =new SimpleCursorAdapter(this,R.layout.single_movie_item,c,from,to);
        myCursorAdapter adapter=new myCursorAdapter(MainActivity.this,c,0);
        lv.setAdapter(adapter);

        Log.d("Main","Ended Refresh ListView");

    }

    public int getDbID(int lvPosition){
        Cursor c=commands.getDbQuery();
        c.moveToPosition(lvPosition);
        int dbID=c.getInt(c.getColumnIndexOrThrow(myConstants.DB_ID));
        return dbID;

    }
}
