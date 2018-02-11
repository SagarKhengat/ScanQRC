package khengat.sagar.scanqrc.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import khengat.sagar.scanqrc.model.Area;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.model.Product;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.model.User;


public class DatabaseHandler {
	private static final String TAG = "DatabaseHandler";

	RuntimeExceptionDao<Area, Integer> areaDao;
	RuntimeExceptionDao<Store, Integer> storeDao;

	private RuntimeExceptionDao<User, Integer> userDao;
	private RuntimeExceptionDao<Product, Integer> productDao;
	private RuntimeExceptionDao<Cart, Integer> cartDao;


	private DatabaseHelper databaseHelper;

	private Context context;

	public DatabaseHandler() {

	}

	public DatabaseHandler(Context context) {
		this.context = context;
		initElements();
	}

	public void initElements() {


		areaDao = getHelper().getAreaDao();

		storeDao = getHelper().getStoreDao();

		userDao = getHelper().getUserDao();
		productDao = getHelper().getProductDao();
		cartDao = getHelper().getCartDao();




	}


	public void insertUser(User user) {
		try {
			userDao.create(user);
		} catch (OutOfMemoryError e) {

			e.printStackTrace();
			Toast.makeText(context, "Problem in memory allocation. Please free some memory space and try again.", Toast.LENGTH_LONG).show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}


	private DatabaseHelper getHelper() {
		databaseHelper = null;
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	/**
	 * This method to check user exist or not
	 *
	 * @param email
	 * @return true/false
	 */
	public boolean checkUser(String email) {

		try {
			QueryBuilder < User, Integer > qb = userDao.queryBuilder();

			long noOfRecords = qb.where().like( "email", email ).countOf();


			if( noOfRecords == 0 ) {
				return false;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return true;
	}
	/**
	 * This method to check user exist or not
	 *
	 * @param email
	 * @param password
	 * @return true/false
	 */
	public boolean checkUser(String email, String password) {

		try {
			QueryBuilder<User, Integer> qb = userDao.queryBuilder();

			long noOfRecords = qb.where().like("email", email).and().eq("password",password).countOf();


			if (noOfRecords == 0) {
				return false;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return true;
	}

	public void addUser(User user) {
		try
		{
			userDao.createIfNotExists( user );
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText( context, "Problem in memory allocation. Please free some memory space and try again.", Toast.LENGTH_LONG ).show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List<Store> fnGetAllStore() {
		List< Store > mListIndustry = new ArrayList<>();

		try {
			QueryBuilder< Store, Integer > queryBuilder = storeDao.queryBuilder();
			PreparedQuery< Store > preparedQuery = null;
			preparedQuery = queryBuilder.prepare();
			mListIndustry = storeDao.query( preparedQuery );
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return mListIndustry;
	}

	public List<Area> fnGetAllArea() {
		List< Area > mListIndustry = new ArrayList<>();

		try {
			QueryBuilder< Area, Integer > queryBuilder = areaDao.queryBuilder();
			PreparedQuery< Area > preparedQuery = null;
			preparedQuery = queryBuilder.prepare();
			mListIndustry = areaDao.query( preparedQuery );
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return mListIndustry;
	}


	public void addStore(Store store) {
		try
		{
			storeDao.createIfNotExists( store );
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText( context, "Problem in memory allocation. Please free some memory space and try again.", Toast.LENGTH_LONG ).show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public void addArea(Area area) {
		try
		{
			areaDao.createIfNotExists( area );
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText( context, "Problem in memory allocation. Please free some memory space and try again.", Toast.LENGTH_LONG ).show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public List< Store> fnGetStoreInArea( Area area ) {
		List <Store> mListStores = new ArrayList<>();
		List <Store> mListAllStores = fnGetAllStore();

		try {
//			QueryBuilder < Store, Integer > qb = storeDao.queryBuilder();
//			Where<Store, Integer> where = qb.where();
//
//			where.like( "areaId", area.getAreaId() );//.or().like("customerPrintAs", "%"+nameToSearch+"%");
//
//
//
//			// It filters only data present in DB fetched at the time of sync.
//			PreparedQuery < Store> pq = where.prepare();
//			mListStores = storeDao.query( pq );


			for (Store store : mListAllStores)
			{
				if(store.getArea().getAreaId()==area.getAreaId())
				{
					mListStores.add(store);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return mListStores;
	}

	public void addProduct(Product product) {
		try
		{
			productDao.createIfNotExists( product );
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText( context, "Problem in memory allocation. Please free some memory space and try again.", Toast.LENGTH_LONG ).show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addToCart(Cart product) {
		try
		{
			cartDao.createIfNotExists( product );
			Toast.makeText( context, "Product Added in Cart Successfully.", Toast.LENGTH_LONG ).show();
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText( context, "Problem in memory allocation. Please free some memory space and try again.", Toast.LENGTH_LONG ).show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}