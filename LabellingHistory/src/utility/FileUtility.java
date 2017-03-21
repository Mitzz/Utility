package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import file.filter.ExcludeFolderFilter;
import file.filter.IncludeExtensionFilter;
import file.filter.ModifiedAfterFilter;
import file.model.FileBeanRetriver;
import file.model.FileCopy;
import model.FileBean;

public class FileUtility {
	public static final String NEW_LINE = System.getProperty("line.separator");

	
	public static Map<String,String> getFileNameToFilePath(final File folder) {
		Map<String, String> fileNameToFilePath = new HashMap<String, String>();
		SortedSet<FileBean> fileBeanSet = getFileBean(folder);
	    for (final FileBean fileBean : fileBeanSet) {
	    	fileNameToFilePath.put(fileBean.name(), fileBean.absolutePath());
	    }
	    return fileNameToFilePath;
	}
	
	public static SortedSet<FileBean> getFileBean(final File folder) {
		return new FileBeanRetriver(folder).get();
	}
	
	public static SortedSet<FileBean> getFileBean(final String folder) {
		return new FileBeanRetriver(folder).get();
	}
	
	public static SortedSet<FileBean> getFileBean(final File folder, Set<String> excludeFolderSet) {
		SortedSet<FileBean> fileBeanSet = getFileBean(folder);
		SortedSet<FileBean> fileBeanSet1 = new TreeSet<FileBean>();
		for(FileBean fileBean: fileBeanSet){
			if(!fileBean.isAnyFolderPresent(excludeFolderSet)){
				fileBeanSet1.add(fileBean);
			}
		}
		return fileBeanSet1;
	}
	
	public static SortedSet<FileBean> getFileBean(final File folder, Set<String> excludeFolderSet, Set<String> extensionSet) {
		SortedSet<FileBean> fileBeanSet = getFileBean(folder);
		SortedSet<FileBean> fileBeanSet1 = new TreeSet<FileBean>();
		for(FileBean fileBean: fileBeanSet){
			if(!fileBean.isAnyFolderPresent(excludeFolderSet) && fileBean.isExtensionPresent(extensionSet)){
				fileBeanSet1.add(fileBean);
			}
		}
		return fileBeanSet1;
	}
	
	public static SortedSet<FileBean> getFileBean(final File folder, Set<String> excludeFolderSet, Set<String> includeFileExtensionSet, Date date) {
		SortedSet<FileBean> fileBeanSet = getFileBean(folder);
		SortedSet<FileBean> fileBeanSet1 = new TreeSet<FileBean>();
		if(date != null && excludeFolderSet != null && includeFileExtensionSet != null){
			long time = date.getTime();
			for(FileBean fileBean: fileBeanSet){
				if(fileBean.isModifiedAfter(time) && fileBean.isExtensionPresent(includeFileExtensionSet) && !fileBean.isAnyFolderPresent(excludeFolderSet)){
					fileBeanSet1.add(fileBean);
				}
			}
		}
		return fileBeanSet1;
		
	}
	
	public static Map<String,String> getFileNameToFilePath(final String folder) {
		return getFileNameToFilePath(new File(folder));
	}

	public static void createBackup(String fileName, String searchDirectory, String backupDirectory) throws IOException {
		Set<String> s = new HashSet<String>();
		s.add(fileName);
		createBackup(s, searchDirectory, backupDirectory);
	}
	
	public static void createBackup(Set<String> fileNameList, String searchDirectory, String backupDirectory) throws IOException {
		Map<String, String> fileNameToFilePath = getFileNameToFilePath(searchDirectory);
		for(String fileName: fileNameList){
			if(fileNameToFilePath.containsKey(fileName)){
				new FileCopy(fileNameToFilePath.get(fileName), backupDirectory + File.separator + fileName).copy();
			}
		}
	}

	public static List<FileBean> getFileBeanListAfterGivenDate(Date date, String searchDirectory, Set<String> excludeFolderNameSet, Set<String> extensionSet){
		return new ArrayList<FileBean>(getFileBean(new File(searchDirectory), excludeFolderNameSet, extensionSet, date));
	}

	public static void writeToFile(String file, String content) throws IOException {
		PrintWriter out = new PrintWriter(file);
		out.println(content);
		if(out != null) out.close();
	}
	
