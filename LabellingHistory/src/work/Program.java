package work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.ChildTable;
import model.ColumnModel;
import model.Entity;
import model.EntityRevisited;
import model.GrandChildTable;
import model.ParentTable;
import model.Table;
import model.TbEntColMaster;
import model.TbEntMasterModel;

public class Program {

	public static void main(String[] args) {
//		Connection connection = new Connection();
//		Entity entity = getEnquiryEntity("Enquiry_Entity");
//		
//		insertIntoTbEntMaster(connection, entity);
//		insertIntoTbColumnMaster(connection, entity);
//		insertIntoTbEntityPrimaryMaster(connection, entity);
//		insertIntoTbEntDetailMaster(connection, entity);
	}
	
	private static void insertIntoTbEntDetailMaster(Connection connection, Entity entity) {
		PreparedStatement pStmt2 = null;
		String mainQuery = "";
		try {
			String parentTableName = entity.getParentTableName();
			for(String childTableName: entity.getSecondaryTableNames()){
				mainQuery = 
		    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, CHILD_TABLE_NAME) VALUES ('%s','%s','%s')", 
		    		entity.getEntityName(), parentTableName, childTableName);
				    
			    System.out.println(mainQuery + ";");
			    
			    pStmt2 = connection.prepareStatement(mainQuery);
			    pStmt2.execute();
			    
			    if (pStmt2 != null) {
					pStmt2.close();
					pStmt2 = null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insertIntoTbEntDetailMaster(Connection connection, EntityRevisited entity) {
		
		PreparedStatement pStmt2 = null;
		String mainQuery = "";
		try {
			
			
			String entityName = entity.getEntityName();
			ParentTable parent = entity.getParentTable();
			String parentPrimaryColumnName = parent.getPrimaryColumnNames().get(0);
			List<ChildTable> childTables = parent.getChildTables();
			String parentTableName = parent.getName();
			int groupNumber = 0;
			for(ChildTable child: childTables){
				String childTableName = child.getName();
				groupNumber++;
				for(String foreignKeyName: child.getForeignColumnNames()){ 	
					mainQuery = 
			    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, TABLE_COLUMN_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, GROUP_NUMBER) "
			    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
			    		entityName, parentTableName, parentPrimaryColumnName, childTableName, foreignKeyName, groupNumber);
					    
				    System.out.println(mainQuery + ";");
				    
				    pStmt2 = connection.prepareStatement(mainQuery);
				    pStmt2.execute();
				    
				    if (pStmt2 != null) {
						pStmt2.close();
						pStmt2 = null;
					}
				}
			}
			for(ChildTable child: childTables){
				for(GrandChildTable grandChild: child.getGrandChildTables()){
					groupNumber++;
					int count = -1;
					for(String foreignKeyName: grandChild.getForeignColumnNames()){
						count++;
						mainQuery = 
				    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, TABLE_COLUMN_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, GROUP_NUMBER) "
				    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
				    		entityName, child.getName().toUpperCase(), child.getPrimaryColumnNames().get(count), grandChild.getName(), foreignKeyName, groupNumber);
						    
					    System.out.println(mainQuery + ";");
					    
					    pStmt2 = connection.prepareStatement(mainQuery);
					    pStmt2.execute();
					    
					    if (pStmt2 != null) {
							pStmt2.close();
							pStmt2 = null;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ChildTable getParent(String grandChildParentName, List<ChildTable> childTable) {
		
		for(ChildTable c: childTable){
			if(grandChildParentName.equalsIgnoreCase(c.getName()))
				return c;
		}

		throw new IllegalArgumentException("Not Parent Found");
	}

	private static void insertIntoTbEntityPrimaryMaster(Connection connection, Entity entity) {
		
		PreparedStatement pStmt2 = null;
		String mainQuery = "";
		try {
			
			TbEntColMaster model = entity.getEntityColumnMaster();
			
			List<String> tableNames = model.getTableNames();

			for(String tableName: tableNames){
				mainQuery = 
		    		String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
		    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
		    		entity.getEntityName(), tableName);
				    
			    System.out.println(mainQuery + ";");
			    
			    pStmt2 = connection.prepareStatement(mainQuery);
			    pStmt2.execute();
			    
			    if (pStmt2 != null) {
					pStmt2.close();
					pStmt2 = null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getTableNames(EntityRevisited entity){
		List<String> tableNames = new ArrayList<String>();
		
		ParentTable parent = entity.getParentTable();
		tableNames.add(parent.getName());
		for(ChildTable child: parent.getChildTables()){
			tableNames.add(child.getName());
			for(GrandChildTable grandChild: child.getGrandChildTables()){
				tableNames.add(grandChild.getName());
			}
		}
		
		return tableNames;
	}
	
	public static List<String> getHistoryTableNames(EntityRevisited entity){
		List<String> tableNames = new ArrayList<String>();
		
		tableNames.add(entity.getParentTable().getHistoryName());
		for(ChildTable child: entity.getParentTable().getChildTables()){
			tableNames.add(child.getHistoryName());
			for(GrandChildTable grandChild: child.getGrandChildTables()){
				tableNames.add(grandChild.getHistoryName());
			}
		}
		
		return tableNames;
	}
	
	public static List<String> getHistoryTableNamesRevisited(EntityRevisited entity){
		return getHistoryTableNamesRevisited(entity.getParent());
	}
	
	private static List<String> getHistoryTableNamesRevisited(Table table){
		List<String> tableNames = new ArrayList<String>();
		
		for(Table child: table.getChilds()){
			tableNames.add(child.getHistoryName());
			tableNames.addAll(getHistoryTableNamesRevisited(child));
		}
		
		return tableNames;
	}
	public static void insertIntoTbEntityPrimaryMaster(Connection connection, EntityRevisited entity) {
		
		PreparedStatement pStmt2 = null;
		String mainQuery = "";
		try {
			
			String entityName = entity.getEntityName();
			ParentTable parent = entity.getParentTable();
			List<ChildTable> childs = parent.getChildTables();
			String parentTableName = parent.getName();
			List<String> parentPrimaryColumnNames = parent.getPrimaryColumnNames();
			for(String column: parentPrimaryColumnNames){
				ColumnModel col = parent.primaryColumnDetails.get(column);
				mainQuery = 
						String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
			    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
			    		entityName, parentTableName, column, col.getSequence(), col.getDatatype());
				    
			    System.out.println(mainQuery + ";");
			    
			    pStmt2 = connection.prepareStatement(mainQuery);
			    pStmt2.execute();
			    
			    if (pStmt2 != null) {
					pStmt2.close();
					pStmt2 = null;
				}
			}
			
			for(ChildTable child: childs){
				String childTableName = child.getName();
				List<String> childPrimaryColumnNames = child.getPrimaryColumnNames();
				for(String column: childPrimaryColumnNames){
					ColumnModel col = child.primaryColumnDetails.get(column);
					mainQuery = 
							String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
				    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
				    		entityName, childTableName, column, col.getSequence(), col.getDatatype());
					    
				    System.out.println(mainQuery + ";");
				    
				    pStmt2 = connection.prepareStatement(mainQuery);
				    pStmt2.execute();
				    
				    if (pStmt2 != null) {
						pStmt2.close();
						pStmt2 = null;
					}
				}
			}
			for(ChildTable child: childs){
				for(GrandChildTable grandChild: child.getGrandChildTables()){
					String grandChildTableName = grandChild.getName();
					List<String> grandChildPrimaryColumnNames = grandChild.getPrimaryColumnNames();
					for(String column: grandChildPrimaryColumnNames){
						ColumnModel col = grandChild.primaryColumnDetails.get(column);
						mainQuery = 
								String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
					    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
					    		entityName, grandChildTableName, column, col.getSequence(), col.getDatatype());
						    
					    System.out.println(mainQuery + ";");
					    
					    pStmt2 = connection.prepareStatement(mainQuery);
					    pStmt2.execute();
					    
					    if (pStmt2 != null) {
							pStmt2.close();
							pStmt2 = null;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void insertIntoTbColumnMaster(Connection connection, Entity entity) {
		
		PreparedStatement pStmt = null;
		ResultSet metaQueryResultSet = null;
		
		PreparedStatement pStmt2 = null;
		String sqlQuery = "";
		String mainQuery = "";
		try {
			
			TbEntColMaster model = entity.getEntityColumnMaster();
			
			List<String> tableNames = model.getTableNames();

			for(String tableName: tableNames){
				sqlQuery = 
						"select TABLE_NAME, column_name,column_name AS COLUMN_CAPTION,COLUMN_ID AS COLUMN_SEQUENCE, " +
				        "'N' AS IS_LISTING_COLUMN,DATA_TYPE,'N' AS IS_TREE_LABEL_COLUMN,'Y' AS IS_HASHING_COLUMN " +
				        "from all_tab_columns " +
				        "where table_name = '" + tableName + "'";
		
				pStmt = connection.prepareStatement(sqlQuery);
				metaQueryResultSet = pStmt.executeQuery();
				
				while(metaQueryResultSet.next()){
					String TABLE_NAME = metaQueryResultSet.getString(1);
					String COLUMN_NAME = metaQueryResultSet.getString(2);
					String COLUMN_CAPTION = metaQueryResultSet.getString(3);
					String COLUMN_SEQUENCE = metaQueryResultSet.getString(4);
					String IS_LISTING_COLUMN = metaQueryResultSet.getString(5); 
				    String DATA_TYPE = metaQueryResultSet.getString(6);
				    String IS_TREE_LABEL_COLUMN = metaQueryResultSet.getString(7);
				    String IS_HASHING_COLUMN = metaQueryResultSet.getString(8);
				    
				    mainQuery = 
				    		String.format("INSERT INTO TB_ENT_COL_MASTER (TABLE_NAME, COLUMN_NAME, COLUMN_CAPTION, COLUMN_SEQUENCE, IS_LISTING_COLUMN, DATA_TYPE, "
				    		+ "IS_TREE_LABEL_COLUMN, IS_HASHING_COLUMN) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')", 
				    		TABLE_NAME, COLUMN_NAME, COLUMN_CAPTION, COLUMN_SEQUENCE, IS_LISTING_COLUMN, DATA_TYPE, IS_TREE_LABEL_COLUMN,getHashingColumnValue(COLUMN_NAME));
				    
				    System.out.println(mainQuery + ";");
				    
				    pStmt2 = connection.prepareStatement(mainQuery);
				    pStmt2.execute();
				    
				    if (pStmt2 != null) {
						pStmt2.close();
						pStmt2 = null;
					}
				}
				
				System.out.println();
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
				if(metaQueryResultSet != null){
					metaQueryResultSet.close();
					metaQueryResultSet = null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insertIntoTbColumnMaster(Connection connection, EntityRevisited entity) {
		
		PreparedStatement pStmt = null;
		ResultSet metaQueryResultSet = null;
		
		PreparedStatement pStmt2 = null;
		String sqlQuery = "";
		String mainQuery = "";
		try {
			
			
			List<String> tableNames = getTableNames(entity);

			for(String tableName: tableNames){
				sqlQuery = 
						"select TABLE_NAME, column_name,column_name AS COLUMN_CAPTION,COLUMN_ID AS COLUMN_SEQUENCE, " +
				        "'N' AS IS_LISTING_COLUMN,DATA_TYPE,'N' AS IS_TREE_LABEL_COLUMN,'Y' AS IS_HASHING_COLUMN " +
				        "from all_tab_columns " +
				        "where table_name = '" + tableName + "'";
		
				pStmt = connection.prepareStatement(sqlQuery);
				metaQueryResultSet = pStmt.executeQuery();
				
				while(metaQueryResultSet.next()){
					String TABLE_NAME = metaQueryResultSet.getString(1);
					String COLUMN_NAME = metaQueryResultSet.getString(2);
					String COLUMN_CAPTION = metaQueryResultSet.getString(3);
					String COLUMN_SEQUENCE = metaQueryResultSet.getString(4);
					String IS_LISTING_COLUMN = metaQueryResultSet.getString(5); 
				    String DATA_TYPE = metaQueryResultSet.getString(6);
				    String IS_TREE_LABEL_COLUMN = metaQueryResultSet.getString(7);
				    
				    mainQuery = 
				    		String.format("INSERT INTO TB_ENT_COL_MASTER (TABLE_NAME, COLUMN_NAME, COLUMN_CAPTION, COLUMN_SEQUENCE, IS_LISTING_COLUMN, DATA_TYPE, "
				    		+ "IS_TREE_LABEL_COLUMN, IS_HASHING_COLUMN) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')", 
				    		TABLE_NAME, COLUMN_NAME, COLUMN_CAPTION, COLUMN_SEQUENCE, IS_LISTING_COLUMN, DATA_TYPE, IS_TREE_LABEL_COLUMN,getHashingColumnValue(entity, tableName, COLUMN_NAME));
				    
				    System.out.println(mainQuery + ";");
				    
				    pStmt2 = connection.prepareStatement(mainQuery);
				    pStmt2.execute();
				    
				    if (pStmt2 != null) {
						pStmt2.close();
						pStmt2 = null;
					}
				}
				
				System.out.println();
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
				if(metaQueryResultSet != null){
					metaQueryResultSet.close();
					metaQueryResultSet = null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getHashingColumnValue(EntityRevisited entity, String tableName, String columnName) {
		ParentTable parent = entity.getParentTable();
		if(parent.getName().equalsIgnoreCase(tableName)){
			for(String nonHashColumnName : parent.getNonHashingColumnNames()){
				if(nonHashColumnName.equalsIgnoreCase(columnName)){
					return "N";
				}
			}
		}
		
		List<ChildTable> childTable = entity.getParentTable().getChildTables();
		for(ChildTable child: childTable){
			if(child.getName().equalsIgnoreCase(tableName)){
				for(String nonHashColumnName : child.getNonHashingColumnNames()){
					if(nonHashColumnName.equalsIgnoreCase(columnName)){
						return "N";
					}
				}
			}
		}
		return "Y";
	}
	
	private static Object getHashingColumnValueRevisited(EntityRevisited entity, String tableName, String columnName) {
		Table parent = entity.getParent();
		if(parent.getName().equalsIgnoreCase(tableName)){
			for(String nonHashColumnName : parent.getNonHashingColumnNames()){
				if(nonHashColumnName.equalsIgnoreCase(columnName)){
					return "N";
				}
			}
		}
		
		List<Table> childTable = parent.getChilds();
		for(Table child: childTable){
			if(child.getName().equalsIgnoreCase(tableName)){
				for(String nonHashColumnName : child.getNonHashingColumnNames()){
					if(nonHashColumnName.equalsIgnoreCase(columnName)){
						return "N";
					}
				}
			}
		}
		return "Y";
	}

	private static String getHashingColumnValue(String columnName) {
		if(columnName.equals("MAKERID")) return "N";
		if(columnName.equals("MAKERDATE")) return "N";
		if(columnName.equals("CHECKERID")) return "N";
		if(columnName.equals("CHECKERDATE")) return "N";
		return "Y";
	}
	
	

	private static void insertIntoTbEntMaster(Connection connection, Entity entity) {

		
		PreparedStatement pStmt = null;
		String sqlQuery = "";
		try {
			
			for (TbEntMasterModel model : entity.getEntMasters()) {

				sqlQuery = "INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, LABEL_TABLE_NAME, TABLE_CAPTION, AUDIT_COLUMN_NAME) " + "VALUES ('" + entity.getEntityName() + "' , '" + model.getTableName() + "', '" + (model.isPrimary() ? "Y" : "N") + "', '" + model.getHistoryTableName() + "', '" + model.getLabelTableName() + "', '" + model.getTableCaption() + "', '" + model.getAuditColumnName() + "')";

				System.out.println(sqlQuery);
				pStmt = connection.prepareStatement(sqlQuery);
				pStmt.execute();
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insertIntoTbEntMaster(Connection connection, EntityRevisited entity) {

		
		PreparedStatement pStmt = null;
		String sqlQuery = "";
		ParentTable parent = entity.getParentTable();
		try {
			
			
			sqlQuery = 
					String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
							+ "AUDIT_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s')", 
							entity.getEntityName(), parent.getName(), "Y", parent.getHistoryName(), 
							parent.getName(), parent.getAuditColumnName()); 

			System.out.println(sqlQuery + ";");
			pStmt = connection.prepareStatement(sqlQuery);
			pStmt.execute();
			if (pStmt != null) {
				pStmt.close();
				pStmt = null;
			}
			
			for (ChildTable child : parent.getChildTables()) {
				sqlQuery = 
						String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
								+ "AUDIT_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s')", 
								entity.getEntityName(), child.getName(), "N", child.getHistoryName(), 
								child.getName(),child.getAuditColumnName()); 

				System.out.println(sqlQuery + ";");
				
				pStmt = connection.prepareStatement(sqlQuery);
				pStmt.execute();
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
				
			}
			for (ChildTable child : parent.getChildTables()) {	
				for (GrandChildTable grandChild : child.getGrandChildTables()) {
					sqlQuery = 
							String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
									+ "AUDIT_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s')", 
									entity.getEntityName(), grandChild.getName(), "N", grandChild.getHistoryName(), 
									grandChild.getName(),grandChild.getAuditColumnName()); 
	
					System.out.println(sqlQuery + ";");
					
					pStmt = connection.prepareStatement(sqlQuery);
					pStmt.execute();
					if (pStmt != null) {
						pStmt.close();
						pStmt = null;
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Entity getEnquiryEntity(String entityName) {
		Entity entity = new Entity();
		entity.setEntityName(entityName);

		TbEntMasterModel entMaster = null;

		entMaster = new TbEntMasterModel().tableName("TB_ENQ_MASTER").historyTableName("TB_ENQ_HISTORY").primary(true);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");

		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_LINKED_FIELD_MASTER").historyTableName("TB_LINKED_FIELD_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDIT_ID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_LINKED_UIC_MASTER");
		entMaster.historyTableName("TB_LINKED_UIC_HISTORY");
		entMaster.primary(false);

		entMaster.labelTableName(entMaster.getTableName() + "_LABEL");
		entMaster.tableCaption(entMaster.getTableName());
		entMaster.auditColumnName("AUDIT_ID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_FIELD_DETAIL_MASTER").historyTableName("TB_ENQ_FIELD_DETAIL_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_REF_PROD_OPERAT_MASTER").historyTableName("TB_ENQ_REF_PROD_OPERAT_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_REF_PRODUCT_MASTER").historyTableName("TB_ENQ_REF_PRODUCT_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_REF_ADDLTABLES_MASTER").historyTableName("TB_ENQ_REF_ADDLTABLES_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_FILTER_MASTER").historyTableName("TB_ENQ_FILTER_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_LINKAGES_MASTER").historyTableName("TB_ENQ_LINKAGES_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_FIELD_LINKAGES_MASTER").historyTableName("TB_ENQ_FIELD_LINKAGES_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		entMaster = new TbEntMasterModel();
		entMaster.tableName("TB_ENQ_GROUP_RANGE_MASTER").historyTableName("TB_ENQ_GROUP_RANGE_HISTORY").primary(false);
		entMaster.labelTableName(entMaster.getTableName() + "_LABEL").tableCaption(entMaster.getTableName()).auditColumnName("AUDITID");
		entity.addEntMaster(entMaster);

		TbEntColMaster columnMaster = new TbEntColMaster();
		columnMaster.addTableName("TB_ENQ_MASTER");
		columnMaster.addTableName("TB_LINKED_FIELD_MASTER");
		columnMaster.addTableName("TB_LINKED_UIC_MASTER");
		columnMaster.addTableName("TB_ENQ_FIELD_DETAIL_MASTER");
		columnMaster.addTableName("TB_ENQ_REF_PROD_OPERAT_MASTER");
		columnMaster.addTableName("TB_ENQ_REF_PRODUCT_MASTER");
		columnMaster.addTableName("TB_ENQ_REF_ADDLTABLES_MASTER");
		columnMaster.addTableName("TB_ENQ_TABLE_LINKS_MASTER");
		columnMaster.addTableName("TB_ENQ_FILTER_MASTER");
		columnMaster.addTableName("TB_ENQ_LINKAGES_MASTER");
		columnMaster.addTableName("TB_ENQ_FIELD_LINKAGES_MASTER");
		columnMaster.addTableName("TB_ENQ_GROUP_RANGE_MASTER");
		
		entity.setEntityColumnMaster(columnMaster);
		return entity;
	}

	public static Map<String, ColumnModel> getColumnDetails(Connection connection, String tableName, List<String> primaryColumnNames) {
		
		PreparedStatement pStmt = null;
		ResultSet metaQueryResultSet = null;
		Map<String, ColumnModel> columnDetails = new HashMap<String, ColumnModel>();
		String sqlQuery = "";
		try {
			
			
			sqlQuery = 
					"select column_name, data_type, column_id " +
			        "from all_tab_columns " +
			        "where table_name = '" + tableName + "' and column_name IN " + Program.getDBQuery(primaryColumnNames);
	
			pStmt = connection.prepareStatement(sqlQuery);
			metaQueryResultSet = pStmt.executeQuery();
			
			while(metaQueryResultSet.next()){
				ColumnModel columnModel = new ColumnModel();
				String COLUMN_NAME = metaQueryResultSet.getString(1);
				int COLUMN_SEQUENCE = metaQueryResultSet.getInt(3);
			    String DATA_TYPE = metaQueryResultSet.getString(2);
			    
			    columnModel.setDatatype(DATA_TYPE);
			    columnModel.setName(COLUMN_NAME);
			    columnModel.setSequence(COLUMN_SEQUENCE);
			    
			    columnDetails.put(COLUMN_NAME, columnModel);
			}
			
			if (pStmt != null) {
				pStmt.close();
				pStmt = null;
			}
			if(metaQueryResultSet != null){
				metaQueryResultSet.close();
				metaQueryResultSet = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columnDetails;
	}

	/*public static void insertActionColumnIfNotPresent(Connection connection, EntityRevisited entity) {
		List<ColumnModel> columns = new ArrayList<ColumnModel>();
		
		ColumnModel columnModel = new ColumnModel();
		columnModel.setDatatype("VARCHAR2");
		columnModel.setName("ACTION");
		columnModel.setDataLength(20);
		columns.add(columnModel);
		
		List<String> tableNames = Program.getHistoryTableNames(entity);
		List<String> historyTableNames = getTableNamesNotHavingParticularColumn(connection, entity, tableNames, columnModel);
		addActionColumnToTables(connection, historyTableNames, columnModel);
	}*/
	
	public static void insertActionColumnIfNotPresent(Connection connection, EntityRevisited entity) {
		List<ColumnModel> columns = new ArrayList<ColumnModel>();
		
		ColumnModel columnModel = new ColumnModel();
		columnModel.setDatatype("VARCHAR2");
		columnModel.setName("ACTION");
		columnModel.setDataLength(20);
		columns.add(columnModel);
		
		List<String> parentHistoryTablesNames = Arrays.asList(entity.getParentTable().getName());
		List<String> historyTableNames = getTableNamesNotHavingParticularColumn(connection, entity, parentHistoryTablesNames, columnModel);
		addActionColumnToTables(connection, historyTableNames, columnModel);
	}

	private static void addActionColumnToTables(Connection connection, List<String> tableNames, ColumnModel columnModel) {
		
		PreparedStatement pStmt = null;
		String sqlQuery = "";
		try {
			
			for(String tableName: tableNames){
				
				sqlQuery = String.format("ALTER TABLE %s ADD %s VARCHAR2(20)", tableName.toUpperCase(), columnModel.getName().toUpperCase());
		
				System.out.println(sqlQuery + ";");
//				pStmt = connection.prepareStatement(sqlQuery);
//				pStmt.execute();
				
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<String> getTableNamesNotHavingParticularColumn(Connection connection, EntityRevisited entity, List<String> tableNames, ColumnModel column) {
		List<String> tablesHavingColumn = new ArrayList<String>();
		List<String> tablesNotHavingColumn = new ArrayList<String>();
		
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		String sqlQuery = "";
		try {
			
			for(String tableName: tableNames){
				
				sqlQuery = 
						"Select table_name " +
				        "from all_tab_columns " +
				        "where table_name = '" + tableName.toUpperCase() + "' and column_name = '" + column.getName().toUpperCase() + "'";
		
				pStmt = connection.prepareStatement(sqlQuery);
				resultSet = pStmt.executeQuery();
				
				while(resultSet.next()){
					tablesHavingColumn.add(resultSet.getString("TABLE_NAME"));
				}
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
				if(resultSet != null){
					resultSet.close();
					resultSet = null;
				}
			}
			for(String table: tableNames){
				if(tablesHavingColumn.contains(table)) continue;
				tablesNotHavingColumn.add(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tablesNotHavingColumn;
	}

	/*private static List<String> getHistoryTableNamesNotHavingColumnAction(Connection connection, EntityRevisited entity) {
		return null;
	}*/

	public static void detectExtraMasterColumns(Connection connection, Map<String, String> tableNameHistoryTableName) {
		
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		String sqlQuery = "";
		Map<String, List<String>> extraColumns = new HashMap<String, List<String>>();
		try {
			
			for(String tableName: tableNameHistoryTableName.keySet()){
				String historyTable = tableNameHistoryTableName.get(tableName);
				
				sqlQuery = 
						String.format("select a.column_name, a.data_type "
								+ "from all_tab_columns a where table_name in ('%s') "
								+ "MINUS "
								+ "select a.column_name, a.data_type "
								+ "from all_tab_columns a "
								+ "where table_name in ('%s')", tableName, historyTable);
		
				pStmt = connection.prepareStatement(sqlQuery);
				resultSet = pStmt.executeQuery();
				List<String> columns = new ArrayList<String>();
				while(resultSet.next()){
					columns.add(resultSet.getString("column_name"));
				}
				extraColumns.put(tableName, columns);
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
				if(resultSet != null){
					resultSet.close();
					resultSet = null;
				}
			}
			addExtraColumnToAudit(connection, extraColumns, tableNameHistoryTableName);
//			System.out.println(extraColumns);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addExtraColumnToAudit(Connection connection, Map<String, List<String>> extraColumns, Map<String, String> tableNameHistoryTableName){
		
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		String sqlQuery = "";
		try {
			
			for(String tableName: extraColumns.keySet()){
				List<String> columns = extraColumns.get(tableName);
				if(columns.size() == 0) continue;
				sqlQuery = 
						String.format("select a.column_name, a.data_type, a.data_length, a.data_scale, a.data_precision "
								+ "from all_tab_columns a "
								+ "where a.table_name in ('%s') "
								+ "and a.column_name in %s", tableName, getDBQuery(columns));
		
				pStmt = connection.prepareStatement(sqlQuery);
				resultSet = pStmt.executeQuery();
				System.out.println("------" + tableName + "--" + tableNameHistoryTableName.get(tableName));
				while(resultSet.next()){
					String columnName = resultSet.getString("COLUMN_NAME");
					String datatype = resultSet.getString("data_type").toUpperCase();
					int length = resultSet.getInt("data_length");
					String scale = resultSet.getString("data_scale");
					String precision = resultSet.getString("data_precision");
					
					System.out.println(String.format("--columnName: %s, datatype: %s, length: %s, scale: %s, precision: %s", columnName, datatype, length, scale, precision));
					String q = "";
					q = String.format("Select %s from %s;", columnName, tableNameHistoryTableName.get(tableName));
					System.out.println(q);
					if(datatype.contains("VARCHAR2")){
						q = String.format("ALTER TABLE %s ADD %s VARCHAR2(%d)", tableNameHistoryTableName.get(tableName), columnName, length);
					} else if(datatype.contains("CHAR")){
						q = String.format("ALTER TABLE %s ADD %s VARCHAR2(%d)", tableNameHistoryTableName.get(tableName), columnName, length);
					} else if (datatype.contains("NUMBER")) {
						q = String.format("ALTER TABLE %s ADD %s NUMBER(%s, %s)", tableNameHistoryTableName.get(tableName), columnName, precision, scale);
					} else {
						q = "Special Case - " + resultSet.getString("column_name");
					}
					System.out.println(q + ";");
				}
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
				if(resultSet != null){
					resultSet.close();
					resultSet = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDBQuery(List<String> list){
		return list.toString().replace(", ", "','").replace("[", "('").replace("]", "')");
	}

	public static boolean validatePrimaryKey(Connection connection, EntityRevisited entity) {
		Map<String, Set<String>> tableNameColumnList = new HashMap<String, Set<String>>(); 
		
		ParentTable parentTable = entity.getParentTable();
	 	Set<String> parentColumns = new HashSet<String>(parentTable.getPrimaryColumnNames());
	 	tableNameColumnList.put(parentTable.getName(), parentColumns);
	 	
	 	for(ChildTable child: parentTable.getChildTables()){
	 		Set<String> childColumns = new HashSet<String>(child.getPrimaryColumnNames());
		 	tableNameColumnList.put(child.getName(), childColumns);
		 	
		 	for(GrandChildTable grandChild: child.getGrandChildTables()){
		 		Set<String> grandChildColumns = new HashSet<String>(grandChild.getPrimaryColumnNames());
			 	tableNameColumnList.put(grandChild.getName(), grandChildColumns);
		 	}
	 	}
	 	Map<String, Set<String>> contains = contains(connection, tableNameColumnList);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}
	
	public static boolean validatePrimaryKeyRevisited(Connection connection, EntityRevisited entity) {
		Map<String, Set<String>> tableNameColumnList = new HashMap<String, Set<String>>(); 
		
		Table parent = entity.getParent();
	 	Set<String> parentColumns = new HashSet<String>(parent.getPrimaryColumnNames());
	 	tableNameColumnList.put(parent.getName(), parentColumns);
	 	
	 	for(Table child: parent.getChilds()){
	 		Set<String> childColumns = new HashSet<String>(child.getPrimaryColumnNames());
		 	tableNameColumnList.put(child.getName(), childColumns);
		 	
		 	for(Table grandChild: child.getChilds()){
		 		Set<String> grandChildColumns = new HashSet<String>(grandChild.getPrimaryColumnNames());
			 	tableNameColumnList.put(grandChild.getName(), grandChildColumns);
			 	for(Table greatGrandChild: grandChild.getChilds()){
			 		Set<String> greatGrandChildColumns = new HashSet<String>(greatGrandChild.getPrimaryColumnNames());
				 	tableNameColumnList.put(greatGrandChild.getName(), greatGrandChildColumns);
			 	}
		 	}
	 	}
	 	Map<String, Set<String>> contains = contains(connection, tableNameColumnList);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}

	public static boolean validateReferenceKey(Connection connection, EntityRevisited entity) {
		Map<String, Set<String>> data = new HashMap<String, Set<String>>(); 
		
		ParentTable parentTable = entity.getParentTable();
	 	
	 	for(ChildTable child: parentTable.getChildTables()){
	 		Set<String> childColumns = new HashSet<String>(child.getForeignColumnNames());
		 	data.put(child.getName(), childColumns);
		 	
		 	for(GrandChildTable grandChild: child.getGrandChildTables()){
		 		Set<String> grandChildColumns = new HashSet<String>(grandChild.getForeignColumnNames());
			 	data.put(grandChild.getName(), grandChildColumns);
		 	}
	 	}
	 	Map<String, Set<String>> contains = contains(connection, data);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}
	
	public static boolean validateReferenceKeyRevisited(Connection connection, EntityRevisited entity) {
		Map<String, Set<String>> data = new HashMap<String, Set<String>>(); 
		
		Table parent = entity.getParent();
		data.put(parent.getName(), new HashSet<String>());
	 	for(Table child: parent.getChilds()){
	 		Set<String> childColumns = new HashSet<String>(child.getForeignColumnMapping().values());
		 	data.put(child.getName(), childColumns);
		 	data.get(parent.getName()).addAll(child.getForeignColumnMapping().keySet());
		 	for(Table grandChild: child.getChilds()){
		 		Set<String> grandChildColumns = new HashSet<String>(grandChild.getForeignColumnMapping().values());
			 	data.put(grandChild.getName(), grandChildColumns);
			 	data.get(child.getName()).addAll(new HashSet<String>(grandChild.getForeignColumnMapping().keySet()));
			 	for(Table greatGrandChild: grandChild.getChilds()){
			 		Set<String> greatGrandChildColumns = new HashSet<String>(greatGrandChild.getForeignColumnMapping().values());
				 	data.put(greatGrandChild.getName(), greatGrandChildColumns);
				 	data.get(grandChild.getName()).addAll(new HashSet<String>(greatGrandChild.getForeignColumnMapping().keySet()));
			 	}
		 	}
	 	}
	 	Map<String, Set<String>> contains = contains(connection, data);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}

	public static boolean validateActionColumn(Connection connection, EntityRevisited entity) {
		Map<String, Set<String>> data = new HashMap<String, Set<String>>(); 
		
		ParentTable parentTable = entity.getParentTable();
		Set<String> s = new HashSet<String>();
		s.add("ACTION");
	 	data.put(parentTable.getHistoryName(), s);
	 	Map<String, Set<String>> contains = contains(connection, data);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}
	
	public static boolean validateActionColumnRevisited(Connection connection, EntityRevisited entity) {
		Map<String, Set<String>> data = new HashMap<String, Set<String>>(); 
		
		Table parent = entity.getParent();
		Set<String> s = new HashSet<String>();
		s.add(entity.getParent().getActionColumnName() == null ? "ACTION": entity.getParent().getActionColumnName());
	 	data.put(parent.getHistoryName(), s);
	 	Map<String, Set<String>> contains = contains(connection, data);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}

	public static boolean validateAuditIdColumn(Connection connection, EntityRevisited entity) {
		Map<String, Set<String>> data = new HashMap<String, Set<String>>(); 
		
		ParentTable parentTable = entity.getParentTable();
		
	 	Set<String> parentColumns = new HashSet<String>(Arrays.asList(parentTable.getAuditColumnName()));
	 	data.put(parentTable.getHistoryName(), parentColumns);
	 	
	 	for(ChildTable child: parentTable.getChildTables()){
	 		Set<String> childColumns = new HashSet<String>(Arrays.asList(child.getAuditColumnName()));
		 	data.put(child.getHistoryName(), childColumns);
		 	
		 	for(GrandChildTable grandChild: child.getGrandChildTables()){
		 		Set<String> grandChildColumns = new HashSet<String>(Arrays.asList(grandChild.getAuditColumnName()));
			 	data.put(grandChild.getHistoryName(), grandChildColumns);
		 	}
	 	}
	 	Map<String, Set<String>> contains = contains(connection, data);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}

	public static boolean validateAuditIdColumnRevisited(Connection connection,  EntityRevisited entity) {
		Map<String, Set<String>> data = new HashMap<String, Set<String>>(); 
		
		Table parent = entity.getParent();
		
	 	Set<String> parentColumns = new HashSet<String>(Arrays.asList(parent.getAuditColumnName()));
	 	data.put(parent.getHistoryName(), parentColumns);
	 	
	 	for(Table child: parent.getChilds()){
	 		Set<String> childColumns = new HashSet<String>(Arrays.asList(child.getAuditColumnName()));
		 	data.put(child.getHistoryName(), childColumns);
		 	
		 	for(Table grandChild: child.getChilds()){
		 		Set<String> grandChildColumns = new HashSet<String>(Arrays.asList(grandChild.getAuditColumnName()));
			 	data.put(grandChild.getHistoryName(), grandChildColumns);
			 	
			 	for(Table greatGrandChild: grandChild.getChilds()){
			 		Set<String> greatGrandChildColumns = new HashSet<String>(Arrays.asList(greatGrandChild.getAuditColumnName()));
				 	data.put(greatGrandChild.getHistoryName(), greatGrandChildColumns);
			 	}
		 	}
	 	}
	 	Map<String, Set<String>> contains = contains(connection, data);
	 	if(contains.size() != 0){
	 		System.out.println(contains);
	 		return false;
	 	}
	 	
		return true;
	}
	
	public static Map<String, Set<String>> contains(Connection connection, Map<String, Set<String>> userData){
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		Map<String, Set<String>> presentData = getColumns(connection, userData.keySet());
		for(String tableName: userData.keySet()){
			if(!presentData.containsKey(tableName)){
				result.put(tableName, userData.get(tableName));
				continue;
			} 
			Set<String> c = new HashSet<String>();
			for(String userColumn: userData.get(tableName)){
				if(!presentData.get(tableName).contains(userColumn)){
					c.add(userColumn);;
				}
			}
			if(c.size() != 0){
				result.put(tableName, c);
			}
		}
		return result;
	}

	public static Map<String, Set<String>> getColumns(Connection connection,  Set<String> tables){
		Map<String, Set<String>> columns = new HashMap<String, Set<String>>();
		
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		String sqlQuery = "";
		try {
			
			sqlQuery = 
					String.format("select a.table_name, a.column_name, a.data_type, a.data_length, a.data_scale, a.data_precision "
							+ "from all_tab_columns a "
							+ "where a.table_name in %s ", Program.getDBQuery(new ArrayList<String>(tables)));
	
			pStmt = connection.prepareStatement(sqlQuery);
			resultSet = pStmt.executeQuery();
			while(resultSet.next()){
				String tableName = resultSet.getString("TABLE_NAME");
				String columnName = resultSet.getString("COLUMN_NAME");
				if(columns.containsKey(tableName)){
					columns.get(tableName).add(columnName);
				} else {
					Set<String> s = new HashSet<String>();
					s.add(columnName);
					columns.put(tableName, s);
				}
			}
			if (pStmt != null) {
				pStmt.close();
				pStmt = null;
			}
			if(resultSet != null){
				resultSet.close();
				resultSet = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}

	public static boolean validateMasterColumnsInHistory(EntityRevisited entity) {
		
		return false;
	}

	public static void insertIntoTbEntMasterRevisited(Connection connection, EntityRevisited entity) {
		
		PreparedStatement pStmt = null;
		String sqlQuery = "";
		Table parent = entity.getParent();
		try {
			
			if(parent.getActionColumnName() == null){
				sqlQuery = 
						String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
								+ "AUDIT_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s')", 
								entity.getEntityName(), parent.getName(), "Y", parent.getHistoryName(), 
								parent.getName(), parent.getAuditColumnName()); 
			} else {
				sqlQuery = 
						String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
								+ "AUDIT_COLUMN_NAME, ACTION_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s','%s')", 
								entity.getEntityName(), parent.getName(), "Y", parent.getHistoryName(), 
								parent.getName(), parent.getAuditColumnName(), parent.getActionColumnName());
			}
			if(!isPresentInEntityMaster(connection, entity.getEntityName(), parent.getName())){
				System.out.println(sqlQuery + ";");	
				pStmt = connection.prepareStatement(sqlQuery);
				pStmt.execute();
				if (pStmt != null) {
					pStmt.close();
				}
			}
			for (Table child : parent.getChilds()) {
				sqlQuery = 
						String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
								+ "AUDIT_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s')", 
								entity.getEntityName(), child.getName(), "N", child.getHistoryName(), 
								child.getName(),child.getAuditColumnName()); 

				if(!isPresentInEntityMaster(connection, entity.getEntityName(), child.getName())){
					System.out.println(sqlQuery + ";");	
					pStmt = connection.prepareStatement(sqlQuery);
					pStmt.execute();
					if (pStmt != null) {
						pStmt.close();
					}
				}
			}
			for (Table child : parent.getChilds()) {	
				for (Table grandChild : child.getChilds()) {
					sqlQuery = 
							String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
									+ "AUDIT_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s')", 
									entity.getEntityName(), grandChild.getName(), "N", grandChild.getHistoryName(), 
									grandChild.getName(),grandChild.getAuditColumnName()); 
	
					if(!isPresentInEntityMaster(connection, entity.getEntityName(), grandChild.getName())){
						System.out.println(sqlQuery + ";");	
						pStmt = connection.prepareStatement(sqlQuery);
						pStmt.execute();
						if (pStmt != null) {
							pStmt.close();
						}
					}
					
				}
			}
			for (Table child : parent.getChilds()) {	
				for (Table grandChild : child.getChilds()) {
					for (Table greatGrandChild : grandChild.getChilds()) {
						sqlQuery = 
								String.format("INSERT INTO TB_ENT_MASTER(ENTITY_NAME, TABLE_NAME, IS_PRIMARY, HISTORY_TABLE_NAME, TABLE_CAPTION, "
										+ "AUDIT_COLUMN_NAME) VALUES ('%s','%s','%s','%s','%s','%s')", 
										entity.getEntityName(), greatGrandChild.getName(), "N", greatGrandChild.getHistoryName(), 
										greatGrandChild.getName(),greatGrandChild.getAuditColumnName()); 
		
						if(!isPresentInEntityMaster(connection, entity.getEntityName(), greatGrandChild.getName())){
							System.out.println(sqlQuery + ";");	
							pStmt = connection.prepareStatement(sqlQuery);
							pStmt.execute();
							if (pStmt != null) {
								pStmt.close();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isPresentInEntityMaster(Connection connection, String entityName, String tableName) {
		boolean isPresent = false;
		PreparedStatement pStmt = null;
		ResultSet metaQueryResultSet = null;
		String sql = String.format("SELECT COUNT(1) AS COUNT FROM TB_ENT_MASTER WHERE ENTITY_NAME = '%s' AND TABLE_NAME = '%s'", entityName, tableName);
		try {
			pStmt = connection.prepareStatement(sql);
			metaQueryResultSet = pStmt.executeQuery();
			while(metaQueryResultSet.next()){
				isPresent = metaQueryResultSet.getInt("COUNT") > 0; 
			}
			return isPresent;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pStmt != null) pStmt.close();
				if(metaQueryResultSet != null) metaQueryResultSet.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return isPresent;
	}

	public static void insertIntoTbColumnMasterRevisited(Connection connection, EntityRevisited entity) {
		
		PreparedStatement pStmt = null;
		ResultSet metaQueryResultSet = null;
		
		PreparedStatement pStmt2 = null;
		String sqlQuery = "";
		String mainQuery = "";
		try {
			
			
			List<String> tableNames = getTableNamesRevisited(entity);

//			for(String tableName: tableNames){
				sqlQuery = 
						"select TABLE_NAME, column_name,column_name AS COLUMN_CAPTION,COLUMN_ID AS COLUMN_SEQUENCE, " +
				        "'N' AS IS_LISTING_COLUMN,DATA_TYPE,'N' AS IS_TREE_LABEL_COLUMN,'Y' AS IS_HASHING_COLUMN " +
				        "from all_tab_columns " +
				        "where table_name in " + Program.getDBQuery(tableNames);
		
				pStmt = connection.prepareStatement(sqlQuery);
				metaQueryResultSet = pStmt.executeQuery();
				
				while(metaQueryResultSet.next()){
					String TABLE_NAME = metaQueryResultSet.getString(1);
					String COLUMN_NAME = metaQueryResultSet.getString(2);
					String COLUMN_CAPTION = metaQueryResultSet.getString(3);
					String COLUMN_SEQUENCE = metaQueryResultSet.getString(4);
					String IS_LISTING_COLUMN = metaQueryResultSet.getString(5); 
				    String DATA_TYPE = metaQueryResultSet.getString(6);
				    String IS_TREE_LABEL_COLUMN = metaQueryResultSet.getString(7);
			
				    if(!checkDataExist(TABLE_NAME, COLUMN_NAME, connection)) {
					    mainQuery = 
					    		String.format("INSERT INTO TB_ENT_COL_MASTER (TABLE_NAME, COLUMN_NAME, COLUMN_CAPTION, COLUMN_SEQUENCE, IS_LISTING_COLUMN, DATA_TYPE, "
					    		+ "IS_TREE_LABEL_COLUMN, IS_HASHING_COLUMN) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')", 
					    		TABLE_NAME, COLUMN_NAME, COLUMN_CAPTION, COLUMN_SEQUENCE, IS_LISTING_COLUMN, DATA_TYPE, IS_TREE_LABEL_COLUMN,getHashingColumnValueRevisited(entity, TABLE_NAME, COLUMN_NAME));
					    
					    System.out.println(mainQuery + ";");
					    
					    pStmt2 = connection.prepareStatement(mainQuery);
					    pStmt2.execute();
					    
					    if (pStmt2 != null) {
							pStmt2.close();
							pStmt2 = null;
						}
				    }
				}
				
				System.out.println();
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
				if(metaQueryResultSet != null){
					metaQueryResultSet.close();
					metaQueryResultSet = null;
				}
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean checkDataExist(String tableName, String columnName, Connection connection) {
		boolean exist = false;
		
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		String sqlQuery = "";
		try {
			
			sqlQuery = 
					String.format("select COUNT(1) as COUNT "
							+ "from TB_ENT_COL_MASTER "
							+ "where TABLE_NAME = '%s' and COLUMN_NAME = '%s' ", tableName.toUpperCase(), columnName.toUpperCase());
	
			pStmt = connection.prepareStatement(sqlQuery);
			resultSet = pStmt.executeQuery();
			while(resultSet.next()){
				return resultSet.getInt("COUNT") > 0;
			}
			if (pStmt != null) {
				pStmt.close();
				pStmt = null;
			}
			if(resultSet != null){
				resultSet.close();
				resultSet = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exist;
	}

	public static List<String> getTableNamesRevisited(EntityRevisited entity) {
		Set<String> tableNames = new HashSet<String>();
		
		Table parent = entity.getParent();
		tableNames.add(parent.getName());
		for(Table child: parent.getChilds()){
			tableNames.add(child.getName());
			for(Table grandChild: child.getChilds()){
				tableNames.add(grandChild.getName());
				for(Table greatGrandChild: grandChild.getChilds()){
					tableNames.add(greatGrandChild.getName());
				}
			}
		}
		
		return new ArrayList<String>(tableNames);
	}

	public static void insertIntoTbEntityPrimaryMasterRevisited(Connection connection, EntityRevisited entity) {
		
		PreparedStatement pStmt2 = null;
		String mainQuery = "";
		try {
			
			String entityName = entity.getEntityName();
			Table parent = entity.getParent();
			String parentTableName = parent.getName();
			List<String> parentPrimaryColumnNames = parent.getPrimaryColumnNames();
			for(String column: parentPrimaryColumnNames){
				ColumnModel col = parent.primaryColumnDetails.get(column);
				mainQuery = 
						String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
			    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
			    		entityName, parentTableName, column, col.getSequence(), col.getDatatype());
				if(!isPresentInEntityPrimaryKeyMaster(connection, entityName, parent.getName(), col.getName())){    
				    System.out.println(mainQuery + ";");
				    
				    pStmt2 = connection.prepareStatement(mainQuery);
				    pStmt2.execute();
				    
				    if (pStmt2 != null) {
						pStmt2.close();
						pStmt2 = null;
					}
				}
			}
			
			for(Table child: parent.getChilds()){
				String childTableName = child.getName();
				List<String> childPrimaryColumnNames = child.getPrimaryColumnNames();
				for(String column: childPrimaryColumnNames){
					ColumnModel col = child.primaryColumnDetails.get(column);
					mainQuery = 
							String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
				    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
				    		entityName, childTableName, column, col.getSequence(), col.getDatatype());
					    
					if(!isPresentInEntityPrimaryKeyMaster(connection, entityName, childTableName, col.getName())){    
					    System.out.println(mainQuery + ";");
					    
					    pStmt2 = connection.prepareStatement(mainQuery);
					    pStmt2.execute();
					    
					    if (pStmt2 != null) {
							pStmt2.close();
							pStmt2 = null;
						}
					}
				}
			}
			for(Table child: parent.getChilds()){
				for(Table grandChild: child.getChilds()){
					String grandChildTableName = grandChild.getName();
					List<String> grandChildPrimaryColumnNames = grandChild.getPrimaryColumnNames();
					for(String column: grandChildPrimaryColumnNames){
						ColumnModel col = grandChild.primaryColumnDetails.get(column);
						mainQuery = 
								String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
					    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
					    		entityName, grandChildTableName, column, col.getSequence(), col.getDatatype());
						    
						if(!isPresentInEntityPrimaryKeyMaster(connection, entityName, grandChildTableName, col.getName())){    
						    System.out.println(mainQuery + ";");
						    
						    pStmt2 = connection.prepareStatement(mainQuery);
						    pStmt2.execute();
						    
						    if (pStmt2 != null) {
								pStmt2.close();
								pStmt2 = null;
							}
						}
					}
				}
			}
			for(Table child: parent.getChilds()){
				for(Table grandChild: child.getChilds()){
					for(Table greatGrandChild: grandChild.getChilds()){
						String greatGrandChildTableName = greatGrandChild.getName();
						List<String> grandChildPrimaryColumnNames = greatGrandChild.getPrimaryColumnNames();
						for(String column: grandChildPrimaryColumnNames){
							ColumnModel col = greatGrandChild.primaryColumnDetails.get(column);
							mainQuery = 
									String.format("INSERT INTO TB_ENT_PK_MASTER (ENTITY_NAME, TABLE_NAME, COLUMN_NAME, COLUMN_SEQUENCE, COLUMN_DATA_TYPE) "
						    				+ "VALUES ('%s','%s','%s',%s,'%s')", 
						    		entityName, greatGrandChildTableName, column, col.getSequence(), col.getDatatype());
							    
							if(!isPresentInEntityPrimaryKeyMaster(connection, entityName, greatGrandChildTableName, col.getName())){    
							    System.out.println(mainQuery + ";");
							    
							    pStmt2 = connection.prepareStatement(mainQuery);
							    pStmt2.execute();
							    
							    if (pStmt2 != null) {
									pStmt2.close();
									pStmt2 = null;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isPresentInEntityPrimaryKeyMaster(Connection connection, String entityName, String tableName, String columnName) {
		boolean isPresent = false;
		PreparedStatement pStmt = null;
		ResultSet metaQueryResultSet = null;
		String sql = String.format("SELECT COUNT(1) AS COUNT FROM TB_ENT_PK_MASTER WHERE ENTITY_NAME = '%s' AND TABLE_NAME = '%s' AND COLUMN_NAME = '%s'", entityName, tableName, columnName);
		try {
			pStmt = connection.prepareStatement(sql);
			metaQueryResultSet = pStmt.executeQuery();
			while(metaQueryResultSet.next()){
				isPresent = metaQueryResultSet.getInt("COUNT") > 0; 
			}
			return isPresent;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pStmt != null) pStmt.close();
				if(metaQueryResultSet != null) metaQueryResultSet.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return isPresent;
	}

	public static void insertIntoTbEntDetailMasterRevisited(Connection connection, EntityRevisited entity) {
		
		PreparedStatement pStmt2 = null;
		String mainQuery = "";
		try {
			
			
			String entityName = entity.getEntityName();
			Table parent = entity.getParent();
			List<Table> childTables = parent.getChilds();
			String parentTableName = parent.getName();
			int groupNumber = 0;
			for(Table child: childTables){
				String childTableName = child.getName();
				groupNumber++;
				for(String parentColumnName: child.getForeignColumnMapping().keySet()){ 	
					mainQuery = 
			    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, TABLE_COLUMN_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, GROUP_NUMBER) "
			    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
			    		entityName, parentTableName, parentColumnName, childTableName, child.getForeignColumnMapping().get(parentColumnName), groupNumber);
					if(!isPresentInEntityDetailMaster(connection, entityName, parentTableName, parentColumnName, childTableName, child.getForeignColumnMapping().get(parentColumnName))){    
					    System.out.println(mainQuery + ";");
					    
					    pStmt2 = connection.prepareStatement(mainQuery);
					    pStmt2.execute();
					    
					    if (pStmt2 != null) {
							pStmt2.close();
							pStmt2 = null;
						}
					}
				}
				for(String columnName: child.getColumnNameFixedValueMap().keySet()){ 	
					mainQuery = 
			    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, CHILD_FIXED_VALUE, GROUP_NUMBER) "
			    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
			    		entityName, parentTableName, childTableName, columnName, child.getColumnNameFixedValueMap().get(columnName), groupNumber);
					    
				    System.out.println(mainQuery + ";");
				    
				    pStmt2 = connection.prepareStatement(mainQuery);
				    pStmt2.execute();
				    
				    if (pStmt2 != null) {
						pStmt2.close();
						pStmt2 = null;
					}
				}
			}
			for(Table child: parent.getChilds()){
				String p = child.getName();
				for(Table grandChild: child.getChilds()){
					groupNumber++;
					int count = -1;
					for(String foreignKeyName: grandChild.getForeignColumnMapping().keySet()){
						count++;
						mainQuery = 
				    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, TABLE_COLUMN_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, GROUP_NUMBER) "
				    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
				    		entityName, child.getName().toUpperCase(), foreignKeyName, grandChild.getName(), grandChild.getForeignColumnMapping().get(foreignKeyName), groupNumber);
						    
					    System.out.println(mainQuery + ";");
					    
					    pStmt2 = connection.prepareStatement(mainQuery);
					    pStmt2.execute();
					    
					    if (pStmt2 != null) {
							pStmt2.close();
							pStmt2 = null;
						}
					}
					for(String columnName: grandChild.getColumnNameFixedValueMap().keySet()){ 	
						mainQuery = 
				    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, CHILD_FIXED_VALUE, GROUP_NUMBER) "
				    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
				    		entityName, p, grandChild.getName(), columnName, child.getColumnNameFixedValueMap().get(columnName), groupNumber);
						    
					    System.out.println(mainQuery + ";");
					    
					    pStmt2 = connection.prepareStatement(mainQuery);
					    pStmt2.execute();
					    
					    if (pStmt2 != null) {
							pStmt2.close();
							pStmt2 = null;
						}
					}
				}
			}
			for(Table child: parent.getChilds()){
				for(Table grandChild: child.getChilds()){
				String p = grandChild.getName();
					for(Table greatGrandChild: grandChild.getChilds()){
						groupNumber++;
						int count = -1;
						for(String foreignKeyName: greatGrandChild.getForeignColumnMapping().keySet()){
							count++;
							mainQuery = 
					    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, TABLE_COLUMN_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, GROUP_NUMBER) "
					    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
					    		entityName, grandChild.getName().toUpperCase(), foreignKeyName, greatGrandChild.getName(), greatGrandChild.getForeignColumnMapping().get(foreignKeyName), groupNumber);
							    
						    System.out.println(mainQuery + ";");
						    
						    pStmt2 = connection.prepareStatement(mainQuery);
						    pStmt2.execute();
						    
						    if (pStmt2 != null) {
								pStmt2.close();
								pStmt2 = null;
							}
						}
						for(String columnName: greatGrandChild.getColumnNameFixedValueMap().keySet()){ 	
							mainQuery = 
					    		String.format("INSERT INTO TB_ENT_DETAIL_MASTER (ENTITY_NAME, TABLE_NAME, CHILD_TABLE_NAME, CHILD_TBL_COL_NAME, CHILD_FIXED_VALUE, GROUP_NUMBER) "
					    				+ "VALUES ('%s','%s','%s','%s','%s', %d)", 
					    		entityName, p, greatGrandChild.getName(), columnName, grandChild.getColumnNameFixedValueMap().get(columnName), groupNumber);
							    
						    System.out.println(mainQuery + ";");
						    
						    pStmt2 = connection.prepareStatement(mainQuery);
						    pStmt2.execute();
						    
						    if (pStmt2 != null) {
								pStmt2.close();
								pStmt2 = null;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isPresentInEntityDetailMaster(Connection connection, String entityName, String parentTableName, String parentColumnName, String childTableName, String childTableColumnName) {
		boolean isPresent = false;
		PreparedStatement pStmt = null;
		ResultSet metaQueryResultSet = null;
		String sql = String.format("SELECT COUNT(1) AS COUNT FROM TB_ENT_DETAIL_MASTER WHERE ENTITY_NAME = '%s' AND TABLE_NAME = '%s' AND TABLE_COLUMN_NAME = '%s' AND CHILD_TABLE_NAME = '%s' AND CHILD_TBL_COL_NAME = '%s'", entityName, parentTableName, parentColumnName, childTableName, childTableColumnName);
		try {
			pStmt = connection.prepareStatement(sql);
			metaQueryResultSet = pStmt.executeQuery();
			while(metaQueryResultSet.next()){
				isPresent = metaQueryResultSet.getInt("COUNT") > 0; 
			}
			return isPresent;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pStmt != null) pStmt.close();
				if(metaQueryResultSet != null) metaQueryResultSet.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return isPresent;
	}

	public static void displayLinkTableData(Connection connection, EntityRevisited entity) {
		String entityName = entity.getEntityName();
		
		Map<String, Set<String>> columns = new HashMap<String, Set<String>>();
		
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		String sqlQuery = "";
		try {
			
			sqlQuery = 
					String.format("select MODULE_TYPE, ENTITY_NAME, PARENT_ENTITY_NAME, ENTITY_LEVEL, ENTITY_LEVEL_SEQ, HAS_CHILD, ENTITY_CAPTION "
							+ "from TB_ENT_LINK_MASTER "
							+ "where ENTITY_NAME = '%s' ", entityName);
	
			pStmt = connection.prepareStatement(sqlQuery);
			resultSet = pStmt.executeQuery();
			while(resultSet.next()){
				String moduleType = resultSet.getString("MODULE_TYPE");
				String parentEntityName = resultSet.getString("PARENT_ENTITY_NAME");
				int entityLevel = resultSet.getInt("ENTITY_LEVEL");
				int entityLevelSeq = resultSet.getInt("ENTITY_LEVEL_SEQ");
				String hasChild = resultSet.getString("HAS_CHILD");
				String entityCaption = resultSet.getString("ENTITY_CAPTION");
				
				String insertQuery = String.format("Insert into TB_ENT_LINK_MASTER (MODULE_TYPE,ENTITY_NAME,PARENT_ENTITY_NAME,ENTITY_LEVEL,ENTITY_LEVEL_SEQ,HAS_CHILD,ENTITY_CAPTION) "
						+ "values ('%s','%s','%s',%d,%d,'%s','%s');",moduleType, entityName, parentEntityName, entityLevel, entityLevelSeq, hasChild, entityCaption);
				System.out.println(insertQuery);
			}
			if (pStmt != null) {
				pStmt.close();
				pStmt = null;
			}
			if(resultSet != null){
				resultSet.close();
				resultSet = null;
			}
			
			sqlQuery = 
					String.format("select ENTITY_NAME, ENT_TABLE_NAME, ENT_TABLE_COLUMN_NAME, TABLE_SEQUENCE, PARENT_ENTITY_NAME, PARENT_ENT_TABLE_NAME, PARENT_ENT_TBL_COL_NAME, FIXED_VALUE "
							+ "from tb_ent_link_detail_master "
							+ "where ENTITY_NAME = '%s' ", entityName);
	
			pStmt = connection.prepareStatement(sqlQuery);
			resultSet = pStmt.executeQuery();
			while(resultSet.next()){
				String entityTableName = resultSet.getString("ENT_TABLE_NAME");
				String entityTableColumnName = resultSet.getString("ENT_TABLE_COLUMN_NAME");
				int tableSequence = resultSet.getInt("TABLE_SEQUENCE");
				String parentEntityName = resultSet.getString("PARENT_ENTITY_NAME");
				String parentEntityTableName = resultSet.getString("PARENT_ENT_TABLE_NAME");
				String parentEntityTableColumnName = resultSet.getString("PARENT_ENT_TBL_COL_NAME");
				String fixedValue = resultSet.getString("FIXED_VALUE");
				if(fixedValue != null){
					String insertQuery = String.format("Insert into TB_ENT_LINK_DETAIL_MASTER (ENTITY_NAME,ENT_TABLE_NAME,ENT_TABLE_COLUMN_NAME,TABLE_SEQUENCE,PARENT_ENTITY_NAME,PARENT_ENT_TABLE_NAME,PARENT_ENT_TBL_COL_NAME,FIXED_VALUE) "
							+ "values ('%s','%s','%s',%d,'%s','%s','%s','%s');",entityName, entityTableName, entityTableColumnName, tableSequence, parentEntityName, parentEntityTableName, parentEntityTableColumnName, fixedValue);
					System.out.println(insertQuery);
				}
				else if(fixedValue == null){
					String insertQuery = String.format("Insert into TB_ENT_LINK_DETAIL_MASTER (ENTITY_NAME,ENT_TABLE_NAME,ENT_TABLE_COLUMN_NAME,TABLE_SEQUENCE,PARENT_ENTITY_NAME,PARENT_ENT_TABLE_NAME,PARENT_ENT_TBL_COL_NAME,FIXED_VALUE) "
							+ "values ('%s','%s','%s',%d,'%s','%s','%s',null);",entityName, entityTableName, entityTableColumnName, tableSequence, parentEntityName, parentEntityTableName, parentEntityTableColumnName);
					System.out.println(insertQuery);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
