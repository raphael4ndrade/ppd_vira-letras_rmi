package client.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ViewIP extends JFrame {
	
	private class ContinueListener implements ActionListener{
		private static final String IPADDRESS_PATTERN =
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		private Matcher matcher;
		@Override
		public void actionPerformed(ActionEvent e) {
			String ip = ipText.getText();
			boolean stop = false;
			while (!stop) {
				if (validateIP(ip)) {
					setServerIP(ip);
					setVisible(false);
					stop = true;
				} else {
					getTextFieldIP().setText("");
					getTextFieldIP().requestFocus();
				}
			}
		}
		
		public boolean validateIP(String ip){
			Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
			matcher = pattern.matcher(ip);
			return matcher.matches();
		}
	}
	
	private String serverIP;
	
	private JButton continueButton;
	private JTextField ipText;
	
	public ViewIP(){
		
		this.add(createPanel(new JPanel(new FlowLayout(SwingConstants.CENTER, 0, 0))));
		
		this.addContinueListener(new ContinueListener());
		
	}
	
	private void setUI(){
		this.setTitle("Insira o IP do servidor");
		this.setSize(250, 100);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
	}
	
	public JPanel createPanel(JPanel panel){
		panel.setPreferredSize(new Dimension(250, 100));
		
		ipText = new JTextField("localhost");
		ipText.setPreferredSize(new Dimension(180, 100));
		
		continueButton = new JButton("Continuar");
		continueButton.setPreferredSize(new Dimension(50, 100));
		
		panel.add(ipText);
		panel.add(continueButton);
		
		return panel;
	}
	
	public void addContinueListener(ActionListener listener){
		continueButton.addActionListener(listener);
	}
	
	public JTextField getTextFieldIP(){
		return ipText;
	}
	
	public String getServerIP(){
		return this.serverIP;
	}
	
	public void setServerIP(String serverIP){
		this.serverIP = serverIP;
	}

}
