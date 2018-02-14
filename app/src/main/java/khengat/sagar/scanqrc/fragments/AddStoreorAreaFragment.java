package khengat.sagar.scanqrc.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import khengat.sagar.scanqrc.Adapters.SpinnerAreaAdapter;
import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Area;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.util.DatabaseHandler;


public class AddStoreorAreaFragment extends Fragment {


	View view;
	private Context context;
	private DatabaseHandler db;
	private TextInputEditText mTextArea;
	private TextView mTextTitle;
	Spinner spinner;
	FloatingActionButton fabAdd;
	String isFrom;
	Area area;
	Store store;
	private SpinnerAreaAdapter areaAdapter;
	public static FragmentManager manager;
	public static FragmentTransaction ft;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



		context = getActivity();
		db = new DatabaseHandler(context);

		area = new Area();
		store = new Store();




	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_add_store_area, container, false);

		mTextArea = (TextInputEditText) view.findViewById(R.id.txtAdd);
		mTextTitle = (TextView) view.findViewById(R.id.txtArea);
		spinner = (Spinner) view.findViewById(R.id.spinnerArea);

		fabAdd = (FloatingActionButton) view.findViewById(R.id.fabArea);


		if(db.fnGetAllArea().size()==0)
		{
			mTextTitle.setText("Please Add Area First");

			spinner.setVisibility(View.GONE);
		}
		if(db.fnGetAllArea().size()!=0)
		{
			loadSpinnerArea();
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				area = areaAdapter.getItem(position);



			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		Bundle bundle = this.getArguments();
		isFrom = bundle.getString("isFrom");

		if(isFrom.equalsIgnoreCase("Area"))
		{
			mTextTitle.setText("Add Area");

			spinner.setVisibility(View.GONE);
		}


		fabAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isFrom.equalsIgnoreCase("Area") || db.fnGetAllArea().size()==0)
				{
					if (mTextArea.getText().toString()!= null) {
						String areaName = mTextArea.getText().toString();
						Area area1 =new Area();
						area1.setAreaName(areaName);

						db.addArea(area1);
						Toast.makeText(context, "Added Area "+areaName+" Successfully", Toast.LENGTH_SHORT).show();
						setUpFragment(new StoreListingFragment(),"Area");
					}
				}
				else {
					if (mTextArea.getText().toString()!= null) {
						String storeName = mTextArea.getText().toString();
						store.setStoreName(storeName);
						store.setArea(area);
						db.addStore(store);
						Toast.makeText(context, "Added Store "+storeName+" Successfully", Toast.LENGTH_SHORT).show();
						setUpFragment(new StoreListingFragment(),"Store");
					}
				}
			}
		});


		return view;
	}
	private void setUpFragment(Fragment fragment, String isFrom) {
		Bundle bundle = new Bundle();
		bundle.putString("isFrom", isFrom);

		manager = getActivity().getSupportFragmentManager();
		ft = manager.beginTransaction();
		ft.replace(android.R.id.tabcontent, fragment);
		fragment.setArguments(bundle);
		ft.addToBackStack(null);
		ft.commit();

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
		List<Area> allAreas = db.fnGetAllArea();

		// Creating adapter for spinner
		areaAdapter = new SpinnerAreaAdapter(getActivity(),
				android.R.layout.simple_spinner_item,
				allAreas);

		// Drop down layout style - list view with radio button


		// attaching data adapter to spinner
		spinner.setAdapter(areaAdapter);
	}

}