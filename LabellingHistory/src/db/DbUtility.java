package db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

import model.DBConnector;
import model.FileBean;
import utility.CollectionUtility;
import utility.StringUtility;
import db.oracle.OracleDbUtility;
import db.oracle.OracleTransformParam;
import db.oracle.OracleUserDDL;
import file.model.FileBeanRetriver;

public class DbUtility{

	public static void closeResources(AutoCloseable ... resources) throws Exception{
		for(AutoCloseable resource: resources){
			if (resource != null) resource.close();
		}
	}
	
	public static String getPrepareStatement(int numColumns, StringBuilder columnNames, Table table) {
		StringBuilder sb = new StringBuilder();
		while(numColumns != 0){
			sb.append("?,");
			numColumns--;
		}
		return String.format("INSERT INTO %s (%s) values (%s)", table.getName(), columnNames, StringUtility.removeLastCharacter(sb));
	}
	
	
	public static void min(String[] args) throws Exception {
		Connection sourceConnection = null;
		Connection destinationConnection = null;
		try{	
			DBConnector.init();
			sourceConnection = DBConnector.getConnection(DBConnector.LOCAL_COPY_2);
			destinationConnection = DBConnector.getConnection(DBConnector.LOCAL_COPY);
			OracleDbUtility.setTransform(sourceConnection, 
					Arrays.asList(	OracleTransformParam.SEGMENT_ATTRIBUTES,
									OracleTransformParam.CONSTRAINTS,
									OracleTransformParam.CONSTRAINTS_AS_ALTER,
									OracleTransformParam.REF_CONSTRAINTS,
									OracleTransformParam.STORAGE,
									OracleTransformParam.TABLESPACE,
									OracleTransformParam.SQLTERMINATOR), false);

//			CollectionUtility.displayCollection(OracleUserDDL.getUserSequenceDDL(sourceConnection));
//			List<String> ddlList = new ArrayList<String>();
//			ddlList.addAll(OracleUserDDL.getUserTypeDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserSequenceDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserTableDDL(sourceConnection));
//			ddlList.addAll(OracleUserDDL.getUserViewDDL(sourceConnection));
//
//			OracleDbUtility.executeDdl(destinationConnection, ddlList);

			OracleDbUtility.transferTableData(sourceConnection, destinationConnection);
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
			DbUtility.closeResources(sourceConnection, destinationConnection);
		}
	}
	
	public static void main2(String[] args) throws Exception {
		Connection sourceConnection = null;
		try{	
			DBConnector.init();
			sourceConnection = DBConnector.getConnection(DBConnector.SIT2);
			OracleDbUtility.setTransform(sourceConnection, 
					Arrays.asList(	OracleTransformParam.SEGMENT_ATTRIBUTES,
									OracleTransformParam.CONSTRAINTS,
									OracleTransformParam.CONSTRAINTS_AS_ALTER,
									OracleTransformParam.REF_CONSTRAINTS,
									OracleTransformParam.STORAGE,
									OracleTransformParam.TABLESPACE,
									OracleTransformParam.SQLTERMINATOR), false);

//			CollectionUtility.displayCollection(OracleUserDDL.getUserSequenceDDL(sourceConnection));
			List<String> ddlList = new ArrayList<String>();
			ddlList = OracleUserDDL.getUserTypeDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\type.ser");
			
			ddlList = OracleUserDDL.getUserSequenceDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\sequence.ser");
			
			ddlList = OracleUserDDL.getUserTableDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\table.ser");
			
			ddlList = OracleUserDDL.getUserViewDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\view.ser");
			
			ddlList = OracleUserDDL.getUserTablePrimaryKeyConstraintDdl(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\pkconstraint.ser");
			
			ddlList = OracleUserDDL.getUserTableUniqueKeyConstraintDdl(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\uniqueconstraint.ser");
			
			ddlList = OracleUserDDL.getUserCheckConstraintDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\checkconstraint.ser");
			
			ddlList = OracleUserDDL.getNotNullConstraintDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\notnullconstraint.ser");
			
			ddlList = OracleUserDDL.getForeignKeyConstraintDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\fkconstraint.ser");
			
			ddlList = OracleUserDDL.getUserFunctionDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\function.ser");
			
			ddlList = OracleUserDDL.getUserPackageDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\package.ser");
			
			ddlList = OracleUserDDL.getUserProcedureDDL(sourceConnection);
			CollectionUtility.serialize((ArrayList<String>) ddlList, "D:\\SIT2\\procedure.ser");
			
//			OracleDbUtility.executeDdl(destinationConnection, ddlList);
			
//			CollectionUtility.displayList(OracleInsertScriptGenerator.getInsertScript(sourceConnection, "TB_LANGUAGE_MASTER"), ";");
		}
		finally{
			DbUtility.closeResources(sourceConnection);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		Connection sourceConnection = null;
		try{	
			DBConnector.init();
			sourceConnection = DBConnector.getConnection(DBConnector.LOCAL_COPY);
			OracleDbUtility.setTransform(sourceConnection, 
					Arrays.asList(	OracleTransformParam.SEGMENT_ATTRIBUTES,
									OracleTransformParam.CONSTRAINTS,
									OracleTransformParam.CONSTRAINTS_AS_ALTER,
									OracleTransformParam.REF_CONSTRAINTS,
									OracleTransformParam.STORAGE,
									OracleTransformParam.TABLESPACE,
									OracleTransformParam.SQLTERMINATOR), false);
			
			FileBeanRetriver beanRetriver = new FileBeanRetriver("D:/SIT2/post");
			SortedSet<FileBean> fileBeanSet = beanRetriver.get();
			List<String> ddlList = new ArrayList<String>();
			ArrayList<FileBean> fileBeanList = new ArrayList<FileBean>(fileBeanSet);
			Collections.sort(fileBeanList, FileBean.SORT_BY_LAST_MODIFIED_ASC);
			for(FileBean bean: fileBeanList) {
				ddlList.addAll(CollectionUtility.<String>deserialize(bean.absolutePath()));
			}
			OracleDbUtility.executeDdl(sourceConnection, ddlList);
			
		}
		finally{
			DbUtility.closeResources(sourceConnection);
		}
	}
}


