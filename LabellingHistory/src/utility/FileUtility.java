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
import java.io.PrintWriter;
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
		SortedSet<FileBean> fileBeanSet = new TreeSet<FileBean>();
		
	    for (final File fileEntry : folder.listFiles())
	    	if (fileEntry.isDirectory()) 	fileBeanSet.addAll(getFileBean(fileEntry));
	        else 							fileBeanSet.add(new FileBean().name(fileEntry.getName()).absolutePath(fileEntry.getAbsolutePath()).lastModified(fileEntry.lastModified()).length(fileEntry.length()));
	    
	    for(FileBean bean: fileBeanSet){
	    	bean.relativePath(bean.absolutePath().replace(folder.getAbsolutePath(), ""));
	    }
	    return fileBeanSet;
	}
	
	public static SortedSet<FileBean> getFileBean(final String folder) {
		return getFileBean(new File(folder));
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
				copyFile(new File(fileNameToFilePath.get(fileName)), new File(backupDirectory + File.separator + fileName));
			}
		}
	}

	public static List<FileBean> getFileNameListAfterGivenDate(Date date, String searchDirectory, Set<String> excludeFolderNameSet, Set<String> extensionSet){
		SortedSet<FileBean> fileBeanSet = getFileBean(new File(searchDirectory), excludeFolderNameSet, extensionSet);
		long time = date.getTime();
		List<FileBean> fileBeanList = new ArrayList<FileBean>();
		for(FileBean fileBean: fileBeanSet){
			if(fileBean.lastModified() > time){
				fileBeanList.add(fileBean);
			}
		}
		return fileBeanList;
	}

	private static void copyFile(File fileFrom, File fileTo) throws IOException {
		InputStream inStream = null;
		OutputStream outStream = null;

		inStream = new FileInputStream(fileFrom);
		outStream = new FileOutputStream(fileTo);

		byte[] buffer = new byte[1024];

		int length;
		// copy the file content in bytes
		while ((length = inStream.read(buffer)) > 0) {
			outStream.write(buffer, 0, length);
		}

		inStream.close();
		outStream.close();
	}
	
	public static void writeToFile(String file, String content) throws IOException {
		PrintWriter out = new PrintWriter(file);
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
//		final String WORKSPACE_PATH = "D:/HUBSWorkspace/Workspace";
//		Map<String, String> listFilesForFolder = FileUtility.getFileNameToFilePath("C:/Users/mithul.bhansali/Desktop/Patch");
//		System.out.println(listFilesForFolder.keySet());
		
//		logWorkspaceFileChanges();
		
		diff();
		
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

	private static void logWorkspaceFileChanges(){
		final String WORKSPACE_PATH = "D:/HUBSWorkspace/Workspace";
//		final String WORKSPACE_PATH = "D:/SVN/16.2.1.0_WIP/SOURCE";
		final int YEAR = 2017;
		final int MONTH = Calendar.FEBRUARY;
		final int DATE = 1;
		final int HOUR_OF_DAY = 9;
		final int MINUTE = 00;
		
		Calendar instance = Calendar.getInstance();
		instance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);
		
		SortedSet<String> excludedFolderNameSet = CollectionUtility.createSortedSet("bin", "svn", "buildFiles",".metadata", "LabellingHistory", "Z");
		SortedSet<String> includedFileExtensionNameSet = CollectionUtility.createSortedSet("java", "js", "jsp", "class", "xml", "properties", "sql");
//		SortedSet<String> includedFileExtensionNameSet = CollectionUtility.createSortedSet("sql");
		List<FileBean> fileBeanListAfterGivenDate = getFileNameListAfterGivenDate(instance.getTime(), WORKSPACE_PATH, excludedFolderNameSet, includedFileExtensionNameSet);
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
	
	private static void diff(){
		final String WORKSPACE_PATH = "D:/HUBSWorkspace/Workspace";
		final String SVN_PATH = "D:/SVN/16.2.1.0_WIP/SOURCE";
		final int YEAR = 2016;
		final int MONTH = Calendar.FEBRUARY;
		final int DATE = 1;
		final int HOUR_OF_DAY = 19;
		final int MINUTE = 30;
		
		Calendar instance = Calendar.getInstance();
		instance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);
		
		SortedSet<String> excludedFolderNameSet = CollectionUtility.createSortedSet("bin", "svn", ".metadata", "LabellingHistory", "BLOB", "build", ".settings","Z");
		SortedSet<String> includedFileExtensionNameSet = CollectionUtility.createSortedSet("java", "js", "jsp", "class", "xml", "properties", "sql", "css", "html");
		List<FileBean> workspaceFileBeanSet = getFileNameListAfterGivenDate(instance.getTime(), WORKSPACE_PATH, excludedFolderNameSet, includedFileExtensionNameSet);
		
		SortedSet<FileBean> svnFileBeanSet = getFileBean(SVN_PATH);
		
		StringBuilder builder = new StringBuilder();
		builder.append(
				String.format("Workspace and SVN File Difference Details\nWorkspace Path: %s\nSVN Path:%s\nExcluded Folder: %s\nIncluded File Extension: %s\n",
					WORKSPACE_PATH, 
					SVN_PATH,
					excludedFolderNameSet, 
					includedFileExtensionNameSet));
		
		for(FileBean workspaceFileBean: workspaceFileBeanSet){
			boolean isFound = false;
			for(FileBean svnFileBean: svnFileBeanSet){
				if(workspaceFileBean.name().equals(svnFileBean.name()) && workspaceFileBean.relativePath().equals(svnFileBean.relativePath())){
					isFound = true;
					if(workspaceFileBean.length() != svnFileBean.length()){
						builder.append(workspaceFileBean.absolutePath() + "____" + workspaceFileBean.name() + " - ");
						break;
					}
				}
			}
			if(!isFound)
				builder.append(workspaceFileBean.absolutePath() + "****" + workspaceFileBean.name() + " - ");
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
}

