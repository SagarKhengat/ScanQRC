package khengat.sagar.scanqrc.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Area;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.model.History;
import khengat.sagar.scanqrc.model.Product;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.model.User;


/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application
	public static final String DATABASE_NAME = "ScanQRC.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version

	//increase database version from 18 to 19 due to add formxml field in adminform xml responce
	private static final int DATABASE_VERSION  = 1;

	// the DAO object we use to access the Appointments table
	private RuntimeExceptionDao<Area, Integer> areaDao = null;
	private RuntimeExceptionDao<Store, Integer> storeDao = null;

	private RuntimeExceptionDao<User, Integer> userDao= null;
	private RuntimeExceptionDao<Product, Integer> productDao= null;
	private RuntimeExceptionDao<Cart, Integer> cartDao= null;
	private RuntimeExceptionDao<History, Integer> historyDao= null;


	 Context context1;



	private static final String TABLE_SCANNED = "scanned";
	private static final String COLUMN_SCANNED_ID = "scanned_id";
	private static final String COLUMN_SCANNED_QRCODE = "code";

	private String CREATE_SCANNED_TABLE = "CREATE TABLE " + TABLE_SCANNED + "(" + COLUMN_SCANNED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_SCANNED_QRCODE + " TEXT)";

	private String DROP_SCANNED_TABLE = "DROP TABLE IF EXISTS" + TABLE_SCANNED;














	public DatabaseHelper(Context context) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config );

		context1 = context;
		//getWritableDatabase();
	}

	@Override
	public void setWriteAheadLoggingEnabled(boolean enabled) {
		super.setWriteAheadLoggingEnabled(enabled);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
//			db.enableWriteAheadLogging();

			setWriteAheadLoggingEnabled(true);
			TableUtils.createTable(connectionSource, Area.class);
			TableUtils.createTable(connectionSource, Store.class);

			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Product.class);
			TableUtils.createTable(connectionSource, Cart.class);
			TableUtils.createTable(connectionSource, History.class);
			db.execSQL(CREATE_SCANNED_TABLE);


			//**********@@@***************Log.e("onCreate Database Info. = ", "db="+db.toString()+" Connection String"+connectionSource.toString());
		} catch (SQLException e) {
			//Log.d(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion)
	{
		try {
			//Log.i(DatabaseHelper.class.getName(), "onUpgrade");

			TableUtils.dropTable(connectionSource, Area.class, true);
			TableUtils.dropTable(connectionSource, Store.class, true);

			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Product.class, true);
			TableUtils.dropTable(connectionSource, Cart.class, true);
			TableUtils.dropTable(connectionSource, History.class, true);

			db.execSQL(DROP_SCANNED_TABLE);

			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);

			//**********@@@***************Log.e("onUpgrade Database Info. = ", "db="+db.toString()+" Connection String = "+connectionSource.toString());

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao
	 * for our Appointments class. It will create it or just give the cached
	 * value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public RuntimeExceptionDao<Area, Integer> getAreaDao() {
		if (areaDao == null) {
			areaDao = getRuntimeExceptionDao(Area.class);
		}
		return areaDao;
	}
	public RuntimeExceptionDao<Cart, Integer> getCartDao() {
		if (cartDao == null) {
			cartDao = getRuntimeExceptionDao(Cart.class);
		}
		return cartDao;
	}

	public RuntimeExceptionDao<Product, Integer> getProductDao() {
		if (productDao == null) {
			productDao = getRuntimeExceptionDao(Product.class);
		}
		return productDao;
	}


	public RuntimeExceptionDao<Store, Integer> getStoreDao() {
		if (storeDao == null) {
			storeDao = getRuntimeExceptionDao(Store.class);
		}
		return storeDao;
	}



	public RuntimeExceptionDao<User, Integer> getUserDao() {
		if (userDao == null) {
			userDao = getRuntimeExceptionDao(User.class);
		}
		return userDao;
	}


	public RuntimeExceptionDao<History, Integer> getHistoryDao() {
		if (historyDao == null) {
			historyDao = getRuntimeExceptionDao(History.class);
		}
		return historyDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		areaDao = null;
		storeDao = null;
		userDao = null;
		productDao = null;
		cartDao = null;
		historyDao = null;

	}








	/**
	 * This method adds
	 * @param item = item that will be added to the database
	 * @return a boolean if the code was added to the database or not
	 */
	public boolean addData(String item){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_SCANNED_QRCODE, item);
		//Insert Data into Database with a checking if the insert into the database worked
		long result = db.insert(TABLE_SCANNED, null, values);
		if(result == -1){
			db.close();
			return false;
		} else {
			db.close();
			return true;
		}
	}

	/**
	 * Returns all the data from the database
	 * @return a Cursor pointing on the requested table
	 */
	public Cursor getData(){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_SCANNED;
		Cursor data = db.rawQuery(query, null);
		return data;
	}

	/**
	 * Returns the ID of the scanned code given as a parameter
	 * @param code
	 * @return a Cursor pointing on the id
	 */
	public Cursor getItemID(String code){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT " + COLUMN_SCANNED_ID + " FROM " + TABLE_SCANNED + " WHERE " + COLUMN_SCANNED_QRCODE + " = '" + code + "'";
		Cursor data = db.rawQuery(query, null);
		return  data;

	}

	/**
	 * Delete a specific Item from database
	 * @param id
	 * @param code
	 */
	public void deleteItem(int id, String code){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_SCANNED + " WHERE " + COLUMN_SCANNED_ID + " = '" + id + "'";
		db.execSQL(query);
	}

	public void resetDatabase(){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_SCANNED;
		db.execSQL(query);
	}





}
