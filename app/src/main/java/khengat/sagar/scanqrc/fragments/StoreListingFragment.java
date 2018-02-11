package khengat.sagar.scanqrc.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import khengat.sagar.scanqrc.Adapters.SpinnerAreaAdapter;
import khengat.sagar.scanqrc.Adapters.SpinnerStoreAdapter;
import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Area;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.util.DatabaseHandler;

/**
 * Created by Sagar Khengat on 10/02/2018.
 */

public class StoreListingFragment extends Fragment {

    View view;
    private TextView mTextStore;
    private TextView mTextArea;
    Store store;

    private DatabaseHandler mDatabaeHelper;

    public static FragmentManager manager;
    public static FragmentTransaction ft;
    Spinner spinnerStore;
    Spinner spinnerArea;
    FloatingActionButton fabArea;
    FloatingActionButton fabStore;
    FloatingActionButton fabGo;
    private SpinnerAreaAdapter areaAdapter;
    private SpinnerStoreAdapter storeAdapter;
       static String storeName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        mDatabaeHelper = new DatabaseHandler(getActivity());






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_store_listing, container, false);

        mTextStore = (TextView) view.findViewById(R.id.txtStore);
        mTextArea = (TextView) view.findViewById(R.id.txtArea);
        spinnerArea = (Spinner) view.findViewById(R.id.spinnerArea);
        spinnerStore = (Spinner) view.findViewById(R.id.spinnerStore);
        fabStore = (FloatingActionButton) view.findViewById(R.id.fabStore);
        fabArea = (FloatingActionButton) view.findViewById(R.id.fabArea);
        fabGo = (FloatingActionButton) view.findViewById(R.id.fabGo);


        if(mDatabaeHelper.fnGetAllArea().size()==0)
        {
            mTextArea.setText("Please Add Area First");
            fabArea.setVisibility(View.VISIBLE);
            spinnerArea.setVisibility(View.GONE);
        }
        if(mDatabaeHelper.fnGetAllArea().size()!=0)
        {
            loadSpinnerArea();
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
                Intent accountsIntent = new Intent(getActivity(), khengat.sagar.scanqrc.activities.MainActivity.class);
                accountsIntent.putExtra("store",store);
                startActivity(accountsIntent);

            }
        });
        fabStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpFragment(new AddStoreorAreaFragment(), "Store");
            }
        });

        fabArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpFragment(new AddStoreorAreaFragment(), "Area");

            }
        });

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Area area = areaAdapter.getItem(position);

                if(mDatabaeHelper.fnGetAllStore().size()!=0)
                {
                    loadSpinnerStore(area);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 store = storeAdapter.getItem(position);

                storeName = store.getStoreName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


    }
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerArea() {
        // database handler


        // Spinner Drop down elements
        List<Area> allAreas = mDatabaeHelper.fnGetAllArea();

        // Creating adapter for spinner
        areaAdapter = new SpinnerAreaAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                allAreas);

        // Drop down layout style - list view with radio button


        // attaching data adapter to spinner
        spinnerArea.setAdapter(areaAdapter);
    }



    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerStore(Area area) {
        // database handler


        // Spinner Drop down elements
        List<Store> allAreas = mDatabaeHelper.fnGetStoreInArea(area);

        if (allAreas.size()==0)
        {
            mTextStore.setText("Please Add Store First");
            fabStore.setVisibility(View.VISIBLE);
            spinnerStore.setVisibility(View.GONE);
        }

        // Creating adapter for spinner
        storeAdapter = new SpinnerStoreAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                allAreas);

        // Drop down layout style - list view with radio button


        // attaching data adapter to spinner
        spinnerStore.setAdapter(storeAdapter);
    }


    private void setUpFragment(Fragment fragment, String isFrom) {
        Bundle bundle = new Bundle();
        bundle.putString("isFrom", isFrom);

        manager = getActivity().getSupportFragmentManager();
        ft = manager.beginTransaction();
        ft.replace(android.R.id.tabcontent, fragment);
        fragment.setArguments(bundle);
        ft.commit();

    }

}