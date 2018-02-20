package khengat.sagar.scanqrc.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khengat.sagar.scanqrc.Adapters.CustomHistory;
import khengat.sagar.scanqrc.Adapters.CustomcheckOut;
import khengat.sagar.scanqrc.Constants.Config;
import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.model.History;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.util.DatabaseHandler;
import khengat.sagar.scanqrc.util.DatabaseHelper;
import khengat.sagar.scanqrc.util.MyAdapterListener;




/**
 * This class is the HistoryActivity and lists all scanned qr-codes
 */


public class HistoryActivity extends AppCompatActivity {

    private  int userToken;
    private List<History> cartList;
    private RecyclerView recyclerView;
    final Activity activity = this;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private DatabaseHandler mDatabaeHelper;
    Gson gson;
    Store storeBarcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.product_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        gson = new Gson();
        //Fetching the boolean value form sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Config.STORE_SHARED_PREF, "");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        storeBarcode = gson.fromJson(json, Store.class);

        cartList = new ArrayList<>();
        mDatabaeHelper = new DatabaseHandler(this);


        fnOrderHistory();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistoryActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
    private void fnOrderHistory() {


        cartList = mDatabaeHelper.fnGetAllHistory(storeBarcode);


        if (cartList.isEmpty()) {
//LinearLayOut Setup
            LinearLayout linearLayout= new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

//ImageView Setup
            ImageView imageView = new ImageView(this);

//setting image resource
            imageView.setImageResource(R.drawable.empty_cart);

//setting image position
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

//adding view to layout
            linearLayout.addView(imageView);
//make visible to program
            setContentView(linearLayout);
        }
//
        else {

            adapter = new CustomHistory(cartList, activity, new MyAdapterListener() {
                @Override
                public void buttonViewOnClick(View v, int position) {

                }

                @Override
                public void imageViewOnClick(View v, int position) {
                    History p = cartList.get(position);
                    Cart cart = new Cart();
                    cart.setProductCartId(p.getProductCartId());
                    cart.setProductSize(p.getProductSize());
                    cart.setStore(storeBarcode);
                    cart.setProductUnit(p.getProductUnit());
                    cart.setProductBrand(p.getProductBrand());
                    cart.setProductName(p.getProductName());
                    cart.setProductDescription(p.getProductDescription());
                    cart.setProductQuantity(p.getProductQuantity());
                    cart.setProductTotalPrice(p.getProductTotalPrice());
                    addCart(cart);
                }
            });

            //Adding adapter to recyclerview
            recyclerView.setAdapter(adapter);


        }

    }

    public void addCart(final Cart cart )
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        final TextView unit = (TextView) dialogView.findViewById(R.id.unit);

        dialogBuilder.setTitle("Add Quantity");
        unit.setText(cart.getProductUnit());
        dialogBuilder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String edtQ =   edt.getText().toString().trim();
                int value = Integer.parseInt(edtQ);





                cart.setProductQuantity(value);











                mDatabaeHelper.addToCart(cart);




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
