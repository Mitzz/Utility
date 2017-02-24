package labelling;

import java.util.Set;

public class Entity {
	private String name;
	private Set<EntityRecord> primaryRecords;
	private Set<EntityRecord> auditRecords;
}

class EntityRecord{
	
	//Entity Record consists of Parent, Child and Grandchild Records
	
}

class TableRecord {
	private Set<Column> columns;

	public String getHashValue() {
		StringBuilder hash = new StringBuilder();
		for (Column column : columns) {
			if (column.isHash()) {
				String str = column.getValueInString();
				hash.append(str);
			}
		}
		
		return new String(hash);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPrimaryColumnValue() == null) ? 0 : getPrimaryColumnValue().hashCode());
		return result;
	}
	
	public String getPrimaryColumnValue(){
		StringBuilder str = new StringBuilder();
		for(Column column: columns){
			if(column.isPrimary()){
				str.append(column.getValueInString());
			}
		}
		return new String(str);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableRecord other = (TableRecord) obj;
		if (columns == null) {
			if (other.columns != null)
				return false;
		} else if(!this.getPrimaryColumnValue().equals(other.getPrimaryColumnValue())){
			return false;	
		}
			
		return true;
	}
}

class Column {
	private String name;
	private Object value;
	private String type;
	private boolean isHash;
	private boolean isPrimary;

	public String getName() {
		return name;
	}

	public String getValueInString() {
		return value.toString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isHash() {
		return isHash;
	}

	public void setHash(boolean isHash) {
		this.isHash = isHash;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

}