	public static void writeToFile(String file, String content, Charset charset) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
			    new FileOutputStream(file), charset)));
		out.println(content);
		if(out != null) out.close();
	}
	
	public static void appendToFile(String file, String content) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.println(content);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	    if(out != null) out.close();
	}
	
	public static String readFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}

	public static void main(String[] args) throws IOException {
		System.out.println("----------Start: " + new Date() + "----------");
		final String WORKSPACE_PATH = "D:/HUBSWorkspace/Workspace";
//		final String SVN_PATH = "D:/SVN/16.2.1.0_WIP/SOURCE";
		final String SVN_PATH = "D:/SVN/16.2.2.0_DEV_WIP";
//		Map<String, String> listFilesForFolder = FileUtility.getFileNameToFilePath("C:/Users/mithul.bhansali/Desktop/Patch");
//		System.out.println(listFilesForFolder.keySet());
		
//		logFileChanges(WORKSPACE_PATH);
//		logFileChanges(SVN_PATH);
//		logFileChanges("D:/SVN/16.2.2.0_DEV_WIP/SOURCE");
		diff(WORKSPACE_PATH, SVN_PATH);
		
//		List<String> fileNameList = getDuplicateFileNameList(WORKSPACE_PATH);
//		System.out.println(fileNameList.size());
//		FileUtility.createBackup(new HashSet<String>(Arrays.asList("web.xml")), WORKSPACE_PATH, "D:/f1");
//		FileUtility.createBackup(listFilesForFolder.keySet(), "D:/HUBSWorkspace/Workspace", "D:/ConflictFiles/Backup");
//		conflictFileBackup();
		System.out.println("------------End: " + new Date() + "----------");
	}
	
	private static void conflictFileBackup() throws IOException {
		final String WORKSPACE_PATH = "D:/HUBSWorkspace/Workspace";
		final String CONFLICT_FILE_BACKUP_PATH = "D:/ConflictFiles/Backup";
		
		FileUtility.createBackup(new HashSet<String>(Arrays.asList("web.xml")), WORKSPACE_PATH, CONFLICT_FILE_BACKUP_PATH);
	}
	
	private static void logFileChanges(String filePath){

		final int YEAR = 2017;
		final int MONTH = Calendar.MARCH;
		final int DATE = 21;
		final int HOUR_OF_DAY = 9;
		final int MINUTE = 00;
		
		Calendar instance = Calendar.getInstance();
		instance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);

		ModifiedAfterFilter modifiedAfterFilter = new ModifiedAfterFilter(instance.getTime());
		ExcludeFolderFilter excludeFolderFilter = new ExcludeFolderFilter(CollectionUtility.createSortedSet("bin", "svn", "buildFiles",".metadata", "LabellingHistory", "Z"));
		IncludeExtensionFilter extensionFilter = new IncludeExtensionFilter("java", "js", "jsp", "class", "xml", "properties", "sql", "css", "html");
		
		FileBeanRetriver fileBeanRetriver = 
				new FileBeanRetriver(filePath)
					.addFileFilters(modifiedAfterFilter, excludeFolderFilter, extensionFilter);

		SortedSet<FileBean> fileBeanSet = fileBeanRetriver.get();
		String log = new String(new StringBuilder().append(
				String.format("---- File Changes Details after %s for Folder Path %s ----\nExcluded Folder: %s\nIncluded File Extension: %s\nFile Size: %d\nFile Name: %s",
					instance.getTime(),
					filePath, 
					excludeFolderFilter.getFolderSet(), 
					extensionFilter.getExtensions(), 
					fileBeanSet.size(), 
					fileBeanSet)));
		
		System.out.println(log);
		
	
	}

	private static void logWorkspaceFileChanges(){
		final String WORKSPACE_PATH = "D:/HUBSWorkspace/Workspace";
//		final String WORKSPACE_PATH = "D:/SVN/16.2.1.0_WIP/SOURCE";
		final int YEAR = 2017;
		final int MONTH = Calendar.MARCH;
		final int DATE = 1;
		final int HOUR_OF_DAY = 9;
		final int MINUTE = 00;
		
		Calendar instance = Calendar.getInstance();
		instance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);
		
		SortedSet<String> excludedFolderNameSet = CollectionUtility.createSortedSet("bin", "svn", "buildFiles",".metadata", "LabellingHistory", "Z");
		SortedSet<String> includedFileExtensionNameSet = CollectionUtility.createSortedSet("java", "js", "jsp", "class", "xml", "properties", "sql", "html", "css");
