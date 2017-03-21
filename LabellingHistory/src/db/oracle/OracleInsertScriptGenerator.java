package db.oracle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.DBConnector;
import utility.DateUtility;
import utility.FileUtility;
import utility.LogUtility;
import db.DbUtility;
import db.Table;

public class OracleInsertScriptGenerator{
	public static List<String> getInsertScript(Connection conn, Table table) throws SQLException, UnsupportedEncodingException{
		List<String> insertStatementList = new ArrayList<String>();
		String tableName = table.getName();
		LogUtility.log("Generating Insert statements for: " + tableName + " <-> " + table.getSelectSqlForInsertion());
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(table.getSelectSqlForInsertion());
		StringBuilder columnValues = new StringBuilder();
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		int[] columnTypes = new int[numColumns];
		StringBuilder columnNames = new StringBuilder("");
		for (int i = 0; i < numColumns; i++) {
			columnTypes[i] = rsmd.getColumnType(i + 1);
			if (i != 0) {
				columnNames.append(",");
			}
			columnNames.append(rsmd.getColumnName(i + 1));
		}

		java.util.Date d = null;
		
		while (rs.next()) {
			columnValues.setLength(0);
			for (int i = 0; i < numColumns; i++) {
				if (i != 0) 
					columnValues.append(",");

				Object data = rs.getObject(i + 1);
				if(data == null) 
					columnValues.append("null");
				else {
					switch (columnTypes[i]) {
						case Types.NUMERIC:
						case Types.BIGINT:
						case Types.BIT:
						case Types.BOOLEAN:
						case Types.DECIMAL:
						case Types.DOUBLE:
						case Types.FLOAT:
						case Types.INTEGER:
						case Types.SMALLINT:
						case Types.TINYINT:
							String v = rs.getString(i + 1);
							columnValues.append(v);
							break;
		
						case Types.DATE:
							d = rs.getDate(i + 1);
						case Types.TIME:
							d = rs.getTime(i + 1);
						case Types.TIMESTAMP:
						case -101:
							d = rs.getTimestamp(i + 1);
//							System.out.println("Date: " + d + ", Time: " + d.getTime());
							columnValues.append("TO_TIMESTAMP('" + DateUtility.ORALCE_DATE_FORMAT.format(d) + "', 'YYYY/MM/DD HH24:MI:SS')");
							break;
						case Types.BLOB:
							columnValues.append("null");
							break;
						default:
							v = new String(rs.getString(i + 1).getBytes(), "UTF-8");
							columnValues.append("'" + v.replaceAll("'", "''") + "'");
							break;
					}
				}
			}
			insertStatementList.add(String.format("INSERT INTO %s (%s) values (%s)", tableName, columnNames, columnValues));
		}
		
		if(stmt != null) stmt.close();
		if(rs != null) rs.close();
		
		return insertStatementList;
	}
	
	public static List<String> getInsertScript(Connection conn, String tableName) throws SQLException, UnsupportedEncodingException {
		return getInsertScript(conn, new Table(tableName));
	}
	
	public static String getInsertScriptAsString(Connection conn, Table table) throws Exception {
		List<String> insertDMLList = getInsertScript(conn, table);
		StringBuilder builder = new StringBuilder();
		
		for(String insertDML: insertDMLList)
			builder.append(insertDML).append(";").append(FileUtility.NEW_LINE);
		
		return new String(builder);
	}
	
	public static void main(String[] args) {
		Connection connection = null;
		try {
			DBConnector.init();
			connection = DBConnector.getConnection(DBConnector.SIT2);
			List<String> insertScriptList = OracleInsertScriptGenerator.getInsertScript(connection, "TB_MULTILANG_MASTER");
			for(String inserString: insertScriptList){
				FileUtility.appendToFile("C:/APT.txt", inserString);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtility.closeResources(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

