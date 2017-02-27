package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import model.DBConnector;
import utility.CollectionUtility;
import utility.DateUtility;
import utility.FileUtility;
import db.oracle.OracleInsertScriptGenerator;

public class GenerateInsertStatements {
	private static Integer CONNECTION_ID = 7;
	private static final String FILE_EXTENSION = "_insert.sql";
	
	private static final String BLOB_SEPARATOR = "~";

	public static void main(String[] args) throws Exception {
//		generateAndWriteInsertScriptToFile("TB_MULTILANG_MASTER");
		aptConfigurationTables();
//		insertRecordsFromFile("C:\\Users\\mithul.bhansali\\Desktop\\tttt.txt");
	}	

	private static void aptConfigurationTables() throws Exception {
		
		List<Table> tableSet = getAptConfigurationTableList();
		
		List<String> tableNames = Arrays.asList(
				"TB_ENT_MASTER", 
				"TB_ENT_LINK_MASTER", 
				"TB_ENT_LINK_DETAIL_MASTER",
				"TB_ENT_COL_MASTER",
				"TB_ENT_PK_MASTER",
				"TB_ENT_DETAIL_MASTER",
				"TB_ENT_FK_DETAIL_MASTER"
			);
		
		generateAndWriteInsertScriptToFile(tableSet);
		
		String truncateScriptContent = getTruncateScriptContent(tableNames); 
		String insertScriptContent = getInsertScriptContent(tableNames);
		
		String scriptContent = new String(new StringBuilder(truncateScriptContent).append(FileUtility.NEW_LINE).append(insertScriptContent));
		writeContentToFile("InsertScript" + new Date().toString().replace(":", "_") + "(" + CONNECTION_ID + ").sql", scriptContent);
		
	}
	
	private static List<Table> getAptConfigurationTableList() {
		List<Table> tableList = new ArrayList<Table>();
		
		String tableName= null;
		SortedSet<String> primaryColumnNameSet = null;

		tableList.add(new Table("TB_ENT_MASTER").addPrimaryColumn("ENTITY_NAME").addPrimaryColumn("TABLE_NAME"));
		
		tableName = "TB_ENT_LINK_MASTER";
		primaryColumnNameSet = CollectionUtility.createSortedSet("ENTITY_NAME", "APP_TYPE", "PARENT_ENTITY_NAME");
		tableList.add(new Table(tableName, primaryColumnNameSet));

		tableName = "TB_ENT_LINK_DETAIL_MASTER";
		primaryColumnNameSet = CollectionUtility.createSortedSet("ENT_TABLE_NAME", "ENTITY_NAME", "ENT_TABLE_COLUMN_NAME", "TABLE_SEQUENCE", "PARENT_ENTITY_NAME");
		tableList.add(new Table(tableName, primaryColumnNameSet));

		tableName = "TB_ENT_COL_MASTER";
		primaryColumnNameSet = CollectionUtility.createSortedSet("TABLE_NAME", "COLUMN_NAME");
		tableList.add(new Table(tableName, primaryColumnNameSet));

		tableName = "TB_ENT_PK_MASTER";
		primaryColumnNameSet = CollectionUtility.createSortedSet("ENTITY_NAME", "TABLE_NAME", "COLUMN_NAME");
		tableList.add(new Table(tableName, primaryColumnNameSet));
		
		tableName = "TB_ENT_DETAIL_MASTER";
		primaryColumnNameSet = CollectionUtility.createSortedSet("ENTITY_NAME", "TABLE_NAME", "TABLE_COLUMN_NAME", "CHILD_TABLE_NAME", "CHILD_TBL_COL_NAME");
		tableList.add(new Table(tableName, primaryColumnNameSet));

		tableName = "TB_ENT_FK_DETAIL_MASTER";
		primaryColumnNameSet = CollectionUtility.createSortedSet("COLUMN_NAME", "TABLE_NAME", "CONSTRAINT_NAME");
		tableList.add(new Table(tableName, primaryColumnNameSet));
		
		return tableList;
	}

