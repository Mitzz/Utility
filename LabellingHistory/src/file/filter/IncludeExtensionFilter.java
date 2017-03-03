package file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import file.model.FileConstants;
import utility.CollectionUtility;
import utility.StringUtility;

public class IncludeExtensionFilter implements FileFilter{
	
	private Set<String> extensions;
	
	public IncludeExtensionFilter(String extension){
		this.extensions =  CollectionUtility.createSortedSet(extension);
	}
	
	public IncludeExtensionFilter(String ... extension){
		this.extensions =  CollectionUtility.getSetFromArray(extension);
	}
	
	public IncludeExtensionFilter(Set<String> extensions){
		this.extensions =  extensions;
	}
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()) return true;
		String extension = StringUtility.getLastPartAfterDelimiter(pathname.getName(), FileConstants.EXTENSTION_SEPARATOR);
		
		return CollectionUtility.isNonEmpty(extensions) && this.extensions.contains(extension);
	}

	public Set<String> getExtensions() {
		return extensions;
	}

	
}
