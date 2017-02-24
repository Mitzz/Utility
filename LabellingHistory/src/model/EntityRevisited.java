package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import work.Program;
import db.DbUtility;
import db.DbUtility.DBConnector;

public class EntityRevisited {
	
	public static final int CONNECTION_ID = 3;
	
	public static final List<String> defaultNonHashingColumns =  new ArrayList<String>(){{
		   add("MAKERID");
		   add("MAKER_ID");
		   add("MAKERDATE");
		   add("CHECKERID");
		   add("CHECKERDATE");
	}};
	
	private String entityName;
	private ParentTable parentTable;
	private Table parent;

	public ParentTable getParentTable() {
		return parentTable;
	}

	public EntityRevisited setParentTable(ParentTable parentTable) {
		this.parentTable = parentTable;
		return this;
	}
	
	public EntityRevisited setParent(Table parent) {
		this.parent = parent;
		return this;
	}
	
	public static void main(String[] args) {
        // Generate data
        int arraySize = 32768;
        int data[] = new int[arraySize];

        Random rnd = new Random(0);
        for (int c = 0; c < arraySize; ++c)
            data[c] = rnd.nextInt() % 256;

        // !!! With this, the next loop runs faster
//        Arrays.sort(data);

        // Test
        long start = System.nanoTime();
        long sum = 0;

        for (int i = 0; i < 100000; ++i)
        {
            // Primary loop
            for (int c = 0; c < arraySize; ++c)
            {
                if (data[c] >= 128)
                    sum += data[c];
            }
        }

        System.out.println((System.nanoTime() - start) / 1000000000.0);
        System.out.println("sum = " + sum);
	}

