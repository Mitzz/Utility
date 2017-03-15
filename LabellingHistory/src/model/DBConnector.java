package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import utility.ExceptionUtility;
import file.model.PropertyFileLoader;

public class DBConnector {
	private final static String CONNNECTION_DETAIL_FILE_PATH = "D:/Connection.properties";
	private final static String DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	
	public final static int MITHULLOCALUSER = 1;
	public final static int LOCAL_SYS_USER = 2;
	public final static int LOCAL_DEV2 = 3;
	public final static int SIT2 = 6;
	public final static int LOCAL_COPY = 7;
	public final static int LOCAL_COPY_2 = 9;
	public final static int SIT3 = 10;
	public final static int LOCAL_COPY_3 = 11;
	
	private static Properties prop = null;
	
	public static void init() throws IOException, ClassNotFoundException{
		Class.forName(DRIVER_CLASS);
		prop = new PropertyFileLoader(CONNNECTION_DETAIL_FILE_PATH).load();
	}

	public static Connection getConnection(int id) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		if(prop == null) loadPropertyFile();
		String url = prop.getProperty("url" + id);
		String username = prop.getProperty("username" + id);
		String password = prop.getProperty("password" + id);
		ExceptionUtility.throwIfAnyNull("Url, username or password have null value", url, username, password);
		connection = DriverManager.getConnection(url, username, password);
		return connection;
	}

	private static void loadPropertyFile() {
		try {
			init();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static Connection getConnection(String connectionName) throws ClassNotFoundException, SQLException {
		String connectionNameProp = prop.getProperty(connectionName);
		ExceptionUtility.throwIfAnyNull("Connction Name does not exists", connectionNameProp);
		int id = Integer.parseInt(connectionNameProp);
		return getConnection(id);
	}

}
