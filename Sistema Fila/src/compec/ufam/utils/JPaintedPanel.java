package compec.ufam.utils;

import java.awt.*;
import javax.swing.*;

/** Cria um painel personalizado com imagem de fundo
*	@author Felipe André
*	@version 2.5, 22/01/2015 */
public class JPaintedPanel extends JPanel {

	/** Atributos de controle da classe */
	private transient static final long serialVersionUID = 1L;
	private String arquivoImagem;

	/** Instancia a classe e seta os atributos */
	public JPaintedPanel(String arquivoImagem) {
		this.arquivoImagem = arquivoImagem;
	}
	
	@Override
	/** Cria o plano de fundo do painel */
	public void paintComponent(Graphics graphics) {
		Image imagem = new ImageIcon(StringUtils.getResource(arquivoImagem)).getImage();    
		graphics.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
	}
	
}