	private static void labelling() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = null; 
				
//			entity	 = enquiryEntity();
//		getTestingQuery("Enquiry", entity);
//		channelEntity();
//		getTestingQuery("Channel");
//		schedulerEntity();
//		getTestingQuery("Scheduler");
//		mlcEntity();
//		getTestingQuery("MessageLifeCycle");
		entity = ruleSetEntity();
		getTestingQuery("RuleSet", entity);
//		messageFormat();
//		getTestingQuery("MessageFormat");
//		globalAttribute();//30
//		getTestingQuery("GlobalAttribute");
//		stageEntity();//30
//		getTestingQuery("Stage");
//		queueEntity();//30
//		getTestingQuery("Queue");
//		sourceEntity();//30
//		getTestingQuery("Source");
//		calendarEntity();//15
//		getTestingQuery("Calendar");
//		listViewMaintenance();//15
//		getTestingQuery("ListViewMaintenance");
//		processTreeEntity();//15
//		getTestingQuery("Process");
//		rulesEntity();//30
//		getTestingQuery("Rules");
//		componentEntity();
//		controlClassEntity();//15
//		getTestingQuery("ControlClass");
//		dataFieldEntity();//15
//		getTestingQuery("DataField");
//		dataClassEntity();
//		getTestingQuery("DataClass");
//		applicationEntity();
//		processEntity();
//		getTestingQuery("Entity");
//		entityEntity();
//		getTestingQuery("Entity");
//		branchEntity();
//		getTestingQuery("Branch");
//		uicEntity();
//		getTestingQuery("Uic");
//		slaEntity();
//		getTestingQuery("Sla");
//		operationEntity();
//		getTestingQuery("Operation");
//		propertyEntity();
//		workspaceEntity();
	}

	private static void workspaceEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getWorkspaceEntityRevisited("Workspace");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getWorkspaceEntityRevisited(String string) {
		EntityRevisited workspaceEntity = new EntityRevisited();
		
		workspaceEntity.setEntityName(string);
		
		workspaceEntity.setParent(
				build("TB_WORKSPACE", "TB_WORKSPACE_HISTORY", "AUDIT_ID", "ACTION", null, 
						Arrays.asList("WORKSPACE_CODE")));
		
		Table parent = workspaceEntity.getParent();
		
		parent.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "ELEMENT_ID", "ENTITY_NAME", "LANG_CODE"),
				new HashMap<String, String>() {{
				    put("WORKSPACE_CODE","ELEMENT_KEY");
				}},
				new HashMap<String, String>() {{
					put("ENTITY_NAME","Workspace");
				}}
		));
		
		Table child = build("TB_WDZ_PAGES", "TB_WDZ_PAGES_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("PAGE_ID", "PARENT_PAGE_ID", "WORKSPACE_CODE"),
				new HashMap<String, String>() {{
				    put("WORKSPACE_CODE","WORKSPACE_CODE");
				}}
		); 
		
		parent.addChild(child);
		child.addChild(build("TB_WDZ_WIDGET_INSTANCE", "TB_WDZ_WIDGET_INSTANCE_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("WIDGET_INSTANCE_CODE", "PAGE_ID", "WORKSPACE_CODE", "WIDGET_DICTIONARY_CODE"),
				new HashMap<String, String>() {{
				    put("WORKSPACE_CODE","WORKSPACE_CODE");
				    put("PAGE_ID","PAGE_ID");
				}}
		));
		
		
		parent.addChild(build("TB_WORKSPACE_EC", "TB_WORKSPACE_EC_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("WORKSPACE_CODE", "GRID_SEQ"),
				new HashMap<String, String>() {{
				    put("WORKSPACE_CODE","WORKSPACE_CODE");
				}}
		));
		
		child = build("TB_MENU", "TB_MENU_HISTORY", "AUDITID", null, 
				Arrays.asList("WORKSPACE_CODE", "MENUID"),
				new HashMap<String, String>() {{
				    put("WORKSPACE_CODE","WORKSPACE_CODE");
				}}
		); 
		
		parent.addChild(child);
		
		child.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "ELEMENT_ID", "ENTITY_NAME", "LANG_CODE"),
				new HashMap<String, String>() {{
				    put("MENUID","VALUE");
				}},
				new HashMap<String, String>() {{
					put("ENTITY_NAME","WORKSPACE_MENU");
				}}
		));
		
		parent.addChild(build("TB_PROCESS_MENU", "TB_PROCESS_MENU_HISTORY", "AUDITID", null, 
				Arrays.asList("WORKSPACE_CODE", "PROCESS_MENU_ID"),
				new HashMap<String, String>() {{
				    put("WORKSPACE_CODE","WORKSPACE_CODE");
				}}
		));
		
		return workspaceEntity;
	}

	private static String getTestingQuery(String entityName, EntityRevisited enquiryEntity) {
		String query = 
				"--Entity Presence Query\nSELECT * FROM TB_ENT_MASTER WHERE ENTITY_NAME = '???';\n" + 
				"--All Parent and Child Master Tables present in DB\n " +
						"Select table_name " +
						"from tb_ent_master " +
						"where entity_name = '???' " +
						"minus " +
						"Select Table_name " +
						"from all_tables " +
						"where table_name in " +
						"(Select table_name " +
						"from tb_ent_master " +
						"where entity_name = '???'); " +
						" \n " +
						"--All Parent and Child Audit Tables present in DB\n " +
						"Select history_table_name " +
						"from tb_ent_master " +
						"where entity_name = '???' " +
						"minus " +
						"Select Table_name " +
						"from all_tables " +
						"where table_name in " +
						"(Select history_table_name " +
						"from tb_ent_master " +
						"where entity_name = '???'); " +
						"  " +
						"\n--Audit Columns Present in DB\n " +
						"Select history_table_name,AUDIT_COLUMN_NAME " +
						"from tb_ent_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select AUDIT_COLUMN_NAME from tb_ent_master where entity_name = '???') " +
						"and table_name in (Select HISTORY_TABLE_NAME from tb_ent_master where entity_name = '???'); " +
						" \n " +
						"--All Parent and Child Master Columns Present in DB\n  " +
						"Select column_name " +
						"from tb_ent_col_master " +
						"where table_name in (Select table_name from tb_ent_master where entity_name = '???') " +
						"minus " +
						"Select column_name " +
						"from all_tab_cols " +
						"where table_name in (Select table_name from tb_ent_master where entity_name = '???'); " +
						" \n " +
						"--All Primary Keys Columns of Parent and Child Master Present in DB\n " +
						"Select Table_name, COLUMN_NAME " +
						"from tb_ent_pk_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select COLUMN_NAME from tb_ent_pk_master where entity_name = '???') " +
						"and TABLE_NAME in (Select Table_name from tb_ent_pk_master where entity_name = '???'); " +
						" \n " +
						"--All Linking Keys Columns of Parent and Child Master Present in DB\n " +
						"Select Table_name, TABLE_COLUMN_NAME " +
						"from tb_ent_detail_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select COLUMN_NAME from tb_ent_detail_master where entity_name = '???') " +
						"and TABLE_NAME in (Select Table_name from tb_ent_detail_master where entity_name = '???'); " +
						" \n " +
						"--All Reference Keys Columns of Parent and Child Master Present in DB\n " +
						"Select CHILD_TABLE_NAME, CHILD_TBL_COL_NAME " +
						"from tb_ent_detail_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select CHILD_TBL_COL_NAME from tb_ent_detail_master where entity_name = '???') " +
						"and TABLE_NAME in (Select CHILD_TABLE_NAME from tb_ent_detail_master where entity_name = '???');\n " ;
		
		query += "--All column from tb_col_master present in history table\n";
		query += new String(validateMasterColumnPresentInAuditTableQuery(enquiryEntity));
		System.out.println(query.replace("???", entityName));
		return query.replace("???", entityName);
	}

	private static void propertyEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getPropertyEntityRevisited("Property");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getPropertyEntityRevisited(String string) {
		EntityRevisited propertyEntity = new EntityRevisited();
		
		propertyEntity.setEntityName(string);
		
		propertyEntity.setParent(
				build("TB_PROPERTY_MASTER", "TB_PROPERTY_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROPERTY_ID")));
		
		Table parent = propertyEntity.getParent();
		
		parent.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "ELEMENT_ID", "ENTITY_NAME", "LANG_CODE"),
				new HashMap<String, String>() {{
				    put("PROPERTY_ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
					put("ENTITY_NAME","PROPERTYDEF");
				}}
		));
		
		parent.addChild(build("TB_PROPERTY_VALUE", "TB_PROPERTY_VALUE_AUDIT", "AUDITID", null, 
				Arrays.asList("PROPERTY_ID", "NODEID"),
				new HashMap<String, String>() {{
				    put("PROPERTY_ID","PROPERTY_ID");
				}}
		));
		
		parent.addChild(build("TB_PROPERTY_LIST_VALUE", "TB_PROPERTY_LIST_VALUE_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("PROPERTY_ID", "SEQ_NO"),
				new HashMap<String, String>() {{
				    put("PROPERTY_ID","PROPERTY_ID");
				}}
		));
		
		return propertyEntity;
	}

	private static void operationEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getOperationEntityRevisited("Operation");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getOperationEntityRevisited(String string) {
		EntityRevisited operationEntity = new EntityRevisited();
		
		operationEntity.setEntityName(string);
		
		operationEntity.setParent(
				build("TB_OPERATION_MASTER", "TB_OPERATION_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("OPERATION_ID")));
		
		Table par = operationEntity.getParent();
		
		par.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "ELEMENT_ID", "ENTITY_NAME", "LANG_CODE"),
				new HashMap<String, String>() {{
				    put("OPERATION_ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
					put("ENTITY_NAME","OPERATION");
				}}
		));
		
		par.addChild(build("OW_ITEMTYPE", "OW_ITEMTYPE_HISTORY", "AUDITID", null, 
				Arrays.asList("ITEMTYPEID"),
				new HashMap<String, String>() {{
				    put("OPERATION_ID","OPERATION_ID");
				}}
		));
		
		return operationEntity;
	}

	private static void slaEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getSlaEntityRevisited("Sla");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getSlaEntityRevisited(String string) {
		EntityRevisited slaEntity = new EntityRevisited();
		
		slaEntity.setEntityName(string);
		
		slaEntity.setParent(
				build("TB_SLA_MASTER", "TB_SLA_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("SLAID")));
		
		Table par = slaEntity.getParent();
		
		par.addChild(build("TB_SLA_ESCALATION_USER", "TB_SLA_ESCALATION_USER_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ID", "ESCALATIONLEVEL", "SLAID", "USERGROUPFLAG"),
				new HashMap<String, String>() {{
				    put("SLAID","SLAID");
				}}
		));
		
		par.addChild(build("TB_SLA_DETAIL_MASTER", "TB_SLA_DETAIL_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SLAID", "ESCALATIONLEVEL"),
				new HashMap<String, String>() {{
				    put("SLAID","SLAID");
				}}
		));
		
		return slaEntity;
	}

	private static void uicEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getUicEntityRevisited("Uic");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getUicEntityRevisited(String string) {
		EntityRevisited uicEntity = new EntityRevisited();
		
		uicEntity.setEntityName(string);
		
		uicEntity.setParent(
				build("TB_UIC_MASTER", "TB_UIC_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("APPLICATIONID", "UIC_ID")));
		
		Table par = uicEntity.getParent();
		
		par.addChild(build("TB_TRANSACTION_TABLES_MASTER", "TB_TRANSACTION_TABLES_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("TRANSACTION_TABLE_NAME", "ENC_ID", "ENC_TYPE"),
				new HashMap<String, String>() {{
				    put("UIC_ID","ENC_ID");
				}}
		));
		
		par.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("PRODUCT_DOCTYPE_FLAG", "SEQ_NO", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "ELEMENT_ID", "ENTITY_PROPERTY", "PRODUCT_DOCTYPE_ID", "ENTITY_NAME", "LANG_CODE"),
				new HashMap<String, String>() {{
				    put("UIC_ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
					put("ENTITY_NAME","UIC");
				}}
		));

		par.addChild(build("TB_MASTER_TABLES_MASTER", "TB_MASTER_TABLES_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ENC_ID", "MASTER_TABLE_NAME", "ENC_TYPE"),
				new HashMap<String, String>() {{
				    put("UIC_ID","ENC_ID");
				}}
		));
		
		par.addChild(build("TB_UIC_DTL_MASTER", "TB_UIC_DTL_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("UIC_ID", "CHILD_UIC_ID"),
				new HashMap<String, String>() {{
				    put("UIC_ID","UIC_ID");
				}}
		));
		
		par.addChild(build("TB_LINKED_FIELD_MASTER", "TB_LINKED_FIELD_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ENC_ID", "FIELD_ID", "ENC_TYPE", "ELEMENT_TYPE", "CONTROL_ID"),
				new HashMap<String, String>() {{
				    put("UIC_ID","ENC_ID");
				}}
		));
		
		return uicEntity;
	}

	private static void branchEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getBranchEntityRevisited("Branch");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getBranchEntityRevisited(String string) {
		EntityRevisited branchEntity = new EntityRevisited();
		
		branchEntity.setEntityName(string);
		
		branchEntity.setParent(
				build("TB_BRANCH", "TB_BRANCH_HISTORY", "AUDITID", null, 
						Arrays.asList("BRANCH_CODE", "ENTITY_CODE", "COUNTRY_CODE")));
		
		return branchEntity;
	}

	private static void entityEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getEntityEntityRevisited("Entity");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getEntityEntityRevisited(String string) {
		EntityRevisited entityEntity = new EntityRevisited();
		
		entityEntity.setEntityName(string);
		
		entityEntity.setParent(
				build("TB_ENTITY", "TB_ENTITY_HISTORY", "AUDITID", null, 
						Arrays.asList("ENTITY_CODE")));
		
		Table par = entityEntity.getParent();
		
		par.addChild(build("TB_ENTITY_COUNTRY", "TB_ENTITY_COUNTRY_HISTORY", "AUDITID", null, 
				Arrays.asList("ENTITY_CODE", "COUNTRY_CODE"),
				new HashMap<String, String>() {{
				    put("ENTITY_CODE","ENTITY_CODE");
				}}
		));
		
		return entityEntity;

	}

	private static void applicationEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getApplicationEntityRevisited("Application");
		entity.populateColumnDetailsRevisited();
		Program.insertIntoTbColumnMasterRevisited(DBConnector.getConnection(CONNECTION_ID) , entity);
		
	}

	private static EntityRevisited getApplicationEntityRevisited(String string) {
		EntityRevisited dataClassEntity = new EntityRevisited();
		
		dataClassEntity.setEntityName(string);
		
		dataClassEntity.setParent(
				build("OW_APPLICATION", "TB_DATACLASS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ID", "ID_TYPE")));
		
		Table par = dataClassEntity.getParent();
		
		par.addChild(build("TB_APP_INFO", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","DATACLASS");
				}}
		));
		
		par.addChild(build("TB_APP_DEPENDENCIES", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","DATACLASS");
				}}
		));
		
		par.addChild(build("TB_APP_ENTITY_DETAIL", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","DATACLASS");
				}}
		));
		
		return dataClassEntity;
	}
	

	private static void dataClassEntity() throws ClassNotFoundException, SQLException {
		Connection connector = DBConnector.getConnection(CONNECTION_ID);
		
		EntityRevisited entity = getDataClassEntityRevisited("DataClass");
		entity.populateColumnDetailsRevisited();
		
		detectExtraMasterColumnsRevisited(connector, entity);
		if(validateRevisited(entity)){
			processLabellingModuleRevisited(connector, entity);
			
			//displayTablesRevisited(entity);
			
			//testingQueryRevisited(entity);
		}
	}

	private static EntityRevisited getDataClassEntityRevisited(String string) {
		EntityRevisited dataClassEntity = new EntityRevisited();
		
		dataClassEntity.setEntityName(string);
		
		dataClassEntity.setParent(
				build("TB_DATACLASS_MASTER", "TB_DATACLASS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ID", "ID_TYPE")));
		
		Table par = dataClassEntity.getParent();
		
		par.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","DATACLASS");
				}}
		));
		return dataClassEntity;
	}

	private static void dataFieldEntity() throws ClassNotFoundException, SQLException {
		Connection connector = DBConnector.getConnection(CONNECTION_ID);
		
		EntityRevisited entity = getDataFieldEntity("DataField");
		entity.populateColumnDetailsRevisited();
		
		detectExtraMasterColumnsRevisited(connector, entity);
		if(validateRevisited(entity)){
			processLabellingModuleRevisited(connector, entity);
			
			displayTablesRevisited(entity);
			
			testingQueryRevisited(entity);
		}
	}

	private static void displayTablesRevisited(EntityRevisited entity) {
		List<String> tableNames = Program.getTableNamesRevisited(entity);
		System.out.println(Program.getDBQuery(tableNames));
		
		tableNames = Program.getHistoryTableNamesRevisited(entity);
		System.out.println(Program.getDBQuery(tableNames));
	}

	private static void processLabellingModuleRevisited(Connection connector, EntityRevisited entity) {
		System.out.println(String.format("--Start Entity: %s", entity.getEntityName()));
		Program.displayLinkTableData(connector, entity);
		System.out.println();
		Program.insertIntoTbEntMasterRevisited(connector, entity);
		System.out.println();
		Program.insertIntoTbColumnMasterRevisited(connector, entity);
		System.out.println();
		Program.insertIntoTbEntityPrimaryMasterRevisited(connector, entity);
		System.out.println();
		Program.insertIntoTbEntDetailMasterRevisited(connector, entity);
		
		System.out.println(String.format("--End Entity: %s", entity.getEntityName()));
		
	}

	private static EntityRevisited getDataFieldEntity(String name) {
		EntityRevisited dataFieldEntity = new EntityRevisited();
		
		dataFieldEntity.setEntityName(name);
		
		dataFieldEntity.setParent(
				build("TB_DATAFIELD_MASTER", "TB_DATAFIELD_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("FIELD_ID")));
		
		Table par = dataFieldEntity.getParent();
		
		par.addChild(build("TB_TRANSACTION_TABLES_MASTER", "TB_TRANSACTION_TABLES_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ENC_ID", "ENC_TYPE", "TRANSACTION_TABLE_NAME"), 
						new HashMap<String, String>() {{
						    put("FIELD_ID","ENC_ID");
						}},
						new HashMap<String, String>() {{
						    put("ENC_TYPE","G");
						}}
				));
		
		par.addChild(build("TB_DF_CC_MAPPING", "TB_DF_CC_MAPPING_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("FIELD_ID", "CC_ID"), 
				new HashMap<String, String>() {{
				    put("FIELD_ID","FIELD_ID");
				}}
		));
		
		par.addChild(build("TB_MASTER_TABLES_MASTER", "TB_MASTER_TABLES_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ENC_ID", "ENC_TYPE", "MASTER_TABLE_NAME"), 
				new HashMap<String, String>() {{
				    put("FIELD_ID","ENC_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENC_TYPE","G");
				}}
		));
		
		par.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("FIELD_ID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","DATAFIELDDEF");
				}}
		));
		
		par.addChild(build("TB_CONTROLCLASS_MASTER", "TB_CONTROLCLASS_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ID", "ID_TYPE"),
				new HashMap<String, String>() {{
				    put("FIELD_ID","ID");
				}},
				new HashMap<String, String>() {{
				    put("ID_TYPE","DF");
				}}
		));
		
		par.addChild(build("TB_DATACLASS_MASTER", "TB_DATACLASS_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ID", "ID_TYPE"),
				new HashMap<String, String>() {{
				    put("FIELD_ID","ID");
				}},
				new HashMap<String, String>() {{
				    put("ID_TYPE","DF");
				}}
		));
		
		par.addChild(build("TB_GRID_FIELD_MASTER", "TB_GRID_FIELD_HISTORY", "AUDITID", null,
				Arrays.asList("CONTROL_OR_FIELD_ID", "FIELD_ID"),
				new HashMap<String, String>() {{
				    put("FIELD_ID","CONTROL_OR_FIELD_ID");
				}}
		));
		
		Table ch =build("TB_COMPONENT", "TB_COMPONENT_HISTORY", "AUDIT_ID", null,
				Arrays.asList("APP_ID", "COMP_CODE"),
				new HashMap<String, String>() {{
				    put("FIELD_CODE","COMP_CODE");
				}}
		);
		
		par.addChild(ch);
		
		ch.addChild(build("TB_COMPONENT_PARAM", "TB_COMPONENT_PARAM_HISTORY", "AUDIT_ID", null,
				Arrays.asList("APP_ID", "COMP_CODE", "PARAM_NAME"),
				new HashMap<String, String>() {{
				    put("APP_ID","APP_ID");
				    put("COMP_CODE","COMP_CODE");
				}}
		));
		
				
		return dataFieldEntity;
	}

	private static Table build(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, List<String> primaryColumns) {
		return build(tableName, historyTableName, auditColumnName, nonHashingColumns, primaryColumns, new HashMap<String, String>(), new HashMap<String, String>());
	}
	
	private static Table build(String tableName, String historyTableName, String auditColumnName, String actionColumnName, List<String> nonHashingColumns, List<String> primaryColumns) {
		return build(tableName, historyTableName, auditColumnName, actionColumnName, nonHashingColumns, primaryColumns, new HashMap<String, String>(), new HashMap<String, String>());
	}
	
	private static Table build(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, List<String> primaryColumns, Map<String, String> foreignColumns) {
		return build(tableName, historyTableName, auditColumnName, nonHashingColumns, primaryColumns, foreignColumns, new HashMap<String, String>());
	}
	
	private static Table build(String tableName, String historyTableName, String auditColumnName, String actionColumnName, List<String> nonHashingColumns, List<String> primaryColumns, Map<String, String> foreignColumns, Map<String, String> fixedValue) {
		Table table = new Table();
		String childTableName = tableName;
		String childHistoryName = historyTableName;
		String childAuditColumnName = auditColumnName;

		table.setName(childTableName);
		table.setHistoryName(childHistoryName);
		table.setAuditColumnName(childAuditColumnName);
		table.setNonHashingColumnNames(nonHashingColumns == null ? defaultNonHashingColumns : nonHashingColumns);
		table.setPrimaryColumnNames(primaryColumns);
		table.setForeignColumnMapping(foreignColumns);
		table.setColumnNameFixedValueMap(fixedValue);
		table.setActionColumnName(actionColumnName);
		return table;
	}
	
	private static Table build(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, List<String> primaryColumns, Map<String, String> foreignColumns, Map<String, String> fixedValue) {
		Table table = new Table();
		String childTableName = tableName;
		String childHistoryName = historyTableName;
		String childAuditColumnName = auditColumnName;

		table.setName(childTableName);
		table.setHistoryName(childHistoryName);
		table.setAuditColumnName(childAuditColumnName);
		table.setNonHashingColumnNames(nonHashingColumns == null ? defaultNonHashingColumns : nonHashingColumns);
		table.setPrimaryColumnNames(primaryColumns);
		table.setForeignColumnMapping(foreignColumns);
		table.setColumnNameFixedValueMap(fixedValue);
		return table;
	}

	private static void componentEntity() throws ClassNotFoundException, SQLException {
		Connection connector = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		
		EntityRevisited entity = getComponentEntity("Component");
		entity.populateColumnDetails();
		
		detectExtraMasterColumns(connector, entity);
		if(validate(entity)){
			processLabellingModule(connector, entity);
			
			displayTables(entity);
			
			testingQuery(entity);
			
			validateMasterColumnPresentInAuditTableQuery(entity);
		}
	}

	private static String validateMasterColumnPresentInAuditTableQuery(EntityRevisited entity) {
		StringBuilder query = validateQuery(entity.getParent());
		return new String(query);
	}

	private static StringBuilder validateQuery(Table parent) {
		StringBuilder query = new StringBuilder();
		query.append("Select 'SELECT COLUMN_NAME from tb_ent_col_master where table_name = ''' || table_name || ''' MINUS Select COLUMN_NAME from all_tab_columns where TABLE_NAME = ''' || history_table_name || ''';' from tb_ent_master where entity_name = '???';");
//		query.append(String.format("Select COLUMN_NAME from tb_ent_col_master where table_name = '%s' MINUS Select COLUMN_NAME from all_tab_columns where TABLE_NAME = '%s';\n", parent.getName(), parent.getHistoryName()));
//		for(Table child: parent.getChilds()){
//			query.append(validateQuery(child));
//		}
		return query;
	}

	private static EntityRevisited getComponentEntity(String name) {
		EntityRevisited componentEntity = new EntityRevisited();
		
		componentEntity.setEntityName(name);
		componentEntity.setParentTable(
				buildParent("TB_COMPONENT", "TB_COMPONENT_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("APP_ID", "COMP_CODE")));
		
		ParentTable parent = componentEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("TB_COMPONENT_PARAM", "TB_COMPONENT_PARAM_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("APP_ID", "COMP_CODE", "PARAM_NAME"), 
					Arrays.asList("APP_ID", "COMP_CODE")));
		
		return componentEntity;
		
	}

	private static void controlClassEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getControlClassEntityRevisited("ControlClass");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getControlClassEntityRevisited(String name) {
		EntityRevisited controlClassEntity = new EntityRevisited();
		
		controlClassEntity.setEntityName(name);
//		controlClassEntity.setParentTable(
//				buildParent("TB_CONTROLCLASS_MASTER", "TB_CONTROLCLASS_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("ID", "ID_TYPE")));
		
		controlClassEntity.setParent(
				build("TB_CONTROLCLASS_MASTER", "TB_CONTROLCLASS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ID", "ID_TYPE")));
		
//		ParentTable parent = controlClassEntity.getParentTable();
		Table parent = controlClassEntity.getParent();
		
//		parent.addChildTable(buildChild("TB_TRANSACTION_TABLES_MASTER", "TB_TRANSACTION_TABLES_HISTORY", "AUDIT_ID", null, 
//				Arrays.asList("ENC_ID", "ENC_TYPE", "TRANSACTION_TABLE_NAME"), 
//				Arrays.asList("ENC_ID")));
//		
		parent.addChild(build("TB_TRANSACTION_TABLES_MASTER", "TB_TRANSACTION_TABLES_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ENC_ID", "ENC_TYPE", "TRANSACTION_TABLE_NAME"),
				new HashMap<String, String>() {{
				    put("ID","ENC_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENC_TYPE","G");
				}}
			));
		
