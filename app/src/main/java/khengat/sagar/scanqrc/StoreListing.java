package khengat.sagar.scanqrc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import khengat.sagar.scanqrc.Constants.Config;
import khengat.sagar.scanqrc.activities.*;
import khengat.sagar.scanqrc.util.DatabaseHandler;
import khengat.sagar.scanqrc.util.DatabaseHelper;

public class StoreListing extends AppCompatActivity {
    private TextView mTextStore;
    private TextView mTextArea;

    final Activity activity = this;

    private DatabaseHandler mDatabaeHelper;


    Spinner spinnerStore;
    Spinner spinnerArea;
    FloatingActionButton fabArea;
    FloatingActionButton fabStore;
    FloatingActionButton fabGo;
    /**
     * This method saves all data before the Activity will be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

    }

    /**
     * Standard Android on create method that gets called when the activity
     * initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_listing);

        mTextStore = (TextView) findViewById(R.id.txtStore);
        mTextArea = (TextView) findViewById(R.id.txtArea);
        spinnerArea = (Spinner) findViewById(R.id.spinnerArea);
        spinnerStore = (Spinner) findViewById(R.id.spinnerStore);
        fabStore = (FloatingActionButton) findViewById(R.id.fabStore);
        fabArea = (FloatingActionButton) findViewById(R.id.fabArea);
        fabGo = (FloatingActionButton) findViewById(R.id.fabGo);
        mDatabaeHelper = new DatabaseHandler(this);


        if(mDatabaeHelper.fnGetAllArea().size()==0)
        {
            mTextArea.setText("Please Add Area First");
            fabArea.setVisibility(View.VISIBLE);
            spinnerArea.setVisibility(View.GONE);
        }

        if(mDatabaeHelper.fnGetAllStore().size()==0)
        {
            mTextStore.setText("Please Add Store First");
            fabStore.setVisibility(View.VISIBLE);
            spinnerStore.setVisibility(View.GONE);
        }

        fabGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountsIntent = new Intent(activity, khengat.sagar.scanqrc.activities.MainActivity.class);

                startActivity(accountsIntent);

            }
        });
    }

    /**
     * This method inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                AboutDialog aboutDialog = new AboutDialog(this);
                aboutDialog.setTitle(R.string.about_dialog);
                aboutDialog.show();
                return true;
            case R.id.settings:
                startActivity(new Intent(StoreListing.this, SettingsActivity.class));
            case R.id.logout:
                logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }







    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.logout_title_msg);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.NAME, "");

                        //putting blank value to usertoken
                        editor.putInt(Config.USERTOKEN,0);

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(StoreListing.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
