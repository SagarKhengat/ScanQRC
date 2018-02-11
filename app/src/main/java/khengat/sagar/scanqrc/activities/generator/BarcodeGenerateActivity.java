package khengat.sagar.scanqrc.activities.generator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.activities.MainActivity;
import khengat.sagar.scanqrc.model.Product;


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

    private TextInputEditText textInputEditTextProductName;
    private TextInputEditText textInputEditTextProductBrand;
    private TextInputEditText textInputEditTextProductDescription;
    private TextInputEditText textInputEditTextProductPrice;
    private TextInputEditText textInputEditTextProductUnit;
    private TextInputEditText textInputEditTextProductStore;
    Product product;

    ImageView image;
    BarcodeFormat format;
    String text2Barcode;
    MultiFormatWriter multiFormatWriter;
    Bitmap bitmap;
    final Activity activity = this;
    private static final String STATE_TEXT = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        product = new Product();
        textInputLayoutProductName = (TextInputLayout) findViewById(R.id.textInputLayoutProductName);
        textInputLayoutProductBrand = (TextInputLayout) findViewById(R.id.textInputLayoutProductBrand);
        textInputLayoutProductDescription = (TextInputLayout) findViewById(R.id.textInputLayoutProductDescription);
        textInputLayoutProductPrice = (TextInputLayout) findViewById(R.id.textInputLayoutProductPrice);
        textInputLayoutProductUnit = (TextInputLayout) findViewById(R.id.textInputLayoutProductUnit);
        textInputLayoutProductStore = (TextInputLayout) findViewById(R.id.textInputLayoutProductStore);

        textInputEditTextProductName = (TextInputEditText) findViewById(R.id.textInputEditTextProductName);
        textInputEditTextProductBrand = (TextInputEditText) findViewById(R.id.textInputEditTextProductBrand);
        textInputEditTextProductDescription = (TextInputEditText) findViewById(R.id.textInputEditTextProductDescription);
        textInputEditTextProductPrice = (TextInputEditText) findViewById(R.id.textInputEditTextProductPrice);
        textInputEditTextProductUnit = (TextInputEditText) findViewById(R.id.textInputEditTextProductUnit);
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



        // Get intent, action and MINE type and check if the intent was started by a share to modul from an other app
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

//        if (Intent.ACTION_SEND.equals(action) && type != null){
//            if("text/plain".equals(type)){
//                handleSendText(intent); //call method to handle sended text
//            }
//        }

        //OnClickListener for the "+" Button and functionality
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                text2Barcode = text.getText().toString().trim();
                if(textInputEditTextProductName.getText().toString().trim()==null)
                {
                    textInputEditTextProductName.setError("Please Enter Product Name");
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

                if(textInputEditTextProductPrice.getText().toString().trim()==null)
                {
                    textInputEditTextProductPrice.setError("Please Enter Product Price");
                }
                else
                {
                    product.setProductTotalPrice(Double.parseDouble(textInputEditTextProductPrice.getText().toString().trim()));
                }

                if(textInputEditTextProductUnit.getText().toString().trim()==null)
                {
                    textInputEditTextProductUnit.setError("Please Enter Product Unit");
                }
                else
                {
                    product.setProductUnit(textInputEditTextProductUnit.getText().toString().trim());
                }

                Gson gson = new Gson();
                text2Barcode = gson.toJson(product);

                if(text2Barcode.equals("")){
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_text_first), Toast.LENGTH_SHORT).show();
                } else {
                    multiFormatWriter = new MultiFormatWriter();
                    try{
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2Barcode, format, 500,500);
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
