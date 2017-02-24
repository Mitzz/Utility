package db.oracle;

public enum OracleObjectType{
	VIEW("VIEW"), TYPE("TYPE"), TABLE("TABLE"), SEQUENCE("SEQUENCE"), PROCEDURE("PROCEDURE"), PACKAGE_BODY("PACKAGE BODY"), PACKAGE("PACKAGE"), LOB("LOB"), INDEX("INDEX"), CONSTRAINT("CONSTRAINT"), REF_CONSTRAINT("REF_CONSTRAINT"), FUNCTION("FUNCTION");
	
	private String name;
	
	private OracleObjectType(String name){
		this.name = name;
	}
	
	public String typeName(){
		return this.name;
	}
}
