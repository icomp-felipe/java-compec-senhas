package compec.ufam.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import compec.ufam.io.*;
import compec.ufam.model.*;
import compec.ufam.utils.*;

public class TelaConecta extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField textEndereco;
	private String userID;
	private JButton botaoConectar, botaoSair;
	private IOClient ioClient;
	private Socket socket;
	private JButton botaoID;
	private JLabel labelNumeroMesa;

	public static void main(String[] args) {
		new TelaConecta();
	}

	public TelaConecta() {
		super("Sistema de Senha");
		
		Font  fonte = GraphicsHelper.getFont(25);
		Font  font1 = GraphicsHelper.getFont();
		Color color = GraphicsHelper.getColor();
		
		Icon exitIcon  = ResourceManager.getResizedIcon("icon/exit.png",23,23);
		Icon loginIcon = ResourceManager.getResizedIcon("icon/login.png",20,20);
		Icon idIcon    = ResourceManager.getResizedIcon("icon/id.png",25,25);
		
		userID = PropertiesManager.getPropertyAsString("mesa.id");
		
		setSize(350,190);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JLabel labelMesa = new JLabel("Mesa");
		labelMesa.setFont(fonte);
		labelMesa.setBounds(120, 11, 88, 33);
		getContentPane().add(labelMesa);
		
		labelNumeroMesa = new JLabel(userID);
		labelNumeroMesa.setFont(fonte);
		labelNumeroMesa.setBounds(195, 11, 88, 33);
		getContentPane().add(labelNumeroMesa);
		
		JPanel painelServidor = new JPanel();
		painelServidor.setBorder(GraphicsHelper.getTitledBorder("Servidor"));
		painelServidor.setBounds(10, 46, 332, 68);
		getContentPane().add(painelServidor);
		painelServidor.setLayout(null);
		
		JLabel labelEndereco = new JLabel("Endere\u00E7o:");
		labelEndereco.setFont(font1);
		labelEndereco.setBounds(10, 30, 79, 23);
		painelServidor.add(labelEndereco);
		
		textEndereco = new JTextField(PropertiesManager.getPropertyAsString("server.addr"));
		textEndereco.setForeground(color);
		textEndereco.setFont(font1);
		textEndereco.setBounds(98, 30, 220, 23);
		painelServidor.add(textEndereco);
		textEndereco.setColumns(10);
		textEndereco.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					botaoConectar.doClick();
			}
		});
		
		botaoConectar = new JButton(loginIcon);
		botaoConectar.setToolTipText("Conectar ao Servidor");
		botaoConectar.addActionListener(this);
		
		botaoSair = new JButton(exitIcon);
		botaoSair.setToolTipText("Sair do Sistema");
		botaoSair.addActionListener(this);
		botaoSair.setBounds(109, 120, 35, 30);
		getContentPane().add(botaoSair);
		
		botaoID = new JButton(idIcon);
		botaoID.addActionListener(this);
		botaoID.setToolTipText("Trocar Identificação da Mesa");
		botaoID.setBounds(156, 120, 35, 30);
		getContentPane().add(botaoID);
		botaoConectar.setBounds(203, 120, 35, 30);
		getContentPane().add(botaoConectar);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if (source == botaoConectar)
			conectar();
		
		else if (source == botaoID)
			trocarID();
		
		else if (source == botaoSair)
			dispose();
	}

	private void trocarID() {
		
		String id = JOptionPane.showInputDialog("Informe a nova identificação");

		if (id != null) {
			
			PropertiesManager.setProperty("mesa.id",id);
			labelNumeroMesa.setText(id);
			this.userID = id;
			AlertDialog.informativo("Identificação trocada com sucesso!");
			
		}
		
	}

	@SuppressWarnings("rawtypes")
	private synchronized void conectar() {
		SwingWorker worker = new SwingWorker() {
			protected Void doInBackground() throws Exception {
				tryLogin();
				return null;
			}
		};
		worker.execute();
	}
	
	/** Tenta se conectar ao servidor */
	private void tryLogin() {
		String host = textEndereco.getText();
		
		if (host.equals("")) {
			AlertDialog.erro("Tela de Login", "Informe o endereço do servidor!");
			return;
		}
		
		try {
			if (socket == null) {
				this.socket   = new Socket(host, Constants.Ports.PASSWD_PORT);
				this.ioClient = new IOClient(socket, this);
			}
			ioClient.tryLogin(userID);
		} catch (IOException exception) {
			AlertDialog.erro("Conexão de Rede", "Falha ao conectar ao servidor: " + host + "\nVerifique se o servidor está online!") ;
			socket = null;
			return;
		}
	}
	
	/** Sinaliza a desconexão do servidor */
	public void semConexao(IOException exception) {
		AlertDialog.erro("Erro de Conexão", exception.getMessage());
	}
	
	/** Tratamento da resposta do servidor */
	public void returnStatus(Senha senha) {
		if (senha == null) {
			AlertDialog.erro("O servidor recusou o login porque\nesta estação já está conectada!");
		}
		else {
			AlertDialog.informativo("Login realizado com sucesso!");
			new TelaPrincipal(userID, senha, ioClient);
			dispose();
		}
	}
}
