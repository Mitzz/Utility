package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import work.Program;

public class Table {

	protected String name;
	protected String historyName;
	protected List<String> primaryColumnNames;
	public Map<String, ColumnModel> primaryColumnDetails;
	protected String auditColumnName;
	protected List<String> nonHashingColumnNames;
	private Map<String, String> foreignColumnMapping;
	private Map<String, String> columnNameFixedValueMap;
	private List<Table> childs = new ArrayList<Table>();
	private String actionColumnName;
	
	public Table addChild(Table table){
		childs.add(table);
		return this;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHistoryName() {
		return historyName;
	}
	public void setHistoryName(String historyName) {
		this.historyName = historyName;
	}
	public List<String> getPrimaryColumnNames() {
		return primaryColumnNames;
	}
	public void setPrimaryColumnNames(List<String> primaryColumnNames) {
		this.primaryColumnNames = primaryColumnNames;
	}
	public String getAuditColumnName() {
		return auditColumnName;
	}
	public void setAuditColumnName(String auditColumnName) {
		this.auditColumnName = auditColumnName;
	}
	public List<String> getNonHashingColumnNames() {
		return nonHashingColumnNames;
	}
	public void setNonHashingColumnNames(List<String> nonHashingColumnNames) {
		this.nonHashingColumnNames = nonHashingColumnNames;
	}
	
//	private String getLabelTableName() {
//		String label = name + "_LABEL";
//		if(label.length() > 30) label = label.replace("MASTER_", "");
//		return label;
//	}
	
	public Table populateColumnDetails(){
		this.primaryColumnDetails = Program.getColumnDetails(null, name, primaryColumnNames);
		return this;
	}
	/*if(labelTableName.length() > 30) labelTableName = labelTableName.replace("MASTER_", "");
	return labelTableName;*/
	public Map<String, String> getColumnNameFixedValueMap() {
		return columnNameFixedValueMap;
	}
	public void setColumnNameFixedValueMap(Map<String, String> columnNameFixedValueMap) {
		this.columnNameFixedValueMap = columnNameFixedValueMap;
	}
	public Map<String, String> getForeignColumnMapping() {
		return foreignColumnMapping;
	}
	public void setForeignColumnMapping(Map<String, String> foreignColumnMapping) {
		this.foreignColumnMapping = foreignColumnMapping;
	}
	public List<Table> getChilds() {
		return childs;
	}
	public void setChilds(List<Table> childs) {
		this.childs = childs;
	}

	public String getActionColumnName() {
		return actionColumnName;
	}

	public void setActionColumnName(String actionColumnName) {
		this.actionColumnName = actionColumnName;
	}
}