//		parent.addChildTable(
//				buildChild("TB_MASTER_TABLES_MASTER", "TB_MASTER_TABLES_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("ENC_ID", "ENC_TYPE", "MASTER_TABLE_NAME"), 
//					Arrays.asList("ENC_ID")));

		parent.addChild(
				build("TB_MASTER_TABLES_MASTER", "TB_MASTER_TABLES_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("ENC_ID", "ENC_TYPE", "MASTER_TABLE_NAME"), 
					new HashMap<String, String>() {{
					    put("ID","ENC_ID");
					}},
					new HashMap<String, String>() {{
					    put("ENC_TYPE","G");
					}}
				));

//		ChildTable child = buildChild("TB_COMPONENT", "TB_COMPONENT_HISTORY", "AUDIT_ID", null, 
//				Arrays.asList("APP_ID", "COMP_CODE"), 
//				Arrays.asList("COMP_CODE"));
//		
		Table child = build("TB_COMPONENT", "TB_COMPONENT_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("APP_ID", "COMP_CODE"),
				new HashMap<String, String>() {{
				    put("ID","COMP_CODE");
				}}
			);
		
//		parent.addChildTable(child);
		
		parent.addChild(child);
//		child.addGrandChildTable(
//				buildGrandChild("TB_COMPONENT_PARAM", "TB_COMPONENT_PARAM_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("APP_ID", "COMP_CODE", "PARAM_NAME"), 
//					Arrays.asList("APP_ID", "COMP_CODE")));
//		
		child.addChild(
				build("TB_COMPONENT_PARAM", "TB_COMPONENT_PARAM_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("APP_ID", "COMP_CODE", "PARAM_NAME"),
					new HashMap<String, String>() {{
					    put("APP_ID","APP_ID");
					    put("COMP_CODE","COMP_CODE");
					}}
				));		
		
//		parent.addChildTable(
//				buildChild("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"), 
//					Arrays.asList("ELEMENT_ID")));
//		
		parent.addChild(
				build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
					new HashMap<String, String>() {{
					    put("ID","ELEMENT_ID");
					}},
					new HashMap<String, String>() {{
					    put("ENTITY_NAME","CONTROLCLASS");
					}}
				));
		
		
		return controlClassEntity;
	}

	private static EntityRevisited getControlClassEntity(String name) {
		EntityRevisited controlClassEntity = new EntityRevisited();
		
		controlClassEntity.setEntityName(name);
		controlClassEntity.setParentTable(
				buildParent("TB_CONTROLCLASS_MASTER", "TB_CONTROLCLASS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ID", "ID_TYPE")));
		
		ParentTable parent = controlClassEntity.getParentTable();
		
		parent.addChildTable(buildChild("TB_TRANSACTION_TABLES_MASTER", "TB_TRANSACTION_TABLES_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ENC_ID", "ENC_TYPE", "TRANSACTION_TABLE_NAME"), 
				Arrays.asList("ENC_ID")));
		
		parent.addChildTable(
				buildChild("TB_MASTER_TABLES_MASTER", "TB_MASTER_TABLES_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("ENC_ID", "ENC_TYPE", "MASTER_TABLE_NAME"), 
					Arrays.asList("ENC_ID")));
		
		ChildTable child = buildChild("TB_COMPONENT", "TB_COMPONENT_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("APP_ID", "COMP_CODE"), 
				Arrays.asList("COMP_CODE"));
		parent.addChildTable(child);
		
		child.addGrandChildTable(
				buildGrandChild("TB_COMPONENT_PARAM", "TB_COMPONENT_PARAM_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("APP_ID", "COMP_CODE", "PARAM_NAME"), 
					Arrays.asList("APP_ID", "COMP_CODE")));
		
		parent.addChildTable(
				buildChild("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"), 
					Arrays.asList("ELEMENT_ID")));
		
		
		return controlClassEntity;
		
	}

	private static void rulesEntity() throws ClassNotFoundException, SQLException {
		
		EntityRevisited entity = getRulesEntityRevisited("Rules");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getRulesEntityRevisited(String name) {
		EntityRevisited rulesEntity = new EntityRevisited();
		
		rulesEntity.setEntityName(name);
//		rulesEntity.setParentTable(
//				buildParent("TB_RULES_MASTER", "TB_RULES_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("ENC_ID", "RULE_ID" ,"ENC_TYPE")));
		
		rulesEntity.setParent(
				build("TB_RULES_MASTER", "TB_RULES_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ENC_ID", "RULE_ID" ,"ENC_TYPE")));
		
		
//		ParentTable parent = rulesEntity.getParentTable();	
		Table parent = rulesEntity.getParent();	
		
//		ChildTable child = buildChild("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
//				Arrays.asList("RULE_ID", "RULE_SET_ID"), 
//				Arrays.asList("RULE_SET_ID", "RULE_ID"));
		
		Table child = build("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_SET_ID"),
				new HashMap<String, String>() {{
				    put("ENC_ID","RULE_SET_ID");
				    put("RULE_ID","RULE_ID");
				}}
			);
		
		parent.addChild(child);
		
//		child.addGrandChildTable(buildGrandChild("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
//				Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"),
//				Arrays.asList("RULE_ID", "ENTITY_ID")));
		
		child.addChild(build("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_ID","RULE_ID");
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
//		child.addGrandChildTable(buildGrandChild("PH_RULE_VARIABLES_MASTER", "PH_RULE_VARIABLES_AUDIT", "AUDIT_ID", null, 
//				Arrays.asList("RULE_ID", "RULE_VARIABLE_ID", "ENTITY_ID"),
//				Arrays.asList("RULE_ID", "ENTITY_ID")));
//		
		child.addChild(build("PH_RULE_VARIABLES_MASTER", "PH_RULE_VARIABLES_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_VARIABLE_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_ID","RULE_ID");
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
//		child.addGrandChildTable(buildGrandChild("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
//				Arrays.asList("RULE_NODE_ID", "ENTITY_ID"),
//				Arrays.asList("RULE_NODE_ID")));
//		
		Table grandChild = build("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_NODE_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("ROOT_NODE_ID","RULE_NODE_ID");
				}}
			);
		child.addChild(grandChild);
		
//		child.addGrandChildTable(buildGrandChild("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
//				Arrays.asList("EXPRESSION_ID", "ENTITY_ID"),
//				Arrays.asList("RULE_NODE_ID", "ENTITY_ID")));

		grandChild.addChild(build("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
				Arrays.asList("EXPRESSION_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_NODE_ID","RULE_NODE_ID");
				    put("ENTITY_ID","ENTITY_ID");
				}}
			));
		
		return rulesEntity;
	}

	private static EntityRevisited getRulesEntity(String name) {
		EntityRevisited rulesEntity = new EntityRevisited();
		
		rulesEntity.setEntityName(name);
		rulesEntity.setParentTable(
				buildParent("TB_RULES_MASTER", "TB_RULES_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ENC_ID", "RULE_ID" ,"ENC_TYPE")));
		
		ParentTable parent = rulesEntity.getParentTable();
		
		ChildTable child = buildChild("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_SET_ID"), 
				Arrays.asList("RULE_SET_ID", "RULE_ID"));
		parent.addChildTable(child);
		
		child.addGrandChildTable(buildGrandChild("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"),
				Arrays.asList("RULE_ID", "ENTITY_ID")));
		
		child.addGrandChildTable(buildGrandChild("PH_RULE_VARIABLES_MASTER", "PH_RULE_VARIABLES_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_VARIABLE_ID", "ENTITY_ID"),
				Arrays.asList("RULE_ID", "ENTITY_ID")));
		
		child.addGrandChildTable(buildGrandChild("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_NODE_ID", "ENTITY_ID"),
				Arrays.asList("RULE_NODE_ID")));
		
		child.addGrandChildTable(buildGrandChild("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
				Arrays.asList("EXPRESSION_ID", "ENTITY_ID"),
				Arrays.asList("RULE_NODE_ID", "ENTITY_ID")));

		return rulesEntity;
	}

	private static void processTreeEntity() throws ClassNotFoundException, SQLException {
		Connection connector = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		
		EntityRevisited entity = getProcessTreeEntityRevisited("ProcessTree");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getProcessTreeEntityRevisited(String string) {
		EntityRevisited processTreeEntity = new EntityRevisited();
		
		processTreeEntity.setEntityName(string);
//		processTreeEntity.setParentTable(
//				buildParent("OW_MENUTREE", "OW_MENUTREE_HISTORY", "AUDITID", null, 
//						Arrays.asList("ID")));
		processTreeEntity.setParent(
				build("OW_MENUTREE", "OW_MENUTREE_HISTORY", "AUDITID", null, 
						Arrays.asList("ID")));
		
//		ParentTable parent = processTreeEntity.getParentTable();
		
		Table parent = processTreeEntity.getParent();
		
//		parent.addChildTable(
//				buildChild("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("ENTITY_PROPERTY", "ENTITY_NAME"), 
//						Arrays.asList("VALUE")));

		parent.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("ID","VALUE");
				}}
			));

		
		return processTreeEntity;

	}

	private static EntityRevisited getProcessTreeEntity(String name) {
		EntityRevisited processTreeEntity = new EntityRevisited();
		
		processTreeEntity.setEntityName(name);
		processTreeEntity.setParentTable(
				buildParent("OW_MENUTREE", "OW_MENUTREE_HISTORY", "AUDITID", null, 
						Arrays.asList("ID")));
		
		ParentTable parent = processTreeEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ENTITY_PROPERTY", "ENTITY_NAME"), 
						Arrays.asList("VALUE")));

		return processTreeEntity;
		
	}

	private static void listViewMaintenance() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getListViewMaintenanceEntityRevisited("ListViewMaintenance");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getListViewMaintenanceEntityRevisited(String name) {
		EntityRevisited listViewMaintenanceEntity = new EntityRevisited();
		
		listViewMaintenanceEntity.setEntityName(name);
		
//		listViewMaintenanceEntity.setParentTable(
//				buildParent("TB_LISTVIEW", "TB_LISTVIEW_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("LISTVIEW_ID")));
		
		listViewMaintenanceEntity.setParent(
				build("TB_LISTVIEW", "TB_LISTVIEW_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID")));
		
//		ParentTable parent = listViewMaintenanceEntity.getParentTable();
		
		Table parent = listViewMaintenanceEntity.getParent();

//		parent.addChildTable(
//				buildChild("TB_LISTVIEW_QUEUE_MAP", "TB_LISTVIEW_QUEUE_MAP_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("LISTVIEW_ID", "TASK_ID", "QUEUE_ID"), 
//						Arrays.asList("LISTVIEW_ID")));
		
		parent.addChild(
				build("TB_LISTVIEW_QUEUE_MAP", "TB_LISTVIEW_QUEUE_MAP_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID", "TASK_ID", "QUEUE_ID"), 
						new HashMap<String, String>() {{
						    put("LISTVIEW_ID","LISTVIEW_ID");
						}}
					));
		
		
