package model;

import java.util.ArrayList;
import java.util.List;

public class TbEntColMaster {

	private List<String> tableNames = new ArrayList<String>();

	public List<String> getTableNames() {
		return tableNames;
	}

	public TbEntColMaster setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
		return this;
	}
	
	public TbEntColMaster addTableName(String tableName){
		tableNames.add(tableName);
		return this;
	}
}
