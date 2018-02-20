package khengat.sagar.scanqrc.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khengat.sagar.scanqrc.Adapters.CustomcheckOut;
import khengat.sagar.scanqrc.Constants.Config;
import khengat.sagar.scanqrc.LoginActivity;
import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.model.History;
import khengat.sagar.scanqrc.model.Product;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.util.DatabaseHandler;
import khengat.sagar.scanqrc.util.MyAdapterListener;



public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Button placeOrder;
    private TextView totalAmount;
    private List<Cart> productList;
    private ArrayList<Double> alTotalAmount;
    private DatabaseHandler mDatabaeHelper;
    final Activity activity = this;
    Gson gson;
    Store storeBarcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        totalAmount = (TextView) findViewById(R.id.tv_total_amount);

        mDatabaeHelper = new DatabaseHandler(this);

        productList = new ArrayList<>();

        alTotalAmount = new ArrayList<>();

        gson = new Gson();
        //Fetching the boolean value form sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Config.STORE_SHARED_PREF, "");

        storeBarcode = gson.fromJson(json, Store.class);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Review Order");

        recyclerView = (RecyclerView) findViewById(R.id.product_recycler);

        placeOrder = (Button) findViewById(R.id.btn_place_order);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        productList = mDatabaeHelper.fnGetAllCart(storeBarcode);



        fnOrderHistory();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fnSubmitOrder();


            }
        });

    }




    private void fnOrderHistory() {
        int quantity = 0;
        if (productList.isEmpty()) {
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

        } else {
            for (Cart cart : productList) {
                double d = cart.getProductTotalPrice();
                quantity = cart.getProductQuantity();
                double psp = quantity * d;

                double multiQ = quantity * d;
                alTotalAmount.add(multiQ);

            }

            double sum = 0;
            for (int i = 0; i < alTotalAmount.size(); i++) {
                sum = sum + alTotalAmount.get(i);
            }
            String stringPrice = Double.toString(sum);
            totalAmount.setText(stringPrice);
//
            adapter = new CustomcheckOut(productList, CartActivity.this, new MyAdapterListener() {
                @Override
                public void buttonViewOnClick(View v, int position) {

                }

                @Override
                public void imageViewOnClick(View v, int position) {
                    Cart p = productList.get(position);

                    fnDeleteOrder(p);
                }
            });

            //Adding adapter to recyclerview
            recyclerView.setAdapter(adapter);


        }
    }

    private void fnDeleteOrder(Cart id)
    {



                    mDatabaeHelper.deleteCartitem(id);

                    Intent intent = new Intent(CartActivity.this,CartActivity.class);
                    startActivity(intent);
                    finish();





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
        Intent intent = new Intent(CartActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void fnSubmitOrder()
    {

        String amt = totalAmount.getText().toString();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please pay Rs. "+amt+" .");
        alertDialogBuilder.setTitle("Check-Out");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        for(Cart cart : productList)
                        {
                            History history = new History();
                           Product product =  mDatabaeHelper.fnGetProductFromCart(cart);
                            history.setProductCartId(product.getProductId());
                            history.setProductSize(product.getProductSize());
                            history.setStore(storeBarcode);
                            history.setProductUnit(product.getProductUnit());
                            history.setProductBrand(product.getProductBrand());
                            history.setProductName(product.getProductName());
                            history.setProductDescription(product.getProductDescription());
                            history.setProductQuantity(product.getProductQuantity());
                            history.setProductTotalPrice(product.getProductTotalPrice());

                            mDatabaeHelper.addProductHistory(history);
                        }

                        mDatabaeHelper.fnDeleteAllCart(storeBarcode);

                        Toast.makeText(activity, "CheckOut done successfully. Thank you ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CartActivity.this,MainActivity.class);
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