	private static String getTruncateScriptContent(List<String> tableNames) {
		StringBuilder truncateScriptBuilder = new StringBuilder();
		for(String tableName: tableNames)
			truncateScriptBuilder.append(getTruncateScriptContent(tableName) + "\n");
		return new String(truncateScriptBuilder);
	}

	private static String getTruncateScriptContent(String tableName) {
		return new String(new StringBuilder().append("TRUNCATE TABLE ").append(tableName).append(";"));
	}

	public static void writeContentToFile(String filePath, String content) throws IOException{
		FileUtility.writeToFile(filePath, content);
	}
	
	private static String getInsertScriptContent(List<String> tableNames) throws IOException {
		StringBuilder builder = new StringBuilder();
		for(String tableName: tableNames){
			builder.append(getInsertScriptContent(tableName) + "\n\n");
		}
		return new String(builder);
	}
	
	private static String getInsertScriptContent(String tableName) throws IOException {
		return FileUtility.readFile(tableName + FILE_EXTENSION);
	}

	private static void generateAndWriteInsertScriptToFile(Set<String> tableNames) throws Exception{
		for(String tableName: tableNames)
			generateAndWriteInsertScriptToFile(tableName);
	}
	
	private static void generateAndWriteInsertScriptToFile(List<Table> tableList) throws Exception{
		for(Table table: tableList)
			generateAndWriteInsertScriptToFile(table);
	}
	
	private static void generateAndWriteInsertScriptToFile(Table table) throws Exception {
		String tableName = table.getName();
		String fileName = null;
		Connection conn = null;
		try {
			conn = DBConnector.getConnection(CONNECTION_ID);
			if (tableName != null) {
				String insertScript = OracleInsertScriptGenerator.getInsertScriptAsString(conn, table);
				FileUtility.writeToFile(tableName + FILE_EXTENSION, insertScript);
				//generateInsertStatements(conn, table);
			} else {
				PrintWriter p = new PrintWriter(new FileWriter("insert_all.sql"));
				p.println("spool insert_all.log");

				BufferedReader r = new BufferedReader(new FileReader(fileName));
				tableName = r.readLine();
				while (tableName != null) {
					p.println(String.format("@%s%s", tableName, FILE_EXTENSION));
					generateInsertStatements(conn, table);
					tableName = r.readLine();
				}
				r.close();

				p.println("spool off");
				p.close();
			}
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	private static void generateAndWriteInsertScriptToFile(String tableName) throws Exception {
		generateAndWriteInsertScriptToFile(new Table(tableName));
	}

	private static void generateInsertStatements(Connection conn, Table table) throws Exception {
		String tableName = table.getName();
		log("Generating Insert statements for: " + tableName);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(table.getSelectSqlForInsertion());
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
		PrintWriter p = new PrintWriter(new FileWriter(tableName + FILE_EXTENSION));
//		p.println("set sqlt off");
//		p.println("set sqlblanklines on");
//		p.println("set define off");
		while (rs.next()) {
			StringBuilder columnValues = new StringBuilder("");
			for (int i = 0; i < numColumns; i++) {
				if (i != 0) {
					columnValues.append(",");
				}

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
					if (d == null)
						d = rs.getTime(i + 1);
				case Types.TIMESTAMP:
					if (d == null)
						d = rs.getTimestamp(i + 1);

					if (d == null) {
						columnValues.append("null");
					} else {
						columnValues.append("TO_DATE('" + DateUtility.ORALCE_DATE_FORMAT.format(d) + "', 'YYYY/MM/DD HH24:MI:SS')");
					}
					break;
				case Types.BLOB:
					Blob blob = rs.getBlob(i + 1);
					if(blob != null)
						System.out.println("not null");
					columnValues.append("null");
					break;				 
				default:
					v = rs.getString(i + 1);
					if (v != null) {
						columnValues.append("'" + v.replaceAll("'", "''") + "'");
					} else {
						columnValues.append("null");
					}
					break;
				}
			}
			p.println(String.format("INSERT INTO %s (%s) values (%s);", tableName, columnNames, columnValues));
		}
		p.close();
	}
	
	private static void log(String s) {
		System.out.println(s);
	}
}