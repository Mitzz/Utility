package model;

public class TbEntMasterModel {

	private String tableName;
	private boolean isPrimary;
	private String historyTableName;
	private String labelTableName;
	private String tableCaption;
	private String auditColumnName;

	public String getTableName() {
		return tableName;
	}

	public TbEntMasterModel tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public TbEntMasterModel primary(boolean isPrimary) {
		this.isPrimary = isPrimary;
		return this;
	}

	public String getHistoryTableName() {
		return historyTableName;
	}

	public TbEntMasterModel historyTableName(String historyTableName) {
		this.historyTableName = historyTableName;
		return this;
	}

	public String getLabelTableName() {
		if(labelTableName.length() > 30) labelTableName = labelTableName.replace("MASTER_", "");
		return labelTableName;
	}

	public TbEntMasterModel labelTableName(String labelTableName) {
		this.labelTableName = labelTableName;
		return this;
	}

	public String getTableCaption() {
		return tableCaption;
	}

	public TbEntMasterModel tableCaption(String tableCaption) {
		this.tableCaption = tableCaption;
		return this;
	}

	public String getAuditColumnName() {
		return auditColumnName;
	}

	public TbEntMasterModel auditColumnName(String auditColumnName) {
		this.auditColumnName = auditColumnName;
		return this;
	}

}
