package compec.ufam.server;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;

import com.phill.libs.PropertiesManager;
import com.phill.libs.ResourceManager;
import com.phill.libs.ui.AlertDialog;
import com.phill.libs.ui.GraphicsHelper;
import com.phill.libs.ui.JPaintedPanel;

import compec.ufam.io.*;
import compec.ufam.model.*;
import compec.ufam.utils.*;

public class TelaServidor extends JFrame implements ActionListener, Runnable {

	private static final long serialVersionUID = 1L;
	private JLabel textSenhaAtual, textMesaAtual;
	private String separador = Constants.String.SEPARATOR;
	private ServerSocket socket;
	private Thread serverThread;
	private Senha senhaAtual;
	private ArrayList<Cliente> listaClientes;
	private JMenuBar menuBar;
	private JMenuItem configReinicia, configIP, configSair;
	private JPanel panel;
	private JPanel panel_1;
	private JTextArea textArea;
	private final KeyListener keyListener;
	
	public static void main(String[] args) {
		new TelaServidor();
	}
	
	public TelaServidor() {
		super("Servidor de Senhas");
		
		this.keyListener   = new KeyEventHandler();
		this.listaClientes = new ArrayList<Cliente>();
		
		carregaHistorico();
		
		Dimension dimension = GraphicsHelper.getScreenSize();
		
		Font fonte = GraphicsHelper.getInstance().getFont(DimensionManager.getHeigthScale(dimension,19.0));
		Font texts = GraphicsHelper.getInstance().getFont(DimensionManager.getHeigthScale(dimension,30.0));
		
		JPanel painelPrincipal = new JPaintedPanel("img/background.jpg",dimension);
		setContentPane(painelPrincipal);
		painelPrincipal.setLayout(new BorderLayout());
		
		int panelScale = DimensionManager.getHeigthScale(dimension,1.5);
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(0,panelScale));
		panel.setOpaque(false);
		painelPrincipal.add(panel, BorderLayout.NORTH);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		int senhaWestAlign = DimensionManager.getWidthScale(dimension, 2.97);
		
		textSenhaAtual = new JLabel(senhaAtual.getTextFieldSenha());
		sl_panel.putConstraint(SpringLayout.NORTH, textSenhaAtual, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, textSenhaAtual, senhaWestAlign, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, textSenhaAtual, 0, SpringLayout.SOUTH, panel);
		panel.add(textSenhaAtual);
		textSenhaAtual.setFont(fonte);
		
		int mesaWestAlign = DimensionManager.getWidthScale(dimension, 1.25);
		
		textMesaAtual = new JLabel(senhaAtual.getTextFieldMesa());
		sl_panel.putConstraint(SpringLayout.NORTH, textMesaAtual, 0, SpringLayout.NORTH, textSenhaAtual);
		sl_panel.putConstraint(SpringLayout.WEST, textMesaAtual, mesaWestAlign, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, textMesaAtual, 0, SpringLayout.SOUTH, textSenhaAtual);
		sl_panel.putConstraint(SpringLayout.EAST, textMesaAtual, 0, SpringLayout.EAST, panel);
		panel.add(textMesaAtual);
		textMesaAtual.setFont(fonte);
		
		textArea = new JTextArea();
		textArea.setFont(texts);
		textArea.addKeyListener(keyListener);
		textArea.setEditable(false);
		textArea.setOpaque(false);
		
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setOpaque(false);
		
		int panel_1Scale = DimensionManager.getHeigthScale(dimension,2.32);
		
		panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setPreferredSize(new Dimension(0,panel_1Scale));
		painelPrincipal.add(panel_1, BorderLayout.SOUTH);
		panel_1.add(scroll);
		
		int panel2SouthAlign = DimensionManager.getHeigthScale(dimension,2.92);
		int panel2EastAlign  = DimensionManager.getWidthScale(dimension,1.075);
		int panel2WestAlign  = DimensionManager.getWidthScale(dimension,13.66);
		
		SpringLayout sl_panel2 = new SpringLayout();
		sl_panel2.putConstraint(SpringLayout.NORTH, scroll, 0, SpringLayout.NORTH, panel);
		sl_panel2.putConstraint(SpringLayout.WEST, scroll, panel2WestAlign, SpringLayout.WEST, panel);
		sl_panel2.putConstraint(SpringLayout.SOUTH, scroll, panel2SouthAlign, SpringLayout.NORTH, panel);
		sl_panel2.putConstraint(SpringLayout.EAST, scroll, panel2EastAlign, SpringLayout.WEST, panel);
		panel_1.setLayout(sl_panel2);
		
		onCreateOptionsMenu();
		
