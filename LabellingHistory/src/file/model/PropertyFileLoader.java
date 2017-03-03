package file.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileLoader{
	private String filePath;
	
	public PropertyFileLoader(String filePath){
		this.filePath = filePath;
	}
	
	public Properties load() throws IOException{
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(filePath);
			prop.load(input);
			return prop;
		} finally {
			try {
				if (input != null) input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
