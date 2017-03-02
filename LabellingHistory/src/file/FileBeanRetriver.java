package file;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import model.FileBean;

public class FileBeanRetriver {

	private File directory;
	
	public FileBeanRetriver(String directory){
		this.directory = new File(directory);
	}
	
	public FileBeanRetriver(File directory){
		this.directory = directory;
	}
	
	public SortedSet<FileBean> get(){
		SortedSet<FileBean> fileBeanSet = getWithoutRelativePath();
		populateRelativePath(fileBeanSet);
	    return fileBeanSet;
	}
	
	public SortedSet<FileBean> getWithoutRelativePath(){
		SortedSet<FileBean> fileBeanSet = new TreeSet<FileBean>();
		for (final File fileEntry : directory.listFiles())
	    	if (fileEntry.isDirectory()) 	fileBeanSet.addAll(new FileBeanRetriver(fileEntry).get());
	        else 							fileBeanSet.add(new FileBean().name(fileEntry.getName()).absolutePath(fileEntry.getAbsolutePath()).lastModified(fileEntry.lastModified()).length(fileEntry.length()));
	    
	    return fileBeanSet;
	}
	
	private void populateRelativePath(SortedSet<FileBean> fileBeanSet){
		for(FileBean bean: fileBeanSet){
			bean.relativePath(bean.absolutePath().replace(directory.getAbsolutePath() + File.separator, "")/*.replace(bean.name(), "")*/);
			System.out.println(bean.relativePath() + "~~" + bean.relativePathWithoutFileName());
		}
	}
}
