package Utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtility {
	
	private Properties prop;
	
	public PropertiesUtility() throws Exception {
		
		FileInputStream fis = new FileInputStream("./src/main/resources/Commondata.properties");
		prop = new Properties();
		prop.load(fis);
	}
	
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
}
