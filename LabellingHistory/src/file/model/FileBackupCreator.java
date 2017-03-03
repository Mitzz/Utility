package file.model;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import utility.CollectionUtility;
import model.FileBean;

public class FileBackupCreator {

	private Set<String> fileNameSet = new HashSet<String>();
	private String searchDirectory;
	private String backupDirectory;
	private boolean replicateDirectory;

	public FileBackupCreator(String searchDirectory, String backupDirectory) {
		this.searchDirectory = searchDirectory;
		this.backupDirectory = backupDirectory;
	}
	
	public FileBackupCreator(Set<String> fileNameSet, String searchDirectory, String backupDirectory) {
		this(searchDirectory, backupDirectory);
		setFileNameSet(fileNameSet);
	}

	public FileBackupCreator replicateDirectory(boolean replicate) {
		this.replicateDirectory = replicate;
		return this;
	}

	public void addFileName(String fileName) {
		fileNameSet.add(fileName);
	}

	public FileBackupCreator setFileNameSet(Set<String> fileNameSet) {
		this.fileNameSet = fileNameSet;
		return this;
	}

	public void backup() throws IOException {
		boolean isFileNamesAvailable = CollectionUtility.isNonEmpty(fileNameSet);
		if(isFileNamesAvailable) backupParticularFiles();
		else 					 backupAllFiles();
		
	}
	
	private void backupAllFiles() throws IOException {
		SortedSet<FileBean> fileBeanSet = new FileBeanRetriver(searchDirectory).get();
		for (FileBean fileBean : fileBeanSet)
			new FileCopy(fileBean.absolutePath(), getBackupDirectory(fileBean)).makeDirectory(replicateDirectory).copy();
	}
	
	private void backupParticularFiles() throws IOException {
		SortedSet<FileBean> fileBeanSet = new FileBeanRetriver(searchDirectory).get();
		for (FileBean fileBean : fileBeanSet)
			if (fileNameSet.contains(fileBean.name()))
				new FileCopy(fileBean.absolutePath(), getBackupDirectory(fileBean)).makeDirectory(replicateDirectory).copy();
			
	}
	
	private String getBackupDirectory(FileBean fileBean) {
		if(replicateDirectory)
			return backupDirectory + File.separator + fileBean.relativePathWithoutFileName();
		else 
			return backupDirectory;
		
	}

	public static void main(String[] args) throws IOException {
		String searchDirectory = "D:/SVN/16.2.2.0_DEV_WIP";
		String backupDirectory = "D:/f2/SVN";

		FileBackupCreator backupCreator = new FileBackupCreator(searchDirectory, backupDirectory);
		backupCreator.replicateDirectory = true;
		backupCreator.backup();
	}
}
