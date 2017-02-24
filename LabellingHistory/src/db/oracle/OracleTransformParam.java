package db.oracle;

public enum OracleTransformParam{
	PRETTY, SQLTERMINATOR, SEGMENT_ATTRIBUTES, STORAGE, TABLESPACE, CONSTRAINTS, REF_CONSTRAINTS, CONSTRAINTS_AS_ALTER;
	
	public String trueSql(){
		return "{CALL dbms_metadata.SET_TRANSFORM_PARAM(dbms_metadata.session_transform, '" + name() +"', true)}";
	}
	
	public String falseSql(){
		return "{CALL dbms_metadata.SET_TRANSFORM_PARAM(dbms_metadata.session_transform, '" + name() +"', false)}";
	}
}