//		SortedSet<String> includedFileExtensionNameSet = CollectionUtility.createSortedSet("sql");
		List<FileBean> fileBeanListAfterGivenDate = getFileBeanListAfterGivenDate(instance.getTime(), WORKSPACE_PATH, excludedFolderNameSet, includedFileExtensionNameSet);
		List<String> fileNameList = new ArrayList<String>();
		
		for(FileBean fileBean: fileBeanListAfterGivenDate)
			fileNameList.add(fileBean.absolutePath());
		
		System.out.println(instance.getTime());
		String log = new String(new StringBuilder().append(
				String.format("File Changes Details\nFolder Path: %s\nExcluded Folder: %s\nIncluded File Extension: %s\nFile Size: %d\nFile Name: %s",
					WORKSPACE_PATH, 
					excludedFolderNameSet, 
					includedFileExtensionNameSet, 
					fileNameList.size(), 
					fileNameList)));
		
		System.out.println(log);
		
	}
	
	private static void diff(String WORKSPACE_PATH, String SVN_PATH){
		final int YEAR = 2016;
		final int MONTH = Calendar.FEBRUARY;
		final int DATE = 1;
		final int HOUR_OF_DAY = 19;
		final int MINUTE = 30;
		
		Calendar instance = Calendar.getInstance();
		instance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);
		
		SortedSet<String> excludedFolderNameSet = 
				CollectionUtility.createSortedSet("bin", "svn", ".metadata", "LabellingHistory", "BLOB", "build", ".settings","Z", "ArchivalSystem");
		
		diff(WORKSPACE_PATH, SVN_PATH, instance, excludedFolderNameSet, getFileExtensionNameSetToBeIncluded());
	}
	
	private static SortedSet<String> getFileExtensionNameSetToBeIncluded(){
		return CollectionUtility.createSortedSet("java", "js", "jsp", "class", "xml", "properties", "sql", "css", "html");
	}
	
	private static void diff(String pathX, String pathY, Calendar modifiedAfter, SortedSet<String> excludedFolderNameSet, SortedSet<String> includedFileExtensionNameSet){
		
		
		List<FileBean> pathXFileBeanList = getFileBeanListAfterGivenDate(modifiedAfter.getTime(), pathX, excludedFolderNameSet, includedFileExtensionNameSet);
		SortedSet<FileBean> pathYFileBeanSet = getFileBean(new File(pathY), excludedFolderNameSet, includedFileExtensionNameSet, modifiedAfter.getTime());
		
		StringBuilder builder = new StringBuilder();
		builder.append(
				String.format("Workspace and SVN File Difference Details\nWorkspace Path: %s\nSVN Path:%s\nExcluded Folder: %s\nIncluded File Extension: %s\n",
					pathX, 
					pathY,
					excludedFolderNameSet, 
					includedFileExtensionNameSet));
		
		for(FileBean pathXFile: pathXFileBeanList){
			boolean isFound = false;
			for(FileBean pathYFileBean: pathYFileBeanSet){
				if(pathXFile.name().equals(pathYFileBean.name()) && pathXFile.relativePath().equals(pathYFileBean.relativePath())){
					isFound = true;
					if(pathXFile.length() != pathYFileBean.length()){
						builder.append(pathXFile.absolutePath() + "____" + pathXFile.name() + " - ");
						break;
					}
				}
			}
			if(!isFound)
				builder.append(pathXFile.absolutePath() + "****" + pathXFile.name() + " - ");
		}
		System.out.println(builder);
	}
	
	private static List<String> getDuplicateFileNameList(String folderPath) {
		Map<String, Integer> fileNameToOccurenceCount = getFileNameToOccurenceCount(folderPath);
		List<String> fileNameList = new ArrayList<String>();
		for(String fileName: fileNameToOccurenceCount.keySet()){
			if(fileNameToOccurenceCount.get(fileName) > 1){
				fileNameList.add(fileName); 
			}
		}
		return fileNameList;
	}

	private static Map<String, Integer> getFileNameToOccurenceCount(String searchDirectory) {
		return getFileNameToOccurenceCount(new File(searchDirectory));
	}

	private static Map<String, Integer> getFileNameToOccurenceCount(File folder) {
		Map<String, Integer> fileNameToOccurenceCount = new HashMap<String, Integer>();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	getFileNameToOccurenceCount(fileEntry, fileNameToOccurenceCount);
	        } else {
	        	String fileName = fileEntry.getName();
	        	if(fileNameToOccurenceCount.containsKey(fileName)){
	        		Integer occurenceCount = fileNameToOccurenceCount.get(fileName);
	        		fileNameToOccurenceCount.put(fileName, occurenceCount + 1);
	        	} else {
	        		fileNameToOccurenceCount.put(fileName, 1);
	        	}
	        }
	    }
	    return fileNameToOccurenceCount;
	}
	
	private static void getFileNameToOccurenceCount(File folder, Map<String, Integer> fileNameToOccurenceCount) {
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	getFileNameToOccurenceCount(fileEntry, fileNameToOccurenceCount);
	        } else {
	        	String fileName = fileEntry.getName();
	        	if(fileNameToOccurenceCount.containsKey(fileName)){
	        		Integer occurenceCount = fileNameToOccurenceCount.get(fileName);
	        		fileNameToOccurenceCount.put(fileName, occurenceCount + 1);
	        	} else {
	        		fileNameToOccurenceCount.put(fileName, 1);
	        	}
	        }
	    }
	    
	}
	
	public static void createFile(File absoluteFilePath) throws IOException{
		if(!absoluteFilePath.exists())
			absoluteFilePath.createNewFile();
	}
	
	public static void createDirectory(File absoluteDirectoryPath) throws IOException{
		if(!absoluteDirectoryPath.exists())
			absoluteDirectoryPath.mkdirs();
	}
	
	public static void createFileAndDirectory(File file) throws IOException{
		createDirectory(file.getParentFile());
		createFile(file);
	}
}

