package khengat.sagar.scanqrc.activities.generator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.activities.MainActivity;


/**
 * This class is all about the geo location to QR-Code Generate Activity. In this Class the functionality of generating a QR-Code Picture is covered.
 */
public class GeoGeneratorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText tfLatitude, tfLongtitude;
    CheckBox cbLatitude, cbLongtitude;
    Boolean north = true, east = true;
    ImageView image;
    BarcodeFormat format;
    String latitude, longtitude, geo;
    MultiFormatWriter multiFormatWriter;
    Bitmap bitmap;
    final Activity activity = this;
    private static final String STATE_LATITUDE = MainActivity.class.getName();
    private static final String STATE_LONGTITUDE = "";
    private static final String STATE_NORTH = "north";
    private static final String STATE_EAST = "east";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_generator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tfLatitude = (EditText) findViewById(R.id.tfLatitude);
        tfLongtitude = (EditText) findViewById(R.id.tfLongtitude);
        cbLatitude = (CheckBox) findViewById(R.id.cbLatitude);
        cbLongtitude = (CheckBox) findViewById(R.id.cbLongtitude);
        image = (ImageView) findViewById(R.id.image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setup the Spinner Menu for the different formats
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.formats_geo_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //If the device were rotated then restore information
        if(savedInstanceState != null){
            latitude = (String) savedInstanceState.get(STATE_LATITUDE);
            tfLatitude.setText(latitude);
            longtitude = (String) savedInstanceState.get(STATE_LONGTITUDE);
            tfLongtitude.setText(longtitude);
            north = (Boolean) savedInstanceState.get(STATE_NORTH);
            if(!north){
                cbLatitude.setChecked(false);
            }
            east = (Boolean) savedInstanceState.get(STATE_EAST);
            if(!east){
                cbLongtitude.setChecked(false);
            }
        }

        //OnClickListener for the "+" Button and functionality
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = tfLatitude.getText().toString().trim();
                longtitude = tfLongtitude.getText().toString().trim();
                if(latitude.equals("") || longtitude.equals("")){
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_geo_first), Toast.LENGTH_SHORT).show();
                } else {
                    multiFormatWriter = new MultiFormatWriter();
                    try{
                        if(north && east) {
                            geo = "geo:" + latitude + "," + longtitude;
                        } else if (!east && north){
                            geo = "geo:" + latitude + ",-" + longtitude;
                        } else if (!north && east){
                            geo = "geo:-" + latitude + "," + longtitude;
                        } else {
                            geo = "geo:-" + latitude + ",-" + longtitude;
                        }
                        BitMatrix bitMatrix = multiFormatWriter.encode(geo, format, 500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        image.setImageBitmap(bitmap);
                    } catch (Exception e){
                        Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.error_generate), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * This method saves all data before the Activity will be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(STATE_LATITUDE, latitude);
        savedInstanceState.putString(STATE_LONGTITUDE, longtitude);
        savedInstanceState.putBoolean(STATE_NORTH, north);
        savedInstanceState.putBoolean(STATE_EAST, east);
    }

    /**
     * Generates the chosen format from the spinner menu
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String compare = parent.getItemAtPosition(position).toString();
        if(compare.equals("AZTEC")){
            format = BarcodeFormat.AZTEC;
        }
        else if(compare.equals("QR_CODE")){
            format = BarcodeFormat.QR_CODE;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        format = BarcodeFormat.QR_CODE;
    }

    /**
     * Handles functionality behind the Checkboxes
     */
    public void onClickCheckboxes(View v){
        if(cbLatitude.isChecked() && cbLongtitude.isChecked()){
            north = true;
            east = true;
        } else if(!cbLatitude.isChecked() && cbLongtitude.isChecked()){
            north = false;
            east = true;
        } else if(cbLatitude.isChecked() && !cbLongtitude.isChecked()){
            north = true;
            east = false;
        } else{
            north = false;
            east = false;
        }
    }

}
