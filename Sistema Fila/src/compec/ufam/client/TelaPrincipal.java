package compec.ufam.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import compec.ufam.io.*;
import compec.ufam.model.*;
import compec.ufam.utils.*;

public class TelaPrincipal extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel textMesa, textSenha;
	private Senha senhaAtual;
	private IOClient ioClient;
	private JButton botaoSolicitar;

	public TelaPrincipal(String user, Senha senha, IOClient ioClient) {
		super("Sistema de Senhas");
		
		Font  fonte = GraphicsHelper.getFont();
		Font  font1 = GraphicsHelper.getFont(25);
		Color color = GraphicsHelper.getColor();
		
		this.senhaAtual = senha;
		this.ioClient = ioClient;
		this.ioClient.setCurrentFrame(this);
		
		setSize(350,190);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel labelTitulo = new JLabel("Mesa " + user);
		labelTitulo.setFont(font1);
		labelTitulo.setBounds(123, 11, 122, 33);
		getContentPane().add(labelTitulo);
		
		JPanel painelChamada = new JPanel();
		painelChamada.setBorder(GraphicsHelper.getTitledBorder("Última Chamada"));
		painelChamada.setBounds(10, 55, 324, 63);
		getContentPane().add(painelChamada);
		painelChamada.setLayout(null);
		
		JLabel labelSenha = new JLabel("Senha:");
		labelSenha.setFont(fonte);
		labelSenha.setBounds(10, 29, 79, 19);
		painelChamada.add(labelSenha);
		
		textSenha = new JLabel();
		textSenha.setForeground(color);
		textSenha.setFont(fonte);
		textSenha.setBounds(70, 29, 79, 19);
		painelChamada.add(textSenha);
		
		JLabel labelMesa = new JLabel("Mesa");
		labelMesa.setFont(fonte);
		labelMesa.setBounds(195, 29, 46, 19);
		painelChamada.add(labelMesa);
		
		textMesa = new JLabel();
		textMesa.setForeground(color);
		textMesa.setFont(fonte);
		textMesa.setBounds(247, 29, 56, 19);
		painelChamada.add(textMesa);
		
		botaoSolicitar = new JButton("Próxima");
		botaoSolicitar.addActionListener(this);
		botaoSolicitar.setBounds(61, 127, 104, 23);
		getContentPane().add(botaoSolicitar);
		
		JButton botaoAtencao = new JButton("Atenção!");
		botaoAtencao.addActionListener(this);
		botaoAtencao.setBounds(177, 127, 104, 23);
		getContentPane().add(botaoAtencao);
		
		atualizaSenha(senhaAtual);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/** Sinaliza a desconexão do servidor */
	public void semConexao(IOException exception) {
		AlertDialog.erro("Erro de Conexão", exception.getMessage());
		dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == botaoSolicitar)
			solicitar();
		else
			chamarAtencao();
	}

	private void chamarAtencao() {
		ioClient.chamarAtencao();
	}
	
	private void solicitar() {
		ioClient.solicitaSenha();
	}
	
	public void atualizaSenha(Senha senha) {
		Runnable job = new FieldUpdater(senha);
		SwingUtilities.invokeLater(job);
	}
	
	
	private class FieldUpdater implements Runnable {

		private final Senha senha;
		
		public FieldUpdater(Senha senha) {
			this.senha = senha;
		}
		
		@Override
		public void run() {
			textMesa.setText (senha.getTextFieldMesa ());
			textSenha.setText(senha.getTextFieldSenha());
			repaint();
		}
		
	}
	
}
