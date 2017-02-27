package db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.DBConnector;
import db.oracle.OracleDbUtility;
import db.oracle.OracleTransformParam;
import db.oracle.OracleUserDDL;

public class DbUtility{

	
	public static String getPrepareStatement(int numColumns, StringBuilder columnNames, Table table) {
		StringBuilder s = new StringBuilder();
		while(numColumns != 0){
			s.append("?,");
			numColumns--;
			
		}
		
		return String.format("INSERT INTO %s (%s) values (%s)", table.getName(), columnNames, new String(s.substring(0, s.length() - 1)));
	}
	
	
	public static void main(String[] args) throws Exception {
		Connection sourceConnection = null;
		Connection destinationConnection = null;
		try{	
			sourceConnection = DBConnector.getConnection("MITHULLOCALUSER");
			destinationConnection = DBConnector.getConnection("LOCAL_COPY_2");
			OracleDbUtility.setTransform(sourceConnection, 
					Arrays.asList(	OracleTransformParam.SEGMENT_ATTRIBUTES,
									OracleTransformParam.CONSTRAINTS,
									OracleTransformParam.CONSTRAINTS_AS_ALTER,
									OracleTransformParam.REF_CONSTRAINTS,
									OracleTransformParam.STORAGE,
									OracleTransformParam.TABLESPACE,
									OracleTransformParam.SQLTERMINATOR), false);

			List<String> ddlList = new ArrayList<String>();
			ddlList.addAll(OracleUserDDL.getUserTypeDDL(sourceConnection));
			ddlList.addAll(OracleUserDDL.getUserSequenceDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserTableDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserViewDDL(sourceConnection));
//
			OracleDbUtility.executeDdl(destinationConnection, ddlList);
//
//			OracleDbUtility.transferTableData(sourceConnection, destinationConnection);
//			
//			ddlList = new ArrayList<String>();
//			ddlList.addAll(OracleUserDDL.getUserTablePrimaryKeyConstraintDdl(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserTableUniqueKeyConstraintDdl(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserCheckConstraintDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getNotNullConstraintDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getForeignKeyConstraintDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserFunctionDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserPackageDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserProcedureDDL(sourceConnection));
//
//			OracleDbUtility.executeDdl(destinationConnection, ddlList);
			
//			CollectionUtility.displayList(OracleInsertScriptGenerator.getInsertScript(sourceConnection, "TB_LANGUAGE_MASTER"), ";");
		}
		finally{
			if(sourceConnection != null) sourceConnection.close();
			if(destinationConnection != null) destinationConnection.close();
		}
	}
	
}


