package file.model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import utility.CollectionUtility;
import file.filter.ExcludeFolderFilter;
import file.filter.IncludeExtensionFilter;
import file.filter.ModifiedAfterFilter;
import model.FileBean;

public class FileBeanRetriver {

	private File directory;
	private List<FileFilter> fileFilterList = new ArrayList<FileFilter>();
	
	public FileBeanRetriver addFileFilter(FileFilter fileFilter){
		fileFilterList.add(fileFilter);
		return this;
	}
	
	public FileBeanRetriver addFileFilters(FileFilter ... fileFilters){
		for(FileFilter fileFilter: fileFilters)
			fileFilterList.add(fileFilter);
		return this;
	}
	
	public FileBeanRetriver setFileFilterList(List<FileFilter> fileFilterList){
		this.fileFilterList = fileFilterList;
		return this;
	}
	
	public FileBeanRetriver(String directory){
		this.directory = new File(directory);
	}
	
	public FileBeanRetriver(File directory){
		this.directory = directory;
	}
	
	public SortedSet<FileBean> get(){
		SortedSet<FileBean> fileBeanSet = getWithoutRelativePath(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				boolean f = true;
				for(FileFilter filter: fileFilterList){
					if(!filter.accept(pathname)){
						f = false;
						break;
					};
				}
				return f;
			}
		});
		populateRelativePath(fileBeanSet);
	    return fileBeanSet;
	}
	
	public SortedSet<FileBean> get(FileFilter filter){
		SortedSet<FileBean> fileBeanSet = getWithoutRelativePath(filter);
		populateRelativePath(fileBeanSet);
	    return fileBeanSet;
	}
	
	private SortedSet<FileBean> getWithoutRelativePath(FileFilter filter) {
		SortedSet<FileBean> fileBeanSet = new TreeSet<FileBean>();
		for (final File fileEntry : directory.listFiles(filter))
	    	if (fileEntry.isDirectory()) 	fileBeanSet.addAll(new FileBeanRetriver(fileEntry).setFileFilterList(fileFilterList).get());
	        else 							fileBeanSet.add(new FileBean().name(fileEntry.getName()).absolutePath(fileEntry.getAbsolutePath()).lastModified(fileEntry.lastModified()).length(fileEntry.length()));
	    
	    return fileBeanSet;
	}

	private void populateRelativePath(SortedSet<FileBean> fileBeanSet){
		for(FileBean bean: fileBeanSet){
			bean.relativePath(bean.absolutePath().replace(directory.getAbsolutePath() + File.separator, "")/*.replace(bean.name(), "")*/);
		}
	}
	
	public static void main(String[] args) {
		IncludeExtensionFilter extensionFilter = new IncludeExtensionFilter("sql");
		final int YEAR = 2017;
		final int MONTH = Calendar.FEBRUARY;
		final int DATE = 28;
		final int HOUR_OF_DAY = 9;
		final int MINUTE = 00;
		
		Calendar instance = Calendar.getInstance();
		instance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);
		
		ModifiedAfterFilter modifiedAfter = new ModifiedAfterFilter(instance.getTime());
		FileBeanRetriver fileBeanRetriver = new FileBeanRetriver("D:\\SVN\\16.2.2.0_DEV_WIP");
		ExcludeFolderFilter excludeFolderFilter = new ExcludeFolderFilter(CollectionUtility.createSortedSet("Release"));
		
		fileBeanRetriver.addFileFilters(extensionFilter, modifiedAfter, excludeFolderFilter);
		
		SortedSet<FileBean> fileBeanSet = fileBeanRetriver.get();
		System.out.println(fileBeanSet.size());
		CollectionUtility.displayCollection(fileBeanRetriver.get());
//		
	}
}
