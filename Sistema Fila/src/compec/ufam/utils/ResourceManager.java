package compec.ufam.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/** Faz a interface do programa com arquivos externos
 *  @author Felipe André
 *  @version 3.0, 23/03/2016 */
public class ResourceManager {	

	/** Retorna o diretório de trabalho atual */
	public static String getCurrentPath() {
		Path currentRelativePath = Paths.get("");
		return currentRelativePath.toAbsolutePath().toString();
	}
	
	/** Retorna o diretório de trabalho atual */
	public static String getResource(String resource) {
		String baseDirectory = getCurrentPath();
		return (baseDirectory + "/res/" + resource);
	}

	/** Cria um arquivo a partir de um recurso de sistema */
	public static File getResourceAsFile(String resource) {
		return new File(getResource(resource));
	}
	
	/** Carrega um ícone a partir de um recurso do sistema e retorna sua cópia redimensionada */
	public static Icon getResizedIcon(String resourceIcon, int width, int height) {
		
		try {
			
			File imagePath = getResourceAsFile(resourceIcon);
			BufferedImage rawImage = ImageIO.read(imagePath);
			Image resized = rawImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			
			return new ImageIcon(resized);
		}
		catch (Exception exception) {
			return null;
		}
		
	}
	
}
