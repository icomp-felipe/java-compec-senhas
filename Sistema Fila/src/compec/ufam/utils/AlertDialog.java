package compec.ufam.utils;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;

/** Classe que contém métodos Ãºteis de manipulação de telas de diálogo
 *  @author Felipe AndrÃ©
 *  @version 2.5, 19/01/2015 */
public class AlertDialog {
	
	/** Classe implementa uma Thread para nÃ£o travar a execuÃ§Ã£o do programa
	 *  ao se utilizar a classe JOptionPane */
	private static class MensagemThread extends Thread {
		
		/** Atributos do JOptionPane */
		private String titulo, mensagem;
		private int icone;
		
		/** Inicializa os atributos e a execuÃ§Ã£o da Thread */
		public MensagemThread(String titulo, String mensagem, int icone) {
			this.titulo = titulo;
			this.mensagem = mensagem;
			this.icone = icone;
			start();
		}
		
		@Override
		/** Exibe o JOptionPane */
		public void run() {
			JOptionPane.showMessageDialog(null,mensagem,titulo,icone);
		}
		
	}
	
	/** Mostra uma mensagem de informaÃ§Ã£o padrÃ£o */
	public static void informativo(String mensagem) {
		informativo("Informação", mensagem);
	}
	
	/** Mostra uma mensagem de informaÃ§Ã£o personalizada */
	public static void informativo(String titulo, String mensagem) {
		new MensagemThread(titulo, mensagem, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/** Mostra uma mensagem de erro padrÃ£o */
	public static void erro(String mensagem) {
		erro("Tela de Erro",mensagem);
	}
	
	/** Mostra uma mensagem de erro personalizada */
	public static void erro(String titulo, String mensagem) {
		new MensagemThread(titulo, mensagem, JOptionPane.ERROR_MESSAGE);
	}
	
	/** Mostra uma janela de diÃ¡logo */
	public static int dialog(String mensagem) {
		return JOptionPane.showConfirmDialog(null,mensagem);
	}
	
	/** Cola um texto na Ã¡rea de transferÃªncia */
	public static void pasteToClibpoard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
    }
	
	/** Copia um texto da Ã¡rea de transferÃªncia */
	public static String copyFromClipboard() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
		   	try { result = (String)contents.getTransferData(DataFlavor.stringFlavor); }
		   	catch (UnsupportedFlavorException | IOException ex) { ex.printStackTrace(); }
		}
	    return result;
	}

	public static void playAudio() {
		new PlayAudio();
	}
	
	/** Reproduz um áudio ao chegar uma mensagem nova */
	public static class PlayAudio extends Thread {
		
		private File arquivoAudio = new File(StringUtils.getResource("audio/msn.wav"));
		
		public PlayAudio() { start(); }
		
		@Override
		public void run() {
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(arquivoAudio));
				clip.start();
				while (!clip.isRunning())
					Thread.sleep(10);
				while (clip.isRunning())
					Thread.sleep(10);
				clip.close();
			}
			catch (Exception exception) { }
		}
		
	}
	
}
