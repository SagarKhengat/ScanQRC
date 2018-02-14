package khengat.sagar.scanqrc.activities.generator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import khengat.sagar.scanqrc.Constants.Config;
import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.activities.MainActivity;
import khengat.sagar.scanqrc.model.Product;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.util.DatabaseHandler;
import khengat.sagar.scanqrc.util.InputValidation;


/**
 * This class is all about the value to BARCODE Generate Activity. In this Class the functionality of generating a BARCODE Picture is covered.
 */

public class BarcodeGenerateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputLayout textInputLayoutProductName;
    private TextInputLayout textInputLayoutProductBrand;
    private TextInputLayout textInputLayoutProductDescription;
    private TextInputLayout textInputLayoutProductPrice;
    private TextInputLayout textInputLayoutProductUnit;
    private TextInputLayout textInputLayoutProductStore;
    private TextInputLayout textInputLayoutProductSize;

    private TextInputEditText textInputEditTextProductName;
    private TextInputEditText textInputEditTextProductBrand;
    private TextInputEditText textInputEditTextProductDescription;
    private TextInputEditText textInputEditTextProductPrice;
    private TextInputEditText textInputEditTextProductUnit;
    private TextInputEditText textInputEditTextProductSize;
    private TextInputEditText textInputEditTextProductStore;
    Product product;
    Store storeBarcode;
    ImageView image;
    BarcodeFormat format;
    String text2Barcode;
    MultiFormatWriter multiFormatWriter;
    Bitmap bitmap;
    final Activity activity = this;
    DatabaseHandler mDatabaseHandler;
    String FOLDER_NAME="ScanQRC";
    FloatingActionButton fab;
    FloatingActionButton fabShare;
    Gson gson;
    InputValidation inputValidation ;
    int RANGE = 999;
    private static final String STATE_TEXT = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        product = new Product();
          gson = new Gson();
        storeBarcode = new Store();
        inputValidation = new InputValidation(activity);

        mDatabaseHandler = new DatabaseHandler(activity);
        textInputLayoutProductName = (TextInputLayout) findViewById(R.id.textInputLayoutProductName);
        textInputLayoutProductBrand = (TextInputLayout) findViewById(R.id.textInputLayoutProductBrand);
        textInputLayoutProductDescription = (TextInputLayout) findViewById(R.id.textInputLayoutProductDescription);
        textInputLayoutProductPrice = (TextInputLayout) findViewById(R.id.textInputLayoutProductPrice);
        textInputLayoutProductUnit = (TextInputLayout) findViewById(R.id.textInputLayoutProductUnit);

        textInputLayoutProductStore = (TextInputLayout) findViewById(R.id.textInputLayoutProductStore);
        textInputLayoutProductSize= (TextInputLayout) findViewById(R.id.textInputLayoutProductSize);

        textInputEditTextProductName = (TextInputEditText) findViewById(R.id.textInputEditTextProductName);
        textInputEditTextProductBrand = (TextInputEditText) findViewById(R.id.textInputEditTextProductBrand);
        textInputEditTextProductDescription = (TextInputEditText) findViewById(R.id.textInputEditTextProductDescription);
        textInputEditTextProductPrice = (TextInputEditText) findViewById(R.id.textInputEditTextProductPrice);
        textInputEditTextProductUnit = (TextInputEditText) findViewById(R.id.textInputEditTextProductUnit);
        textInputEditTextProductSize = (TextInputEditText) findViewById(R.id.textInputEditTextProductSize);
        textInputEditTextProductStore = (TextInputEditText) findViewById(R.id.textInputEditTextProductStore);
        image = (ImageView) findViewById(R.id.image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setup the Spinner Menu for the different formats
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.barcode_formats_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        String json = sharedPreferences.getString(Config.STORE_SHARED_PREF, "");

        storeBarcode = gson.fromJson(json, Store.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        // Get intent, action and MINE type and check if the intent was started by a share to modul from an other app
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

//        if (Intent.ACTION_SEND.equals(action) && type != null){
//            if("text/plain".equals(type)){
//                handleSendText(intent); //call method to handle sended text
//            }
//        }
        textInputEditTextProductStore.setText(storeBarcode.getStoreName());
        textInputEditTextProductStore.setEnabled(false);
        //OnClickListener for the "+" Button and functionality
       fab = (FloatingActionButton) findViewById(R.id.fab);
       fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                text2Barcode = text.getText().toString().trim();

                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isAcceptingText()) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); // this method use to close keyboard forcefully
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (!inputValidation.isInputEditTextFilled(textInputEditTextProductName, textInputLayoutProductName, "Enter Product Name")) {
                    return;
                }
                else
                {
                    product.setProductName(textInputEditTextProductName.getText().toString().trim());
                }


                if(textInputEditTextProductBrand.getText().toString().trim()==null)
                {
                }
                else
                {
                    product.setProductBrand(textInputEditTextProductBrand.getText().toString().trim());
                }

                if(textInputEditTextProductDescription.getText().toString().trim()==null)
                {
                }
                else
                {
                    product.setProductDescription(textInputEditTextProductDescription.getText().toString().trim());
                }

                if(!inputValidation.isInputEditTextFilled(textInputEditTextProductPrice, textInputLayoutProductPrice, "Enter Product Price"))
                {
                    return;
                }
                else
                {
                    product.setProductTotalPrice(Double.parseDouble(textInputEditTextProductPrice.getText().toString().trim()));
                }

                if(!inputValidation.isInputEditTextFilled(textInputEditTextProductUnit, textInputLayoutProductUnit, "Enter Product Unit"))
                {
                    return;
                }
                else
                {
                    product.setProductUnit(textInputEditTextProductUnit.getText().toString().trim());
                }
                if(!inputValidation.isInputEditTextFilled(textInputEditTextProductSize, textInputLayoutProductSize, "Enter Product Size"))
                {
                    return;
                }
                else
                {
                    product.setProductSize(textInputEditTextProductSize.getText().toString().trim());
                }
                product.setStore(storeBarcode);
                product.setProductId(getUniqueId());
                text2Barcode = gson.toJson(product);

                if(textInputEditTextProductName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_text_first), Toast.LENGTH_SHORT).show();
                } else {
                    multiFormatWriter = new MultiFormatWriter();
                    try{
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2Barcode, format, 500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        image.setImageBitmap(bitmap);
                        mDatabaseHandler.addProduct(product);


                        saveImageToSDCard(bitmap,product.getProductName(),storeBarcode.getStoreName());
                        Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.success_generate), Toast.LENGTH_LONG).show();
                        fab.setVisibility(View.GONE);
                        fabShare.setVisibility(View.VISIBLE);
                    } catch (Exception e){
                        Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.error_generate), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save this bitmap to a file.
                File cache = activity.getExternalCacheDir();
                File sharefile = new File(cache, "toshare.png");
                Log.d("share file type is", sharefile.getAbsolutePath());
                try {
                    FileOutputStream out = new FileOutputStream(sharefile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e("ERROR", String.valueOf(e.getMessage()));

                }


                // Now send it out to share
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file://" + sharefile));

                startActivity(Intent.createChooser(share,
                        "Share This QR code with"));





            }
        });


    }

