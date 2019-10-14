package compec.ufam.utils;

import java.util.*;
import java.io.*;

/** Classe que gerencia as propriedades do sistema
 *  @author Felipe André
 *  @version 2.00, 17/01/2015 */
public class PropertiesManager {
	
	/** Atualiza uma propriedade */
	public static boolean setProperty(String key, int value) {
		try {
			Properties properties = getProperties();
			properties.setProperty(key, Integer.toString(value));
			FileOutputStream os = new FileOutputStream(getPropertiesFile());
			properties.store(os, null);
			return true;
		}
		catch (IOException exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	/** Retorna uma propriedade */
	public static int getPropertyAsInteger(String key) {
		try {
			Properties properties = getProperties();
			String property = properties.getProperty(key);
			return Integer.parseInt(property);
		}
		catch (IOException exception) {
			exception.printStackTrace();
			return 0;
		}
	}
	
	public static String getPropertyAsString(String key) {
		try {
			Properties properties = getProperties();
			String property = properties.getProperty(key);
			return property;
		}
		catch (IOException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	/** Instancia a classe de propriedades do sistema */
	private static Properties getProperties() throws IOException {
		Properties props = new Properties();
		File arquivo = getPropertiesFile();
		FileInputStream stream = new FileInputStream(arquivo);
		props.load(stream);
		return props;
	}

	/** Busca o arquivo de propriedades do sistema */
	private static File getPropertiesFile() {
		String arquivo = StringUtils.getResource("configs/program.properties");
		return new File(arquivo);
	}

	public static boolean setProperty(String key, String id) {
		try {
			Properties properties = getProperties();
			properties.setProperty(key, id);
			FileOutputStream os = new FileOutputStream(getPropertiesFile());
			properties.store(os, null);
			return true;
		}
		catch (IOException exception) {
			exception.printStackTrace();
			return false;
		}
	}
}
