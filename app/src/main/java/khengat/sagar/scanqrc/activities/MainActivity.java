package khengat.sagar.scanqrc.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import khengat.sagar.scanqrc.Constants.Config;
import khengat.sagar.scanqrc.LoginActivity;
import khengat.sagar.scanqrc.R;

import khengat.sagar.scanqrc.activities.generator.GenerateActivity;
import khengat.sagar.scanqrc.util.BottomNavigationViewHelper;
import khengat.sagar.scanqrc.util.DatabaseHelper;

import static khengat.sagar.scanqrc.util.ButtonHandler.copyToClipboard;
import static khengat.sagar.scanqrc.util.ButtonHandler.resetScreenInformation;
import static khengat.sagar.scanqrc.util.ButtonHandler.shareTo;
import static khengat.sagar.scanqrc.util.ButtonHandler.webSearch;


/**
 * This class is the MainActivity and is the starting point of the App
 * From here the User can start a QR-Code scan and can go to the Generate Activity
 */
public class MainActivity extends AppCompatActivity {

    private TextView mTvInformation, mTvFormat, mLabelInformation, mLabelFormat;
    private BottomNavigationView action_navigation;
    final Activity activity = this;
    private String qrcode = "", qrcodeFormat = "";
    private DatabaseHelper mDatabaeHelper;
    private static final String STATE_QRCODE = MainActivity.class.getName();
    private static final String STATE_QRCODEFORMAT = "";

    /**
     * This method handles the main navigation
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                    return true;
                case R.id.navigation_scan:
                    zxingScan();
                    return true;
                case R.id.navigation_generate:
                    startActivity(new Intent(MainActivity.this, GenerateActivity.class));
                    return true;
                //Following cases using a method from ButtonHandler
                case R.id.main_action_navigation_copy:
                    copyToClipboard(mTvInformation, qrcode, activity);
                    return true;
                case R.id.main_action_navigation_reset:
                    resetScreenInformation(mTvInformation, qrcode, action_navigation);
                    return true;
                case R.id.main_action_navigation_search:
                    webSearch(qrcode, activity);
                    return true;
                case R.id.main_action_navigation_share:
                    shareTo(qrcode, activity);
                    return true;


            }
            return false;
        }

    };

    /**
     * This method saves all data before the Activity will be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(STATE_QRCODE, qrcode);
        savedInstanceState.putString(STATE_QRCODEFORMAT, qrcodeFormat);
    }

    /**
     * Standard Android on create method that gets called when the activity
     * initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvInformation = (TextView) findViewById(R.id.tvTxtqrcode);
        mTvFormat = (TextView) findViewById(R.id.tvFormat);
        mLabelInformation = (TextView) findViewById(R.id.labelInformation);
        mLabelFormat = (TextView) findViewById(R.id.labelFormat);
        mDatabaeHelper = new DatabaseHelper(this);

        BottomNavigationView main_navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(main_navigation);
        main_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        main_navigation.clearFocus();

        action_navigation = (BottomNavigationView) findViewById(R.id.main_action_navigation);
        BottomNavigationViewHelper.disableShiftMode(action_navigation);
        action_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //If the device were rotated then restore information
        if(savedInstanceState != null){
            qrcode = savedInstanceState.getString(STATE_QRCODE);
            qrcodeFormat = savedInstanceState.getString(STATE_QRCODEFORMAT);
            if(qrcode.equals("")){

            } else {
                mTvFormat.setVisibility(View.VISIBLE);
                mLabelInformation.setVisibility(View.VISIBLE);
                mLabelFormat.setVisibility(View.VISIBLE);
                mTvFormat.setText(qrcodeFormat);
                mTvInformation.setText(qrcode);
                action_navigation.setVisibility(View.VISIBLE);

            }

        } else {
            //Autostart Scanner if activated
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String auto_scan = prefs.getString("pref_auto_scan", "");
            if(auto_scan.equals("true")){
                zxingScan();
            }
        }


    }

    /**
     * This method inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
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
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            case R.id.logout:
                logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method handles the results of the scan
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, getResources().getText(R.string.error_canceled_scan), Toast.LENGTH_LONG).show();
            } else {
                qrcodeFormat = result.getFormatName();
                qrcode = result.getContents();
                if(!qrcode.equals("")){
                    mTvFormat.setVisibility(View.VISIBLE);
                    mLabelInformation.setVisibility(View.VISIBLE);
                    mLabelFormat.setVisibility(View.VISIBLE);
                    mTvFormat.setText(qrcodeFormat);
                    mTvInformation.setText(qrcode);
                    action_navigation.setVisibility(View.VISIBLE);
                    addToDatabase(mTvInformation.getText().toString());
                    //Automatic Clipboard if activated
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    String auto_scan = prefs.getString("pref_auto_clipboard", "");
                    if(auto_scan.equals("true")){
                        copyToClipboard(mTvInformation, qrcode, activity);
                    }
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * This method handles the communication to the ZXING API -> Apache License 2.0
     * For more information please check out the link below.
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     */
    public void zxingScan(){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt((String) getResources().getText(R.string.xzing_label));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    /**
     * Takes the scanned code hands over the code to the method addData in the DatabaseHelper
     * @param newCode = scanned qr-code/barcode
     */
    public void addToDatabase(String newCode){
        boolean insertData = mDatabaeHelper.addData(newCode);
        if(!insertData){
            Toast.makeText(this, getResources().getText(R.string.error_add_to_database), Toast.LENGTH_LONG).show();
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
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
