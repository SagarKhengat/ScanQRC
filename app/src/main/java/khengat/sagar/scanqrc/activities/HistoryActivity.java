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

        storeBarcode = gson.fromJson(json, Store.class);

        cartList = new ArrayList<>();
        mDatabaeHelper = new DatabaseHandler(this);
        ActionBar actionBar = getSupportActionBar();

        fnOrderHistory();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button

        }
        return super.onOptionsItemSelected(item);
    }

    private void fnOrderHistory() {


        cartList = mDatabaeHelper.fnGetAllHistory(storeBarcode);


//


        adapter = new CustomHistory(cartList, activity, new MyAdapterListener() {
            @Override
            public void buttonViewOnClick(View v, int position) {

            }

            @Override
            public void imageViewOnClick(View v, int position) {
              History p = cartList.get(position);
                Cart c = mDatabaeHelper.fnGetCartFromCartHistory(p);
                addCart(c);
            }
        });

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


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



                double multiQ = value * cart.getProductTotalPrice();

                cart.setProductQuantity(value);
                cart.setProductTotalPrice(multiQ);










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
