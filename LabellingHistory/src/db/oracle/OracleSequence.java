package db.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.DBConnector;

public class OracleSequence {

	private String name;
	private Connection connection;
	
	public OracleSequence(String name, Connection connection) {
		super();
		this.name = name;
		this.connection = connection;
		
	}
	
	private long getLastNo() throws SQLException{
		long no = getNextValue();
		incrementBy(-1);
		no = getNextValue();
		incrementBy(1);
		return no;
	}
	
	private long getNextValue() throws SQLException{
		long nextValue = 0;
		PreparedStatement psmt = connection.prepareStatement(String.format("Select %s.nextval as VALUE from dual", name));
		ResultSet rs = psmt.executeQuery();
		while(rs.next()){
			nextValue = rs.getLong("VALUE");
		}
		if(rs != null) rs.close();
		if(psmt != null) psmt.close();
		return nextValue;
	}
	
	private void incrementBy(int n) throws SQLException{
		PreparedStatement psmt = connection.prepareStatement(String.format("ALTER SEQUENCE %s INCREMENT BY %d", name, n));
		psmt.executeUpdate();
		if(psmt != null) psmt.close();
	}
	
	public static void main(String[] args) {
		Connection connection = null;
		
		try {
			DBConnector.init();
			connection = DBConnector.getConnection("SIT2");
			OracleSequence sequenceNoGetter = new OracleSequence("AUDIT_ID", connection);
			long currentValue = sequenceNoGetter.getLastNo();
			System.out.println("Current Value: " + currentValue);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if(connection != null) connection.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
