package model;

import java.util.ArrayList;
import java.util.List;

public class Entity {

	private String entityName;
	private List<TbEntMasterModel> entMasters = new ArrayList<TbEntMasterModel>();
	private TbEntColMaster entityColumnMaster = new TbEntColMaster();
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public List<TbEntMasterModel> getEntMasters() {
		return entMasters;
	}
	public void addEntMaster(TbEntMasterModel entMaster){
		entMasters.add(entMaster);
	}
	
	public void setEntMasters(List<TbEntMasterModel> entMasters) {
		this.entMasters = entMasters;
	}
	public TbEntColMaster getEntityColumnMaster() {
		return entityColumnMaster;
	}
	public void setEntityColumnMaster(TbEntColMaster entityColumnMaster) {
		this.entityColumnMaster = entityColumnMaster;
	}
	
	public String getParentTableName(){
		for(TbEntMasterModel model: entMasters){
			if(model.isPrimary()) return model.getTableName();
		}
		return null;
	}
	
	public List<String> getSecondaryTableNames(){
		List<String> tableNames = new ArrayList<String>();
		for(TbEntMasterModel model: entMasters){
			if(!model.isPrimary()) tableNames.add(model.getTableName());
		}
		return tableNames;
	}
}
