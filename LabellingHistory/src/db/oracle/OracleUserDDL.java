package db.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import utility.CollectionUtility;
import db.Table;
import db.TableDao;

public class OracleUserDDL{
	public static List<String> getUserTypeDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.TYPE, OracleUserObject.getUserType(connection));
	}
	
	public static List<String> getForeignKeyConstraintDDL(Connection connection) throws SQLException {
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.REF_CONSTRAINT, OracleUserObject.getUserReferenceConstraint(connection));
	}
	
	public static String getUserTableDDL(Connection connection, String tableName) throws SQLException{
		return getUserObjectTypeDDL(connection, tableName, OracleObjectType.TABLE);
	}

	public static List<String> getUserTableDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.TABLE, OracleUserObject.getUserTable(connection));
	}
	
	public static List<String> getUserSequenceDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.SEQUENCE, OracleUserObject.getUserSequence(connection));
	}
	
	public static List<String> getUserFunctionDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.FUNCTION, OracleUserObject.getUserFunction(connection));
	}
	
	public static List<String> getUserPackageDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.PACKAGE, OracleUserObject.getUserPackage(connection));
	}
	
	public static List<String> getUserProcedureDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.PROCEDURE, OracleUserObject.getUserProcedure(connection));
	}
	
	public static List<String> getUserViewDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.VIEW, OracleUserObject.getUserView(connection));
	}
	
	public static List<String> getUserCheckConstraintDDL(Connection connection) throws SQLException{
		return getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), OracleObjectType.CONSTRAINT, OracleUserObject.getUserCheckConstraint(connection));
	}
	
	private static List<String> getUserObjectTypeDDL(Connection connection, String username, OracleObjectType objectType, List<String> objectNameList) throws SQLException{
		List<String> userObjectDllList = new ArrayList<String>();
		String sql = "SELECT DBMS_METADATA.GET_DDL(?, ?, ?) as DDL FROM DUAL";
		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = null;
		String replacee = "\"" + username + "\".";
		
		for(String objectName : objectNameList){
			st.setObject(1, objectType.typeName());
			st.setObject(2, objectName);
			st.setObject(3, username);
			rs = st.executeQuery();
			
			while(rs.next())
				userObjectDllList.add(rs.getString("DDL").replace(replacee, ""));
			
			if(rs != null) rs.close();
			if(st != null) st.clearParameters();
		}
		return userObjectDllList;
	}
	
	private static String getUserObjectTypeDDL(Connection connection, String objectName, OracleObjectType objectType) throws SQLException {
		List<String> ddList = getUserObjectTypeDDL(connection, OracleUserObject.getUserName(connection), objectType, Arrays.asList(objectName));
		for (String ddl : ddList) 
			return ddl;
		return null;
	}
	
	public static List<String> getNotNullConstraintDDL(Connection connection) throws SQLException {
		List<String> ddlList = new ArrayList<String>();
		String sql = "SELECT USRCOL.TABLE_NAME, USRCOL.COLUMN_NAME FROM USER_TAB_COLUMNS USRCOL, USER_TABLES USRTABLE WHERE USRTABLE.TABLE_NAME = USRCOL.TABLE_NAME AND USRCOL.NULLABLE = 'N'";
		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		
		while(rs.next()){
			String tableName = rs.getString("TABLE_NAME");
			String columnName = rs.getString("COLUMN_NAME");
			ddlList.add(String.format("ALTER TABLE %s MODIFY (%s NOT NULL ENABLE)", tableName, columnName));
		}
		if(rs != null) rs.close();
		if(st != null) st.close();
		return ddlList;
	}
	
	public static List<String> getUserTablePrimaryKeyConstraintDdl(Connection connection) throws SQLException{
		return getUserTablePrimaryKeyConstraintDdl(connection, TableDao.getUserTableBeanSet(connection));
	}
	
	private static List<String> getUserTablePrimaryKeyConstraintDdl(Connection connection, Set<Table> tableBeanSet) throws SQLException{
		List<String> primaryKeyConstraintDdlList = new ArrayList<String>();
		
		OracleDbUtility.populatePrimaryKeyConstraint(connection, tableBeanSet);
		for(Table table: tableBeanSet){
			if(table.primaryKeyConstraintName() != null)
				primaryKeyConstraintDdlList.add(table.primaryKeyConstraintDdl());
		}
		
		return primaryKeyConstraintDdlList;
	}
	
	public static List<String> getUserTableUniqueKeyConstraintDdl(Connection connection) throws SQLException{
		return getUserTableUniqueKeyConstraintDdl(connection, TableDao.getUserTableBeanSet(connection));
	}
	
	private static List<String> getUserTableUniqueKeyConstraintDdl(Connection connection, Set<Table> tableBeanSet) throws SQLException{
		List<String> uniqueKeyConstraintDdlList = new ArrayList<String>();
		Statement st = null;
		ResultSet rs = null;
		String sql = null;
		Map<String, Table > tableNameToTableBeanMap = new TreeMap<String, Table>();
		
		for(Table table: tableBeanSet)
			tableNameToTableBeanMap.put(table.getName(), table);
		
		for(List<String> l: CollectionUtility.splitCollectionToList(tableNameToTableBeanMap.keySet() , 950)){ 
			st = connection.createStatement();
			
			sql = 
					String.format(
							"SELECT A.table_name, A.constraint_name, listagg(A.column_name, ',') within group (order by A.column_name) as column_names " +
							"FROM user_cons_columns A, user_constraints b " +
							"WHERE A.owner        =b.owner " +
							"AND A.constraint_name=b.constraint_name " +
							"AND A.table_name     =b.table_name " +
							"and b.constraint_type = 'U' " +
							"and a.table_name in ('%s') " +
							"group by A.table_name, A.constraint_name ", CollectionUtility.join(l, "','")) ;
			
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				
				String tableName = rs.getString("TABLE_NAME");
				String constraintName = rs.getString("CONSTRAINT_NAME");
				String columnNames = rs.getString("COLUMN_NAMES");
				
				uniqueKeyConstraintDdlList.add(String.format("ALTER TABLE %s ADD CONSTRAINT %s UNIQUE (%s)", tableName, constraintName.startsWith("SYS") ? constraintName : constraintName, columnNames));
			}
			
			if(rs != null) rs.close();
			if(st != null) st.close();
		}
		
		if(rs != null) rs.close();
		if(st != null) st.close();
		
		return uniqueKeyConstraintDdlList;
	}
}
