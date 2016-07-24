package clm.mymovies;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // todo link list to simple cursor adapter
        ListView lv = (ListView) findViewById(R.id.moviesLV);
        myCommands commands = new myCommands(this);
        Cursor c = commands.getDbQuery();
        String[] from ={myConstants.DB_MOVIE_NAME,myConstants.DB_MOVIE_DESC};
        int[] to ={R.id.nameTV,R.id.descTV};
        SimpleCursorAdapter adapter =new SimpleCursorAdapter(this,R.layout.single_movie_item,c,from,to);

    }
}
