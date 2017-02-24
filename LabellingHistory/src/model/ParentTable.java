package model;

import java.util.ArrayList;
import java.util.List;

public class ParentTable extends Table{

	private List<ChildTable> childTables = new ArrayList<ChildTable>();
	
	public ParentTable addChildTable(ChildTable child){
		childTables.add(child);
		return this;
	}

	public List<ChildTable> getChildTables() {
		return childTables;
	}
	
	
}
