package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import beans.Password;
import utils.BruteForce;

public class Menu extends JFrame {
	private static final long serialVersionUID = 2L;
	
	private JPanel content;
	private JPasswordField passwordField;
	private static LocalTime tempoInicial;
	private static LocalTime tempoFinal;
	private JButton btnBruteForce;
	private JLabel lblSomenteNumerais;
	private JLabel lblOpes;
	private JCheckBox chckbxHabilitarSegurana;
	private JCheckBox chckbxHabilitarLog;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JLabel lblOpcoes;
	public static SwingWorker<String, Void> worker;
	private JLabel lblStatus;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void centerWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
	
	public Menu() {
		setTitle("Brute Force - SEGURAN\u00C7A E AUDITORIA EM ENGENHARIA DE SOFTWARE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 621, 225);
		content = new JPanel();
		content.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(content);
		setResizable(false);
		content.setLayout(null);
		centerWindow(this);

		lblOpcoes = new JLabel("Digite a senha de 6 d\u00EDgitos:");
		lblOpcoes.setBounds(10, 11, 156, 14);
		content.add(lblOpcoes);

		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();

				if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
					e.consume(); // if it's not a number, ignore the event
				}

			}
		});
		passwordField.setBounds(10, 36, 92, 20);
		passwordField.setDocument(new JTextFieldLimit(6));
		content.add(passwordField);

		textArea = new JTextArea(0, 0);
		textArea.setEditable(false);
		textArea.setLineWrap(true);

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Log"));
		scrollPane.setBounds(244, 11, 361, 160);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		content.add(scrollPane);

		chckbxHabilitarLog = new JCheckBox("Imprimir Log");
		chckbxHabilitarLog.setSelected(true);
		chckbxHabilitarLog.setBounds(10, 140, 97, 23);
		content.add(chckbxHabilitarLog);

		chckbxHabilitarSegurana = new JCheckBox("Habilitar seguran\u00E7a");
		chckbxHabilitarSegurana.setBounds(10, 166, 156, 23);
		content.add(chckbxHabilitarSegurana);

		lblOpes = new JLabel("Op\u00E7\u00F5es:");
		lblOpes.setBounds(10, 121, 122, 14);
		content.add(lblOpes);

		lblSomenteNumerais = new JLabel("Obs: Somente numerais");
		lblSomenteNumerais.setForeground(Color.GRAY);
		lblSomenteNumerais.setBackground(Color.GRAY);
		lblSomenteNumerais.setBounds(10, 67, 192, 14);
		content.add(lblSomenteNumerais);

		btnBruteForce = new JButton("Brute Force!");
		btnBruteForce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acaoBotaoBruteForce(e);
			}
		});
		btnBruteForce.setBounds(112, 33, 122, 23);
		content.add(btnBruteForce);
		
		lblStatus = new JLabel("STATUS");
		lblStatus.setBounds(244, 174, 122, 14);
		lblStatus.setVisible(false);
		content.add(lblStatus);
	}
	
	public void acaoBotaoBruteForce(ActionEvent e) {
		
	    final JDialog loading = new JDialog();
	    JPanel p1 = new JPanel(new BorderLayout());
	    p1.add(new JLabel("Processando..."), BorderLayout.SOUTH);
	    loading.setUndecorated(true);
	    loading.getContentPane().add(p1);
	    loading.setLocationRelativeTo(content);
	    
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) (((dimension.getWidth() - loading.getWidth()) / 2) + 60);
	    int y = (int) (((dimension.getHeight() - loading.getHeight()) / 2) + 85);
	    loading.setLocation(x, y);
	    
	    loading.pack();
	    loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    loading.setModal(true);

	    worker = new SwingWorker<String, Void>() {
	    	@Override
	    	protected String doInBackground() throws Exception {
	    		if (passwordField.getPassword().length < 6) {
					JOptionPane.showMessageDialog(null, "Por favor, insira 6 dígitos.");
					return "";
				}

				textArea.setText("");

				Password p = new Password(String.valueOf(passwordField.getPassword()));
				tempoInicial = LocalTime.now();
				BruteForce bf = new BruteForce(chckbxHabilitarLog.isSelected(), chckbxHabilitarSegurana.isSelected(),
						textArea);
				String senha = bf.bruteForce(p, 6);
				tempoFinal = LocalTime.now();
				Long tempoExecucao = Duration.between(tempoInicial, tempoFinal).getSeconds();

				textArea.append("----------------------------------------------------------------------\n");
				textArea.append("Senha encontrada: " + senha + "\n");
				textArea.append("Tempo de execução em segundos (s): " + tempoExecucao + "\n");
				textArea.append("Total de senhas comparadas: " + bf.getContador() + "\n");
				textArea.append("----------------------------------------------------------------------\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());
				
				return "";
	    	}
	    	
	    	@Override
	    	protected void done() {
	    		loading.dispose();
	    	}
	    };
	    worker.execute();
	    loading.setVisible(true);
	    try {
	        worker.get();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	}
}
