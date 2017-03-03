package file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
import java.util.regex.Pattern;

import utility.CollectionUtility;

public class ExcludeFolderFilter implements FileFilter{

	private Set<String> folderSet;
	
	public ExcludeFolderFilter(Set<String> folderNameSet) {
		this.folderSet = folderNameSet;
	}

	@Override
	public boolean accept(File pathname) {
		return !isAnyFolderPresent(pathname);
	}
	
	public boolean isAnyFolderPresent(File file) {
		Set<String> folderNameSet = folderNameSet(file);
		if(CollectionUtility.isNonEmpty(folderSet)){
			for(String folder: folderSet){
				if(folderNameSet.contains(folder)){
	    			return true;
	    		}
	    	}
		}
    	return false;
	}
	
	private Set<String> folderNameSet(File file){
		return CollectionUtility.getSetFromArray(file.getAbsolutePath().split(Pattern.quote(File.separator)));
	}

	public Set<String> getFolderSet() {
		return folderSet;
	}
}
