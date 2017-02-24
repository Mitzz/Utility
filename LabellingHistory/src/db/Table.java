package db;

import java.util.SortedSet;
import java.util.TreeSet;

import utility.CollectionUtility;

public class Table implements Comparable<Table>{
	private String tableName;
	private SortedSet<String> primaryColumnNameSet = new TreeSet<String>();
	private String primaryKeyConstraintName;
	
	public Table() {
		super();
	}
	
	public String getSelectSqlForInsertion() {
		if(isPrimaryColumnPresent())
			return "SELECT * FROM " + tableName + " ORDER BY " + getPrimaryColumnNameCommaSeparated();
		return "SELECT * FROM " + tableName;
	}

	private boolean isPrimaryColumnPresent() {
		return getPrimaryColumnNameSet() != null && getPrimaryColumnNameSet().size() != 0;
	}

	public Table(String tableName, SortedSet<String> primaryColumnNameSet) {
		this.tableName = tableName;
		this.primaryColumnNameSet = primaryColumnNameSet;
	}
	
	public Table addPrimaryColumn(String columnName) {
		primaryColumnNameSet.add(columnName);
		return this;
	}
	
	public Table(String tableName) {
		this.tableName = tableName;
	}

	public String getName() {
		return tableName;
	}

	public SortedSet<String> getPrimaryColumnNameSet() {
		return primaryColumnNameSet;
	}

	private String getPrimaryColumnNameCommaSeparated() {
		return CollectionUtility.join(primaryColumnNameSet, ",");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	@Override
	public int compareTo(Table o) {
		return this.tableName.compareTo(o.tableName);
	}

	public String primaryKeyConstraintName() {
		return primaryKeyConstraintName;
	}

	public Table primaryKeyConstraintName(String primaryKeyConstraintName) {
		this.primaryKeyConstraintName = primaryKeyConstraintName;
		return this;
	}
	
	public String primaryKeyConstraintDdl(){
		return String.format("ALTER TABLE %s ADD CONSTRAINT %s PRIMARY KEY (%s)", tableName, primaryKeyConstraintName, CollectionUtility.join(primaryColumnNameSet, ","));
	}
}

