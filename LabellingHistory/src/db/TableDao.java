package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import db.oracle.OracleUserObject;

public class TableDao {

	public static SortedSet<Table> getUserTableBeanSet(Connection conn) throws SQLException{
		List<String> userTable = OracleUserObject.getUserTable(conn);
		SortedSet<Table> tableBeanSet = new TreeSet<Table>();
		
		for(String tableName: userTable){
			tableBeanSet.add(new Table(tableName));
		}
		
		return tableBeanSet;
	}
}