//		parent.addChildTable(
//				buildChild("TB_LISTVIEW_FIELDS", "TB_LISTVIEW_FIELDS_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("LISTVIEW_ID", "FIELD_ID", "FIELD_CATEGORY"), 
//						Arrays.asList("LISTVIEW_ID")));
//		
		parent.addChild(
				build("TB_LISTVIEW_FIELDS", "TB_LISTVIEW_FIELDS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID", "FIELD_ID", "FIELD_CATEGORY"), 
						new HashMap<String, String>() {{
						    put("LISTVIEW_ID","LISTVIEW_ID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("TB_LISTVIEW_SORTING", "TB_LISTVIEW_SORTING_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("LISTVIEW_ID", "FIELD_ID"), 
//						Arrays.asList("LISTVIEW_ID")));
		
		parent.addChild(
				build("TB_LISTVIEW_SORTING", "TB_LISTVIEW_SORTING_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID", "FIELD_ID"), 
						new HashMap<String, String>() {{
						    put("LISTVIEW_ID","LISTVIEW_ID");
						}}
				));
		
		return listViewMaintenanceEntity;
	}

	private static EntityRevisited getListViewMaintenanceEntity(String name) {
		EntityRevisited listViewMaintenanceEntity = new EntityRevisited();
		
		listViewMaintenanceEntity.setEntityName(name);
		listViewMaintenanceEntity.setParentTable(
				buildParent("TB_LISTVIEW", "TB_LISTVIEW_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID")));
		
		ParentTable parent = listViewMaintenanceEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("TB_LISTVIEW_QUEUE_MAP", "TB_LISTVIEW_QUEUE_MAP_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID", "TASK_ID", "QUEUE_ID"), 
						Arrays.asList("LISTVIEW_ID")));
		
		parent.addChildTable(
				buildChild("TB_LISTVIEW_FIELDS", "TB_LISTVIEW_FIELDS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID", "FIELD_ID", "FIELD_CATEGORY"), 
						Arrays.asList("LISTVIEW_ID")));
		
		parent.addChildTable(
				buildChild("TB_LISTVIEW_SORTING", "TB_LISTVIEW_SORTING_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("LISTVIEW_ID", "FIELD_ID"), 
						Arrays.asList("LISTVIEW_ID")));
		
		return listViewMaintenanceEntity;
	}

	private static void calendarEntity() throws ClassNotFoundException, SQLException {
		Connection connector = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		
		EntityRevisited entity = getCalendarEntityRevisited("Calendar");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getCalendarEntityRevisited(String name) {
		EntityRevisited calendarEntity = new EntityRevisited();
		
		calendarEntity.setEntityName(name);
//		calendarEntity.setParentTable(
//				buildParent("OW_CALENDAR", "OW_CALENDAR_HISTORY", "AUDITID", null, 
//						Arrays.asList("CALENDAR_CODE")));
		
		calendarEntity.setParent(
				build("OW_CALENDAR", "OW_CALENDAR_HISTORY", "AUDITID", null, 
						Arrays.asList("CALENDAR_CODE")));
		
		Table parent = calendarEntity.getParent();
		
//		parent.addChildTable(
//				buildChild("OW_CALENDAR_DETAILS", "OW_CALENDAR_DETAILS_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("CALENDAR_CODE", "DAY"), 
//						Arrays.asList("CALENDAR_CODE")));
		
		parent.addChild(
				build("OW_CALENDAR_DETAILS", "OW_CALENDAR_DETAILS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CALENDAR_CODE", "DAY"), 
						new HashMap<String, String>() {{
						    put("CALENDAR_CODE","CALENDAR_CODE");
						}}
				));
		
//		parent.addChildTable(
//				buildChild("OW_HOLIDAY", "OW_HOLIDAY_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("CALENDAR_CODE", "HOLIDAY_DATE"), 
//						Arrays.asList("CALENDAR_CODE")));
		
		parent.addChild(
				build("OW_HOLIDAY", "OW_HOLIDAY_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CALENDAR_CODE", "HOLIDAY_DATE"),
//						Arrays.asList("CALENDAR_CODE"),
						new HashMap<String, String>() {{
						    put("CALENDAR_CODE","CALENDAR_CODE");
						}}
				));
		
		return calendarEntity;
	}

	private static EntityRevisited getCalendarEntity(String name) {
		EntityRevisited calendarEntity = new EntityRevisited();
		
		calendarEntity.setEntityName(name);
		calendarEntity.setParentTable(
				buildParent("OW_CALENDAR", "OW_CALENDAR_HISTORY", "AUDITID", null, 
						Arrays.asList("CALENDAR_CODE")));
		
		ParentTable parent = calendarEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("OW_CALENDAR_DETAILS", "OW_CALENDAR_DETAILS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CALENDAR_CODE", "DAY"), 
						Arrays.asList("CALENDAR_CODE")));
		
		parent.addChildTable(
				buildChild("OW_HOLIDAY", "OW_HOLIDAY_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CALENDAR_CODE", "HOLIDAY_DATE"), 
						Arrays.asList("CALENDAR_CODE")));
		
		return calendarEntity;
	}

	private static void sourceEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getSourceEntityRevisited("Source");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getSourceEntityRevisited(String name) {
		EntityRevisited sourceEntity = new EntityRevisited();
		
		sourceEntity.setEntityName(name);
//		queueEntity.setParentTable(
//				buildParent("OW_SOURCE", "OW_SOURCE_HISTORY", "AUDITID", null, 
//						Arrays.asList("SOURCE_CODE")));
		
		sourceEntity.setParent(
				build("OW_SOURCE", "OW_SOURCE_HISTORY", "AUDITID", null, 
						Arrays.asList("SOURCE_CODE")));
		
		return sourceEntity;
	}

	private static EntityRevisited getSourceEntity(String entityName) {
		EntityRevisited queueEntity = new EntityRevisited();
		
		queueEntity.setEntityName(entityName);
		queueEntity.setParentTable(
				buildParent("OW_SOURCE", "OW_SOURCE_HISTORY", "AUDITID", null, 
						Arrays.asList("SOURCE_CODE")));
		
		return queueEntity;
	}

	private static void queueEntity() throws ClassNotFoundException, SQLException {
		
		EntityRevisited entity = getQueueEntityRevisited("Queue");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getQueueEntityRevisited(String entityName) {
		EntityRevisited queueEntity = new EntityRevisited();
		
		queueEntity.setEntityName(entityName);
//		queueEntity.setParentTable(
//				buildParent("OW_QUEUE", "OW_QUEUE_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSID" , "TASKID", "QUEUEID")));
		
		queueEntity.setParent(
				build("OW_QUEUE", "OW_QUEUE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID" , "TASKID", "QUEUEID")));
		Table parent = queueEntity.getParent();
		parent.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("PROCESSID#_#TASKID#_#QUEUEID","ELEMENT_KEY");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","Queue");
				}}
		));
		
		return queueEntity;
	}

	private static EntityRevisited getQueueEntity(String entityName) {
		EntityRevisited queueEntity = new EntityRevisited();
		
		queueEntity.setEntityName(entityName);
		queueEntity.setParentTable(
				buildParent("OW_QUEUE", "OW_QUEUE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID" , "TASKID", "QUEUEID")));
		
		Table parent = queueEntity.getParent();
		parent.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("PROCESSID#_#TASKID#_#QUEUEID","ELEMENT_KEY");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","Queue");
				}}
		));
		
		return queueEntity;
	}

	private static void stageEntity() throws ClassNotFoundException, SQLException {
		Connection connector = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		
		EntityRevisited entity = getStageEntityRevisited("Stage");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getStageEntityRevisited(String string) {
		EntityRevisited stageEntity = new EntityRevisited();
		
		stageEntity.setEntityName(string);
//		stageEntity.setParentTable(
//				buildParent("OW_TASK", "OW_TASK_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSID" , "TASKID")));
//		
		
		stageEntity.setParent(
				build("OW_TASK", "OW_TASK_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID" , "TASKID")));
		
		Table parent = stageEntity.getParent();
		parent.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("PROCESSID#_#TASKID","ELEMENT_KEY");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","Task");
				}}
		));
		
		return stageEntity;
	}

	private static void getMultiLang(EntityRevisited entity, boolean b) {
		StringBuilder builder = new StringBuilder();
		String entityName = entity.getEntityName();
		builder.append("Insert into TB_ENT_MASTER (ENTITY_NAME,TABLE_NAME,IS_PRIMARY,HISTORY_TABLE_NAME,TABLE_CAPTION,AUDIT_COLUMN_NAME) values ('" + entityName + "','TB_MULTILANG_MASTER','N','TB_MULTILANG_HISTORY','Multi Lang','AUDIT_ID');\n\n");
		
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','ENTITY_NAME','1','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','SEQ_NO','4','NUMBER');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','PRODUCT_DOCTYPE_ID','5','NUMBER');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','ELEMENT_KEY','8','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','CONTROL_ID','11','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','ELEMENT_TYPE','12','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','LANG_CODE','3','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','PRODUCT_DOCTYPE_FLAG','6','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','ENTITY_PROPERTY','7','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','GRIDID','9','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','GRIDROWNO','10','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','STATUS','13','VARCHAR2');\n");
		builder.append("Insert into TB_ENT_PK_MASTER (ENTITY_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_SEQUENCE,COLUMN_DATA_TYPE) values ('" + entityName + "','TB_MULTILANG_MASTER','ELEMENT_ID','2','NUMBER');\n\n");
		
		builder.append("Insert into TB_ENT_DETAIL_MASTER (ENTITY_NAME,TABLE_NAME,TABLE_COLUMN_NAME,CHILD_TABLE_NAME,CHILD_TBL_COL_NAME,CHILD_FIXED_VALUE,GROUP_NUMBER) values ('" + entityName + "','OW_PROCESS','PROCESSID','TB_MULTILANG_MASTER','ELEMENT_ID',null,22);\n");
		builder.append("Insert into TB_ENT_DETAIL_MASTER (ENTITY_NAME,TABLE_NAME,TABLE_COLUMN_NAME,CHILD_TABLE_NAME,CHILD_TBL_COL_NAME,CHILD_FIXED_VALUE,GROUP_NUMBER) values ('" + entityName + "',null,null,'TB_MULTILANG_MASTER','ENTITY_NAME','" + entityName + "',22);\n");
		
		System.out.println(builder);
	}

	private static EntityRevisited getStageEntity(String entityName) {
		EntityRevisited stageEntity = new EntityRevisited();
		
		stageEntity.setEntityName(entityName);
		stageEntity.setParentTable(
				buildParent("OW_TASK", "OW_TASK_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID" , "TASKID")));
		
		/*ParentTable parent = globalAttributeEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("PH_GLOBAL_ATTR_ARG_MASTER", "PH_GLOBAL_ATTR_ARG_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("GLOBAL_ATTRIBUTE_ID", "ARGUMENT_NAME"), 
						Arrays.asList("GLOBAL_ATTRIBUTE_ID")));
		*/
		
		
		return stageEntity;
	}

	private static void processEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getProcessEntityRevisited("Process");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}
	
	private static EntityRevisited getProcessEntity(String name) {
		EntityRevisited processEntity = new EntityRevisited();
		
		processEntity.setEntityName(name);
		processEntity.setParentTable(
				buildParent("OW_PROCESS", "OW_PROCESS_HISTORY", "AUDITID", null, 
						Arrays.asList("PROCESSID")));
		
		ParentTable parent = processEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("OW_USER_PROCESSES", "OW_USER_PROCESSES_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("USERID", "PROCESSID"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_PROCESS_OWNER", "OW_PROCESS_OWNER_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ID", "PROCESSID"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_EVENT", "OW_EVENT_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "EVENT"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_ATTRIBUTE", "OW_ATTRIBUTE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ATTRIBUTEID"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_PROCESS_ASSO_ITEM", "OW_PROCESS_ASSO_ITEM_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID" , "ITEMTYPEID"), 
						Arrays.asList("PROCESSID")));
		
		ChildTable child = buildChild("OW_QUEUE", "OW_QUEUE_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("PROCESSID" , "QUEUEID"), 
				Arrays.asList("PROCESSID"));
		//Has Geand CHild
		parent.addChildTable(child);
		
		child.addGrandChildTable(buildGrandChild("OW_QUEUE_LOADBALANCE", "OW_QUEUE_LOADBALANCE_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("PROCESSID" , "QUEUEID", "ITEMTYPEID", "TASKID"), //Guess
				Arrays.asList("PROCESSID" , "QUEUEID")));
		
		child = buildChild("OW_TASK", "OW_TASK_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("TASKID" , "PROCESSID"), 
				Arrays.asList("PROCESSID"));
		//Has GrandChild
		parent.addChildTable(child);
		
			child.addGrandChildTable(buildGrandChild("OW_FORMS", "OW_FORMS_HISTORY", "AUDITID", null, 
					Arrays.asList("TASKID", "ITEMTYPEID", "SEQNO", "PROCESSID"),
					Arrays.asList("TASKID", "PROCESSID")));
			
			child.addGrandChildTable(buildGrandChild("OW_INTERPROCESS_ATTRIBUTE", "OW_INTERPROCESS_ATTRIBUTE_HIST", "AUDIT_ID", null, 
					Arrays.asList("PROCESSFROMID", "PROCESSTOID", "ITEMTYPEFROMID", "ITEMTYPETOID"),//Guess
					Arrays.asList("TASKID")));
			
			child.addGrandChildTable(buildGrandChild("OW_TASK_CHECK", "OW_TASK_CHECK_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKID", "MAKERTASKID"),
					Arrays.asList("TASKID", "PROCESSID")));
			
			child.addGrandChildTable(buildGrandChild("OW_TASK_ROUTELIST", "OW_TASK_ROUTELIST_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKFROMID", "SEQNO"),
					Arrays.asList("TASKFROMID", "PROCESSID")));
			
			child.addGrandChildTable(buildGrandChild("OW_JOIN_NODE", "OW_JOIN_NODE_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKID", "ROWSEQNO", "SEQNO"),
					Arrays.asList("TASKID", "PROCESSID")));
			
			
		parent.addChildTable(
				buildChild("OW_SPLIT_NODE", "OW_SPLIT_NODE_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKFROMID", "RULEID", "SEQNO"),
					Arrays.asList("PROCESSTOID")));
			
		parent.addChildTable(
				buildChild("OW_ITEM_ASSO_TASK", "OW_ITEM_ASSO_TASK_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID" , "TASKID", "ITEMTYPEID"), 
					Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_CONDITION", "OW_CONDITION_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CONDITIONID"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_RULE", "OW_RULE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "RULEID", "SEQNO"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_RULE_VALIDITY", "OW_RULE_VALIDITY_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "RULEID"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_USER_RIGHTS", "OW_USER_RIGHTS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("TASKID","ITEMTYPEID","PROCESSID"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_PROCESS_ROUTELIST", "OW_PROCESS_ROUTELIST_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "SEQNO"), 
						Arrays.asList("PROCESSID")));
		
		parent.addChildTable(
				buildChild("OW_PROCESS_SPLIT_NODE", "OW_PROCESS_SPLIT_NODE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSFROMID", "RULEID", "SEQNO"), 
						Arrays.asList("PROCESSFROMID")));
		
		
		return processEntity;
	}

	private static EntityRevisited getProcessEntityRevisited(String name) {
		EntityRevisited processEntity = new EntityRevisited();
		
		processEntity.setEntityName(name);
		
		
//		processEntity.setParentTable(
//				buildParent("OW_PROCESS", "OW_PROCESS_HISTORY", "AUDITID", null, 
//						Arrays.asList("PROCESSID")));
//		
		processEntity.setParent(
				build("OW_PROCESS", "OW_PROCESS_HISTORY", "AUDITID", null, 
						Arrays.asList("PROCESSID")));
		
//		ParentTable parent = processEntity.getParentTable();
		Table parent = processEntity.getParent();
		
//		parent.addChildTable(
//				buildChild("OW_USER_PROCESSES", "OW_USER_PROCESSES_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("USERID", "PROCESSID"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_USER_PROCESSES", "OW_USER_PROCESSES_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("USERID", "PROCESSID"),
						new HashMap<String, String>() {{
						    put("PROCESSID","PROCESSID");
						}}
				));
		
