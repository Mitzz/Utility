package file.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import utility.FileUtility;

public class FileCopy {

	private int BUF_SIZE = 10240;
	private String absoluteFilePath;
	private String copyDirectory;
	private boolean makeDirectory;

	public FileCopy(String filePath, String copyDirectory) {
		this.absoluteFilePath = filePath;
		this.copyDirectory = copyDirectory;
	}

	public FileCopy makeDirectory(boolean makeDirectory) {
		this.makeDirectory = makeDirectory;
		return this;
	}

	public void copy() throws IOException {
		File sourceFilePath = new File(absoluteFilePath);
		copyFile(absoluteFilePath, copyDirectory + File.separator + sourceFilePath.getName());
	}

	private void copyFile(File in, File out) throws IOException {
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		try {
			byte[] buf = new byte[BUF_SIZE];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} finally {
			if (fis != null)
				fis.close();
			if (fos != null)
				fos.close();
		}
	}

	private void copyFile(String sourceFileName, String destinationFileName) throws IOException {
		File destinationFile = new File(destinationFileName);
		if (makeDirectory && !destinationFile.exists())
			FileUtility.createFileAndDirectory(destinationFile);

		copyFile(new File(sourceFileName), new File(destinationFileName));
	}

	public static void main(String[] args) {
		// test1();
		test2();
	}

	private static void test2() {
		String filePath = new String("D:/ljkasd/App13Jan_DevTrunk_Full_0.0.0_To_1.0.0_20170119145048768.zip");
		String copyDirectory = "D:/f2/ne";

		FileCopy copy = new FileCopy(filePath, copyDirectory);

		try {
			copy.copy();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test1() {
		String filePath = new String("D:/ljkasd/IwRefEntityAPI_DS.java");
		String copyDirectory = "D:/f2";

		FileCopy copy = new FileCopy(filePath, copyDirectory);

		try {
			copy.copy();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
