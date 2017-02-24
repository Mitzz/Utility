package db.oracle;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import utility.CollectionUtility;
import utility.LogUtility;
import db.DbUtility;
import db.Table;

public class OracleDbUtility {

	public static int setTransform(Connection con, OracleTransformParam transform, boolean value) throws SQLException{
		CallableStatement cstm1 = null;
		String sql = null;
		try{
			sql = (value ? transform.trueSql(): transform.falseSql());
			cstm1 = con.prepareCall(sql);
			return cstm1.executeUpdate();
		}
		finally{
			if(cstm1 != null) cstm1.close();
		}
	}
	
	public static void executeDdl(Connection connection, List<String> ddlSqlList) throws SQLException {
		executeBatch(connection, 900, ddlSqlList);
	}
	
	public static void transferTableData(Connection sourceConn, Connection destinationConn) throws Exception{
//		System.out.println(new Date());		
		List<String> conn1UserTableNameList = OracleUserObject.getUserTable(sourceConn);
		List<String> userTableNameClobBlob = OracleUserObject.getUserTableNameHavingClobBlob(sourceConn);
		conn1UserTableNameList.removeAll(userTableNameClobBlob);
		List<String> insertScriptList = new ArrayList<String>();
		
		int INSERT_SCRIPT_SIZE = 10000;
		for(String conn2TableName: conn1UserTableNameList){
			insertScriptList.addAll(OracleInsertScriptGenerator.getInsertScript(sourceConn, new Table(conn2TableName)));
//			System.out.println("Insert Script Size: " + insertScriptList.size());
			if(insertScriptList.size() > INSERT_SCRIPT_SIZE){
				OracleDbUtility.executeBatch(destinationConn, 999, insertScriptList);
				insertScriptList = new ArrayList<String>();
			}
		}
		if(insertScriptList.size() > 0)
			OracleDbUtility.executeBatch(destinationConn, 999, insertScriptList);
		
		for(String t: userTableNameClobBlob)
			doClobInsertScriptEach(sourceConn, destinationConn, new Table(t));
		
//		System.out.println(new Date());
	}
	
	public static List<String> doClobInsertScriptEach(Connection sourceConn, Connection destinationConn, Table table) throws Exception {
		List<String> insertStatementList = new ArrayList<String>();
		String tableName = table.getName();
		LogUtility.log("Generating Insert statements for: " + tableName + " <-> " + table.getSelectSqlForInsertion());
		Statement stmt = sourceConn.createStatement();
		ResultSet rs = stmt.executeQuery(table.getSelectSqlForInsertion());
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		int[] columnTypes = new int[numColumns];
		int counter = 0;
		int BATCH_SIZE = 800;
		StringBuilder columnNames = new StringBuilder("");
		for (int i = 0; i < numColumns; i++) {
			columnTypes[i] = rsmd.getColumnType(i + 1);
			if (i != 0) {
				columnNames.append(",");
			}
			columnNames.append(rsmd.getColumnName(i + 1));
		}
		Clob s = null;
//		System.out.println(DbUtility.getPrepareStatement(numColumns, columnNames, table));
		PreparedStatement psmt = destinationConn.prepareStatement(DbUtility.getPrepareStatement(numColumns, columnNames, table));
		while (rs.next()) {
			counter++;
			
			for (int i = 0; i < numColumns; i++) {
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
					psmt.setObject(i + 1, v);
					break;
				case Types.DATE:
					psmt.setDate(i + 1, rs.getDate(i + 1));
					break;
				case Types.TIME:
					psmt.setTime(i + 1, rs.getTime(i + 1));
					break;
				case Types.TIMESTAMP:
				case -101:
					psmt.setTimestamp(i + 1, rs.getTimestamp(i + 1));
					break;
				
				case Types.BLOB:
					Blob blob = rs.getBlob(i + 1);
					byte[] streamData;
					if(blob != null){
						streamData = blob.getBytes(1, (int) blob.length());
						psmt.setBytes(i + 1, streamData);
					} else {
						psmt.setObject(i + 1, null);
					}
//					if(blob != null) blob.free();
					break;
				case Types.CLOB:
					Clob clob = rs.getClob(i + 1);
					
					if(clob != null){
						StringBuffer sb1 = new StringBuffer(); 

						Reader instream = clob.getCharacterStream(); 
		                BufferedReader in = new BufferedReader(instream); 
		                String line = null; 
		                while ((line = in.readLine()) != null) { 
		                    sb1.append(line).append(System.getProperty("line.separator")); 
		                } 

		                if (in != null) in.close(); 
		                if(instream != null) instream.close();

		                s = destinationConn.createClob();
		                // this is the clob data converted into string
		                String clobdata = sb1.toString();
		                s.setString(1, clobdata);
		                psmt.setClob(i + 1, s);
					}
					else 
						psmt.setObject(i + 1, null);
//					if(clob != null) clob.free();
					break;
				default:
					v = rs.getString(i + 1);
					psmt.setObject(i + 1, v);
					break;
				}
				
			}
			
			psmt.executeUpdate();
//			if(s != null) s.
//			psmt.addBatch();
//			if(counter % BATCH_SIZE == 0){
//				psmt.executeBatch();
//				psmt.clearBatch();
//			}
		}
//		System.out.println(counter);
//		if(counter > 0 && counter % BATCH_SIZE != 0){
//			psmt.executeBatch();
//			psmt.clearBatch();
//		}
		