//    /**
//     * This method handles Text that was shared by an other app to SecScanQR and generates a qr code
//     * @param intent from Share to from other Apps
//     */
//    private void handleSendText(Intent intent){
//        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//        if(sharedText != null){
////            text.setText(sharedText);
//            text2Barcode = sharedText;
//        }
//    }

    /**
     * This method saves all data before the Activity will be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(STATE_TEXT, text2Barcode);
    }




    public void saveImageToSDCard(Bitmap bitmap,String name,String areaName) {
        int num = 0;

        File myDir = new File(
                Environment.getExternalStorageDirectory().getPath()
                        + File.separator
                        + FOLDER_NAME+  File.separator
                        + areaName);

        myDir.mkdirs();
        String fname = name + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) {
            fname = name + (num++) + ".jpg";
            file = new File(myDir, fname);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();



        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity,"Sorry! Failed to save QR code",
                    Toast.LENGTH_LONG).show();
        }
    }

   public int getUniqueId()
   {
       int v = 0;
       final List<Integer> sack = new ArrayList<>(RANGE);
       for (int i = 0; i < RANGE; i++) sack.add(i);
        Collections.shuffle(sack);
       v = sack.get(12);
       return v;
   }

    /**
     * Generates the chosen format from the spinner menu
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String compare = adapterView.getItemAtPosition(position).toString();
            if(compare.equals("QR_CODE")){
            format = BarcodeFormat.QR_CODE;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        format = BarcodeFormat.QR_CODE;
    }
}
