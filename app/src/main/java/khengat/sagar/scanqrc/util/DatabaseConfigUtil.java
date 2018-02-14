package khengat.sagar.scanqrc.util;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

import khengat.sagar.scanqrc.model.Area;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.model.History;
import khengat.sagar.scanqrc.model.Product;
import khengat.sagar.scanqrc.model.Store;
import khengat.sagar.scanqrc.model.User;


/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil
{
	
	
	@SuppressWarnings("rawtypes")
	static Class[] classes = new Class[]{Area.class,Store.class,User.class, Product.class, Cart.class, History.class};
	
	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt",classes);
	}

}