		if(stmt != null) stmt.close();
		if(psmt != null) psmt.close();
		if(rs != null) rs.close();
		
		return insertStatementList;
	}
	
	public static Set<Table> populatePrimaryKeyConstraint(Connection connection, Set<Table> tableBeanSet) throws SQLException{
		Statement st = null;
		ResultSet rs = null;
		Map<String, Table > tableNameToTableBeanMap = new TreeMap<String, Table>();
		
		for(Table table: tableBeanSet)
			tableNameToTableBeanMap.put(table.getName(), table);
		
		for(List<String> l: CollectionUtility.splitCollectionToList(tableNameToTableBeanMap.keySet() , 950)){ 
			String sql = 
					"Select usrcon.table_name , usrcon.constraint_name, usrcol.column_name " +
					"from user_constraints usrcon, user_cons_columns usrcol " +
					"where usrcon.constraint_name = usrcol.constraint_name " +
					"and usrcon.constraint_type = 'P' " +
					"and usrcon.table_name in ('" + CollectionUtility.join(l, "','") + "')";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				String columnName = rs.getString("COLUMN_NAME");
				String constraintName = rs.getString("CONSTRAINT_NAME");
				String tableName = rs.getString("TABLE_NAME");
				tableNameToTableBeanMap.get(tableName).addPrimaryColumn(columnName).primaryKeyConstraintName(constraintName);
			}
			if(rs != null) rs.close();
			if(st != null) st.close();
		}
		return tableBeanSet;
	}
	
	public static void executeBatch(Connection connection, int BATCH_SIZE, List<String> sqlList) throws SQLException{
		Statement st = null;
		int counter = 0;
		try{
			st = connection.createStatement();
			for(String sql: sqlList){
				counter++;
				st.addBatch(sql);
				System.out.println(sql);
				if(counter % BATCH_SIZE == 0){
//					System.out.println(counter + " ");
					st.executeBatch();
					st.clearBatch();
				}
			}
			if(counter > 0 && counter % BATCH_SIZE != 0){
				st.executeBatch();
				st.clearBatch();
			}
		} finally {
			if(st != null) st.close();
		}
	}
	
	public static int setTransform(Connection con, List<OracleTransformParam> transformParameterList, boolean value) throws SQLException{
		CallableStatement cstm1 = null;
		String sql = null;
		try{
			for(OracleTransformParam transformParameter: transformParameterList){
				sql = (value ? transformParameter.trueSql(): transformParameter.falseSql());
				cstm1 = con.prepareCall(sql);
				cstm1.executeUpdate();
				if(cstm1 != null) cstm1.close();
			}
			return 0;
		}
		finally{
			if(cstm1 != null) cstm1.close();
		}
	}
}
