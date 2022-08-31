package utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import beans.Password;

public class BruteForce {

	private boolean ativaLog;
	private boolean ativaSeguranca;
	private JTextArea componenteLog;
	private Long contador;
	private Long MAXIMO_TENTATIVAS = 4L;

	public BruteForce(boolean ativaLog, boolean ativaSeguranca, JTextArea componenteLog) {
		super();
		this.ativaLog = ativaLog;
		this.ativaSeguranca = ativaSeguranca;
		this.componenteLog = componenteLog;
		this.contador = 0L;
	}

	public Long getContador() {
		return contador;
	}

	public String bruteForce(Password p, int length) {
		return bruteForce(p, length, new StringBuilder());
	}

	private String bruteForce(Password password, int length, StringBuilder str) {
		if (length == str.length()) {
			if (password.isPassword(str.toString())) {
				return str.toString();
			} else {
				return "";
			}
		}

		String s;

		for (char c = '0'; c <= '9'; c++) {
			str.append(c);
			
			if (ativaLog) {
				componenteLog.append("Testando senha: " + str.toString() + "\n");
				componenteLog.setCaretPosition(componenteLog.getDocument().getLength());
				componenteLog.update(componenteLog.getGraphics());
			}

			if (ativaSeguranca) {
				if (contador > 0) {
					if (contador == MAXIMO_TENTATIVAS) {
						JOptionPane.showMessageDialog(null, "LIMITE MÁXIMO DE " + MAXIMO_TENTATIVAS + " TENTATIVAS ATINGIDO!\nO software irá fechar a fim de evitar o BruteForce.");
						System.exit(0);
					} else {
						LocalDateTime segundosDepois = LocalDateTime.now().plusSeconds(contador * 4);
						while (LocalDateTime.now().isBefore(segundosDepois)) {
							try {
								LocalDateTime agora = LocalDateTime.now();
								long seconds = agora.until(segundosDepois, ChronoUnit.SECONDS);

								componenteLog.append("Aguarde " + seconds
										+ " segundos antes da próxima tentativa de login.\n");
								componenteLog.setCaretPosition(componenteLog.getDocument().getLength());
								componenteLog.update(componenteLog.getGraphics());
								Thread.sleep(1000);
							} catch (Exception e) {
								System.err.println(e);
							}
						}

					}

				}
			}

			this.contador++;
			s = bruteForce(password, length, str);
			if (!s.equals("")) {
				return s;
			}
			str.deleteCharAt(str.length() - 1);
		}

		return "";
	}
}