//		parent.addChildTable(
//				buildChild("OW_PROCESS_OWNER", "OW_PROCESS_OWNER_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("ID", "PROCESSID"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_PROCESS_OWNER", "OW_PROCESS_OWNER_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ID", "PROCESSID"), 
						new HashMap<String, String>() {{
						    put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_EVENT", "OW_EVENT_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSID", "EVENT"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_EVENT", "OW_EVENT_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "EVENT"),
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_ATTRIBUTE", "OW_ATTRIBUTE_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("ATTRIBUTEID"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_ATTRIBUTE", "OW_ATTRIBUTE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ATTRIBUTEID"),
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_PROCESS_ASSO_ITEM", "OW_PROCESS_ASSO_ITEM_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSID" , "ITEMTYPEID"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_PROCESS_ASSO_ITEM", "OW_PROCESS_ASSO_ITEM_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID" , "ITEMTYPEID"), 
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		ChildTable child = buildChild("OW_QUEUE", "OW_QUEUE_HISTORY", "AUDIT_ID", null, 
//				Arrays.asList("PROCESSID" , "QUEUEID"), 
//				Arrays.asList("PROCESSID"));
		
		Table child = build("OW_QUEUE", "OW_QUEUE_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("PROCESSID" , "QUEUEID"),
				new HashMap<String, String>() {{
					put("PROCESSID","PROCESSID");
				}}
				);
		parent.addChild(child);
		
//		child.addGrandChildTable(buildGrandChild("OW_QUEUE_LOADBALANCE", "OW_QUEUE_LOADBALANCE_HISTORY", "AUDIT_ID", null, 
//				Arrays.asList("PROCESSID" , "QUEUEID", "ITEMTYPEID", "TASKID"), //Guess
//				Arrays.asList("PROCESSID" , "QUEUEID")));
		
		child.addChild(build("OW_QUEUE_LOADBALANCE", "OW_QUEUE_LOADBALANCE_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("PROCESSID" , "QUEUEID", "ITEMTYPEID", "TASKID"), //Guess
				new HashMap<String, String>() {{
					put("PROCESSID","PROCESSID");
					put("QUEUEID","QUEUEID");
				}}
			));
		
//		child = buildChild("OW_TASK", "OW_TASK_HISTORY", "AUDIT_ID", null, 
//				Arrays.asList("TASKID" , "PROCESSID"), 
//				Arrays.asList("PROCESSID"));
		
		child = build("OW_TASK", "OW_TASK_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("TASKID" , "PROCESSID"),
				new HashMap<String, String>() {{
					put("PROCESSID","PROCESSID");
				}}
			);
		//Has GrandChild
		parent.addChild(child);
		
//			child.addGrandChildTable(buildGrandChild("OW_FORMS", "OW_FORMS_HISTORY", "AUDITID", null, 
//					Arrays.asList("TASKID", "ITEMTYPEID", "SEQNO", "PROCESSID"),
//					Arrays.asList("TASKID", "PROCESSID")));
			
			child.addChild(build("OW_FORMS", "OW_FORMS_HISTORY", "AUDITID", null, 
					Arrays.asList("TASKID", "ITEMTYPEID", "SEQNO", "PROCESSID"),
					new HashMap<String, String>() {{
						put("PROCESSID","PROCESSID");
						put("TASKID","TASKID");
					}}
				));			
//			child.addGrandChildTable(buildGrandChild("OW_INTERPROCESS_ATTRIBUTE", "OW_INTERPROCESS_ATTRIBUTE_HIST", "AUDIT_ID", null, 
//					Arrays.asList("PROCESSFROMID", "PROCESSTOID", "ITEMTYPEFROMID", "ITEMTYPETOID"),//Guess
//					Arrays.asList("TASKID")));
			
			child.addChild(build("OW_INTERPROCESS_ATTRIBUTE", "OW_INTERPROCESS_ATTRIBUTE_HIST", "AUDIT_ID", null, 
					Arrays.asList("PROCESSFROMID", "PROCESSTOID", "ITEMTYPEFROMID", "ITEMTYPETOID"),//Guess
					new HashMap<String, String>() {{
						put("TASKID","TASKID");
					}}
				));
			
//			child.addGrandChildTable(buildGrandChild("OW_TASK_CHECK", "OW_TASK_CHECK_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("PROCESSID", "TASKID", "MAKERTASKID"),
//					Arrays.asList("TASKID", "PROCESSID")));
			
			child.addChild(build("OW_TASK_CHECK", "OW_TASK_CHECK_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKID", "MAKERTASKID"),
					new HashMap<String, String>() {{
						put("PROCESSID","PROCESSID");
						put("TASKID","TASKID");
					}}
				));
			
//			child.addGrandChildTable(buildGrandChild("OW_TASK_ROUTELIST", "OW_TASK_ROUTELIST_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("PROCESSID", "TASKFROMID", "SEQNO"),
//					Arrays.asList("TASKFROMID", "PROCESSID")));
			
			child.addChild(build("OW_TASK_ROUTELIST", "OW_TASK_ROUTELIST_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKFROMID", "SEQNO"),
					new HashMap<String, String>() {{
						put("TASKID","TASKFROMID");
						put("PROCESSID","PROCESSID");
					}}
				));
			
//			child.addGrandChildTable(buildGrandChild("OW_JOIN_NODE", "OW_JOIN_NODE_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("PROCESSID", "TASKID", "ROWSEQNO", "SEQNO"),
//					Arrays.asList("TASKID", "PROCESSID")));
			
			child.addChild(build("OW_JOIN_NODE", "OW_JOIN_NODE_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKID", "ROWSEQNO", "SEQNO"),
					new HashMap<String, String>() {{
						put("TASKID","TASKID");
						put("PROCESSID","PROCESSID");
					}}
				));
			
//		parent.addChildTable(
//				buildChild("OW_SPLIT_NODE", "OW_SPLIT_NODE_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("PROCESSID", "TASKFROMID", "RULEID", "SEQNO"),
//					Arrays.asList("PROCESSTOID")));
		
		parent.addChild(
				build("OW_SPLIT_NODE", "OW_SPLIT_NODE_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID", "TASKFROMID", "RULEID", "SEQNO"),
					new HashMap<String, String>() {{
						put("PROCESSID","PROCESSTOID");
					}}
				));
			
//		parent.addChildTable(
//				buildChild("OW_ITEM_ASSO_TASK", "OW_ITEM_ASSO_TASK_HISTORY", "AUDIT_ID", null, 
//					Arrays.asList("PROCESSID" , "TASKID", "ITEMTYPEID"), 
//					Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_ITEM_ASSO_TASK", "OW_ITEM_ASSO_TASK_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("PROCESSID" , "TASKID", "ITEMTYPEID"), 
					new HashMap<String, String>() {{
						put("PROCESSID","PROCESSID");
					}}
				));
		
//		parent.addChildTable(
//				buildChild("OW_CONDITION", "OW_CONDITION_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("CONDITIONID"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_CONDITION", "OW_CONDITION_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CONDITIONID"),
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_RULE", "OW_RULE_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSID", "RULEID", "SEQNO"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_RULE", "OW_RULE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "RULEID", "SEQNO"),
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_RULE_VALIDITY", "OW_RULE_VALIDITY_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSID", "RULEID"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_RULE_VALIDITY", "OW_RULE_VALIDITY_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "RULEID"), 
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_USER_RIGHTS", "OW_USER_RIGHTS_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("TASKID","ITEMTYPEID","PROCESSID"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_USER_RIGHTS", "OW_USER_RIGHTS_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("TASKID","ITEMTYPEID","PROCESSID"),
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_PROCESS_ROUTELIST", "OW_PROCESS_ROUTELIST_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSID", "SEQNO"), 
//						Arrays.asList("PROCESSID")));
		
		parent.addChild(
				build("OW_PROCESS_ROUTELIST", "OW_PROCESS_ROUTELIST_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSID", "SEQNO"), 
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("OW_PROCESS_SPLIT_NODE", "OW_PROCESS_SPLIT_NODE_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("PROCESSFROMID", "RULEID", "SEQNO"), 
//						Arrays.asList("PROCESSFROMID")));
		
		parent.addChild(
				build("OW_PROCESS_SPLIT_NODE", "OW_PROCESS_SPLIT_NODE_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("PROCESSFROMID", "RULEID", "SEQNO"),
						new HashMap<String, String>() {{
							put("PROCESSID","PROCESSFROMID");
						}}
					));
		
		parent.addChild(build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
				new HashMap<String, String>() {{
				    put("PROCESSID","ELEMENT_ID");
				}},
				new HashMap<String, String>() {{
				    put("ENTITY_NAME","PROCESS");
				}}
		));
		
		return processEntity;
	}

	private static void globalAttribute() throws ClassNotFoundException, SQLException {
		EntityRevisited entity =  getGlobalAttributeEntityRevisited("GlobalAttribute");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getGlobalAttributeEntityRevisited(String entity) {
		EntityRevisited globalAttributeEntity = new EntityRevisited();
		
		globalAttributeEntity.setEntityName(entity);
//		globalAttributeEntity.setParentTable(
//				buildParent("PH_GLOBAL_ATTRIBUTE_MASTER", "PH_GLOBAL_ATTRIBUTE_AUDIT", "GLOBAL_ATTRIBUTE_AUDIT_ID", null, 
//						Arrays.asList("GLOBAL_ATTRIBUTE_ID")));
		
		globalAttributeEntity.setParent(
				build("PH_GLOBAL_ATTRIBUTE_MASTER", "PH_GLOBAL_ATTRIBUTE_AUDIT", "GLOBAL_ATTRIBUTE_AUDIT_ID", null, 
						Arrays.asList("GLOBAL_ATTRIBUTE_ID")));
		
		
//		ParentTable parent = globalAttributeEntity.getParentTable();
		
		Table parent = globalAttributeEntity.getParent();
//		parent.addChildTable(
//				buildChild("PH_GLOBAL_ATTR_ARG_MASTER", "PH_GLOBAL_ATTR_ARG_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("GLOBAL_ATTRIBUTE_ID", "ARGUMENT_NAME"), 
//						Arrays.asList("GLOBAL_ATTRIBUTE_ID")));
		
		parent.addChild(
				build("PH_GLOBAL_ATTR_ARG_MASTER", "PH_GLOBAL_ATTR_ARG_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("GLOBAL_ATTRIBUTE_ID", "ARGUMENT_NAME"),
						new HashMap<String, String>() {{
						    put("GLOBAL_ATTRIBUTE_ID","GLOBAL_ATTRIBUTE_ID");
						}}
					));
		
		
		return globalAttributeEntity;
	}

	private static boolean validate(EntityRevisited entity) throws ClassNotFoundException, SQLException {
		Connection connection = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		boolean primaryDetection = false;
		boolean foreignDetection = false;
		boolean actionColumnDetection = false;
		boolean auditIdColumnDetection = false;
		System.out.println("Primary Keys Detection");
		primaryDetection = Program.validatePrimaryKey(connection, entity);
		
		System.out.println("Foreign Keys Detection");
		foreignDetection = Program.validateReferenceKey(connection, entity);
		
		System.out.println("Action Column Detection");
		actionColumnDetection = Program.validateActionColumn(connection, entity);
		
		System.out.println("Audit ID Column Detection");
		auditIdColumnDetection = Program.validateAuditIdColumn(connection, entity);
		
		return (primaryDetection && foreignDetection && actionColumnDetection && auditIdColumnDetection);
	}
	
	private static boolean validateRevisited(EntityRevisited entity) throws ClassNotFoundException, SQLException {
		Connection connection = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		boolean primaryDetection = false;
		boolean foreignDetection = false;
		boolean actionColumnDetection = false;
		boolean auditIdColumnDetection = false;
		System.out.println("Primary Keys Detection");
		primaryDetection = Program.validatePrimaryKeyRevisited(connection, entity);
		
		System.out.println("Foreign Keys Detection");
		foreignDetection = Program.validateReferenceKeyRevisited(connection, entity);
		
		System.out.println("Action Column Detection");
		actionColumnDetection = Program.validateActionColumnRevisited(connection, entity);
		
		System.out.println("Audit ID Column Detection");
		auditIdColumnDetection = Program.validateAuditIdColumnRevisited(connection, entity);
		
		return (primaryDetection && foreignDetection && actionColumnDetection && auditIdColumnDetection);
	}

	private static EntityRevisited getGlobalAttributeEntity(String entity) {
		EntityRevisited globalAttributeEntity = new EntityRevisited();
		
		globalAttributeEntity.setEntityName(entity);
		globalAttributeEntity.setParentTable(
				buildParent("PH_GLOBAL_ATTRIBUTE_MASTER", "PH_GLOBAL_ATTRIBUTE_AUDIT", "GLOBAL_ATTRIBUTE_AUDIT_ID", null, 
						Arrays.asList("GLOBAL_ATTRIBUTE_ID")));
		
		ParentTable parent = globalAttributeEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("PH_GLOBAL_ATTR_ARG_MASTER", "PH_GLOBAL_ATTR_ARG_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("GLOBAL_ATTRIBUTE_ID", "ARGUMENT_NAME"), 
						Arrays.asList("GLOBAL_ATTRIBUTE_ID")));
		
		return globalAttributeEntity;
		
	}

	private static void auditIdColumnGeneration() {
		String columnName = "AUDIT_ID";
		String colDefinition = "NUMBER(10)";
		generateAddColumnQuery("PH_INTERFACE_FORMAT_AUDIT", columnName, colDefinition);
		generateAddColumnQuery("PH_FMT_PROD_FIELDS_MAP_AUDIT", columnName, colDefinition);
		generateAddColumnQuery("PH_FMT_SECTION_GRID_MAP_AUDIT", columnName, colDefinition);
		generateAddColumnQuery("PH_GRID_FLDS_DFT_VAL_AUDIT", columnName, colDefinition);
		generateAddColumnQuery("PH_GRID_EMBEDED_FORMAT_AUDIT", columnName, colDefinition);
	}

	private static void messageFormat() throws ClassNotFoundException, SQLException {
		Connection connector = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		
		EntityRevisited entity =  getMessageFormatEntityRevisited("MessageFormat");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getMessageFormatEntityRevisited(String name) {
		EntityRevisited messageFormatEntity = new EntityRevisited();
		
		messageFormatEntity.setEntityName(name);
//		messageFormatEntity.setParentTable(
//				buildParent("PH_INTERFACE_FORMAT", "PH_INTERFACE_FORMAT_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("INTERFACE_ID")));
		
		messageFormatEntity.setParent(
				build("PH_INTERFACE_FORMAT", "PH_INTERFACE_FORMAT_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("INTERFACE_ID")));
		
//		ParentTable parent = messageFormatEntity.getParentTable();
//		
		Table parent = messageFormatEntity.getParent();
		
//		parent.addChildTable(
//				buildChild("PH_FMT_PROD_FIELDS_MAP_MASTER", "PH_FMT_PROD_FIELDS_MAP_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("TB_FIELD", "PRODUCT_ID", "INTERFACE_ID", "FIELD_NAME"), 
//						Arrays.asList("INTERFACE_ID")));
		
		parent.addChild(
				build("PH_FMT_PROD_FIELDS_MAP_MASTER", "PH_FMT_PROD_FIELDS_MAP_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("TB_FIELD", "PRODUCT_ID", "INTERFACE_ID", "FIELD_NAME"),
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","INTERFACE_ID");
						}}
					));
		
		parent.addChild(
				build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
					new HashMap<String, String>() {{
					    put("INTERFACE_ID","ELEMENT_ID");
					}},
					new HashMap<String, String>() {{
					    put("ENTITY_NAME","FORMAT");
					}}
		));
		
//		parent.addChildTable(
//				buildChild("PH_FMT_SECTION_GRID_MAP_MASTER", "PH_FMT_SECTION_GRID_MAP_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("GRID_NAME", "PRODUCT_ID", "INTERFACE_ID"), 
//						Arrays.asList("INTERFACE_ID")));
		
		parent.addChild(
				build("PH_FMT_SECTION_GRID_MAP_MASTER", "PH_FMT_SECTION_GRID_MAP_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("GRID_NAME", "PRODUCT_ID", "INTERFACE_ID"), 
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","INTERFACE_ID");
						}}
				));

