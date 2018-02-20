package khengat.sagar.scanqrc.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joanzapata.iconify.widget.IconButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import khengat.sagar.scanqrc.Constants.Config;
import khengat.sagar.scanqrc.LoginActivity;
import khengat.sagar.scanqrc.R;

import khengat.sagar.scanqrc.activities.generator.GenerateActivity;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.model.Product;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.util.BadgeView;
import khengat.sagar.scanqrc.util.BottomNavigationViewHelper;
import khengat.sagar.scanqrc.util.DatabaseHandler;
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

    private TextView mTvInformation;
    private BottomNavigationView action_navigation;
    final Activity activity = this;
    private String qrcode = "", qrcodeFormat = "";
    private DatabaseHandler mDatabaeHelper;
    private static final String STATE_QRCODE = MainActivity.class.getName();
    private static final String STATE_QRCODEFORMAT = "";


    public TextView textViewName;
    public TextView textViewSize;
    public TextView textViewunit;
    public TextView textViewDescription;
    public TextView textPrice;
    public TextView textStore;
    public TextView textBrand;
    Product product;
    Cart cart;
    public CardView cardView;
    LayerDrawable icon;
    static BadgeView badge;
    Gson gson;
    Store storeBarcode;
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
                case R.id.main_action_navigation_addToCart:
                    addCart();
                    return true;
                case R.id.main_action_navigation_reset:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
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
        cardView = (CardView) findViewById(R.id.card_view);
        mTvInformation = (TextView) findViewById(R.id.tvTxtqrcode);
        textViewName = (TextView) findViewById(R.id.product_name);
        textViewDescription= (TextView) findViewById(R.id.tv_product_desc);
        textStore= (TextView) findViewById(R.id.tv_product_store);
        textPrice= (TextView) findViewById(R.id.tv_price);
        textViewSize = (TextView) findViewById(R.id.tv_product_size);
        textViewunit = (TextView) findViewById(R.id.tv_product_unit);
        textBrand = (TextView) findViewById(R.id.product_brand);
        mDatabaeHelper = new DatabaseHandler(this);
        cart = new Cart();
        gson = new Gson();
        BottomNavigationView main_navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(main_navigation);
        main_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        main_navigation.clearFocus();



        //Fetching the boolean value form sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String sharedPreferencesString = sharedPreferences.getString(Config.STORE_SHARED_PREF, "");

        storeBarcode = gson.fromJson(sharedPreferencesString, Store.class);



        action_navigation = (BottomNavigationView) findViewById(R.id.main_action_navigation);
        BottomNavigationViewHelper.disableShiftMode(action_navigation);
        action_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);





        //If the device were rotated then restore information
        if(savedInstanceState != null){
            qrcode = savedInstanceState.getString(STATE_QRCODE);
            qrcodeFormat = savedInstanceState.getString(STATE_QRCODEFORMAT);
            if(qrcode.equals("")){

            } else {
                String json = qrcode;
                product = gson.fromJson(json, Product.class);

                if(product.getStore().getStoreId()==storeBarcode.getStoreId()  && product.getStore().getStoreName().equalsIgnoreCase(storeBarcode.getStoreName())) {
                    cardView.setVisibility(View.VISIBLE);

                    textViewName.setText(product.getProductName());

                    textViewDescription.setText(product.getProductDescription());

                    textPrice.setText(String.valueOf(product.getProductTotalPrice()));

                    textViewSize.setText(product.getProductSize());
                    textViewunit.setText(product.getProductUnit());

                    textBrand.setText(product.getProductBrand());

                    textStore.setText(product.getStore().getStoreName());

                    mTvInformation.setVisibility(View.GONE);
                    action_navigation.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(activity, "This product belongs to another store", Toast.LENGTH_SHORT).show();
                }
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
        MenuItem itemCart = menu.findItem(R.id.cart);

         icon = (LayerDrawable) itemCart.getIcon();
       int i =  mDatabaeHelper.fnGetCartCount(storeBarcode);
        setBadgeCount(activity, icon, String.valueOf(i));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.settings:
                startActivity(new Intent(MainActivity.this, ChangePassword.class));
                return true;
            case R.id.logout:
                logout();
                return true;
            case R.id.cart:

                    Intent intent = new Intent(MainActivity.this,CartActivity.class);
                    startActivity(intent);
                    finish();

                return true;
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
                    try {
                        String json = qrcode;
                        product = gson.fromJson(json, Product.class);

                        if (product.getStore().getStoreId() == storeBarcode.getStoreId() && product.getStore().getStoreName().equalsIgnoreCase(storeBarcode.getStoreName())) {
                            cardView.setVisibility(View.VISIBLE);

                            textViewName.setText(product.getProductName());

                            textViewDescription.setText(product.getProductDescription());

                            textPrice.setText(String.valueOf(product.getProductTotalPrice()));

                            textViewSize.setText(product.getProductSize());
                            textViewunit.setText(product.getProductUnit());

                            textBrand.setText(product.getProductBrand());

                            textStore.setText(product.getStore().getStoreName());

                            mTvInformation.setVisibility(View.GONE);
                            action_navigation.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(activity, "This product belongs to another store", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(activity, "Invalid QR Code. Please try again..", Toast.LENGTH_SHORT).show();
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
//        boolean insertData = mDatabaeHelper.addData(newCode);
//        if(!insertData){
//            Toast.makeText(this, getResources().getText(R.string.error_add_to_database), Toast.LENGTH_LONG).show();
//        }
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
    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {



        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeView) {
            badge = (BadgeView) reuse;
        } else {
            badge = new BadgeView(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


    public void addCart()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        final TextView unit = (TextView) dialogView.findViewById(R.id.unit);

        dialogBuilder.setTitle("Add Quantity");
        unit.setText(product.getProductUnit());
        dialogBuilder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String edtQ =   edt.getText().toString().trim();
                int value = Integer.parseInt(edtQ);



                double multiQ = value * product.getProductTotalPrice();

                product.setProductQuantity(value);






                cart.setProductCartId(product.getProductId());
                cart.setProductSize(product.getProductSize());
                cart.setStore(storeBarcode);
                cart.setProductUnit(product.getProductUnit());
                cart.setProductBrand(product.getProductBrand());
                cart.setProductName(product.getProductName());
                cart.setProductDescription(product.getProductDescription());
                cart.setProductQuantity(product.getProductQuantity());
                cart.setProductTotalPrice(product.getProductTotalPrice());



                mDatabaeHelper.addToCart(cart);

                int i =  mDatabaeHelper.fnGetCartCount(storeBarcode);
                setBadgeCount(activity, icon, String.valueOf(i));


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
