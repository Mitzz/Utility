package db.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utility.CollectionUtility;

public class OracleUserObject{
	public static String getUserName(Connection connection) throws SQLException{
		String username = "";
		String sql = "SELECT USER FROM DUAL";
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		while(rs.next())
			username = rs.getString("USER");
		
		if(rs != null) rs.close();
		if(st != null) st.close();
		return username;
	}
	
	public static List<String> getUserType(Connection connection) throws SQLException{
		return getUserObjectTypeName(connection, OracleObjectType.TYPE);
	}
	
	public static List<String> getUserReferenceConstraint(Connection connection) throws SQLException {
		List<String> constraintNameList = new ArrayList<String>();
		String sql = "SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'R'";
		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		
		while(rs.next())
			constraintNameList.add(rs.getString("CONSTRAINT_NAME"));
		
		if(rs != null) rs.close();
		if(st != null) st.close();
		return constraintNameList;
	}
	
	private static List<String> getUserObjectTypeName(Connection connection, OracleObjectType objectType) throws SQLException{
		List<String> userObjectNameList = new ArrayList<String>();
		String sql = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = ? ";
		PreparedStatement st = connection.prepareStatement(sql);
		st.setObject(1, objectType.typeName());
		ResultSet rs = st.executeQuery();
		
		while(rs.next())
			userObjectNameList.add(rs.getString("OBJECT_NAME"));
		
		Collections.sort(userObjectNameList);
		
		if(rs != null) rs.close();
		if(st != null) st.close();
		return userObjectNameList;
	}
	
	public static List<String> getUserTable(Connection conn) throws SQLException{
		return getUserObjectTypeName(conn, OracleObjectType.TABLE);
	}
	
	public static List<String> getUserSequence(Connection connection) throws SQLException{
		return getUserObjectTypeName(connection, OracleObjectType.SEQUENCE);
	}
	
	public static List<String> getUserFunction(Connection connection) throws SQLException{
		return getUserObjectTypeName(connection, OracleObjectType.FUNCTION);
	}
	
	public static List<String> getUserPackage(Connection connection) throws SQLException{
		return getUserObjectTypeName(connection, OracleObjectType.PACKAGE);
	}
	
	public static List<String> getUserProcedure(Connection connection) throws SQLException{
		return getUserObjectTypeName(connection, OracleObjectType.PROCEDURE);
	}
	
	public static List<String> getUserView(Connection connection) throws SQLException {
		return getUserObjectTypeName(connection, OracleObjectType.VIEW);
	}
	
	public static List<String> getUserCheckConstraint(Connection connection) throws SQLException {
		List<String> constraintNameList = new ArrayList<String>();
		String sql = "SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'C' and GENERATED = 'USER NAME'";
		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		
		while(rs.next())
			constraintNameList.add(rs.getString("CONSTRAINT_NAME"));
		
		if(rs != null) rs.close();
		if(st != null) st.close();
		return constraintNameList;
	}
	
	public static List<String> getUserTableNameHavingClobBlob(Connection connection) throws SQLException{
		List<String> userObjectNameList = new ArrayList<String>();
		String sql = "Select TABLE_NAME from user_tab_columns where data_type in ('BLOB', 'CLOB') GROUP BY TABLE_NAME ";
		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		
		while(rs.next())
			userObjectNameList.add(rs.getString("TABLE_NAME"));
		
		if(rs != null) rs.close();
		if(st != null) st.close();
		return userObjectNameList;
	}
}
