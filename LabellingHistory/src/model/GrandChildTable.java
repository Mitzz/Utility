package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrandChildTable extends ChildTable{

	private String parentTableName;
	private List<String> foreignColumnNames;
	private Map<String, String> foreignColumnMapping;
	private List<GreatGrandChildTable> greatGrandChildTables = new ArrayList<GreatGrandChildTable>();

	public List<String> getForeignColumnNames() {
		return foreignColumnNames;
	}

	public void setForeignColumnNames(List<String> foreignColumnNames) {
		this.foreignColumnNames = foreignColumnNames;
	}
	
	public GrandChildTable addGrandChildTable(GreatGrandChildTable child){
		greatGrandChildTables.add(child);
		return this;
	}

	public List<GreatGrandChildTable> getGreatGrandChildTables() {
		return greatGrandChildTables;
	}

	public String getParentTableName() {
		return parentTableName;
	}

	public GrandChildTable setParentTableName(String parentTableName) {
		this.parentTableName = parentTableName;
		return this;
	}
	
	
}
