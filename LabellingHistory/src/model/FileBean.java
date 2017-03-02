package model;

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

import utility.CollectionUtility;
import utility.StringUtility;

public class FileBean implements Comparable<FileBean>{
	private final char EXTENSTION_SEPARATOR = '.';
	private final char FILE_SEPARATOR = File.separatorChar;
	private String name;
	private String absolutePath;
	private long lastModified;
	private String extension;
	private long length;
	private String relativePath;
	
	public long length(){
		return length;
	}
	
	public FileBean relativePath(String relativePath) {
		this.relativePath = relativePath;
		return this;
	}
	
	public String relativePath() {
		return this.relativePath;
	}

	public FileBean length(long length){
		this.length = length;
		return this;
	}
	
	public String extension(){
		return extension;
	}
	
	public long lastModified(){
		return lastModified;
	}
	
	public FileBean lastModified(long lastModified){
		this.lastModified = lastModified;
		return this;
	}
	
	public String name(){
		return name;
	}
	
	public String absolutePath(){
		return absolutePath;
	}
	
	public FileBean name(String name){
		this.name = name;
		return extension(name);
	}
	
	private FileBean extension(String name){
		extension = StringUtility.getLastPartAfterDelimiter(name, EXTENSTION_SEPARATOR);
		return this;
	}
	
	public FileBean absolutePath(String absolutePath){
		this.absolutePath = absolutePath;
		return this;
	}

	@Override
	public int compareTo(FileBean o) {
		return this.absolutePath().compareTo(o.absolutePath());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((absolutePath == null) ? 0 : absolutePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileBean other = (FileBean) obj;
		if (absolutePath == null) {
			if (other.absolutePath != null)
				return false;
		} else if (!absolutePath.equals(other.absolutePath))
			return false;
		return true;
	}
	
	public boolean isAnyFolderPresent(Set<String> folderSet) {
		Set<String> folderNameSet = folderNameSet();
		if(CollectionUtility.isNonEmpty(folderSet)){
			for(String folder: folderSet){
				if(folderNameSet.contains(folder)){
	    			return true;
	    		}
	    	}
		}
    	return false;
	}
	
	private Set<String> folderNameSet(){
		return CollectionUtility.getSetFromArray(absolutePath.split(Pattern.quote(File.separator)));
	}
	
	public boolean isExtensionPresent(Set<String> extensionSet) {
		if(CollectionUtility.isNonEmpty(extensionSet) && extension != null){
			for(String ext: extensionSet){
	    		if(extension.equalsIgnoreCase(ext)){
	    			return true;
	    		}
	    	}
		}
    	return false;
	}
	
	public boolean isModifiedAfter(Date date){
		return lastModified() > date.getTime();
	}
	
	public boolean isModifiedAfter(long time){
		return lastModified() > time;
	}
	
	public String relativePathWithoutFileName(){
		String relativeWithoutName = relativePath.replace(name, "");
		if(relativeWithoutName.length() > 0)
			relativeWithoutName = StringUtility.removeLastPartAfterDelimiter(relativeWithoutName, File.separator);
		return relativeWithoutName;
	}
}