//		parent.addChildTable(
//				buildChild("PH_GRID_FLDS_DFT_VAL_MASTER", "PH_GRID_FLDS_DFT_VAL_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("INTERFACE_ID", "PRODUCT_ID", "BUSINESS_FIELD_NAME"), 
//						Arrays.asList("INTERFACE_ID")));
		
		parent.addChild(
				build("PH_GRID_FLDS_DFT_VAL_MASTER", "PH_GRID_FLDS_DFT_VAL_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("INTERFACE_ID", "PRODUCT_ID", "BUSINESS_FIELD_NAME"), 
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","INTERFACE_ID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("PH_INTERFACE_FORMAT_SECTION", "PH_INTERFACE_FMT_SEC_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("SECTION_NAME", "INTERFACE_ID"), 
//						Arrays.asList("INTERFACE_ID")));
		
		parent.addChild(
				build("PH_INTERFACE_FORMAT_SECTION", "PH_INTERFACE_FMT_SEC_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("SECTION_NAME", "INTERFACE_ID"),
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","INTERFACE_ID");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("PH_GROUPING_ID_MASTER", "PH_GROUPING_ID_AUDIT", "GROUPING_AUDIT_ID", null, 
//						Arrays.asList("GROUPING_ID"), 
//						Arrays.asList("FORMAT_ID")));
//		
		parent.addChild(
				build("PH_GROUPING_ID_MASTER", "PH_GROUPING_ID_AUDIT", "GROUPING_AUDIT_ID", null, 
						Arrays.asList("GROUPING_ID"),
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","FORMAT_ID");
						}}
					));
		
		
//		parent.addChildTable(
//				buildChild("PH_CROSS_FIELD_VALIDATION", "PH_CROSS_FIELD_VALID_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("CROSS_FIELD_VALIDATION_ID"), 
//						Arrays.asList("INTERFACE_ID")));
//		
		parent.addChild(
				build("PH_CROSS_FIELD_VALIDATION", "PH_CROSS_FIELD_VALID_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("CROSS_FIELD_VALIDATION_ID"), 
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","INTERFACE_ID");
						}}
				));
		
//		parent.addChildTable(
//				buildChild("PH_GRID_EMBEDED_FORMAT_MASTER", "PH_GRID_EMBEDED_FORMAT_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("SEQUENCE", "ENVELOPE_INTERFACE_ID"), 
//						Arrays.asList("ENVELOPE_INTERFACE_ID")));
//		
		parent.addChild(
				build("PH_GRID_EMBEDED_FORMAT_MASTER", "PH_GRID_EMBEDED_FORMAT_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("SEQUENCE", "ENVELOPE_INTERFACE_ID"),
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","ENVELOPE_INTERFACE_ID");
						}}
					));
		
		
//		parent.addChildTable(
//				buildChild("PH_ERROR_MASTER", "PH_ERROR_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("ERROR_CODE", "INTERFACE_NAME"), 
//						Arrays.asList("INTERFACE_NAME")));
//		
		parent.addChild(
				build("PH_ERROR_MASTER", "PH_ERROR_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("ERROR_CODE", "INTERFACE_NAME"),
						new HashMap<String, String>() {{
						    put("INTERFACE_ID","INTERFACE_NAME");
						}}
					));
		