		setSize(dimension);
		addKeyListener(keyListener);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		new Thread(this).start();
	}
	
	/** Cria as opções de menu */
	private void onCreateOptionsMenu() {
		
		menuBar = new JMenuBar();
		
		JMenu menuConfig = new JMenu("Configurações");
		menuBar.add(menuConfig);
		
		configReinicia = new JMenuItem("Reiniciar Contagem");
		configReinicia.addActionListener(this);
		menuConfig.add(configReinicia);
		
		configIP = new JMenuItem("Exibir Endereço");
		configIP.addActionListener(this);
		menuConfig.add(configIP);
		
		menuConfig.addSeparator();
		
		configSair = new JMenuItem("Sair");
		configSair.addActionListener(this);
		menuConfig.add(configSair);
		
		menuBar.setVisible(false);
		setJMenuBar(menuBar);
	}

	/*********************************************************************************************/
	/*						BLOCO DE CONTROLE DO SISTEMA										 */
	/*********************************************************************************************/
	
	private void carregaHistorico() {
		int senha = PropertiesManager.getInt("senha.atual","config/server.properties");
		int mesa  = PropertiesManager.getInt("mesa.atual","config/server.properties");
		
		this.senhaAtual = new Senha(new Mesa(mesa),senha);
	}
	
	private void salvaHistorico() {
		int senha = senhaAtual.getSenhaAsInteger();
		int mesa  = senhaAtual.getMesaAsInteger();
		
		PropertiesManager.setInt("senha.atual", senha ,"config/server.properties");
		PropertiesManager.setInt("mesa.atual" , mesa ,"config/server.properties");
	}
	
	public void addCliente(Cliente cliente) {
		listaClientes.add(cliente);
	}
	
	public String tryLogin(Cliente cliente) {
		for (Cliente aux: listaClientes)
			if (aux.getMessage().equals(cliente.getMessage()))
				return Integer.toString(Constants.Login.DENIED_LOGIN);
		listaClientes.add(cliente);
		return senhaAtual.getMessage(false);
	}
	
	@Override
	public void dispose() {
		System.out.println("[INFO] Servidor de Senhas Encerrado!");
		interrupt();
		super.dispose();
	}
	
	/** Mata a thread do servidor */
	public void interrupt() {
		try {
			socket.close();
			Method method = Thread.class.getDeclaredMethod("stop0", new Class[] {Object.class} );
			method.setAccessible(true);
			method.invoke(serverThread , new ThreadDeath());
		}
		catch (Exception exception) { }
		finally { serverThread = null; }
	}
	
	/** Remove um usuário da lista de usuários conectados */
	public void remUsuario(Socket cliente) {
		for (Cliente aux: listaClientes)
			if (aux.getSocket() == cliente) {
				listaClientes.remove(aux);
				return;
			}
	}
	
	@Override
	/** Thread que manipula as conexões via socket */
	public void run() {
		int portNumber = Constants.Ports.PASSWD_PORT;
		serverThread = Thread.currentThread();
		socket = null;
		
		try {
			socket = new ServerSocket(portNumber);
			imprime("[INFO] Servidor de Senhas executando na porta " + portNumber + "...");
	
			/** Este loop fica aceitando conexões infinitamente */
			while(!Thread.currentThread().isInterrupted())
				new IOServer(socket.accept(), this);
		}
		catch (BindException exception) {
			AlertDialog.error("Bind Exception", exception.getMessage());
		}
		catch (IOException exception) {
			AlertDialog.error("I/O Exception", exception.getMessage());
		}
	}
	
	public String getStatus(String IP) {
		setStatus(IP);
		String response = Constants.Passwd.RETURN_PWD + separador + senhaAtual.getSenha() + separador + senhaAtual.getMesa();
		return response;
	}
	
	private synchronized void setStatus(String IP) {
		int mesaID = Integer.parseInt(getUsernameByIP(IP));
		int senhaN = senhaAtual.getSenhaAsInteger();
		
		senhaN++;
		
		this.senhaAtual = new Senha(new Mesa(mesaID),senhaN);
		
		salvaHistorico();
		ResourceManager.playAudio("audio/msn.wav");
		broadcastSenha(IP);
		atualizaView();
	}
	
	private void reiniciaContagem() {
		this.senhaAtual = new Senha(new Mesa(0),0);
		salvaHistorico();
		broadcastSenha("");
		atualizaView();
	}
	
	private void broadcastSenha(String IP) {
		for (Cliente cliente: listaClientes)
			if (!cliente.getHostAddress().equals(IP))
				cliente.sendPassword(senhaAtual);
	}
	
	private void atualizaView() {
		textMesaAtual .setText(senhaAtual.getTextFieldMesa ());
		textSenhaAtual.setText(senhaAtual.getTextFieldSenha());
		
		if (senhaAtual.nula()) {
			textArea.setText(null);
			System.out.println("[INFO] Contagem reiniciada");
		}
		else {
			appendTop(senhaAtual.getResume());
			System.out.println(senhaAtual.getLog());
		}
	}
	
	private void appendTop(String message) {
		try { textArea.getDocument().insertString(0,message,null); }
		catch (BadLocationException exception) { textArea.append(message); }
	}
	
	/** Imprime uma string na área de logs */
	private synchronized void imprime(String mensagem) {
		System.out.println(mensagem);
	}

	public boolean isLogged(String iP) {
		for (Cliente cliente: listaClientes)
			if (cliente.getHostAddress().equals(iP))
				return true;
		return false;
	}

	public String getUsernameByIP(String iP) {
		for (Cliente cliente: listaClientes)
			if (cliente.getHostAddress().equals(iP))
				return cliente.getMesaAsString();
		return null;
	}

	public void echoReply(String iP) {
	}

	public String getUserByIP(String iP) {
		return getUsernameByIP(iP);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		Object source = event.getSource();
		
		if (source == configReinicia)
			reinicia();
		
		else if (source == configIP)
			exibeIP();
		
		else if (source == configSair)
			dispose();
	}

	private void exibeIP() {
		
		try { AlertDialog.info("Server IP", IOModel.getLocalIP()); }
		catch (UnknownHostException exception) { AlertDialog.error("Falha ao obter o endereço IP!"); }
		
	}

	private void reinicia() {
		int option = AlertDialog.dialog("Você deseja realmente reinicar a contagem?");
		if (option == JOptionPane.OK_OPTION)
			reiniciaContagem();
	}

	public void chamarAtencao() {
		ResourceManager.playAudio("audio/msn.wav");
	}
	
	private class KeyEventHandler extends KeyAdapter {
		
		@Override
		public void keyReleased(KeyEvent event) {
			
			if (event.getKeyCode() == KeyEvent.VK_ALT)
				
				if (menuBar.isVisible())
					menuBar.setVisible(false);
				else
					menuBar.setVisible(true);
			
		}
		
	}
	
}
