package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChildTable extends Table {

	private List<String> foreignColumnNames;
	private Map<String, String> foreignColumnMapping;
	private Map<String, String> columnNameFixedValueMap;
	private List<GrandChildTable> grandChildTables = new ArrayList<GrandChildTable>();

	public List<String> getForeignColumnNames() {
		return foreignColumnNames;
	}

	public void setForeignColumnNames(List<String> foreignColumnNames) {
		this.foreignColumnNames = foreignColumnNames;
	}
	
	public ChildTable addGrandChildTable(GrandChildTable child){
		grandChildTables.add(child);
		return this;
	}

	public List<GrandChildTable> getGrandChildTables() {
		return grandChildTables;
	}
	
	
}