//		parent.addChildTable(
//				buildChild("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
//						Arrays.asList("RULE_ID", "RULE_SET_ID"), 
//						Arrays.asList("RULE_SET_ID")));
//		
//		parent.addChildTable(
//				buildChild("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
//						Arrays.asList("EXPRESSION_ID", "ENTITY_ID"), 
//						Arrays.asList("ENTITY_ID")));
//		
//		parent.addChildTable(
//				buildChild("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
//						Arrays.asList("RULE_NODE_ID", "ENTITY_ID"), 
//						Arrays.asList("ENTITY_ID")));
//		
//		parent.addChildTable(
//				buildChild("PH_RULE_VARIABLES_MASTER", "PH_RULE_VARIABLES_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("RULE_ID", "RULE_VARIABLE_ID", "ENTITY_ID"), 
//						Arrays.asList("ENTITY_ID")));
//		
//		parent.addChildTable(
//				buildChild("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
//						Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"), 
//						Arrays.asList("ENTITY_ID")));

		Table child = build("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_SET_ID"),
				new HashMap<String, String>() {{
				    put("INTERFACE_ID","RULE_SET_ID");
				}}
			);
		
		parent.addChild(child);
		
		
		child.addChild(build("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_ID","RULE_ID");
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
		child.addChild(build("PH_RULE_VARIABLES_MASTER", "PH_RULE_VARIABLES_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_VARIABLE_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_ID","RULE_ID");
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
		Table grandChild = build("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_NODE_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("ROOT_NODE_ID","RULE_NODE_ID");
				}}
			);
		child.addChild(grandChild);
		
		grandChild.addChild(build("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
				Arrays.asList("EXPRESSION_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_NODE_ID","RULE_NODE_ID");
				    put("ENTITY_ID","ENTITY_ID");
				}}
			));
		
		return messageFormatEntity;
	}

	private static void generateAddColumnQuery(String tableName, String columnNae, String colDefinition){
		System.out.println(String.format("ALTER TABLE %s ADD %s %s;", tableName.toUpperCase(), columnNae.toUpperCase(), colDefinition.toUpperCase()));
	}
	
	private static EntityRevisited getMessageFormatEntity(String name) {
		EntityRevisited ruleSetEntity = new EntityRevisited();
		
		ruleSetEntity.setEntityName(name);
		ruleSetEntity.setParentTable(
				buildParent("PH_INTERFACE_FORMAT", "PH_INTERFACE_FORMAT_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("INTERFACE_ID")));
		
		ParentTable parent = ruleSetEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("PH_FMT_PROD_FIELDS_MAP_MASTER", "PH_FMT_PROD_FIELDS_MAP_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("TB_FIELD", "PRODUCT_ID", "INTERFACE_ID", "FIELD_NAME"), 
						Arrays.asList("INTERFACE_ID")));
		
		parent.addChildTable(
				buildChild("PH_FMT_SECTION_GRID_MAP_MASTER", "PH_FMT_SECTION_GRID_MAP_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("GRID_NAME", "PRODUCT_ID", "INTERFACE_ID"), 
						Arrays.asList("INTERFACE_ID")));
		
		parent.addChildTable(
				buildChild("PH_GRID_FLDS_DFT_VAL_MASTER", "PH_GRID_FLDS_DFT_VAL_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("INTERFACE_ID", "PRODUCT_ID", "BUSINESS_FIELD_NAME"), 
						Arrays.asList("INTERFACE_ID")));
		
		parent.addChildTable(
				buildChild("PH_INTERFACE_FORMAT_SECTION", "PH_INTERFACE_FMT_SEC_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("SECTION_NAME", "INTERFACE_ID"), 
						Arrays.asList("INTERFACE_ID")));
		
		parent.addChildTable(
				buildChild("PH_GROUPING_ID_MASTER", "PH_GROUPING_ID_AUDIT", "GROUPING_AUDIT_ID", null, 
						Arrays.asList("GROUPING_ID"), 
						Arrays.asList("FORMAT_ID")));
		
		parent.addChildTable(
				buildChild("PH_CROSS_FIELD_VALIDATION", "PH_CROSS_FIELD_VALID_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("CROSS_FIELD_VALIDATION_ID"), 
						Arrays.asList("INTERFACE_ID")));
		
		parent.addChildTable(
				buildChild("PH_GRID_EMBEDED_FORMAT_MASTER", "PH_GRID_EMBEDED_FORMAT_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("SEQUENCE", "ENVELOPE_INTERFACE_ID"), 
						Arrays.asList("ENVELOPE_INTERFACE_ID")));
		
		parent.addChildTable(
				buildChild("PH_ERROR_MASTER", "PH_ERROR_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("ERROR_CODE", "INTERFACE_NAME"), 
						Arrays.asList("INTERFACE_NAME")));
		
		parent.addChildTable(
				buildChild("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_ID", "RULE_SET_ID"), 
						Arrays.asList("RULE_SET_ID")));
		
		parent.addChildTable(
				buildChild("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("EXPRESSION_ID", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_NODE_ID", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_VARIABLES_MASTER", "PH_RULE_VARIABLES_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("RULE_ID", "RULE_VARIABLE_ID", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
						Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		return ruleSetEntity;
	}

	private static EntityRevisited ruleSetEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity =  getRuleSetEntityRevisited("RuleSet");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
		
		return entity;
	}

	private static EntityRevisited getRuleSetEntityRevisited(String name) {
		EntityRevisited ruleSetEntity = new EntityRevisited();
		
		ruleSetEntity.setEntityName(name);
		
		ruleSetEntity.setParent(
				build("PH_RULE_SET_MASTER", "PH_RULE_SET_AUDIT", "RULE_SET_AUDIT_ID", null, 
						Arrays.asList("RULE_SET_ID")));
		
		Table parent = ruleSetEntity.getParent();
		
		Table child = build("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_SET_ID"),
				new HashMap<String, String>() {{
				    put("RULE_SET_ID","RULE_SET_ID");
				}}
			);
		
		parent.addChild(child);
		
		child.addChild(build("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_ID","RULE_ID");
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
		parent.addChild(build("PH_RULE_SET_VARIABLES_MASTER", "PH_RULE_SET_VARIABLES_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_VARIABLE_ID", "RULE_SET_ID"),
				new HashMap<String, String>() {{
				    put("RULE_SET_ID","RULE_SET_ID");
				}}
			));
		
		parent.addChild(build("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_NODE_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
		parent.addChild(build("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
				Arrays.asList("EXPRESSION_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
		return ruleSetEntity;
	}

	private static EntityRevisited getRuleSetEntity(String name) {
		EntityRevisited ruleSetEntity = new EntityRevisited();
		
		ruleSetEntity.setEntityName(name);
		ruleSetEntity.setParentTable(
				buildParent("PH_RULE_SET_MASTER", "PH_RULE_SET_AUDIT", "RULE_SET_AUDIT_ID", null, 
						Arrays.asList("RULE_SET_ID")));
		
		ParentTable parent = ruleSetEntity.getParentTable();
		
		parent.addChildTable(
				buildChild("PH_RULE_SET_VARIABLES_MASTER", "PH_RULE_SET_VARIABLES_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_VARIABLE_ID", "RULE_SET_ID"), 
						Arrays.asList("RULE_SET_ID")));
		
		parent.addChildTable(
				buildChild("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("EXPRESSION_ID", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_MASTER", "PH_RULE_MASTER_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_ID", "RULE_SET_ID"), 
						Arrays.asList("RULE_SET_ID")));
		
		
		parent.addChildTable(
				buildChild("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_NODE_ID", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		
		
		return ruleSetEntity;
	}

	private static void mlcEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity =  getMLCEntityRevisited("MessageLifeCycle");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getMLCEntityRevisited(String name) {
		EntityRevisited mlcEntity = new EntityRevisited();
		
		mlcEntity.setEntityName(name);
		mlcEntity.setParent(
				build("PH_MSG_LIFE_CYCLE_MASTER", "PH_MSG_LIFE_CYCLE_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		Table parent = mlcEntity.getParent();
		parent.addChild(
				build("PH_MLC_ERRGRD_FLD_MAP_MASTER", "PH_MLC_ERRGRD_FLD_MAP_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("LIFE_CYCLE_ID", "SEQ_NO", "FIELD_NAME"), 
						new HashMap<String, String>() {{
						    put("LIFE_CYCLE_ID","LIFE_CYCLE_ID");
						}}
				));
		
		Table child = build("PH_RULE_MASTER", "PH_RULE_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_SET_ID"),
				new HashMap<String, String>() {{
				    put("LIFE_CYCLE_ID","RULE_SET_ID");
				}}
			);
		parent.addChild(child);
		
		child.addChild(build("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_ID","RULE_ID");
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
		child.addChild(build("PH_RULE_VARIABLES_MASTER", "PH_RULE_VARIABLES_AUDIT", "AUDIT_ID", null, 
				Arrays.asList("RULE_ID", "RULE_VARIABLE_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_ID","RULE_ID");
				    put("RULE_SET_ID","ENTITY_ID");
				}}
			));
		
		Table grandChild = build("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
				Arrays.asList("RULE_NODE_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("ROOT_NODE_ID","RULE_NODE_ID");
				}}
			);
		child.addChild(grandChild);
		
		grandChild.addChild(build("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
				Arrays.asList("EXPRESSION_ID", "ENTITY_ID"),
				new HashMap<String, String>() {{
				    put("RULE_NODE_ID","RULE_NODE_ID");
				    put("ENTITY_ID","ENTITY_ID");
				}}
			));

		parent.addChild(
				build("PH_CHANNEL_IDEN_MASTER", "PH_CHANNEL_IDEN_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("CHNL_IDEN_ID"),
						new HashMap<String, String>() {{
						    put("LIFE_CYCLE_ID","LIFE_CYCLE_ID");
						}}
				));
		
		parent.addChild(
				build("PH_PRODUCT_REGROUPING_MASTER", "PH_PRODUCT_REGROUPING_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("PROD_REG_ID"),
						new HashMap<String, String>() {{
						    put("LIFE_CYCLE_ID","LIFE_CYCLE_ID");
						}}
				));
		
		parent.addChild(
				build("PH_PROD_ADDLFIELDS_MASTER", "PH_PROD_ADDLFILDS_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("PROD_FIELD_ID"),
						new HashMap<String, String>() {{
						    put("LIFE_CYCLE_ID","LIFE_CYCLE_ID");
						}}
				));
		
		parent.addChild(
				build("PH_MSG_GENERATION_RULE_MASTER", "PH_MSG_GENERATION_RULE_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("MSG_GEN_ID"), 
						new HashMap<String, String>() {{
						    put("LIFE_CYCLE_ID","LIFE_CYCLE_ID");
						}}
					));
		
		parent.addChild(
				build("PH_CORELATION_RULE_MASTER", "PH_CORELATION_RULE_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("CORELATION_ID"),
						new HashMap<String, String>() {{
						    put("LIFE_CYCLE_ID","LIFE_CYCLE_ID");
						}}
				));
		
		return mlcEntity;
	}

	private static void testingQuery(EntityRevisited entity) {
		String common = "Select * \nfrom ";
		StringBuffer buffer = new StringBuffer();
		ParentTable parent = entity.getParentTable();
		buffer.append(common).append(parent.getName() + "\nwhere " + parent.getPrimaryColumnNames().get(0) + " = ???;\n\n" );
		buffer.append(common).append(parent.getHistoryName() + "\nwhere " + parent.getPrimaryColumnNames().get(0) + " = ???;\n\n" );
		
		
		for(ChildTable child: entity.getParentTable().getChildTables()){
			buffer.append(common).append(child.getName() + "\nwhere " + child.getForeignColumnNames().get(0) + " = ???;\n\n" );
			buffer.append(common).append(child.getHistoryName() + "\nwhere " + child.getForeignColumnNames().get(0) + " = ???;\n\n" );
		}
		
		buffer.append("Select * from tb_hash where AUDIT_ID = ???\n\n");
		buffer.append("Select * from tb_label_snapshot where AUDIT_ID = ???\n");
		
		System.out.println(buffer);
	}
	
	private static void testingQueryRevisited(EntityRevisited entity) {
		String common = "Select * \nfrom ";
		StringBuffer buffer = new StringBuffer();
		Table parent = entity.getParent();
		buffer.append(common).append(parent.getName() + "\nwhere " + parent.getPrimaryColumnNames().get(0) + " = ???;\n\n" );
		buffer.append(common).append(parent.getHistoryName() + "\nwhere " + parent.getPrimaryColumnNames().get(0) + " = ???;\n\n" );
		
		for(Table child: parent.getChilds()){
			buffer.append(common).append(child.getName() + "\nwhere "); 
			for(String c: child.getForeignColumnMapping().values()){
				buffer.append(c + " = ???" );
			}
			buffer.append(";\n\n");
			
			buffer.append(common).append(child.getHistoryName() + "\nwhere "); 
			for(String c: child.getForeignColumnMapping().values()){
				buffer.append(c + " = ???" );
			}
			buffer.append(";\n\n");
		}
		
		buffer.append("Select * from tb_hash where AUDIT_ID = ???\n\n");
		buffer.append("Select * from tb_label_snapshot where AUDIT_ID = ???\n");
		
		System.out.println(buffer);
	}

	private static void detectExtraMasterColumns(Connection connector, EntityRevisited entity) {
		Map<String, String> tableNameHistoryTableName = new HashMap<String, String>();
		
		tableNameHistoryTableName.put(entity.getParentTable().getName(), entity.getParentTable().getHistoryName());
		
		for(ChildTable child: entity.getParentTable().getChildTables()){
			tableNameHistoryTableName.put(child.getName(), child.getHistoryName());
			for(GrandChildTable grandChild: child.getGrandChildTables()){
				tableNameHistoryTableName.put(grandChild.getName(), grandChild.getHistoryName());
			}
		}
		Program.detectExtraMasterColumns(connector, tableNameHistoryTableName);
	}
	
	private static void detectExtraMasterColumnsRevisited(Connection connector, EntityRevisited entity) {
		Map<String, String> tableNameHistoryTableName = new HashMap<String, String>();
		
		tableNameHistoryTableName.put(entity.getParent().getName(), entity.getParent().getHistoryName());
		
		for(Table child: entity.getParent().getChilds()){
			tableNameHistoryTableName.put(child.getName(), child.getHistoryName());
			for(Table grandChild: child.getChilds()){
				tableNameHistoryTableName.put(grandChild.getName(), grandChild.getHistoryName());
				for(Table greatGrandChild: grandChild.getChilds()){
					tableNameHistoryTableName.put(greatGrandChild.getName(), greatGrandChild.getHistoryName());
					
				}
			}
		}
		Program.detectExtraMasterColumns(connector, tableNameHistoryTableName);
	}

	private static EntityRevisited getMLCEntity(String name) {
		EntityRevisited mlcEntity = new EntityRevisited();
		
		mlcEntity.setEntityName(name);
		mlcEntity.setParentTable(
				buildParent("PH_MSG_LIFE_CYCLE_MASTER", "PH_MSG_LIFE_CYCLE_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		ParentTable parent = mlcEntity.getParentTable();
		parent.addChildTable(
				buildChild("PH_MLC_ERRGRD_FLD_MAP_MASTER", "PH_MLC_ERRGRD_FLD_MAP_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("LIFE_CYCLE_ID", "SEQ_NO", "FIELD_NAME"), 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_XML_MASTER", "PH_RULE_XML_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_ID", "SEQUENCE", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_MASTER", "PH_RULE_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_ID", "RULE_SET_ID"), 
						Arrays.asList("RULE_SET_ID")));
		
		parent.addChildTable(
				buildChild("PH_MSG_GENERATION_RULE_MASTER", "PH_MSG_GENERATION_RULE_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("MSG_GEN_ID"), 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		parent.addChildTable(
				buildChild("PH_CHANNEL_IDEN_MASTER", "PH_CHANNEL_IDEN_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("CHNL_IDEN_ID"), 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		parent.addChildTable(
				buildChild("PH_PRODUCT_REGROUPING_MASTER", "PH_PRODUCT_REGROUPING_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("PROD_REG_ID"), 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		parent.addChildTable(
				buildChild("PH_EXPRESSION_SET", "PH_EXPRESSION_SET_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("EXPRESSION_ID", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		
		parent.addChildTable(
				buildChild("PH_PROD_ADDLFIELDS_MASTER", "PH_PROD_ADDLFILDS_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("PROD_FIELD_ID"), 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		parent.addChildTable(
				buildChild("PH_CORELATION_RULE_MASTER", "PH_CORELATION_RULE_AUDIT", "LIFE_CYCLE_AUDIT_ID", null, 
						Arrays.asList("CORELATION_ID"), 
						Arrays.asList("LIFE_CYCLE_ID")));
		
		parent.addChildTable(
				buildChild("PH_RULE_NODE", "PH_RULE_NODE_AUDIT", "RULE_AUDIT_ID", null, 
						Arrays.asList("RULE_NODE_ID", "ENTITY_ID"), 
						Arrays.asList("ENTITY_ID")));
		

		
		return mlcEntity;
	}

	private static void schedulerEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getSchedulerEntityRevisited("Scheduler");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
	}

	private static EntityRevisited getSchedulerEntityRevisited(String string) {
		EntityRevisited schedulerEntity = new EntityRevisited();
		
		schedulerEntity.setEntityName(string);
//		schedulerEntity.setParentTable(
//				buildParent("PH_SCHEDULER_MASTER", "PH_SCHEDULER_AUDIT", "SCHEDULER_AUDIT_ID", null, 
//						Arrays.asList("SCHEDULER_ID")));
		
		schedulerEntity.setParent(
				build("PH_SCHEDULER_MASTER", "PH_SCHEDULER_AUDIT", "SCHEDULER_AUDIT_ID", null, 
						Arrays.asList("SCHEDULER_ID")));		
		return schedulerEntity;
	}

	private static EntityRevisited getSchedulerEntity(String name) {
		EntityRevisited schedulerEntity = new EntityRevisited();
		
		schedulerEntity.setEntityName(name);
		schedulerEntity.setParentTable(
				buildParent("PH_SCHEDULER_MASTER", "PH_SCHEDULER_AUDIT", "SCHEDULER_AUDIT_ID", null, 
						Arrays.asList("SCHEDULER_ID")));
		
		/*channelEntity.addChildTable(
				buildChild("PH_CHANNEL_ACTIVITY_MASTER", "PH_CHANNEL_ACTIVITY_AUDIT", "CHANNEL_AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID"), 
						Arrays.asList("CHANNEL_ID")));
		
		channelEntity.addChildTable(
				buildChild("PH_LINK_CHANNEL_MASTER", "PH_LINK_CHANNEL_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID"), 
						Arrays.asList("CHANNEL_ID")));*/
		
		return schedulerEntity;
	}

	private static void channelEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity = getChannelEntityRevisited("Channel");
		entity.populateColumnDetailsRevisited();
		test(entity);
	}

	private static EntityRevisited getChannelEntityRevisited(String name) {
		EntityRevisited channelEntity = new EntityRevisited();
		
		channelEntity.setEntityName(name);
//		channelEntity.setParentTable(
//				buildParent("PH_CHANNEL_MASTER", "PH_CHANNEL_AUDIT", "CHANNEL_AUDIT_ID", null, 
//						Arrays.asList("CHANNEL_ID")));
		
		channelEntity.setParent(
				build("PH_CHANNEL_MASTER", "PH_CHANNEL_AUDIT", "CHANNEL_AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID")));
		
//		ParentTable parent = channelEntity.getParentTable();
		Table parent = channelEntity.getParent();
//		parent.addChildTable(
//				buildChild("PH_CHANNEL_ACTIVITY_MASTER", "PH_CHANNEL_ACTIVITY_AUDIT", "CHANNEL_AUDIT_ID", null, 
//						Arrays.asList("CHANNEL_ID"), 
//						Arrays.asList("CHANNEL_ID")));
//		
		parent.addChild(
				build("PH_CHANNEL_ACTIVITY_MASTER", "PH_CHANNEL_ACTIVITY_AUDIT", "CHANNEL_AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID"), 
						new HashMap<String, String>() {{
						    put("CHANNEL_ID","CHANNEL_ID");
						}}
				));
		
//		parent.addChildTable(
//				buildChild("PH_LINK_CHANNEL_MASTER", "PH_LINK_CHANNEL_HISTORY", "AUDIT_ID", null, 
//						Arrays.asList("CHANNEL_ID"), 
//						Arrays.asList("CHANNEL_ID")));
		
		parent.addChild(
				build("PH_LINK_CHANNEL_MASTER", "PH_LINK_CHANNEL_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID"), 
						new HashMap<String, String>() {{
						    put("CHANNEL_ID","CHANNEL_ID");
						}}
				));
		
		return channelEntity;
	}

	private static void displayTables(EntityRevisited entity) {
		List<String> tableNames = Program.getTableNames(entity);
		System.out.println(Program.getDBQuery(tableNames));
		
		tableNames = Program.getHistoryTableNames(entity);
		System.out.println(Program.getDBQuery(tableNames));
	}

	private static EntityRevisited getChannelEntity(String name) {
		EntityRevisited channelEntity = new EntityRevisited();
		
		channelEntity.setEntityName(name);
		channelEntity.setParentTable(
				buildParent("PH_CHANNEL_MASTER", "PH_CHANNEL_AUDIT", "CHANNEL_AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID")));
		
		ParentTable parent = channelEntity.getParentTable();
		parent.addChildTable(
				buildChild("PH_CHANNEL_ACTIVITY_MASTER", "PH_CHANNEL_ACTIVITY_AUDIT", "CHANNEL_AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID"), 
						Arrays.asList("CHANNEL_ID")));
		
		parent.addChildTable(
				buildChild("PH_LINK_CHANNEL_MASTER", "PH_LINK_CHANNEL_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("CHANNEL_ID"), 
						Arrays.asList("CHANNEL_ID")));
		
		return channelEntity;
	}

	private static EntityRevisited enquiryEntity() throws ClassNotFoundException, SQLException {
		EntityRevisited entity =  getEnquiryEntityRevisited("Enquiry");
		entity.populateColumnDetailsRevisited();
		
		test(entity);
		
		return entity;
	}
	
	private static void test(EntityRevisited entity) throws ClassNotFoundException, SQLException{
		Connection connector = DbUtility.DBConnector.getConnection(CONNECTION_ID);
		
		detectExtraMasterColumnsRevisited(connector, entity);
		if(validateRevisited(entity)){
//			processLabellingModuleRevisited(connector, entity);
			
			displayTablesRevisited(entity);
			
			testingQueryRevisited(entity);
		}
	}

	private static EntityRevisited getEnquiryEntityRevisited(String entityName) {
		EntityRevisited enquiryEntity = new EntityRevisited();
		
		enquiryEntity.setEntityName(entityName);
		
		enquiryEntity.setParent(build("TB_ENQ_MASTER", "TB_ENQ_HISTORY", "AUDITID", null, 
				Arrays.asList("ENQ_ID")));
		
		Table parent = enquiryEntity.getParent();

		parent.addChild(build("TB_LINKED_FIELD_MASTER", "TB_LINKED_FIELD_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("ELEMENT_TYPE", "CONTROL_ID", "FIELD_ID", "ENC_TYPE", "ENC_ID"),
				new HashMap<String, String>() {{
				    put("ENQ_ID","ENC_ID");
				}}
		));
		
		parent.addChild(build("TB_ENQ_FIELD_DETAIL_MASTER", "TB_ENQ_FIELD_DETAIL_HISTORY", "AUDITID", null, 
				Arrays.asList("DATAFIELD_ID", "ENC_ID"),
				new HashMap<String, String>() {{
				    put("ENQ_ID","ENC_ID");
				}}
		));
		
		parent.addChild(build("TB_LINKED_UIC_MASTER", "TB_LINKED_UIC_HISTORY", "AUDIT_ID", null, 
				Arrays.asList("UIC_ID", "ENC_TYPE", "ENC_ID"),
				new HashMap<String, String>() {{
				    put("ENQ_ID","ENC_ID");
				}}
		));
		
		parent.addChild(build("TB_ENQ_REF_PROD_OPERAT_MASTER", "TB_ENQ_REF_PROD_OPERAT_HISTORY", "AUDITID", null,
				Arrays.asList("ENQ_ID", "PRODUCTID", "OPERATIONID"),
				new HashMap<String, String>() {{
				    put("ENQ_ID","ENQ_ID");
				}}
		));
		
		parent.addChild(build("TB_ENQ_REF_PRODUCT_MASTER", "TB_ENQ_REF_PRODUCT_HISTORY", "AUDITID", null,
				Arrays.asList("ENQ_ID", "ITEMTYPEID"),
				new HashMap<String, String>() {{
				    put("ENQ_ID","ENQ_ID");
				}}
		));
		
		parent.addChild(
				build("TB_ENQ_REF_ADDLTABLES_MASTER", "TB_ENQ_REF_ADDLTABLES_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_ID", "REF_TABLE_NAME"), 
						new HashMap<String, String>() {{
						    put("ENQ_ID","ENQ_ID");
						}}
				));
		
		parent.addChild(
				build("TB_ENQ_TABLE_LINKS_MASTER", "TB_ENQ_TABLE_LINKS_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_ID", "TABLE1_PRODUCTID", "TABLE2_PRODUCTID"), 
						new HashMap<String, String>() {{
						    put("ENQ_ID","ENQ_ID");
						}}
					));
		
		Table child = build("TB_ENQ_LINKAGES_MASTER", "TB_ENQ_LINKAGES_HISTORY", "AUDITID", null, 
				Arrays.asList("ENQ_LINKAGE_ID"), 
				new HashMap<String, String>() {{
				    put("ENQ_ID","ENQ_ID");
				}}
				);
		
		parent.addChild(child);
		
		child.addChild(
				build("TB_ENQ_FIELD_LINKAGES_MASTER", "TB_ENQ_FIELD_LINKAGES_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_LINKAGE_ID", "ENQ_FIELD_ID", "LINKED_ENQ_FIELD_ID"), 
						new HashMap<String, String>() {{
						    put("ENQ_LINKAGE_ID","ENQ_LINKAGE_ID");
						}}
				));
		
		parent.addChild(build("TB_ENQ_GROUP_RANGE_MASTER", "TB_ENQ_GROUP_RANGE_HISTORY", "AUDITID", null, 
				Arrays.asList("ENQ_ID", "ID"), 
				new HashMap<String, String>() {{
				    put("ENQ_ID","ENQ_ID");
				}}
			));
		
		parent.addChild(
				build("TB_ENQ_FILTER_MASTER", "TB_ENQ_FILTER_HISTORY", "AUDITID", null, 
						Arrays.asList("SEQ_NO", "ENQ_FIELD_ID", "ENQ_ID"), 
						new HashMap<String, String>() {{
						    put("ENQ_ID","ENQ_ID");
						}}
				));
		
		parent.addChild(
				build("TB_MULTILANG_MASTER", "TB_MULTILANG_HISTORY", "AUDIT_ID", null, 
					Arrays.asList("SEQ_NO", "PRODUCT_DOCTYPE_ID", "PRODUCT_DOCTYPE_FLAG", "ENTITY_PROPERTY", "ELEMENT_KEY", "GRIDID", "GRIDROWNO", "CONTROL_ID", "ELEMENT_TYPE", "STATUS", "LANG_CODE", "ELEMENT_ID", "ENTITY_NAME"),
					new HashMap<String, String>() {{
					    put("ENQ_ID","PRODUCT_DOCTYPE_ID");
					}},
					new HashMap<String, String>() {{
					    put("ENTITY_NAME","ENQDEF");
					}}
		));
		
		return enquiryEntity;
	}

	private static void processLabellingModule(Connection connector, EntityRevisited entity) {
		System.out.println(String.format("--Start Entity: %s", entity.getEntityName()));
		
		Program.insertIntoTbEntMaster(connector, entity);
		System.out.println();
		Program.insertIntoTbColumnMaster(connector, entity);
		System.out.println();
		Program.insertIntoTbEntityPrimaryMaster(connector, entity);
		System.out.println();
		Program.insertIntoTbEntDetailMaster(connector, entity);
		
		System.out.println(String.format("--End Entity: %s", entity.getEntityName()));
	}

	public EntityRevisited populateColumnDetails() {
		ParentTable parent = getParentTable();
		parent.populateColumnDetails();
		for(ChildTable child: parent.getChildTables()){
			child.populateColumnDetails();
			for(GrandChildTable grandChildTable: child.getGrandChildTables()){
				grandChildTable.populateColumnDetails();
			}
		}
		return this;
	}
	
	public EntityRevisited populateColumnDetailsRevisited() {
		Table parent = getParent();
		parent.populateColumnDetails();
		for(Table child: parent.getChilds()){
			child.populateColumnDetails();
			for(Table grandChildTable: child.getChilds()){
				grandChildTable.populateColumnDetails();
				for(Table greatGrandChildTable: grandChildTable.getChilds()){
					greatGrandChildTable.populateColumnDetails();
				}
			}
		}
		return this;
	}

	private static EntityRevisited getEnquiryEntity(String entityName) {
		EntityRevisited enquiryEntity = new EntityRevisited();
		
		enquiryEntity.setEntityName(entityName);
		enquiryEntity.setParentTable(
				buildParent("TB_ENQ_MASTER", "TB_ENQ_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_ID")));
		
		ParentTable parent = enquiryEntity.getParentTable();
		parent.addChildTable(
				buildChild("TB_LINKED_FIELD_MASTER", "TB_LINKED_FIELD_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("ELEMENT_TYPE", "CONTROL_ID", "FIELD_ID", "ENC_TYPE", "ENC_ID"), 
						Arrays.asList("ENC_ID")));
		
		parent.addChildTable(
				buildChild("TB_ENQ_FIELD_DETAIL_MASTER", "TB_ENQ_FIELD_DETAIL_HISTORY", "AUDITID", null, 
						Arrays.asList("DATAFIELD_ID", "ENC_ID"), 
						Arrays.asList("ENC_ID")));
		
		parent.addChildTable(
				buildChild("TB_LINKED_UIC_MASTER", "TB_LINKED_UIC_HISTORY", "AUDIT_ID", null, 
						Arrays.asList("UIC_ID", "ENC_TYPE", "ENC_ID"), 
						Arrays.asList("ENC_ID")));
		
		parent.addChildTable(
				buildChild("TB_ENQ_REF_PROD_OPERAT_MASTER", "TB_ENQ_REF_PROD_OPERAT_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_ID", "PRODUCTID", "OPERATIONID"), 
						Arrays.asList("ENQ_ID")));
		
		parent.addChildTable(
				buildChild("TB_ENQ_REF_PRODUCT_MASTER", "TB_ENQ_REF_PRODUCT_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_ID", "ITEMTYPEID"), 
						Arrays.asList("ENQ_ID")));
		
		parent.addChildTable(
				buildChild("TB_ENQ_REF_ADDLTABLES_MASTER", "TB_ENQ_REF_ADDLTABLES_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_ID", "REF_TABLE_NAME"), 
						Arrays.asList("ENQ_ID")));
		
		parent.addChildTable(
				buildChild("TB_ENQ_TABLE_LINKS_MASTER", "TB_ENQ_TABLE_LINKS_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_ID", "TABLE1_PRODUCTID", "TABLE2_PRODUCTID"), 
						Arrays.asList("ENQ_ID")));
		
		ChildTable child = buildChild("TB_ENQ_LINKAGES_MASTER", "TB_ENQ_LINKAGES_HISTORY", "AUDITID", null, 
				Arrays.asList("ENQ_LINKAGE_ID"), 
				Arrays.asList("ENQ_ID"));
		parent.addChildTable(child);
		
		child.addGrandChildTable(
				buildGrandChild("TB_ENQ_FIELD_LINKAGES_MASTER", "TB_ENQ_FIELD_LINKAGES_HISTORY", "AUDITID", null, 
						Arrays.asList("ENQ_LINKAGE_ID", "ENQ_FIELD_ID", "LINKED_ENQ_FIELD_ID"), 
						Arrays.asList("ENQ_LINKAGE_ID")));
		
		
		parent.addChildTable(buildChild("TB_ENQ_GROUP_RANGE_MASTER", "TB_ENQ_GROUP_RANGE_HISTORY", "AUDITID", null, 
				Arrays.asList("ENQ_ID", "ID"), 
				Arrays.asList("ENQ_ID")));
		
		
		parent.addChildTable(
				buildChild("TB_ENQ_FILTER_MASTER", "TB_ENQ_FILTER_HISTORY", "AUDITID", null, 
						Arrays.asList("SEQ_NO", "ENQ_FIELD_ID", "ENQ_ID"), 
						Arrays.asList("ENQ_ID")));
		
		return enquiryEntity;
	}
	
	private static ParentTable buildParent(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, 
			List<String> primaryColumns){
		
		ParentTable parent = new ParentTable();
		Table table = buildTable(tableName, historyTableName, auditColumnName, nonHashingColumns, primaryColumns);

		parent.setName(table.getName());
		parent.setHistoryName(table.getHistoryName());
		parent.setAuditColumnName(table.getAuditColumnName());
		parent.setNonHashingColumnNames(table.getNonHashingColumnNames());
		parent.setPrimaryColumnNames(table.getPrimaryColumnNames());
		
		return parent;
	}
	
	private static ChildTable buildChild(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, 
			List<String> primaryColumns, List<String> foreignColumsNames){
		
		ChildTable child = new ChildTable();
		Table table = buildTable(tableName, historyTableName, auditColumnName, nonHashingColumns, primaryColumns);

		child.setName(table.getName());
		child.setHistoryName(table.getHistoryName());
		child.setAuditColumnName(table.getAuditColumnName());
		child.setNonHashingColumnNames(table.getNonHashingColumnNames());
		child.setPrimaryColumnNames(table.getPrimaryColumnNames());
		child.setForeignColumnNames(foreignColumsNames);
		
		return child;
	}
	
	private static GrandChildTable buildGrandChild(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, 
			List<String> primaryColumns, List<String> foreignColumsNames){
		
		GrandChildTable grandChild = new GrandChildTable();
		Table table = buildTable(tableName, historyTableName, auditColumnName, nonHashingColumns, primaryColumns);

		grandChild.setName(table.getName());
		grandChild.setHistoryName(table.getHistoryName());
		grandChild.setAuditColumnName(table.getAuditColumnName());
		grandChild.setNonHashingColumnNames(table.getNonHashingColumnNames());
		grandChild.setPrimaryColumnNames(table.getPrimaryColumnNames());
		grandChild.setForeignColumnNames(foreignColumsNames);
		
		return grandChild;
	}
	
	private static GreatGrandChildTable buildGreatGrandChild(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, 
			List<String> primaryColumns, List<String> foreignColumsNames){
		
		GreatGrandChildTable greatGrandChild = new GreatGrandChildTable();
		Table table = buildTable(tableName, historyTableName, auditColumnName, nonHashingColumns, primaryColumns);

		greatGrandChild.setName(table.getName());
		greatGrandChild.setHistoryName(table.getHistoryName());
		greatGrandChild.setAuditColumnName(table.getAuditColumnName());
		greatGrandChild.setNonHashingColumnNames(table.getNonHashingColumnNames());
		greatGrandChild.setPrimaryColumnNames(table.getPrimaryColumnNames());
		greatGrandChild.setForeignColumnNames(foreignColumsNames);
		
		return greatGrandChild;
	}
	
	private static Table buildTable(String tableName, String historyTableName, String auditColumnName, List<String> nonHashingColumns, 
			List<String> primaryColumns){
		
		Table table = new ChildTable();
		String childTableName = tableName;
		String childHistoryName = historyTableName;
		String childAuditColumnName = auditColumnName;

		table.setName(childTableName);
		table.setHistoryName(childHistoryName);
		table.setAuditColumnName(childAuditColumnName);
		table.setNonHashingColumnNames(nonHashingColumns == null ? defaultNonHashingColumns : nonHashingColumns);
		table.setPrimaryColumnNames(primaryColumns);
		
		return table;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Table getParent() {
		return parent;
	}

	public static String getTestingQuery(String entityName){
		String query = 
				"--Parent/Child Table\n " +
						"Select table_name " +
						"from tb_ent_master " +
						"where entity_name = '???' " +
						"minus " +
						"Select Table_name " +
						"from all_tables " +
						"where table_name in " +
						"(Select table_name " +
						"from tb_ent_master " +
						"where entity_name = '???'); " +
						" \n " +
						"--Audit Table\n " +
						"Select history_table_name " +
						"from tb_ent_master " +
						"where entity_name = '???' " +
						"minus " +
						"Select Table_name " +
						"from all_tables " +
						"where table_name in " +
						"(Select history_table_name " +
						"from tb_ent_master " +
						"where entity_name = '???'); " +
						"  " +
						"--Audit Columns\n " +
						"Select history_table_name,AUDIT_COLUMN_NAME " +
						"from tb_ent_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select AUDIT_COLUMN_NAME from tb_ent_master where entity_name = '???') " +
						"and table_name in (Select HISTORY_TABLE_NAME from tb_ent_master where entity_name = '???'); " +
						" \n " +
						"--Parent/Child Columns Details\n  " +
						"Select column_name " +
						"from tb_ent_col_master " +
						"where table_name in (Select table_name from tb_ent_master where entity_name = '???') " +
						"minus " +
						"Select column_name " +
						"from all_tab_cols " +
						"where table_name in (Select table_name from tb_ent_master where entity_name = '???'); " +
						" \n " +
						"--Primary Keys\n " +
						"Select Table_name, COLUMN_NAME " +
						"from tb_ent_pk_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select COLUMN_NAME from tb_ent_pk_master where entity_name = '???') " +
						"and TABLE_NAME in (Select Table_name from tb_ent_pk_master where entity_name = '???'); " +
						" \n " +
						"--Parent Refering Keys\n " +
						"Select Table_name, TABLE_COLUMN_NAME " +
						"from tb_ent_detail_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select COLUMN_NAME from tb_ent_detail_master where entity_name = '???') " +
						"and TABLE_NAME in (Select Table_name from tb_ent_detail_master where entity_name = '???'); " +
						" \n " +
						"--Child Reference Keys\n " +
						"Select CHILD_TABLE_NAME, CHILD_TBL_COL_NAME " +
						"from tb_ent_detail_master " +
						"where ENTITY_NAME = '???' " +
						"minus " +
						"Select table_name, column_name " +
						"from all_tab_cols " +
						"where column_name in (Select CHILD_TBL_COL_NAME from tb_ent_detail_master where entity_name = '???') " +
						"and TABLE_NAME in (Select CHILD_TABLE_NAME from tb_ent_detail_master where entity_name = '???'); " ;
		
		System.out.println(query.replace("???", entityName));
		System.out.println("------------");
		return query.replace("???", entityName);
		
	}
}
