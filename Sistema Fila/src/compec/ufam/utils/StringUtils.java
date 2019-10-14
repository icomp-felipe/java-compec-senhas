package compec.ufam.utils;

import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

/** Classe que contém métodos úteis de manipulação de Strings
 *  @author Felipe André
 *  @version 1.00, 19/01/2015 */
public class StringUtils {
	
	/** Tamanho do Buffer de E/S = 128 KB */
	private static final int bufferSize = 131072;
	
	public static String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        return sdf.format(cal.getTime());
	}
	
	@Deprecated
	public static String readStream(InputStream stream) throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
		return buffer.readLine();
	}
	
	public static String readStreamBuffer(InputStream stream) throws IOException {
		byte[] streamBuffer = new byte[bufferSize];
		
		stream.read(streamBuffer);
		String string = new String(streamBuffer).trim();

		return string.equals("") ? null : string;
	}

	/** Formata o valor monetário */
	public static String formatValor(double valor) {
		return "R$ " + String.format("%.2f", valor);
	}
	
	/** Converte uma string para ponto flutuante */
	public static double parseDouble(String valor) {
		String tratamento = valor.replaceAll(",",".").trim();
		return Double.parseDouble(tratamento);
	}
	
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
	
}
